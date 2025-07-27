# Quarkus Movie Review API

Este proyecto es una API RESTful para gestionar películas, críticos y reseñas usando Quarkus, JPA y PostgreSQL.

## Requisitos
- Java 24
- Maven
- Docker y Docker Compose

## 1. Levantar la base de datos PostgreSQL

En la raíz del proyecto ejecuta:

```sh
docker-compose -f src/main/docker/docker-compose.yml up -d
```

Esto creará una base de datos PostgreSQL con:
- **Base de datos:** `quarkusDB`
- **Usuario:** `postgres` (verifica en el docker-compose.yml)
- **Contraseña:** `12345`
- **Puerto:** `5432`

> **Nota:** Si cambiaste el usuario en el docker-compose.yml, asegúrate de que `src/main/resources/application.properties` tenga el mismo usuario y contraseña.

## 2. Configuración de la aplicación

El archivo `src/main/resources/application.properties` ya está configurado para conectarse a la base de datos PostgreSQL levantada por Docker.

## 3. Compilar y correr la aplicación

```sh
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path


./mvnw clean package

Si sale error: .\mvnw clean package -DskipTests

./mvnw quarkus:dev
```

La API estará disponible en: [http://localhost:8080](http://localhost:8080)

## 4. Endpoints CRUD

### Películas (Movie)
- **Crear:**
  ```sh
  curl -X POST http://localhost:8080/movies -H "Content-Type: application/json" -d "{\"title\":\"Inception\"}"

  ```
- **Listar:**
  ```sh
  curl http://localhost:8080/movies
  ```
- **Obtener por ID:**
  ```sh
  curl http://localhost:8080/movies/1
  ```
- **Actualizar:**
  ```sh
  curl -X PUT http://localhost:8080/movies/1 -H "Content-Type: application/json" -d "{\"title\":\"Matrix\"}"

  ```
- **Eliminar:**
  ```sh
  curl -X DELETE http://localhost:8080/movies/1
  ```

### Críticos (Critic)
- **Crear:**
  ```sh
  curl -X POST http://localhost:8080/critics -H "Content-Type: application/json" -d "{\"name\":\"Roger Ebert\"}"

  ```
- **Listar:**
  ```sh
  curl http://localhost:8080/critics
  ```

### Reseñas (Review)
- **Crear:**
  ```sh
  curl -X POST http://localhost:8080/reviews -H "Content-Type: application/json" -d "{\"movieId\":1,\"criticId\":1,\"rating\":5,\"comment\":\"Excelente!\"}"

  ```
- **Listar:**
  ```sh
  curl http://localhost:8080/reviews
  ```

## 5. Notas adicionales
- Si cambias el usuario o contraseña de la base de datos en el docker-compose.yml, actualiza también el archivo `application.properties`.
- Si tienes problemas de conexión, asegúrate de que el contenedor de PostgreSQL esté corriendo y accesible en el puerto 5432.
- Puedes detener la base de datos con:
  ```sh
  docker-compose -f src/main/docker/docker-compose.yml down
  ```
- **Si necesitas reiniciar la base de datos y eliminar todos los datos (por ejemplo, para restablecer la contraseña o limpiar el volumen), ejecuta:**
  ```sh
  docker-compose -f src/main/docker/docker-compose.yml down -v
  docker-compose -f src/main/docker/docker-compose.yml up -d
  ```
  > Esto eliminará todos los datos almacenados en PostgreSQL y recreará el contenedor con la configuración actual del archivo docker-compose.yml.

---

¡Listo para usar tu API de películas, críticos y reseñas con Quarkus y PostgreSQL!
