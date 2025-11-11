import 'reflect-metadata';
import { DataSource } from 'typeorm';
import { getDatabaseConfig } from './database.config';
import { DocDocumento } from '../entities/DocDocumento';
import { DocParticipacion } from '../entities/DocParticipacion';
import { DocFechaDocumento } from '../entities/DocFechaDocumento';
import { DocLocacion } from '../entities/DocLocacion';
import { DocObservacion } from '../entities/DocObservacion';
import { DocRelacion } from '../entities/DocRelacion';
import { DocTransporte } from '../entities/DocTransporte';
import { DocTranItemTransporte } from '../entities/DocTranItemTransporte';
import { DocTransGuiaCourier } from '../entities/DocTransGuiaCourier';
import { DocTransbordo } from '../entities/DocTransbordo';
import { DocVistoBueno } from '../entities/DocVistoBueno';
import { DocCargo } from '../entities/DocCargo';
import { DocEstado } from '../entities/DocEstado';
import { DocImagen } from '../entities/DocImagen';

let AppDataSource: DataSource | null = null;
let isInitialized = false;

export async function getDataSource(): Promise<DataSource> {
  if (AppDataSource && AppDataSource.isInitialized) {
    return AppDataSource;
  }

  const config = await getDatabaseConfig();

  AppDataSource = new DataSource({
    type: 'oracle',
    host: config.host,
    port: config.port,
    username: config.username,
    password: config.password,
    database: config.database,
    sid: config.sid,
    synchronize: false,
    logging: config.logging,
    entities: [
      DocDocumento,
      DocParticipacion,
      DocFechaDocumento,
      DocLocacion,
      DocObservacion,
      DocRelacion,
      DocTransporte,
      DocTranItemTransporte,
      DocTransGuiaCourier,
      DocTransbordo,
      DocVistoBueno,
      DocCargo,
      DocEstado,
      DocImagen,
    ],
    extra: {
      poolMin: 2,
      poolMax: 10,
      poolIncrement: 1,
    },
  });

  return AppDataSource;
}

export async function initializeDatabase(): Promise<void> {
  if (!isInitialized) {
    try {
      const dataSource = await getDataSource();
      if (!dataSource.isInitialized) {
        await dataSource.initialize();
      }
      isInitialized = true;
      console.log('Database connection initialized successfully');
    } catch (error) {
      console.error('Error initializing database connection:', error);
      throw error;
    }
  }
}

export async function closeDatabase(): Promise<void> {
  if (isInitialized && AppDataSource && AppDataSource.isInitialized) {
    await AppDataSource.destroy();
    isInitialized = false;
    AppDataSource = null;
    console.log('Database connection closed');
  }
}
