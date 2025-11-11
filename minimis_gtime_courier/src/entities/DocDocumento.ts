import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  OneToMany,
} from 'typeorm';
import { DocParticipacion } from './DocParticipacion';
import { DocFechaDocumento } from './DocFechaDocumento';
import { DocLocacion } from './DocLocacion';
import { DocObservacion } from './DocObservacion';
import { DocRelacion } from './DocRelacion';
import { DocTransporte } from './DocTransporte';
import { DocTransGuiaCourier } from './DocTransGuiaCourier';
import { DocTransbordo } from './DocTransbordo';
import { DocVistoBueno } from './DocVistoBueno';
import { DocCargo } from './DocCargo';
import { DocEstado } from './DocEstado';
import { DocImagen } from './DocImagen';
import { DocTranItemTransporte } from './DocTranItemTransporte';

@Entity('DOC_DOCUMENTOS', { schema: 'DOCUMENTOS' })
export class DocDocumento {
  @PrimaryGeneratedColumn({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'NRO_ENCABEZADO', type: 'varchar2', length: 50 })
  nroEncabezado: string;

  @Column({ name: 'TIPO_DOCUMENTO', type: 'varchar2', length: 50 })
  tipoDocumento: string;

  @Column({ name: 'VERSION', type: 'varchar2', length: 20, nullable: true })
  version?: string;

  @Column({ name: 'NUMERO_REFERENCIA', type: 'varchar2', length: 100, nullable: true })
  numeroReferencia?: string;

  @Column({ name: 'TOTAL_BULTOS', type: 'number', nullable: true })
  totalBultos?: number;

  @Column({ name: 'TOTAL_PESO', type: 'number', nullable: true })
  totalPeso?: number;

  @Column({ name: 'TOTAL_VOLUMEN', type: 'number', nullable: true })
  totalVolumen?: number;

  @Column({ name: 'TOTAL_ITEM', type: 'number', nullable: true })
  totalItem?: number;

  @Column({ name: 'VALOR_DECLARADO', type: 'number', nullable: true })
  valorDeclarado?: number;

  @Column({ name: 'MONEDA_VALOR', type: 'varchar2', length: 3, nullable: true })
  monedaValor?: string;

  @Column({ name: 'UNIDAD_PESO', type: 'varchar2', length: 10, nullable: true })
  unidadPeso?: string;

  @Column({ name: 'UNIDAD_VOLUMEN', type: 'varchar2', length: 10, nullable: true })
  unidadVolumen?: string;

  @Column({ name: 'PARCIAL', type: 'varchar2', length: 1, nullable: true })
  parcial?: string;

  @Column({ name: 'TIPO_OPERACION', type: 'varchar2', length: 50, nullable: true })
  tipoOperacion?: string;

  @Column({ name: 'TIPO_ACCION', type: 'varchar2', length: 50, nullable: true })
  tipoAccion?: string;

  @Column({ name: 'USER_HOST_ORIGEN', type: 'varchar2', length: 100, nullable: true })
  userHostOrigen?: string;

  @Column({ name: 'ID_SENDER', type: 'varchar2', length: 100, nullable: true })
  idSender?: string;

  @Column({ name: 'LOGIN', type: 'varchar2', length: 100, nullable: true })
  login?: string;

  @Column({ name: 'LOGIN_DIGITADOR', type: 'varchar2', length: 100, nullable: true })
  loginDigitador?: string;

  @Column({ name: 'ID_PAYLOAD', type: 'number', nullable: true })
  idPayload?: number;

  @Column({ name: 'ESTADO', type: 'varchar2', length: 50, nullable: true })
  estado?: string;

  @CreateDateColumn({ name: 'FECHA_CREACION', type: 'date' })
  fechaCreacion: Date;

  @UpdateDateColumn({ name: 'FECHA_ACTUALIZACION', type: 'date', nullable: true })
  fechaActualizacion?: Date;

  // Relaciones
  @OneToMany(() => DocParticipacion, (participacion) => participacion.documento)
  participaciones?: DocParticipacion[];

  @OneToMany(() => DocFechaDocumento, (fecha) => fecha.documento)
  fechas?: DocFechaDocumento[];

  @OneToMany(() => DocLocacion, (locacion) => locacion.documento)
  locaciones?: DocLocacion[];

  @OneToMany(() => DocObservacion, (observacion) => observacion.documento)
  observaciones?: DocObservacion[];

  @OneToMany(() => DocRelacion, (relacion) => relacion.documento)
  referencias?: DocRelacion[];

  @OneToMany(() => DocTransporte, (transporte) => transporte.documento)
  transportes?: DocTransporte[];

  @OneToMany(() => DocTranItemTransporte, (item) => item.documento)
  items?: DocTranItemTransporte[];

  @OneToMany(() => DocTransGuiaCourier, (guia) => guia.documento)
  guiasCourier?: DocTransGuiaCourier[];

  @OneToMany(() => DocTransbordo, (transbordo) => transbordo.documento)
  transbordos?: DocTransbordo[];

  @OneToMany(() => DocVistoBueno, (vistoBueno) => vistoBueno.documento)
  vistosBuenos?: DocVistoBueno[];

  @OneToMany(() => DocCargo, (cargo) => cargo.documento)
  cargos?: DocCargo[];

  @OneToMany(() => DocEstado, (estado) => estado.documento)
  estados?: DocEstado[];

  @OneToMany(() => DocImagen, (imagen) => imagen.documento)
  imagenes?: DocImagen[];
}





