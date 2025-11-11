import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_OBSERVACION', { schema: 'DOCUMENTOS' })
export class DocObservacion {
  @PrimaryGeneratedColumn({ name: 'ID_OBSERVACION', type: 'number' })
  idObservacion: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_OBSERVACION', type: 'varchar2', length: 50 })
  tipoObservacion: string;

  @Column({ name: 'TEXTO', type: 'clob', nullable: true })
  texto?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.observaciones)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





