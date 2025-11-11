import { SecretsManagerClient, GetSecretValueCommand } from '@aws-sdk/client-secrets-manager';

interface DatabaseConfig {
  host: string;
  port: number;
  database: string;
  sid?: string;
  username: string;
  password: string;
  logging: boolean;
}

let cachedConfig: DatabaseConfig | null = null;

async function getSecret(secretArn: string): Promise<{ username: string; password: string }> {
  const client = new SecretsManagerClient({});
  const command = new GetSecretValueCommand({ SecretId: secretArn });
  const response = await client.send(command);
  
  if (response.SecretString) {
    return JSON.parse(response.SecretString);
  }
  
  throw new Error('Secret not found or invalid');
}

export async function getDatabaseConfig(): Promise<DatabaseConfig> {
  if (cachedConfig) {
    return cachedConfig;
  }

  const dbHost = process.env.DB_HOST || process.env.RDS_HOSTNAME;
  const dbPort = parseInt(process.env.DB_PORT || '1521', 10);
  const dbName = process.env.DB_NAME || process.env.RDS_DB_NAME;
  const dbSid = process.env.DB_SID;
  const secretArn = process.env.DB_SECRET_ARN;

  if (!dbHost || !dbName || !secretArn) {
    throw new Error('Missing required database configuration environment variables');
  }

  const secret = await getSecret(secretArn);

  cachedConfig = {
    host: dbHost,
    port: dbPort,
    database: dbName,
    sid: dbSid,
    username: secret.username,
    password: secret.password,
    logging: process.env.DB_LOGGING === 'true',
  };

  return cachedConfig;
}

export const config = {
  async get() {
    return getDatabaseConfig();
  },
};





