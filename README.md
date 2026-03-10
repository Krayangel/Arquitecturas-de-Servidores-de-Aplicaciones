# Servidor Web & Micro-Framework IoC (Java)

Este proyecto es un servidor web ligero construido desde cero en Java. Además de servir páginas estáticas (HTML, CSS, JS, imágenes), incluye un framework **IoC (Inversión de Control)** propio que funciona de forma similar a Spring Boot.


---

## Architecture and Design
El sistema se divide en dos partes fundamentales:

1. **Framework IoC (Escaneo Dinámico):** Al arrancar, el programa lee automáticamente el `classpath` (la carpeta del proyecto compilado). Busca todas las clases que tengan la etiqueta `@RestController` y las carga en memoria sin necesidad de archivos de configuración adicionales.
2. **Sistema de Rutas y Anotaciones:** 
   * Usa `@GetMapping("/ruta")` para asignar una URL pública a una función específica de tu código Java.
   * Usa `@RequestParam(value="name", defaultValue="X")` para leer parámetros desde la URL (ej. `?name=Juan`) e inyectarlos directamente como variables en los métodos.
   * **Archivos Estáticos:** Si ingresas una ruta que no sea un servicio programado, el servidor automáticamente intentará devolver el archivo estático (como el `index.html`).

---

## Installation and Usage
Necesitas **Java 17** y **Maven**.

1. **Clona y compila:**
   ```bash
   git clone 
   mvn clean compile
   ```
2. **Ejecuta el servidor:**
   ```bash
   java -cp "target/classes;target/dependency/*" app.MicroSpringBoot
   ```

La aplicación estará visible en `localhost:8080`.

---

## Evidence of Tests

### 1. Pruebas Locales (localhost:8080)
El framework carga el HTML y expone los servicios automáticamente.
![Index HTTP](Images/2%20index%20http.png)
![Funcionamiento Local](Images/3%20local.png)

Endpoints con lógica básica usando `@GetMapping`:

![Endpoint PI](Images/4%20pi.png)
![Endpoint Euler](Images/5%20euler.png)
![Endpoint Hello](Images/6%20hello%20alone.png)

Inyectando valores desde la URL usando `@RequestParam`:

![Endpoint Greeting default](Images/7%20greeting.png)
![Endpoint Greeting custom](Images/8%20hello%20name.png)
![Código Controller](Images/9%20controller.png)

### 2. Despliegue en AWS EC2
Empaquetado y subida via SFTP de los `.class` hacia una instancia de Amazon Linux.
![AWS EC2 Management](Images/10%20aws%20isntancia.png)
![SSH Setup Connection](Images/11%20conectar%20a%20la%20instancia%20e%20isntalar.png)
![SFTP Transfer](Images/12%20connect%20using%20sftp.png)

Servidor HTTP desplegado y funcionando perfectamente en la nube de Amazon:
![Classes Loaded](Images/13%20load%20classes.png)
![Servidor AWS Iniciado](Images/14%20inicio%20server%20aws.png)
![Index en AWS](Images/15%20fucnional%20en%20aws.png)
![Greeting AWS](Images/15%20gretting%20aws.png)
![Hello AWS](Images/16%20hello%20aws.png)
