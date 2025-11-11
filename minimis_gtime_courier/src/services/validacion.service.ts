import { Gtime, Validacion } from '../types';

export class ValidacionService {
  /**
   * Validaciones generales (secuenciales)
   */
  async validarGenerales(gtime: Gtime): Promise<Validacion[]> {
    const validaciones: Validacion[] = [];

    // Validar login digitador
    if (!gtime.loginDigitador || gtime.loginDigitador.trim() === '') {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Login digitador es requerido',
        campo: 'loginDigitador',
      });
    }

    // Validar número referencia
    if (!gtime.numeroReferencia || gtime.numeroReferencia.trim() === '') {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Número de referencia es requerido',
        campo: 'numeroReferencia',
      });
    }

    // Validar totales
    if (gtime.totalBultos && isNaN(parseFloat(gtime.totalBultos))) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Total bultos debe ser numérico',
        campo: 'totalBultos',
      });
    }

    if (gtime.totalPeso && isNaN(parseFloat(gtime.totalPeso))) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Total peso debe ser numérico',
        campo: 'totalPeso',
      });
    }

    if (gtime.totalVolumen && isNaN(parseFloat(gtime.totalVolumen))) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Total volumen debe ser numérico',
        campo: 'totalVolumen',
      });
    }

    if (gtime.totalItem && isNaN(parseFloat(gtime.totalItem))) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Total item debe ser numérico',
        campo: 'totalItem',
      });
    }

    // Validar unidades
    const unidadesPesoValidas = ['KG', 'LB', 'TON'];
    if (gtime.unidadPeso && !unidadesPesoValidas.includes(gtime.unidadPeso.toUpperCase())) {
      validaciones.push({
        tipo: 'ERROR',
        texto: `Unidad de peso inválida. Valores permitidos: ${unidadesPesoValidas.join(', ')}`,
        campo: 'unidadPeso',
      });
    }

    const unidadesVolumenValidas = ['M3', 'FT3', 'L'];
    if (gtime.unidadVolumen && !unidadesVolumenValidas.includes(gtime.unidadVolumen.toUpperCase())) {
      validaciones.push({
        tipo: 'ERROR',
        texto: `Unidad de volumen inválida. Valores permitidos: ${unidadesVolumenValidas.join(', ')}`,
        campo: 'unidadVolumen',
      });
    }

    // Validar valor declarado
    if (gtime.valorDeclarado && isNaN(parseFloat(gtime.valorDeclarado))) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Valor declarado debe ser numérico',
        campo: 'valorDeclarado',
      });
    }

    // Validar moneda
    const monedasValidas = ['USD', 'CLP', 'EUR'];
    if (gtime.monedaValor && !monedasValidas.includes(gtime.monedaValor.toUpperCase())) {
      validaciones.push({
        tipo: 'WARNING',
        texto: `Moneda no estándar: ${gtime.monedaValor}`,
        campo: 'monedaValor',
      });
    }

    // Validar parcial
    if (gtime.parcial && !['S', 'N'].includes(gtime.parcial.toUpperCase())) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Parcial debe ser S o N',
        campo: 'parcial',
      });
    }

    // Validar tipo operación
    const tiposOperacionValidos = ['IMPORTACION', 'EXPORTACION', 'TRANSITO'];
    if (gtime.tipoOperacion && !tiposOperacionValidos.includes(gtime.tipoOperacion.toUpperCase())) {
      validaciones.push({
        tipo: 'ERROR',
        texto: `Tipo de operación inválido. Valores permitidos: ${tiposOperacionValidos.join(', ')}`,
        campo: 'tipoOperacion',
      });
    }

    return validaciones;
  }

  /**
   * Validaciones paralelas nivel 1
   */
  async validarNivel1(gtime: Gtime): Promise<Validacion[]> {
    const validaciones: Validacion[] = [];

    // Validar fechas
    if (gtime.fechas) {
      validaciones.push(...this.validarFechas(gtime.fechas));
    }

    // Validar participaciones
    if (gtime.participaciones) {
      validaciones.push(...this.validarParticipaciones(gtime.participaciones));
    }

    // Validar items
    if (gtime.items) {
      validaciones.push(...this.validarItems(gtime.items));
    }

    // Validar cargos
    if (gtime.cargos) {
      validaciones.push(...this.validarCargos(gtime.cargos));
    }

    // Validar observaciones
    if (gtime.observaciones) {
      validaciones.push(...this.validarObservaciones(gtime.observaciones));
    }

    // Validar vistos buenos
    if (gtime.vistosBuenos) {
      validaciones.push(...this.validarVistosBuenos(gtime.vistosBuenos));
    }

    return validaciones;
  }

  /**
   * Validaciones paralelas nivel 2
   */
  async validarNivel2(gtime: Gtime): Promise<Validacion[]> {
    const validaciones: Validacion[] = [];

    // Validar emisor (primera participación)
    if (gtime.participaciones) {
      validaciones.push(...this.validarEmisor(gtime.participaciones));
    }

    // Validar referencias
    if (gtime.referencias) {
      validaciones.push(...this.validarReferencias(gtime.referencias));
    }

    return validaciones;
  }

  /**
   * Validaciones paralelas nivel 3
   */
  async validarNivel3(gtime: Gtime): Promise<Validacion[]> {
    const validaciones: Validacion[] = [];

    // Validar transbordos
    if (gtime.transbordos) {
      validaciones.push(...this.validarTransbordos(gtime.transbordos));
    }

    // Validar locaciones
    if (gtime.locaciones) {
      validaciones.push(...this.validarLocaciones(gtime.locaciones));
    }

    // Validar RUTs en participaciones
    if (gtime.participaciones) {
      validaciones.push(...this.validarRuts(gtime.participaciones));
    }

    return validaciones;
  }

  private validarFechas(fechas: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const fechaArray = Array.isArray(fechas.fecha) ? fechas.fecha : fechas.fecha ? [fechas.fecha] : [];

    for (const fecha of fechaArray) {
      if (!fecha.tipo || !fecha.valor) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Fecha debe tener tipo y valor',
          campo: 'fechas',
        });
      }
    }

    return validaciones;
  }

  private validarParticipaciones(participaciones: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const partArray = Array.isArray(participaciones.participacion)
      ? participaciones.participacion
      : participaciones.participacion
      ? [participaciones.participacion]
      : [];

    for (const part of partArray) {
      if (!part.tipo) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Participación debe tener tipo',
          campo: 'participaciones',
        });
      }
    }

    return validaciones;
  }

  private validarItems(items: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const itemArray = Array.isArray(items.item) ? items.item : items.item ? [items.item] : [];

    for (const item of itemArray) {
      if (!item.numero) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Item debe tener número',
          campo: 'items',
        });
      }
    }

    return validaciones;
  }

  private validarCargos(cargos: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const cargoArray = Array.isArray(cargos.cargo) ? cargos.cargo : cargos.cargo ? [cargos.cargo] : [];

    for (const cargo of cargoArray) {
      if (!cargo.tipo) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Cargo debe tener tipo',
          campo: 'cargos',
        });
      }
    }

    return validaciones;
  }

  private validarObservaciones(observaciones: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const obsArray = Array.isArray(observaciones.observacion)
      ? observaciones.observacion
      : observaciones.observacion
      ? [observaciones.observacion]
      : [];

    for (const obs of obsArray) {
      if (!obs.tipo) {
        validaciones.push({
          tipo: 'WARNING',
          texto: 'Observación sin tipo definido',
          campo: 'observaciones',
        });
      }
    }

    return validaciones;
  }

  private validarVistosBuenos(vistosBuenos: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const vbArray = Array.isArray(vistosBuenos.vistoBueno)
      ? vistosBuenos.vistoBueno
      : vistosBuenos.vistoBueno
      ? [vistosBuenos.vistoBueno]
      : [];

    // Validaciones específicas según tipo de documento
    // Implementar según reglas de negocio

    return validaciones;
  }

  private validarEmisor(participaciones: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const partArray = Array.isArray(participaciones.participacion)
      ? participaciones.participacion
      : participaciones.participacion
      ? [participaciones.participacion]
      : [];

    const emisor = partArray.find((p: any) => p.tipo === 'EMISOR');
    if (!emisor) {
      validaciones.push({
        tipo: 'ERROR',
        texto: 'Documento debe tener un emisor',
        campo: 'participaciones',
      });
    }

    return validaciones;
  }

  private validarReferencias(referencias: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const refArray = Array.isArray(referencias.referencia)
      ? referencias.referencia
      : referencias.referencia
      ? [referencias.referencia]
      : [];

    for (const ref of refArray) {
      if (!ref.tipo || !ref.numero) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Referencia debe tener tipo y número',
          campo: 'referencias',
        });
      }
    }

    return validaciones;
  }

  private validarTransbordos(transbordos: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const transArray = Array.isArray(transbordos.transbordo)
      ? transbordos.transbordo
      : transbordos.transbordo
      ? [transbordos.transbordo]
      : [];

    for (const trans of transArray) {
      if (!trans.tipo) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Transbordo debe tener tipo',
          campo: 'transbordos',
        });
      }
    }

    return validaciones;
  }

  private validarLocaciones(locaciones: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const locArray = Array.isArray(locaciones.locacion)
      ? locaciones.locacion
      : locaciones.locacion
      ? [locaciones.locacion]
      : [];

    for (const loc of locArray) {
      if (!loc.tipo || !loc.codigo) {
        validaciones.push({
          tipo: 'ERROR',
          texto: 'Locación debe tener tipo y código',
          campo: 'locaciones',
        });
      }
    }

    return validaciones;
  }

  private validarRuts(participaciones: any): Validacion[] {
    const validaciones: Validacion[] = [];
    const partArray = Array.isArray(participaciones.participacion)
      ? participaciones.participacion
      : participaciones.participacion
      ? [participaciones.participacion]
      : [];

    for (const part of partArray) {
      if (part.rut && !this.validarRutChileno(part.rut)) {
        validaciones.push({
          tipo: 'ERROR',
          texto: `RUT inválido: ${part.rut}`,
          campo: 'participaciones',
        });
      }
    }

    return validaciones;
  }

  private validarRutChileno(rut: string): boolean {
    // Eliminar puntos y guiones
    const rutLimpio = rut.replace(/[.\-]/g, '');

    // Validar formato básico
    if (!/^\d{7,8}[\dKk]$/.test(rutLimpio)) {
      return false;
    }

    // Validar dígito verificador
    const cuerpo = rutLimpio.slice(0, -1);
    const dv = rutLimpio.slice(-1).toUpperCase();

    let suma = 0;
    let multiplicador = 2;

    for (let i = cuerpo.length - 1; i >= 0; i--) {
      suma += parseInt(cuerpo[i]) * multiplicador;
      multiplicador = multiplicador === 7 ? 2 : multiplicador + 1;
    }

    const resto = suma % 11;
    const dvCalculado = resto < 2 ? String(resto) : resto === 2 ? 'K' : String(11 - resto);

    return dv === dvCalculado;
  }
}





