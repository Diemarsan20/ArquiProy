# Sistema de Compra de Planes de Salud (SPS)
**Pontificia Universidad Javeriana — Arquitectura de Software 2026-10**

## Arquitectura

| Servicio              | Tecnología          | Puerto | Descripción                                    |
|-----------------------|---------------------|--------|------------------------------------------------|
| **sps-service**       | Spring Boot + H2    | 8080   | Servicio principal SPS                         |
| **sns-mock**          | Spring Boot         | 8081   | Mock SNS (Superintendencia Nacional de Salud)  |
| **saludpay-service**  | .NET 8 ASP.NET Core | 8082   | Sistema de pago Salud Pay                      |
| **shc-service**       | Spring Boot + H2    | 8083   | Sistema de Historias Clínicas                  |
| **sam-service**       | Spring Boot + H2    | 8084   | Sistema de Agenda Médica                       |
| **RabbitMQ**          | RabbitMQ 3          | 5672 / 15672 | Message broker                           |

## Colas RabbitMQ

| Cola                   | Productor       | Consumidor        | Propósito                              |
|------------------------|-----------------|-------------------|----------------------------------------|
| `shc-queue`            | sps-service     | shc-service       | Notificación de compra terminada a SHC |
| `sam-queue`            | sps-service     | sam-service       | Servicios médicos a agendar en SAM     |
| `saludpay-solicitudes` | sps-service     | saludpay-service  | Compras pendientes de pago             |
| `saludpay-pagos`       | saludpay-service| sps-service       | Confirmación de pago                   |

## Flujo de la compra

```
Cliente → POST /api/compras (SPS)
  → Estado: PENDIENTE_VALIDACION
  → @Scheduled cada 15s llama SNS con WebClient (sin MOM)
  → SNS retorna ENPROCESO (x2) → APROBADO
  → Estado: PENDIENTE_PAGO → MQ → saludpay-solicitudes
  → Cliente va a SaludPay → POST /api/compras/pagar
  → MQ → saludpay-pagos → SPS
  → Estado: TERMINADA
  → MQ → shc-queue (datos de planes + persona)
  → MQ → sam-queue (servicios médicos)
```

## Cómo ejecutar

### Requisitos
- Docker Desktop instalado y corriendo

### Levantar todo
```bash
docker-compose up --build
```

### Verificar servicios
- RabbitMQ Management: http://localhost:15672 (adminclientes / adminclientes123)
- SPS H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:file:./data/spsdb`)
- SHC H2 Console: http://localhost:8083/h2-console
- SAM H2 Console: http://localhost:8084/h2-console
- SaludPay Swagger: http://localhost:8082/swagger

## Ejemplo de flujo completo (con curl / Postman)

### 1. Login en SPS
```
POST http://localhost:8080/api/auth/login
{"cedula": "11111111", "password": "pass123"}
```

### 2. Ver planes disponibles
```
GET http://localhost:8080/api/planes
```

### 3. Crear compra (planes 1 y 2)
```
POST http://localhost:8080/api/compras
{"cedulaCliente": "11111111", "planIds": [1, 2]}
```

### 4. Consultar estado de compra (esperar ~30s para validación SNS)
```
GET http://localhost:8080/api/compras/codigo/{codigoCompra}
```

### 5. Login en SaludPay
```
POST http://localhost:8082/api/auth/login
{"cedula": "11111111", "password": "pass123"}
```

### 6. Ver compras pendientes en SaludPay
```
GET http://localhost:8082/api/compras/pendientes/11111111
```

### 7. Pagar en SaludPay
```
POST http://localhost:8082/api/compras/pagar
{"cedulaCliente": "11111111", "numeroCompra": "SPS-XXXXXXXX", "valorPagado": 1100000.0}
```

### 8. Verificar compra terminada en SPS
```
GET http://localhost:8080/api/compras/codigo/{codigoCompra}
```

### 9. Verificar en SHC y SAM
```
GET http://localhost:8083/api/shc/registros
GET http://localhost:8084/api/sam/agenda
```

## Archivos fuente utilizados

| Archivo txt                  | Uso en el proyecto                                                |
|------------------------------|-------------------------------------------------------------------|
| `05_textos_h2 (1).txt`       | Configuración H2, entidades, servicios y controladores CRUD       |
| `08_textos_eda_f.txt`        | RabbitMQConfig, ProductorDeMensajesService, ListenerEventos       |
| `07-textos.txt`              | WebClientConfig, SincronizadorService (→ SnsValidacionService)    |
| `textos_net (2).txt`         | Program.cs, ClientesContext, Controller, Service (→ SaludPay)     |
| `textos_RabbitMQ (2).txt`    | RabbitMqService .NET, consumer/producer (→ SaludPay)              |
| `09-textos (1).txt`          | Referencia de arquitectura nginx (informativo)                    |

## Tecnologías
- **Spring Boot 3.3.5** (Java 17)
- **RabbitMQ 3** con `spring-boot-starter-amqp` y `Jackson2JsonMessageConverter`
- **H2** (modo file `jdbc:h2:file:./data/...`) — una BD por microservicio Java
- **.NET 8 ASP.NET Core** — SaludPay con EF Core InMemory
- **Docker + Docker Compose**
