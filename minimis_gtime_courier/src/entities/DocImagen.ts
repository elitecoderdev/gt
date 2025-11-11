import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_IMAGEN', { schema: 'DOCUMENTOS' })
export class DocImagen {
  @PrimaryGeneratedColumn({ name: 'ID_IMAGEN', type: 'number' })
  idImagen: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_IMAGEN', type: 'varchar2', length: 50, nullable: true })
  tipoImagen?: string;

  @Column({ name: 'IMAGEN', type: 'blob', nullable: true })
  imagen?: Buffer;

  @Column({ name: 'FORMATO', type: 'varchar2', length: 10, nullable: true })
  formato?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.imagenes)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





