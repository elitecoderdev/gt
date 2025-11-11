import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_TRANS_GUIA_COURIER', { schema: 'DOCUMENTOS' })
export class DocTransGuiaCourier {
  @PrimaryGeneratedColumn({ name: 'ID_GUIA', type: 'number' })
  idGuia: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'NUMERO_GUIA', type: 'varchar2', length: 100, nullable: true })
  numeroGuia?: string;

  @Column({ name: 'FECHA_GUIA', type: 'date', nullable: true })
  fechaGuia?: Date;

  @ManyToOne(() => DocDocumento, (documento) => documento.guiasCourier)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





