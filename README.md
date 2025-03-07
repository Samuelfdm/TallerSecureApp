# Property Management CRUD System

## Project Summary

This project is a comprehensive CRUD (Create, Read, Update, Delete) system designed for managing real estate properties. The application allows users to:

- Create new property listings with details such as address, price, size, and description
- View a paginated list of properties with 3 properties per page
- View detailed information for each property listing
- Update existing property information through an interactive form
- Delete property listings that are no longer needed
- Search properties by address or description

The system is built using a modern three-tier architecture with a responsive web frontend, a Spring Boot REST API backend, and a MySQL database for persistent storage. All components are containerized using Docker and deployed on separate Amazon EC2 instances, ensuring scalability and separation of concerns.

## System Architecture

The application follows a standard three-tier architecture:

### Frontend
- **Technologies**: HTML, CSS, JavaScript
- **Features**:
   - Responsive user interface with forms for property information entry
   - Client-side validation for data integrity
   - Fetch API for asynchronous communication with the backend
   - Interactive property listings with options to view, update, and delete entries
   - Pagination with configurable page size
   - Search functionality for filtering properties

### Backend
- **Technologies**: Spring Boot, JPA/Hibernate
- **Features**:
   - RESTful API endpoints for all CRUD operations
   - Data validation and error handling
   - ORM mapping between Java objects and database entities
   - Pagination support through Spring Data
   - Search functionality through custom repository methods
   - Containerized deployment using Docker

### Database
- **Technologies**: MySQL
- **Features**:
   - Relational database with a properties table
   - Persistent storage for all property data
   - Containerized deployment using Docker

### Interaction Flow

![img_4.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_4.png)

1. **Client-Server Communication**: The frontend communicates with the backend through HTTP requests (GET, POST, PUT, DELETE) over port 8080.
2. **Data Persistence**: The backend communicates with the MySQL database using JPA/Hibernate over TCP port 3306.
3. **Service Isolation**: Each tier runs in its own Docker container on separate EC2 instances for improved security and scalability.

## Class Design

The backend application follows a layered architecture with clear separation of concerns:

### Class Diagram (Detailed)

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
Anotada con @Entity y @Id, @GeneratedValue para el identificador

## Capa de persistencia

### PropertyPersistence (Interfaz): Define las operaciones de acceso a datos.

- Métodos: findAll, findById, save, deleteById, findByAddressContainingOrDescriptionContaining
Soporta paginación a través de parámetros Pageable

### JpaPropertyRepository: Implementación JPA de la interfaz PropertyPersistence.

- Extiende JpaRepository de Spring Data
Usa consultas personalizadas con anotaciones @Query
Anotada con @Repository("jpa")

### InMemoryPropertyRepository: Implementación alternativa en memoria para pruebas.

- Mantiene una lista de propiedades y un contador de ID
Anotada con @Repository("memory")

## Capa de servicio

### PropertyService: Contiene la lógica de negocio.

- Depende de PropertyPersistence (inyección de dependencias)
Proporciona métodos para todas las operaciones CRUD
Anotada con @Service

## Capa de controlador

### PropertyController: Maneja las peticiones HTTP REST.

- Depende de PropertyService (inyección de dependencias)
Expone endpoints para todas las operaciones CRUD
Incluye funcionalidad de búsqueda y paginación
Anotada con @RestController

## Deployment Instructions

### Prerequisites
- AWS Account with EC2 access
- Docker installed on local machine and EC2 instances
- Git installed on local machine

### Local Setup and Testing

1. **Clone the repository**
   ```bash
   git clone https://github.com/Samuelfdm/AREP_TALLER05_BONO.git
   cd TallerBono
   ```

2. **Set up the database locally**
   ```bash
   docker run --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=properties -e MYSQL_USER=samuel -e MYSQL_PASSWORD=samuelpass -p 3306:3306 -d mysql:latest
   ```

3. **Build and run the backend**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - http://localhost:8080/

![img.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg.png)

![img_1.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_1.png)

![img_3.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_3.png)

### AWS Deployment

1. **Create EC2 Instances**
   - Create two EC2 instances using Amazon Linux 2 AMI:
      - backendCrud (t2.micro)
      - mysqlbdd (t2.micro)
   - Configure security groups:
      - Backend: Allow inbound traffic on ports 22 (SSH), 8080 (HTTP), and 80 (HTTP)
      - Database: Allow inbound traffic on ports 22 (SSH) and 3306 (MySQL) from the backend server IP only

2. **Install Docker on both EC2 instances**
   ```bash
   sudo yum update -y
   sudo yum install docker
   sudo service docker start
   sudo usermod -a -G docker ec2-user
   exit
   ```
   Log out and log back in to apply the group changes.

3. **Deploy the database container on the database EC2 instance**
   ```bash
   # On the database EC2 instance
   docker run --name mysqlcontainer -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=properties -e MYSQL_USER=samuel -e MYSQL_PASSWORD=samuelpass -p 3306:3306 -d mysql:latest
   ```

![img_6.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_6.png)

4. **Deploy the backend on the application EC2 instance**
   ```bash
   # On the application EC2 instance
   # Clone your repository
   docker pull samuelfdm/tallermysqlrepo
   docker run -d -p 8080:8080 --name propertiescontainer samuelfdm/tallermysqlrepo
   ```
![img_9.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_9.png)

![img_10.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_10.png)

![img_11.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_11.png)

![img_8.png](src%2Fmain%2Fresources%2Fstatic%2Fimg%2Fimg_8.png)

5. **Access the deployed application**
   - http://ec2-54-166-229-47.compute-1.amazonaws.com:8080/properties
- - ESTO PUEDE CAMBIAR

### Monitoring and Maintenance

1. **View container logs**
   ```bash
   docker logs propertiescontainer
   docker logs mysqlcontainer
   ```

2. **Restart containers**
   ```bash
   docker restart propertiescontainer
   docker restart mysqlcontainer
   ```

3. **Stop and remove containers**
   ```bash
   docker stop propertiescontainer && docker rm propertiescontainer
   docker stop mysqlcontainer && docker rm mysqlcontainer
   ```

Videos del funcionamiento
-------



Pruebas
-------


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

See also the list of [contributors](https://github.com/Samuelfdm/TallerWebServer/contributors) who participated in this project.

## Licencia

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Agradecimientos

* [Escuela Colombiana de Ingeniería: Julio Garavito](https://www.escuelaing.edu.co/es/)