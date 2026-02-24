import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { MigrationService } from './services/migration.service';
import { LoaderService } from './shared/services/loader.service';
import { LoaderComponent } from './shared/components/loader.component';
import { randomInt } from 'node:crypto';
import { MigrationRequest, MigrationResponse } from './models/models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, MonacoEditorModule, LoaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  private migrationService = inject(MigrationService);
  private loaderService = inject(LoaderService);

  appliedRules: string[] = [];
  correlationId: string = '';
  executionTimeMs: string = '0';
  modernCode: string = '';
  status: string = '';
  transactionCode: string = '';
  warnings: string[] = [];

  editorOptionsCobol = {
    theme: 'vs-dark',
    language: 'cobol',
    automaticLayout: true,
  };
  editorOptionsJava = {
    theme: 'vs-dark',
    language: 'java',
    readOnly: true,
    automaticLayout: true,
  };

  sourceLanguage = 'COBOL';
  targetLanguage = 'JAVA';

  legacyCode =
    'IF AMOUNT > 0\n  DISPLAY "VALID"\nELSE\n  DISPLAY "INVALID"\nEND-IF';
  targetCode = '// El código modernizado aparecerá aquí...';

  traducir() {
    console.log('Traduciendo el siguiente código:\n', this.legacyCode);
    this.targetLanguage = 'JAVA';
    this.targetCode = '// Código traducido de COBOL a JAVA aparecerá aquí...';

    this.loaderService.show();

    const request: MigrationRequest = {
      transactionCode: 'TRX-001' + crypto.randomUUID(),
      sourceLanguage: this.sourceLanguage,
      targetLanguage: this.targetLanguage,
      legacyCode: this.legacyCode,
    };

    this.migrationService.translateCode(request).subscribe({
      next: (response) => {
        console.log('Respuesta del backend recibida:', response);

        if(response.status != 'FAILED') {
          this.appliedRules = response.appliedRules || [];
          this.correlationId = response.correlationId;
          this.executionTimeMs = response.executionTimeMs;
          this.modernCode = response.modernCode;
          this.status = response.status;
          this.transactionCode = response.transactionCode;
          this.warnings = response.warnings || [];
        }else {
          this.status = 'FAILED';
          this.modernCode = '// La traducción ha fallado. Por favor, revisa el código de origen e inténtalo de nuevo. Si el problema persiste, contacta al soporte técnico.\n\nDetalles del error:\n' + (response.warnings ? response.warnings.join('\n') : 'No se proporcionaron detalles adicionales.');
        }
        this.targetCode = this.modernCode;
        this.loaderService.hide();


      },
      error: (error) => {
        console.error('Error al traducir el código:', error);
        this.targetCode =
          '// Error al traducir el código. Por favor, inténtalo de nuevo.';
        this.loaderService.hide();
      },
    });
  }

  limpiar() {
    this.targetCode = '// El código modernizado aparecerá aquí...';
    this.legacyCode = '// Escribe tu código '+ this.sourceLanguage+' aquí...';
    this.appliedRules = [];
    this.correlationId = '';
    this.executionTimeMs = '0';
    this.modernCode = '';
    this.status = '';
    this.transactionCode = '';
    this.warnings = [];
    console.log('Valores reseteados. Listo para una nueva traducción.');
  }

  onSourceChange() {
    console.log('Lenguaje de origen cambiado a:', this.sourceLanguage);
  }

  onTargetChange() {
    console.log('Lenguaje de destino cambiado a:', this.targetLanguage);
  }
}
