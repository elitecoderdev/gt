import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_TRANSBORDOS', { schema: 'DOCUMENTOS' })
export class DocTransbordo {
  @PrimaryGeneratedColumn({ name: 'ID_TRANSBORDO', type: 'number' })
  idTransbordo: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_TRANSBORDO', type: 'varchar2', length: 50 })
  tipoTransbordo: string;

  @Column({ name: 'LOCACION', type: 'varchar2', length: 100, nullable: true })
  locacion?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.transbordos)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





