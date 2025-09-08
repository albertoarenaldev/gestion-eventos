import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TipoEvento } from './interface/tipo-evento';
import { TipoEventoComponent } from "./component/tipo-evento/tipo-evento.component";

@Component({
  standalone: true,
  imports: [RouterOutlet, TipoEventoComponent],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  title = 'front';
}
