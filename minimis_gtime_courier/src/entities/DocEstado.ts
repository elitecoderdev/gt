import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_ESTADO', { schema: 'DOCUMENTOS' })
export class DocEstado {
  @PrimaryGeneratedColumn({ name: 'ID_ESTADO', type: 'number' })
  idEstado: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'ESTADO', type: 'varchar2', length: 50 })
  estado: string;

  @Column({ name: 'FECHA_ESTADO', type: 'date' })
  fechaEstado: Date;

  @ManyToOne(() => DocDocumento, (documento) => documento.estados)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





