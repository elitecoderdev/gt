import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_LOCACION', { schema: 'DOCUMENTOS' })
export class DocLocacion {
  @PrimaryGeneratedColumn({ name: 'ID_LOCACION', type: 'number' })
  idLocacion: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_LOCACION', type: 'varchar2', length: 50 })
  tipoLocacion: string;

  @Column({ name: 'CODIGO', type: 'varchar2', length: 50, nullable: true })
  codigo?: string;

  @Column({ name: 'NOMBRE', type: 'varchar2', length: 200, nullable: true })
  nombre?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.locaciones)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





