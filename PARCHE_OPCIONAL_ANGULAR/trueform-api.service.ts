import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UsuarioDTO {
 idUsuario: number; nombres: string; apellidos: string; email: string;
 nivel: string; edad: number; peso?: number; altura?: number; idObjetivo?: number; equipo?: string;
}
export interface RegistroUsuarioDTO {
 nombres: string; apellidos: string; email: string; password: string;
 edad: number; peso: number; altura: number; nivel: string; idObjetivo: number; equipo: string;
}
export interface ObjetivoDTO { idObjetivo: number; nombreObjetivo: string; descripcion?: string; }

@Injectable({providedIn:'root'})
export class TrueformApiService {
 private readonly http=inject(HttpClient);
 // Angular envia /api a Spring Boot mediante proxy. SIN JWT ni CSRF (solo local).
 objetivos():Observable<ObjetivoDTO[]> { return this.http.get<ObjetivoDTO[]>('/api/objetivos'); }
 registrar(datos:RegistroUsuarioDTO):Observable<UsuarioDTO> {return this.http.post<UsuarioDTO>('/api/usuarios', datos);}
 iniciarSesion(email:string,password:string):Observable<UsuarioDTO> {
  return this.http.post<UsuarioDTO>('/api/auth/login', {email, password});
 }
}
