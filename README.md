# PROYECTO PARA REALIZAR CAMBIO DE MONEDA

## Descripción
Este proyecto es un servicio web que permite realizar el cambio de moneda de una cantidad de dinero en una moneda origen a una moneda destino.

## Tecnologías
* Java 17 [https://jdk.java.net/java-se-ri/17](https://jdk.java.net/java-se-ri/17)
* Spring Boot 3.0.1
* MongoDB
* Postman

## Requerimientos funcionales
* Se manejo el enfoque de anotaciones en la carpeta /controller y el enfoque funcional en la carpeta /handler con las rutas en /config/RouterConfig.
* Se utilizó JPA Auditing para el manejo de las fechas de creación y actualización de los documentos.
* Para la seguridad se utilizó Spring Security con JWT.
* Se utilizó Swagger para la documentación de la API.
* Se utilizó MongoDB como base de datos.


## Consideraciones
* Se debe tener instalado MongoDB en el equipo.
* En resources/collection-postman se encuentra el archivo de postman para realizar las pruebas.
* En resources/backup-db se encuentra el archivo de backup de la base de datos.
* Utilizar Swagger para realizar las pruebas link: [http://localhost:{#port}/swagger-doc/webjars/swagger-ui/index.html#/](http://localhost:port/swagger-doc/webjars/swagger-ui/index.html#/)
* La clave encriptada es "123" para ambos usuarios.
