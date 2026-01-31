# Sistema de Gestión de Subvenciones - Unión Europea

Aplicación de escritorio desarrollada en Java para gestionar subvenciones de la Unión Europea utilizando el patrón DAO y base de datos MySQL.

![Java](https://img.shields.io/badge/Java-23-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Maven](https://img.shields.io/badge/Maven-3.9-red)
![License](https://img.shields.io/badge/License-MIT-green)

## 📋 Descripción

Este proyecto implementa un sistema completo de gestión de subvenciones europeas, permitiendo realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una base de datos MySQL. La aplicación está desarrollada siguiendo los principios de programación orientada a componentes, utilizando el patrón Data Access Object (DAO) para separar la lógica de acceso a datos de la lógica de negocio.

El proyecto fue desarrollado como parte del módulo de Acceso a Datos del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

## ✨ Características

- **Gestión completa de subvenciones**: Crear, consultar, modificar y eliminar subvenciones
- **Arquitectura basada en componentes**: Diseño modular con patrón DAO
- **Base de datos MySQL**: Almacenamiento persistente y confiable
- **Interfaz gráfica intuitiva**: Desarrollada con Java Swing
- **Validación de datos**: Formularios con validación de campos obligatorios
- **Estadísticas en tiempo real**: Contador de subvenciones e importe total
- **Seguridad SQL**: Uso de PreparedStatement para prevenir inyección SQL
- **Gestión automática de recursos**: Try-with-resources para prevenir fugas de memoria

## 🚀 Tecnologías Utilizadas

- **Java 23**: Lenguaje de programación
- **Maven**: Gestión de dependencias y construcción del proyecto
- **MySQL 8.0**: Sistema de gestión de bases de datos relacional
- **JDBC**: API de Java para conectividad con bases de datos
- **Java Swing**: Framework para interfaz gráfica de usuario
- **Eclipse IDE**: Entorno de desarrollo integrado

## 📁 Estructura del Proyecto

```
gestion-subvenciones-ue/
├── src/main/java/
│   └── com/subvenciones/
│       ├── modelo/
│       │   └── Subvencion.java              # Modelo de datos (POJO)
│       ├── conexion/
│       │   └── ConexionDB.java              # Componente de conexión (Singleton)
│       ├── dao/
│       │   └── SubvencionDAO.java           # Componente DAO (CRUD operations)
│       ├── ui/
│       │   └── VentanaPrincipal.java        # Interfaz gráfica (Swing)
│       └── main/
│           └── Main.java                    # Clase principal
├── pom.xml                                   # Configuración Maven
└── README.md
```

## 🏗️ Arquitectura de Componentes

### 1. Modelo de Datos (`Subvencion.java`)
Clase POJO que representa una subvención con los siguientes atributos:
- `idSubvencion` (int): Identificador único autogenerado
- `paisAsignado` (String): País receptor de la subvención
- `tipoSubvencion` (String): Tipo de subvención (Agrícola, Industrial, Tecnológica, etc.)
- `importe` (BigDecimal): Cantidad económica de la subvención

**Decisión técnica**: Se utiliza `BigDecimal` en lugar de `double` para garantizar precisión exacta en operaciones financieras.

### 2. Componente de Conexión (`ConexionDB.java`)
Gestiona la conexión con MySQL implementando el patrón Singleton:
- Conexión única compartida en toda la aplicación
- Métodos para abrir, cerrar y verificar el estado de la conexión
- Gestión automática de recursos
- Manejo robusto de excepciones

**Patrón aplicado**: Singleton para optimizar recursos y evitar múltiples conexiones innecesarias.

### 3. Componente DAO (`SubvencionDAO.java`)
Implementa el patrón Data Access Object con las siguientes operaciones:

| Método | Descripción | SQL |
|--------|-------------|-----|
| `insertar(Subvencion)` | Crea una nueva subvención | INSERT |
| `actualizar(Subvencion)` | Modifica una subvención existente | UPDATE |
| `eliminar(int)` | Elimina una subvención por ID | DELETE |
| `obtenerTodas()` | Recupera todas las subvenciones | SELECT * |
| `obtenerPorId(int)` | Busca una subvención específica | SELECT WHERE |
| `contarSubvenciones()` | Cuenta el total de registros | COUNT |
| `calcularImporteTotal()` | Suma todos los importes | SUM |

**Seguridad**: Todos los métodos utilizan `PreparedStatement` para prevenir inyección SQL.

### 4. Interfaz Gráfica (`VentanaPrincipal.java`)
Interfaz de usuario organizada en tres paneles:

**Panel Superior (Norte)**:
- Título con colores de la Unión Europea
- Indicador de estado de conexión

**Panel Central**:
- Tabla con todas las subvenciones
- Estadísticas: total de subvenciones e importe acumulado

**Panel Derecho (Este)**:
- Formulario de entrada de datos
- Botones de acción: Crear, Actualizar, Eliminar, Refrescar

### 5. Clase Principal (`Main.java`)
Punto de entrada que:
- Configura el Look and Feel del sistema
- Verifica la conexión con MySQL
- Inicializa todos los componentes
- Gestiona el cierre ordenado de recursos

## 📦 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

1. **Java Development Kit (JDK) 23 o superior**
   - Descarga: https://www.oracle.com/java/technologies/downloads/
   - Verifica la instalación: `java -version`

2. **MySQL 8.0 o superior**
   - Descarga: https://dev.mysql.com/downloads/mysql/
   - El servidor debe estar ejecutándose en `localhost:3306`

3. **Maven 3.9+ (opcional)**
   - Descarga: https://maven.apache.org/download.cgi
   - Verifica la instalación: `mvn -version`

4. **phpMyAdmin (opcional, recomendado)**
   - Para gestión visual de la base de datos
   - Incluido en XAMPP, WAMP o MAMP

## 🔧 Instalación y Configuración

### Paso 1: Crear la Base de Datos

Ejecuta el siguiente script SQL en MySQL:

```sql
-- Crear la base de datos
CREATE DATABASE union_europea;

-- Usar la base de datos
USE union_europea;

-- Crear la tabla subvenciones
CREATE TABLE subvenciones (
    id_subvencion INT AUTO_INCREMENT PRIMARY KEY,
    pais_asignado VARCHAR(100) NOT NULL,
    tipo_subvencion VARCHAR(100) NOT NULL,
    importe DECIMAL(15,2) NOT NULL
);

-- Insertar datos de ejemplo
INSERT INTO subvenciones (pais_asignado, tipo_subvencion, importe) VALUES 
('España', 'Agrícola', 40000000.00),
('Italia', 'Agrícola', 20000000.00);
```

**Usando phpMyAdmin**:
1. Accede a http://localhost/phpmyadmin/
2. Click en "Nueva" para crear una base de datos
3. Nombre: `union_europea`
4. Copia y pega el script SQL en la pestaña "SQL"
5. Click en "Continuar"

### Paso 2: Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/gestion-subvenciones-ue.git
cd gestion-subvenciones-ue
```

### Paso 3: Configurar las Credenciales de MySQL

Edita el archivo `src/main/java/com/subvenciones/conexion/ConexionDB.java` si es necesario:

```java
private static final String URL = "jdbc:mysql://localhost:3306/union_europea";
private static final String USUARIO = "root";
private static final String PASSWORD = ""; // Cambia si tienes contraseña
```

### Paso 4: Compilar el Proyecto

**Opción A: Con Maven (línea de comandos)**
```bash
mvn clean package
```

**Opción B: Con Eclipse**
1. File → Import → Maven → Existing Maven Projects
2. Selecciona la carpeta del proyecto
3. Click derecho en el proyecto → Maven → Update Project

### Paso 5: Ejecutar la Aplicación

**Opción A: Desde línea de comandos**
```bash
# Si compilaste con Maven
java -jar target/gestion-subvenciones-ue-1.0.0.jar
```

**Opción B: Desde Eclipse**
1. Navega a `src/main/java/com/subvenciones/main/Main.java`
2. Click derecho → Run As → Java Application

## 💻 Uso de la Aplicación

### Crear una Subvención

1. Rellena el formulario en el panel derecho:
   - **País Asignado**: Nombre del país (ej: Francia)
   - **Tipo de Subvención**: Selecciona del desplegable
   - **Importe**: Cantidad en euros (ej: 35000000)

2. Click en el botón **Crear** (verde)

3. La subvención aparecerá inmediatamente en la tabla

### Actualizar una Subvención

1. Haz click en una fila de la tabla para seleccionarla
2. Los datos se cargarán automáticamente en el formulario
3. Modifica los campos que desees
4. Click en el botón **Actualizar** (azul)

### Eliminar una Subvención

1. Selecciona una subvención de la tabla
2. Click en el botón **Eliminar** (rojo)
3. Confirma la eliminación en el diálogo emergente

### Refrescar los Datos

Click en el botón **Refrescar** (gris) para recargar todos los datos desde la base de datos.

## 📊 Modelo de Base de Datos

```
┌─────────────────────────────────────┐
│         SUBVENCIONES                │
├─────────────────────────────────────┤
│ id_subvencion (PK)  │ INT           │
│ pais_asignado       │ VARCHAR(100)  │
│ tipo_subvencion     │ VARCHAR(100)  │
│ importe             │ DECIMAL(15,2) │
└─────────────────────────────────────┘
```

**Restricciones**:
- `id_subvencion`: Clave primaria, autoincremental
- Todos los campos son `NOT NULL`
- `importe`: Precisión de 15 dígitos, 2 decimales

## 🔒 Seguridad

- **Prevención de Inyección SQL**: Uso exclusivo de `PreparedStatement`
- **Gestión de recursos**: Try-with-resources para evitar fugas de memoria
- **Validación de datos**: Validación en cliente antes de enviar a la BD
- **Manejo de excepciones**: Captura y gestión robusta de errores

## 🧪 Pruebas Realizadas

Se han realizado las siguientes pruebas de funcionalidad:

✅ **Operaciones CRUD**:
- Inserción de múltiples subvenciones
- Actualización de importes y tipos
- Eliminación con confirmación
- Consultas individuales y masivas

✅ **Validaciones**:
- País vacío → Error controlado
- Importe no numérico → Error de formato
- Importe negativo → Validación rechaza

✅ **Concurrencia**:
- Modificaciones directas en MySQL reflejadas al refrescar
- Sincronización correcta entre aplicación y base de datos

✅ **Manejo de errores**:
- MySQL desconectado → Mensaje de error apropiado
- Credenciales incorrectas → Diagnóstico detallado
- Base de datos inexistente → Instrucciones de creación

## 📈 Dependencias Maven

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

## 🐛 Solución de Problemas

### Error: "Driver de MySQL no encontrado"

**Causa**: Maven no descargó correctamente las dependencias

**Solución**:
```bash
# En la carpeta del proyecto
mvn clean install -U
```

O en Eclipse:
1. Click derecho en el proyecto
2. Maven → Update Project
3. ☑ Force Update of Snapshots/Releases

### Error: "No se pudo conectar con MySQL"

**Causa**: MySQL no está ejecutándose o credenciales incorrectas

**Solución**:
1. Verifica que MySQL esté activo
2. Accede a http://localhost/phpmyadmin/
3. Verifica que la base de datos `union_europea` exista
4. Revisa las credenciales en `ConexionDB.java`

### Error: "Unknown column 'id_subvencion'"

**Causa**: Nombre de columna incorrecto en la base de datos

**Solución**:
```sql
-- Ejecuta en MySQL
SHOW COLUMNS FROM subvenciones;
```
Verifica que los nombres coincidan exactamente con los del código.

## 🎯 Características Técnicas

- **Patrón de diseño**: Data Access Object (DAO)
- **Patrón de diseño**: Singleton (ConexionDB)
- **Arquitectura**: Tres capas (Datos, DAO, Presentación)
- **Persistencia**: Base de datos relacional MySQL
- **API de acceso**: JDBC (Java Database Connectivity)
- **Interfaz**: Java Swing con layouts anidados
- **Gestión de memoria**: Try-with-resources automático

## 🚀 Posibles Mejoras Futuras

- [ ] Implementar búsqueda y filtrado por múltiples criterios
- [ ] Añadir exportación de datos a PDF o Excel
- [ ] Implementar sistema de usuarios y permisos
- [ ] Añadir gráficos estadísticos con JFreeChart
- [ ] Migrar a un ORM como Hibernate
- [ ] Implementar logging con Log4j
- [ ] Añadir validación de países según lista oficial UE
- [ ] Sistema de auditoría (quién y cuándo modificó datos)
- [ ] Soporte para múltiples idiomas (i18n)
- [ ] Migración a JavaFX para interfaz más moderna

## 👨‍💻 Autor

**Esteban Sanchez**
- Proyecto académico - 2º DAM (Desarrollo de Aplicaciones Multiplataforma)
- Módulo: Acceso a Datos
- Año: 2026

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

```
MIT License

Copyright (c) 2026 Esteban Sanchez

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 Agradecimientos

- A la Unión Europea por inspirar el contexto del proyecto
- A la comunidad de MySQL por la documentación y soporte
- A Oracle por el desarrollo de Java y JDBC
- Al equipo docente de DAM por la guía en el proyecto

## 📞 Contacto

Si tienes preguntas, sugerencias o encuentras algún problema:
- Abre un **Issue** en este repositorio
- Envía un **Pull Request** con mejoras

---

⭐ Si este proyecto te ha sido útil, considera darle una estrella en GitHub

**Desarrollado con ❤️ para el aprendizaje de Acceso a Datos en DAM**
