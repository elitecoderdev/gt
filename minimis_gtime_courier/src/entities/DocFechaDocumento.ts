import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_FECHA_DOCUMENTO', { schema: 'DOCUMENTOS' })
export class DocFechaDocumento {
  @PrimaryGeneratedColumn({ name: 'ID_FECHA', type: 'number' })
  idFecha: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_FECHA', type: 'varchar2', length: 50 })
  tipoFecha: string;

  @Column({ name: 'VALOR', type: 'date' })
  valor: Date;

  @ManyToOne(() => DocDocumento, (documento) => documento.fechas)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





