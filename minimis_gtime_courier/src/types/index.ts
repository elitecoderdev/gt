// Tipos para el documento Gtime
export interface PayloadVO {
  id: string;
  documento: Gtime;
}

export interface Gtime {
  idPayload?: number;
  startTime?: number;
  fechaEncolamiento?: string;
  fechaDesencolamiento?: string;
  nroEncabezado?: string;
  pwd?: string;
  xml?: string;
  user?: string;
  parcial?: string;
  tipo?: string;
  version?: string;
  valorDeclarado?: string;
  monedaValor?: string;
  userHostOrigen?: string;
  idSender?: string;
  numeroReferencia?: string;
  unidadPeso?: string;
  tipoOperacion?: string;
  totalPeso?: string;
  tipoAccion?: string;
  totalItem?: string;
  totalBultos?: string;
  totalVolumen?: string;
  unidadVolumen?: string;
  login?: string;
  loginDigitador?: string;
  tipoDocumento?: string;
  estadoGtime?: string;
  fechas?: Fechas;
  participaciones?: Participaciones;
  locaciones?: Locaciones;
  referencias?: Referencias;
  items?: Items;
  cargos?: Cargos;
  observaciones?: Observaciones;
  transbordos?: Transbordos;
  vistosBuenos?: VistosBuenos;
}

export interface Fechas {
  fecha?: Fecha | Fecha[];
}

export interface Fecha {
  tipo?: string;
  valor?: string;
}

export interface Participaciones {
  participacion?: Participacion | Participacion[];
}

export interface Participacion {
  tipo?: string;
  rut?: string;
  nombre?: string;
  direccion?: string;
  ciudad?: string;
  pais?: string;
}

export interface Locaciones {
  locacion?: Locacion | Locacion[];
}

export interface Locacion {
  tipo?: string;
  codigo?: string;
  nombre?: string;
}

export interface Referencias {
  referencia?: Referencia | Referencia[];
}

export interface Referencia {
  tipo?: string;
  numero?: string;
}

export interface Items {
  item?: Item | Item[];
}

export interface Item {
  numero?: string;
  descripcion?: string;
  cantidad?: string;
  peso?: string;
  volumen?: string;
  productos?: ProdItems;
}

export interface ProdItems {
  prodItem?: ProdItem | ProdItem[];
}

export interface ProdItem {
  codigo?: string;
  descripcion?: string;
  cantidad?: string;
}

export interface Cargos {
  cargo?: Cargo | Cargo[];
}

export interface Cargo {
  tipo?: string;
  monto?: string;
  moneda?: string;
}

export interface Observaciones {
  observacion?: Observacion | Observacion[];
}

export interface Observacion {
  tipo?: string;
  texto?: string;
}

export interface Transbordos {
  transbordo?: Transbordo | Transbordo[];
}

export interface Transbordo {
  tipo?: string;
  locacion?: string;
}

export interface VistosBuenos {
  vistoBueno?: VistoBueno | VistoBueno[];
}

export interface VistoBueno {
  tipo?: string;
  numero?: string;
}

// Tipos para validación
export interface Validacion {
  tipo: 'ERROR' | 'WARNING';
  texto: string;
  campo?: string;
}

export interface DocumentoResponse {
  nroDocumento?: string;
  listValidaciones: Validacion[];
  gtime: Gtime;
  user?: string;
  xml?: string;
  listEstadoPersistencia?: Persistencia[];
  estadoFinal: 'ACEPTADO' | 'RECHAZADO';
  fem?: Date;
  tipoManifiesto?: string;
  viaTransporte?: string;
  contieneFEM?: boolean;
}

export interface Persistencia {
  tipo: string;
  valido: boolean;
  schemaTable?: string;
  primariKey?: string;
}

// Tipos para eventos
export interface DocumentoRecibidoEvent {
  source: 'gtime.courier';
  'detail-type': 'documento.recibido';
  detail: {
    idPayload: string;
    gtime: Gtime;
    timestamp: string;
  };
}

export interface DocumentoValidadoEvent {
  source: 'gtime.courier';
  'detail-type': 'documento.validado';
  detail: {
    idPayload: string;
    documentoResponse: DocumentoResponse;
    timestamp: string;
  };
}

export interface DocumentoProcesadoEvent {
  source: 'gtime.courier';
  'detail-type': 'documento.procesado';
  detail: {
    idPayload: string;
    estado: 'ACEPTADO' | 'RECHAZADO';
    timestamp: string;
  };
}





