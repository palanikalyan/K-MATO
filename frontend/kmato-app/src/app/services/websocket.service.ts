import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebsocketService {
  private socket: WebSocket | null = null;
  private readonly message$ = new Subject<any>();

  connect() {
    if (this.socket) return;
    try {
      this.socket = new WebSocket(environment.wsUrl);
      this.socket.onmessage = (ev) => {
        // try to parse JSON payloads
        try { this.message$.next(JSON.parse(ev.data)); } catch { this.message$.next(ev.data); }
      };
      this.socket.onopen = () => console.log('WebSocket connected');
      this.socket.onerror = (e) => console.warn('WebSocket error', e);
    } catch (err) {
      console.warn('Unable to create WebSocket connection', err);
    }
  }

  messages() { return this.message$.asObservable(); }
}
