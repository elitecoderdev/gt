import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_TRANSPORTE', { schema: 'DOCUMENTOS' })
export class DocTransporte {
  @PrimaryGeneratedColumn({ name: 'ID_TRANSPORTE', type: 'number' })
  idTransporte: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_TRANSPORTE', type: 'varchar2', length: 50, nullable: true })
  tipoTransporte?: string;

  @Column({ name: 'NUMERO', type: 'varchar2', length: 100, nullable: true })
  numero?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.transportes)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





