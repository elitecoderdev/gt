import { Gtime } from '../types';
import { getDataSource } from '../config/database';
import { DocDocumento } from '../entities/DocDocumento';
import { DocParticipacion } from '../entities/DocParticipacion';
import { DocFechaDocumento } from '../entities/DocFechaDocumento';
import { DocLocacion } from '../entities/DocLocacion';
import { DocObservacion } from '../entities/DocObservacion';
import { DocRelacion } from '../entities/DocRelacion';
import { DocTransporte } from '../entities/DocTransporte';
import { DocTranItemTransporte } from '../entities/DocTranItemTransporte';
import { DocTransGuiaCourier } from '../entities/DocTransGuiaCourier';
import { DocTransbordo } from '../entities/DocTransbordo';
import { DocVistoBueno } from '../entities/DocVistoBueno';
import { DocCargo } from '../entities/DocCargo';
import { DocEstado } from '../entities/DocEstado';
import { DocImagen } from '../entities/DocImagen';

interface PersistenciaResult {
  exitoso: boolean;
  errores: string[];
  idDocumento?: number;
}

export class PersistenciaService {
  private async getDocumentRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocDocumento);
  }

  private async getParticipacionRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocParticipacion);
  }

  private async getFechaRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocFechaDocumento);
  }

  private async getLocacionRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocLocacion);
  }

  private async getObservacionRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocObservacion);
  }

  private async getRelacionRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocRelacion);
  }

  private async getTransporteRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocTransporte);
  }

  private async getItemRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocTranItemTransporte);
  }

  private async getGuiaRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocTransGuiaCourier);
  }

  private async getTransbordoRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocTransbordo);
  }

  private async getVistoBuenoRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocVistoBueno);
  }

  private async getCargoRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocCargo);
  }

  private async getEstadoRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocEstado);
  }

  private async getImagenRepository() {
    const dataSource = await getDataSource();
    return dataSource.getRepository(DocImagen);
  }

  async persistirDocumento(gtime: Gtime): Promise<PersistenciaResult> {
    const dataSource = await getDataSource();
    const queryRunner = dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    const errores: string[] = [];
    let idDocumento: number | undefined;

    try {
      // Verificar si el documento ya existe
      const documentoRepository = await this.getDocumentRepository();
      const documentoExistente = await documentoRepository.findOne({
        where: { nroEncabezado: gtime.nroEncabezado },
      });

      if (documentoExistente) {
        throw new Error(`Documento ${gtime.nroEncabezado} ya existe`);
      }

      // Persistir encabezado
      const documento = this.mapGtimeToDocDocumento(gtime);
      const documentoGuardado = await queryRunner.manager.save(DocDocumento, documento);
      idDocumento = documentoGuardado.idDocumento;

      // Persistir entidades relacionadas en paralelo
      const persistPromises = [
        this.persistirParticipaciones(queryRunner, idDocumento, gtime),
        this.persistirFechas(queryRunner, idDocumento, gtime),
        this.persistirLocaciones(queryRunner, idDocumento, gtime),
        this.persistirObservaciones(queryRunner, idDocumento, gtime),
        this.persistirReferencias(queryRunner, idDocumento, gtime),
        this.persistirTransporte(queryRunner, idDocumento, gtime),
        this.persistirItems(queryRunner, idDocumento, gtime),
        this.persistirGuiaCourier(queryRunner, idDocumento, gtime),
        this.persistirTransbordos(queryRunner, idDocumento, gtime),
        this.persistirVistosBuenos(queryRunner, idDocumento, gtime),
        this.persistirCargos(queryRunner, idDocumento, gtime),
        this.persistirEstado(queryRunner, idDocumento, gtime),
        this.persistirImagen(queryRunner, idDocumento, gtime),
      ];

      await Promise.allSettled(persistPromises);

      await queryRunner.commitTransaction();

      return {
        exitoso: true,
        errores: [],
        idDocumento,
      };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      errores.push(error instanceof Error ? error.message : 'Unknown error');
      return {
        exitoso: false,
        errores,
        idDocumento,
      };
    } finally {
      await queryRunner.release();
    }
  }

  async rollback(gtime: Gtime): Promise<void> {
    try {
      const documentoRepository = await this.getDocumentRepository();
      const documento = await documentoRepository.findOne({
        where: { nroEncabezado: gtime.nroEncabezado },
      });

      if (documento) {
        await documentoRepository.remove(documento);
      }
    } catch (error) {
      console.error('Error en rollback:', error);
      throw error;
    }
  }

  private mapGtimeToDocDocumento(gtime: Gtime): Partial<DocDocumento> {
    return {
      nroEncabezado: gtime.nroEncabezado || '',
      tipoDocumento: gtime.tipoDocumento || gtime.tipo || '',
      version: gtime.version,
      numeroReferencia: gtime.numeroReferencia,
      totalBultos: gtime.totalBultos ? parseFloat(gtime.totalBultos) : undefined,
      totalPeso: gtime.totalPeso ? parseFloat(gtime.totalPeso) : undefined,
      totalVolumen: gtime.totalVolumen ? parseFloat(gtime.totalVolumen) : undefined,
      totalItem: gtime.totalItem ? parseFloat(gtime.totalItem) : undefined,
      valorDeclarado: gtime.valorDeclarado ? parseFloat(gtime.valorDeclarado) : undefined,
      monedaValor: gtime.monedaValor,
      unidadPeso: gtime.unidadPeso,
      unidadVolumen: gtime.unidadVolumen,
      parcial: gtime.parcial,
      tipoOperacion: gtime.tipoOperacion,
      tipoAccion: gtime.tipoAccion,
      userHostOrigen: gtime.userHostOrigen,
      idSender: gtime.idSender,
      login: gtime.login,
      loginDigitador: gtime.loginDigitador,
      idPayload: gtime.idPayload,
      estado: gtime.estadoGtime || 'PROCESADO',
    };
  }

  private async persistirParticipaciones(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.participaciones?.participacion) return;

    const participaciones = Array.isArray(gtime.participaciones.participacion)
      ? gtime.participaciones.participacion
      : [gtime.participaciones.participacion];

    for (const part of participaciones) {
      const participacion = new DocParticipacion();
      participacion.idDocumento = idDocumento;
      participacion.tipoParticipante = part.tipo || '';
      participacion.rut = part.rut;
      participacion.nombre = part.nombre;
      participacion.direccion = part.direccion;
      participacion.ciudad = part.ciudad;
      participacion.pais = part.pais;
      await queryRunner.manager.save(DocParticipacion, participacion);
    }
  }

  private async persistirFechas(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.fechas?.fecha) return;

    const fechas = Array.isArray(gtime.fechas.fecha) ? gtime.fechas.fecha : [gtime.fechas.fecha];

    for (const fecha of fechas) {
      const fechaDoc = new DocFechaDocumento();
      fechaDoc.idDocumento = idDocumento;
      fechaDoc.tipoFecha = fecha.tipo || '';
      fechaDoc.valor = new Date(fecha.valor || '');
      await queryRunner.manager.save(DocFechaDocumento, fechaDoc);
    }
  }

  private async persistirLocaciones(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.locaciones?.locacion) return;

    const locaciones = Array.isArray(gtime.locaciones.locacion)
      ? gtime.locaciones.locacion
      : [gtime.locaciones.locacion];

    for (const loc of locaciones) {
      const locacion = new DocLocacion();
      locacion.idDocumento = idDocumento;
      locacion.tipoLocacion = loc.tipo || '';
      locacion.codigo = loc.codigo;
      locacion.nombre = loc.nombre;
      await queryRunner.manager.save(DocLocacion, locacion);
    }
  }

  private async persistirObservaciones(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.observaciones?.observacion) return;

    const observaciones = Array.isArray(gtime.observaciones.observacion)
      ? gtime.observaciones.observacion
      : [gtime.observaciones.observacion];

    for (const obs of observaciones) {
      const observacion = new DocObservacion();
      observacion.idDocumento = idDocumento;
      observacion.tipoObservacion = obs.tipo || '';
      observacion.texto = obs.texto;
      await queryRunner.manager.save(DocObservacion, observacion);
    }
  }

  private async persistirReferencias(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.referencias?.referencia) return;

    const referencias = Array.isArray(gtime.referencias.referencia)
      ? gtime.referencias.referencia
      : [gtime.referencias.referencia];

    for (const ref of referencias) {
      const relacion = new DocRelacion();
      relacion.idDocumento = idDocumento;
      relacion.tipoReferencia = ref.tipo || '';
      relacion.numero = ref.numero;
      await queryRunner.manager.save(DocRelacion, relacion);
    }
  }

  private async persistirTransporte(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    // Implementar según estructura de transporte
    // Por ahora solo crear registro básico si existe información
  }

  private async persistirItems(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.items?.item) return;

    const items = Array.isArray(gtime.items.item) ? gtime.items.item : [gtime.items.item];

    for (const item of items) {
      const itemDoc = new DocTranItemTransporte();
      itemDoc.idDocumento = idDocumento;
      itemDoc.numero = item.numero;
      itemDoc.descripcion = item.descripcion;
      itemDoc.cantidad = item.cantidad ? parseFloat(item.cantidad) : undefined;
      itemDoc.peso = item.peso ? parseFloat(item.peso) : undefined;
      itemDoc.volumen = item.volumen ? parseFloat(item.volumen) : undefined;
      await queryRunner.manager.save(DocTranItemTransporte, itemDoc);
    }
  }

  private async persistirGuiaCourier(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    // Implementar según estructura de guía courier
  }

  private async persistirTransbordos(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.transbordos?.transbordo) return;

    const transbordos = Array.isArray(gtime.transbordos.transbordo)
      ? gtime.transbordos.transbordo
      : [gtime.transbordos.transbordo];

    for (const trans of transbordos) {
      const transbordo = new DocTransbordo();
      transbordo.idDocumento = idDocumento;
      transbordo.tipoTransbordo = trans.tipo || '';
      transbordo.locacion = trans.locacion;
      await queryRunner.manager.save(DocTransbordo, transbordo);
    }
  }

  private async persistirVistosBuenos(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.vistosBuenos?.vistoBueno) return;

    const vistosBuenos = Array.isArray(gtime.vistosBuenos.vistoBueno)
      ? gtime.vistosBuenos.vistoBueno
      : [gtime.vistosBuenos.vistoBueno];

    for (const vb of vistosBuenos) {
      const vistoBueno = new DocVistoBueno();
      vistoBueno.idDocumento = idDocumento;
      vistoBueno.tipoVistoBueno = vb.tipo || '';
      vistoBueno.numero = vb.numero;
      await queryRunner.manager.save(DocVistoBueno, vistoBueno);
    }
  }

  private async persistirCargos(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    if (!gtime.cargos?.cargo) return;

    const cargos = Array.isArray(gtime.cargos.cargo) ? gtime.cargos.cargo : [gtime.cargos.cargo];

    for (const cargo of cargos) {
      const cargoDoc = new DocCargo();
      cargoDoc.idDocumento = idDocumento;
      cargoDoc.tipoCargo = cargo.tipo || '';
      cargoDoc.monto = cargo.monto ? parseFloat(cargo.monto) : undefined;
      cargoDoc.moneda = cargo.moneda;
      await queryRunner.manager.save(DocCargo, cargoDoc);
    }
  }

  private async persistirEstado(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    const estado = new DocEstado();
    estado.idDocumento = idDocumento;
    estado.estado = gtime.estadoGtime || 'PROCESADO';
    estado.fechaEstado = new Date();
    await queryRunner.manager.save(DocEstado, estado);
  }

  private async persistirImagen(
    queryRunner: any,
    idDocumento: number,
    gtime: Gtime
  ): Promise<void> {
    // Implementar si hay imágenes en el documento
  }
}

