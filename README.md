# Sistema CRUD de Gestión Inmobiliaria

## Resumen del Proyecto

Este proyecto es un sistema CRUD (Crear, Leer, Actualizar, Eliminar) completo diseñado para la gestión de propiedades inmobiliarias. La aplicación permite a los usuarios:

- Crear nuevos listados de propiedades con detalles como dirección, precio, tamaño y descripción
- Ver una lista paginada de propiedades con 3 propiedades por página
- Ver información detallada de cada listado de propiedades
- Actualizar la información de propiedades existentes a través de un formulario interactivo
- Eliminar listados de propiedades que ya no son necesarios
- Buscar propiedades por dirección o descripción

El sistema está construido utilizando una arquitectura moderna de tres capas con un frontend web responsivo, un backend API REST en Spring Boot y una base de datos MySQL para almacenamiento persistente. Todos los componentes están containerizados usando Docker y desplegados en instancias separadas de Amazon EC2, asegurando escalabilidad y separación de responsabilidades.

## Arquitectura del Sistema

La aplicación sigue una arquitectura estándar de tres capas:

### Frontend
- **Tecnologías**: HTML, CSS, JavaScript
- **Características**:
    - Interfaz de usuario responsiva con formularios para la entrada de información de propiedades
    - Validación del lado del cliente para la integridad de datos
    - API Fetch para comunicación asíncrona con el backend
    - Listados interactivos de propiedades con opciones para ver, actualizar y eliminar entradas
    - Paginación con tamaño de página configurable
    - Funcionalidad de búsqueda para filtrar propiedades

### Backend
- **Tecnologías**: Spring Boot, JPA/Hibernate
- **Características**:
    - Endpoints API RESTful para todas las operaciones CRUD
    - Validación de datos y manejo de errores
    - Mapeo ORM entre objetos Java y entidades de base de datos
    - Soporte de paginación a través de Spring Data
    - Funcionalidad de búsqueda mediante métodos personalizados de repositorio
    - Despliegue containerizado usando Docker

### Base de Datos
- **Tecnologías**: MySQL
- **Características**:
    - Base de datos relacional con una tabla de propiedades
    - Almacenamiento persistente para todos los datos de propiedades
    - Despliegue containerizado usando Docker

### Flujo de Interacción

![img_4.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_4.png)

1. **Comunicación Cliente-Servidor**: El frontend se comunica con el backend a través de peticiones HTTP (GET, POST, PUT, DELETE) sobre el puerto 8080.
2. **Persistencia de Datos**: El backend se comunica con la base de datos MySQL usando JPA/Hibernate sobre el puerto TCP 3306.
3. **Aislamiento de Servicios**: Cada capa se ejecuta en su propio contenedor Docker en instancias EC2 separadas para mejorar la seguridad y escalabilidad.

## Diseño de Clases

La aplicación backend sigue una arquitectura en capas con clara separación de responsabilidades:

### Diagrama de Clases (Detallado)

```
+------------------------+       +------------------------+       +------------------------+
|   PropertyController   |       |    PropertyService     |       |  PropertyPersistence   |
+------------------------+       +------------------------+       +------------------------+
| - propertyService      |------>| - propertyPersistence  |------>| + findAll()            |
+------------------------+       +------------------------+       | + findById()           |
| + createProperty()     |       | + create()             |       | + save()               |
| + getProperties()      |       | + getProperties()      |       | + deleteById()         |
| + getProperty()        |       | + getProperty()        |       | + findByAddress...     |
| + deleteProperty()     |       | + deleteProperty()     |       +------------------------+
| + updateProperty()     |       | + updateProperty()     |              ^        ^
| + searchProperties()   |       | + searchProperties()   |              |        |
+------------------------+       +------------------------+              |        |
                                                                         |        |
                                                                         |        |
+-------------------------------------------+      +-------------------------------------------+
|       JpaPropertyRepository               |      |     InMemoryPropertyRepository           |
+-------------------------------------------+      +-------------------------------------------+
| + findAll()                               |      | - properties: List<Property>             |
| + findByAddressContainingOrDescription... |      | - currentId: Long                        |
+-------------------------------------------+      | + findAll()                              |
                                                   | + findById()                             |
                                                   | + save()                                 |
                                                   | + deleteById()                           |
                                                   | + findByAddressContainingOrDescription...|
                                                   +-------------------------------------------+
                                
                                        |
                                        |
                                        v
                      +------------------------------------+
                      |              Property              |
                      +------------------------------------+
                      | - id: Long                         |
                      | - address: String                  |
                      | - price: long                      |
                      | - size: int                        |
                      | - description: String              |
                      +------------------------------------+
                      | + getters/setters                  |
                      | + toString()                       |
                      +------------------------------------+
```

# Modelo de dominio

### Property (Entidad): Clase principal que representa una propiedad inmobiliaria.

- Atributos: id, address, price, size, description
- Anotada con @Entity y @Id, @GeneratedValue para el identificador

## Capa de persistencia

### PropertyPersistence (Interfaz): Define las operaciones de acceso a datos.

- Métodos: findAll, findById, save, deleteById, findByAddressContainingOrDescriptionContaining
- Soporta paginación a través de parámetros Pageable

### JpaPropertyRepository: Implementación JPA de la interfaz PropertyPersistence.

- Extiende JpaRepository de Spring Data
- Usa consultas personalizadas con anotaciones @Query
- Anotada con @Repository("jpa")

### InMemoryPropertyRepository: Implementación alternativa en memoria para pruebas.

- Mantiene una lista de propiedades y un contador de ID
- Anotada con @Repository("memory")

## Capa de servicio

### PropertyService: Contiene la lógica de negocio.

- Depende de PropertyPersistence (inyección de dependencias)
- Proporciona métodos para todas las operaciones CRUD
- Anotada con @Service

## Capa de controlador

### PropertyController: Maneja las peticiones HTTP REST.

- Depende de PropertyService (inyección de dependencias)
- Expone endpoints para todas las operaciones CRUD
- Incluye funcionalidad de búsqueda y paginación
- Anotada con @RestController

## Instrucciones de Despliegue

### Prerrequisitos
- Cuenta AWS con acceso a EC2
- Docker instalado en la máquina local y en las instancias EC2
- Git instalado en la máquina local

### Configuración Local y Pruebas

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Samuelfdm/AREP_TALLER05_BONO.git
   cd TallerBono
   ```

2. **Configurar la base de datos localmente**
   ```bash
   docker run --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=properties -e MYSQL_USER=samuel -e MYSQL_PASSWORD=samuelpass -p 3306:3306 -d mysql:latest
   ```

3. **Compilar y ejecutar el backend**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**
    - http://localhost:8080/

![img.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg.png)

![img_1.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_1.png)

![img_3.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_3.png)

### Despliegue en AWS

1. **Crear Instancias EC2**
    - Crear dos instancias EC2 usando Amazon Linux 2 AMI:
        - backendCrud (t2.micro)
        - mysqlbdd (t2.micro)
    - Configurar grupos de seguridad:
        - Backend: Permitir tráfico entrante en puertos 22 (SSH), 8080 (HTTP) y 80 (HTTP)
        - Base de datos: Permitir tráfico entrante en puertos 22 (SSH) y 3306 (MySQL) solo desde la IP del servidor backend

![imagenes.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimagenes.gif)
![lanzar.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Flanzar.gif)

2. **Instalar Docker en ambas instancias EC2**
   ```bash
   sudo yum update -y
   sudo yum install docker
   sudo service docker start
   sudo usermod -a -G docker ec2-user
   exit
   ```
   Cerrar sesión y volver a iniciar para aplicar los cambios de grupo.

3. **Desplegar el contenedor de base de datos en la instancia EC2 de base de datos**
   ```bash
   # En la instancia EC2 de base de datos
   docker run --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=properties -e MYSQL_USER=samuel -e MYSQL_PASSWORD=samuelpass -p 3306:3306 -d mysql:latest
   ```

![img_6.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_6.png)

![video1.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fvideo1.gif)

4. **Desplegar el backend en la instancia EC2 de aplicación**
   ```bash
   # En la instancia EC2 de aplicación
   # Clonar tu repositorio
   docker pull samuelfdm/tallermysqlrepo
   docker run -d -p 8080:8080 --name propertiescontainer samuelfdm/tallermysqlrepo
   ```
![img_9.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_9.png)

![img_10.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_10.png)

![img_11.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_11.png)

![img_8.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_8.png)

![despliege.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fdespliege.gif)

5. **Acceder a la aplicación desplegada**
    - http://ec2-54-166-229-47.compute-1.amazonaws.com:8080/properties
- - ESTO PUEDE CAMBIAR

### Monitoreo y Mantenimiento

1. **Ver logs de contenedores**
   ```bash
   docker logs propertiescontainer
   docker logs mysqlcontainer
   ```

2. **Reiniciar contenedores**
   ```bash
   docker restart propertiescontainer
   docker restart mysqlcontainer
   ```

3. **Detener y eliminar contenedores**
   ```bash
   docker stop propertiescontainer && docker rm propertiescontainer
   docker stop mysqlcontainer && docker rm mysqlcontainer
   ```

Videos del funcionamiento
-------

![funcionaremoto.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Ffuncionaremoto.gif)

![postman.gif](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fpostman.gif)

Pruebas unitarias
-------
   ```bash
   mvn test
   ```

![img_12.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_12.png)

## Explicación - pruebas unitarias
## Para PropertyControllerTest:

### Configuración inicial:

- Uso de Mockito para simular el comportamiento del servicio
- Creación de datos de prueba en el método setUp()


### Casos probados:

- Creación de propiedades (éxito y fallo)
- Obtención de lista de propiedades (con y sin resultados)
- Obtención de una propiedad específica (existente y no existente)
- Eliminación de propiedades (éxito y fallo)
- Actualización de propiedades (éxito y fallo)
- Búsqueda de propiedades


### Verificaciones:

- Códigos de estado HTTP correctos
- Respuestas esperadas
- Llamadas a los métodos del servicio con los parámetros correctos



## Para PropertyServiceTest:

### Configuración inicial:

- Uso de Mockito para simular el repositorio
- Creación de datos de prueba en el método setUp()


### Casos probados:

- Creación de propiedades
- Obtención de lista de propiedades
- Obtención de una propiedad específica (existente y no existente)
- Eliminación de propiedades (éxito, ID nulo, no existente)
- Actualización de propiedades
- Búsqueda de propiedades

Contribuciones
--------------

Si deseas contribuir a este proyecto, por favor sigue los siguientes pasos:

1.  Haz un fork del repositorio.

2.  Crea una nueva rama (git checkout -b feature/nueva-funcionalidad).

3.  Realiza tus cambios y haz commit (git commit -am 'Añade nueva funcionalidad').

4.  Haz push a la rama (git push origin feature/nueva-funcionalidad).

5.  Abre un Pull Request.

---

## Construido con

- **Java**: El lenguaje de programación principal utilizado para implementar el servidor web.
- **Maven**: Herramienta de gestión y construcción de proyectos para manejar las dependencias y compilar el código.
- **Java Networking**: Librerías estándar de Java para manejar conexiones de red y protocolos HTTP.
- **Git**: Sistema de control de versiones para gestionar el código fuente.
- **HTML/CSS/JavaScript**: Tecnologías front-end utilizadas para crear la aplicación web de prueba.
- **JUnit**: Framework para realizar pruebas unitarias y asegurar la calidad del código.

---

## Autor

* **Samuel Felipe Díaz Mamanche**

Vea también la lista de [contribuidores](https://github.com/Samuelfdm/TallerWebServer/contributors) que participaron en este proyecto.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo [LICENSE.md](LICENSE.md) para más detalles

## Agradecimientos

* [Escuela Colombiana de Ingeniería: Julio Garavito](https://www.escuelaing.edu.co/es/)
