import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_RELACION', { schema: 'DOCUMENTOS' })
export class DocRelacion {
  @PrimaryGeneratedColumn({ name: 'ID_RELACION', type: 'number' })
  idRelacion: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_REFERENCIA', type: 'varchar2', length: 50 })
  tipoReferencia: string;

  @Column({ name: 'NUMERO', type: 'varchar2', length: 100, nullable: true })
  numero?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.referencias)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





