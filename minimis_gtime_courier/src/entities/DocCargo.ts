import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { DocDocumento } from './DocDocumento';

@Entity('DOC_CARGOS', { schema: 'DOCUMENTOS' })
export class DocCargo {
  @PrimaryGeneratedColumn({ name: 'ID_CARGO', type: 'number' })
  idCargo: number;

  @Column({ name: 'ID_DOCUMENTO', type: 'number' })
  idDocumento: number;

  @Column({ name: 'TIPO_CARGO', type: 'varchar2', length: 50 })
  tipoCargo: string;

  @Column({ name: 'MONTO', type: 'number', nullable: true })
  monto?: number;

  @Column({ name: 'MONEDA', type: 'varchar2', length: 3, nullable: true })
  moneda?: string;

  @ManyToOne(() => DocDocumento, (documento) => documento.cargos)
  @JoinColumn({ name: 'ID_DOCUMENTO' })
  documento: DocDocumento;
}





