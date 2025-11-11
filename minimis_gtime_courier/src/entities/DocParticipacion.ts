import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_PARTICIPACIONES', { schema: 'DOCUMENTOS' })
export class DocParticipacion {
  @PrimaryGeneratedColumn({ name: 'ID_PARTICIPACION', type: 'number' })
  idParticipacion: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_PARTICIPANTE', type: 'varchar2', length: 50 })
  tipoParticipante: string;

  @Column({ name: 'RUT', type: 'varchar2', length: 20, nullable: true })
  rut?: string;

  @Column({ name: 'NOMBRE', type: 'varchar2', length: 200, nullable: true })
  nombre?: string;

  @Column({ name: 'DIRECCION', type: 'varchar2', length: 500, nullable: true })
  direccion?: string;

  @Column({ name: 'CIUDAD', type: 'varchar2', length: 100, nullable: true })
  ciudad?: string;

  @Column({ name: 'PAIS', type: 'varchar2', length: 3, nullable: true })
  pais?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.participaciones)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





