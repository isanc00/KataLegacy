import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, MonacoEditorModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
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

  cobolCode =
    'IF AMOUNT > 0\n  DISPLAY "VALID"\nELSE\n  DISPLAY "INVALID"\nEND-IF';
  javaCode = '// El código modernizado aparecerá aquí...';

  traducir() {
    console.log('Traduciendo el siguiente código:\n', this.cobolCode);
    // Aquí irá la llamada al servicio HTTP
  }

  limpiar() {
    this.cobolCode = '';
    this.javaCode = '';
  }

  onSourceChange() {
    console.log('Lenguaje de origen cambiado a:', this.sourceLanguage);
  }

  onTargetChange() {
    console.log('Lenguaje de destino cambiado a:', this.targetLanguage);

  }
}
