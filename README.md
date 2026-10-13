# LimpiezaIT API

API REST de productos de limpieza, proyecto integrador del curso **Java Backend** de educaciónIT.

## Stack

- Java 21
- Spring Boot 4.1
- Maven

Más adelante: JPA + H2, Spring Security + JWT, springdoc/Swagger.

## Cómo correrlo

**Desde Eclipse:** clic derecho sobre el proyecto → *Run As → Spring Boot App*.

**Desde la terminal:**

```bash
./mvnw spring-boot:run
```

URL base: http://localhost:8083

Prueba rápida:

```bash
curl http://localhost:8083/api/ping
# {"app":"LimpiezaIT","estado":"ok"}
```

## Endpoints

La colección de Postman está en `postman/LimpiezaIT.postman_collection.json` (variable `baseUrl` = `http://localhost:8083`).

| Método | URL | Descripción | Rol requerido |
|---|---|---|---|
| `GET` | `/api/ping` | Verifica que la API esté levantada | — |
| `GET` | `/api/productos` | Lista todos los productos | — |
| `GET` | `/api/productos/{id}` | Busca un producto por id (404 si no existe) | — |
| `POST` | `/api/productos` | Crea un producto (201) | — |
| `PUT` | `/api/productos/{id}` | Modifica un producto (404 si no existe) | — |
| `DELETE` | `/api/productos/{id}` | Borra un producto (204, o 404 si no existe) | — |

## Estructura del repositorio

```
limpiezait-api/
├── src/        código fuente y tests
├── postman/    colección y environment de Postman exportados
├── docs/       openapi.yaml (clase 6), diagramas, notas
├── pom.xml
└── README.md
```

## Recorrido del curso

Al terminar cada clase se crea una etiqueta sobre el último commit:

| Etiqueta | Lo que agrega |
|---|---|
| `clase-1` | CRUD de productos en memoria |
| `clase-2` | Arquitectura en capas + manejo de errores |
| `clase-3` | JPA + H2, Categorías, DTOs y mappers |
| `clase-4` | Spring Security con Basic Auth y roles |
| `clase-5` | Login con JWT y usuarios en base de datos |
| `v1.0` | Documentación con Swagger/OpenAPI |

## Cómo ponerte al día si te atrasaste

Para arrancar la clase N desde donde terminó la clase anterior:

```bash
git fetch --tags
git checkout -b mi-clase-N clase-(N-1)
```

Por ejemplo, para arrancar la clase 3: `git checkout -b mi-clase-3 clase-2`.

## Convención de commits

Usamos [Conventional Commits](https://www.conventionalcommits.org/es/), en español y en imperativo:

```
tipo(alcance): descripción corta
```

| Tipo | Cuándo usarlo |
|---|---|
| `feat` | Funcionalidad nueva (endpoint, entidad, servicio) |
| `refactor` | Cambio de estructura sin cambiar comportamiento (ej. pasar a capas) |
| `fix` | Corrección de un error |
| `test` | Tests |
| `docs` | README, `docs/`, colección Postman |
| `chore` | Configuración, dependencias, `pom.xml`, `.gitignore` |

**Alcance:** la capa o el tema: `model`, `controller`, `service`, `repository`, `exception`, `config`, `security`, `dto`, `mapper`, `postman`.

Ejemplos:

```
feat(model): agregar entidad Producto
feat(controller): CRUD de productos en memoria
refactor(service): mover lógica de negocio a ProductoService
feat(exception): manejo global de errores con @RestControllerAdvice
chore(pom): agregar Spring Data JPA y H2
docs(postman): exportar colección clase 2
```

Regla: **un commit por paso de la guía de la clase**. Hacer varios commits por clase está bien.

### Etiquetas y ramas

- Al terminar cada clase, se crea una etiqueta **anotada** sobre el último commit:
  ```bash
  git tag -a clase-N -m "Fin clase N: <resumen>"
  ```
- Al final del curso se crea la etiqueta `v1.0`.
- Se trabaja sobre una sola rama, `main`, y no se crean ramas por clase.
- Los experimentos o desafíos van en ramas `desafio/<nombre>`, que no se mergean a `main` sin probarlas.
