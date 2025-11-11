import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_VISTOS_BUENOS', { schema: 'DOCUMENTOS' })
export class DocVistoBueno {
  @PrimaryGeneratedColumn({ name: 'ID_VISTO_BUENO', type: 'number' })
  idVistoBueno: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_VISTO_BUENO', type: 'varchar2', length: 50 })
  tipoVistoBueno: string;

  @Column({ name: 'NUMERO', type: 'varchar2', length: 100, nullable: true })
  numero?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.vistosBuenos)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}




