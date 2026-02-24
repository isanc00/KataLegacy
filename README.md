# Kata Legacy- Motor de Migración

Un motor de traducción automatizada diseñado para modernizar sistemas *Core* transaccionales hacia arquitecturas modernas basadas en leguajes de alto nivel. 

Construido con un enfoque de **Arquitectura Hexagonal**, el sistema utiliza un Árbol de Sintaxis Abstracta (AST) para garantizar una traducción semántica precisa, respaldado por una infraestructura *Cloud Native* orientada a alta disponibilidad y observabilidad.

## Arquitectura del Sistema

El proyecto está dividido en dos componentes principales orquestados nativamente mediante contenedores:


* **Backend (Motor Hexagonal):** Construido en Spring Boot (Java 21). Utiliza ANTLR para el análisis léxico y sintáctico del código fuente, aplicando reglas de negocio para la generación de código moderno.
* **Frontend (SPA Reactiva):** Construido en Angular 19. Proporciona una interfaz de auditoría y traducción en tiempo real, servida a través de un proxy inverso NGINX de alto rendimiento.

## ✨ Características Principales

* **Traducción Basada en AST:** El motor comprende la gramática del código fuente para una conversión estructural segura.
* **Infraestructura Cloud Native:** Listo para ser desplegado en entornos Serverless (Google Cloud Run) o clústeres de Kubernetes (GKE).
* **Observabilidad End-to-End:** Implementación nativa de *Correlation IDs* (`X-Transaction-Id`) generados en el frontend e inyectados en el MDC (Mapped Diagnostic Context) de Spring Boot.
* **Logging Estructurado:** Logs emitidos en formato JSON puro, listos para ser ingeridos e indexados por Google Cloud Logging sin agentes externos.
* **Seguridad por Diseño:** Frontend protegido con cabeceras de seguridad estrictas (X-Frame-Options, XSS Protection, nosniff) a nivel de servidor web.

### Decisiones Arquitectónicas

- Se optó por arquitectura hexagonal para desacoplar el motor de migración de detalles de infraestructura.
- El transactionId fue movido del body al header HTTP para separar metadata de transporte del modelo de dominio.
- Se eligió generación stateless por request para evitar acoplamiento a estado global.

## Stack Tecnológico

| Capa | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Backend** | Java / Spring Boot | Lógica core y controladores REST |
| **Parser** | ANTLR4 | Análisis léxico, sintáctico y AST |
| **Frontend** | Angular 19 | Interfaz de usuario (Application Builder) |
| **Web Server** | NGINX (Alpine) | Reverse proxy, compresión Gzip y enrutamiento SPA |
| **DevOps** | Docker & Docker Compose | Containerización (Multi-stage builds) y orquestación local |

## 🔧 Gestión de Entornos

El sistema utiliza perfiles de Spring Boot para segmentar comportamiento por entorno:

- `dev`: Documentación OpenAPI habilitada y logging extendido.
- `prod`: Documentación deshabilitada y logging estructurado optimizado para Cloud Logging.

## Guía de Inicio Rápido (Entorno Local)

La experiencia de desarrollo (DX) está completamente automatizada. No necesitas instalar Node.js ni el JDK de Java en tu máquina anfitriona.

### Prerrequisitos
* Docker Engine y Docker Compose instalados.

### Despliegue Local

1. levantarlos contenedores 
    ```bash
    docker compose up -d --build

2. verificar logs
    ```bash
    docker compose logs -f backend

3. Accede a la plataforma:

- Interfaz de Usuario: http://localhost:4200

- Backend API (Proxy interno): http://localhost:4200/api/v1/...
