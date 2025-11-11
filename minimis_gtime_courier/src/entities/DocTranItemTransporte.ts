import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_TRAN_ITEM_TRANSPORTE', { schema: 'DOCUMENTOS' })
export class DocTranItemTransporte {
  @PrimaryGeneratedColumn({ name: 'ID_ITEM', type: 'number' })
  idItem: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'NUMERO', type: 'varchar2', length: 50, nullable: true })
  numero?: string;

  @Column({ name: 'DESCRIPCION', type: 'varchar2', length: 500, nullable: true })
  descripcion?: string;

  @Column({ name: 'CANTIDAD', type: 'number', nullable: true })
  cantidad?: number;

  @Column({ name: 'PESO', type: 'number', nullable: true })
  peso?: number;

  @Column({ name: 'VOLUMEN', type: 'number', nullable: true })
  volumen?: number;

  @ManyToOne(() => DocDocumento, (documento) => documento.items)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





