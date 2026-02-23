export interface MigrationRequest {
  transactionCode: string;
  sourceLanguage: string;
  targetLanguage?: string;
  legacyCode: string;
}

export interface MigrationResponse {
  correlationId: string;
  transactionCode: string;
  status: string;
  executionTimeMs: string;
  modernCode: string;
  appliedRules: string[];
  warnings: string[];
}
