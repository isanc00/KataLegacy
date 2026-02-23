import { Component, inject, input } from '@angular/core';
import { LoaderService } from '../services/loader.service';

@Component({
  selector: 'app-loader',
  standalone: true,
  template: `
    @if (loaderService.isLoading()) {
      <div class="loader-overlay">
        <div class="loader"></div>
        <p>{{ message() }}</p>
      </div>
    }
  `,
  styleUrl: './loader.component.scss'
})
export class LoaderComponent {

  message = input<string>('Analizando AST y generando código...');
  loaderService = inject(LoaderService);
}
