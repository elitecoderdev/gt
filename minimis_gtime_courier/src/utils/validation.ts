import { Gtime } from '../types';

export function validateGtimeStructure(gtime: Gtime): string[] {
  const errors: string[] = [];

  if (!gtime.nroEncabezado) {
    errors.push('nroEncabezado es requerido');
  }

  if (!gtime.tipo && !gtime.tipoDocumento) {
    errors.push('tipo o tipoDocumento es requerido');
  }

  return errors;
}

export function normalizeArray<T>(value: T | T[] | undefined): T[] {
  if (!value) return [];
  return Array.isArray(value) ? value : [value];
}

export function parseDate(value: string | undefined): Date | undefined {
  if (!value) return undefined;
  return new Date(value);
}

export function parseNumber(value: string | undefined): number | undefined {
  if (!value) return undefined;
  const parsed = parseFloat(value);
  return isNaN(parsed) ? undefined : parsed;
}





