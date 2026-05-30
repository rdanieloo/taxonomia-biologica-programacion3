# 🧬 Sistema de Clasificación Taxonómica Biológica

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Status](https://img.shields.io/badge/status-activo-brightgreen)

> **Proyecto Final de Programación III**  
> Sistema interactivo para consultar, clasificar y gestionar información sobre la taxonomía biológica de diferentes especies.

### 📊 Gestión del Proyecto
Este proyecto utiliza **Trello** como herramienta de seguimiento y organización del equipo.

🔗 **[Ver Tablero de Trello](https://trello.com/b/PIlXPFYC/programming-iii-biological-taxonomy-project)**

---

## 📋 Descripción del Proyecto

Este proyecto implementa una solución integral para trabajar con clasificación taxonómica biológica, permitiendo a los usuarios consultar, clasificar y organizar información sobre diferentes organismos vivos según los niveles taxonómicos estándar: Reino, Filo, Clase, Orden, Familia, Género y Especie.

### Objetivos Principales

- ✅ Implementar una estructura de datos eficiente para almacenar información taxonómica
- ✅ Crear un sistema de búsqueda y filtrado avanzado
- ✅ Desarrollar una interfaz intuitiva y amigable
- ✅ Aplicar principios de programación orientada a objetos
- ✅ Integrar operaciones CRUD completas

---

## 👥 Equipo de Desarrollo

| Nombre | Rol | Responsabilidades |
|--------|-----|-------------------|
| **Estudiante 1** | Líder Técnico | Arquitectura del proyecto, estructura de datos y validación |
| **Estudiante 2** | Backend Developer | Lógica de programación, algoritmos de búsqueda y base de datos |
| **Estudiante 3** | Frontend Developer | Interfaz de usuario, diseño y experiencia del usuario |
| **Estudiante 4** | QA & Documentation | Pruebas, documentación técnica y manual de usuario |

---

## 🚀 Características Principales

### 1. Gestión de Especies
- Agregar nuevas especies con información taxonómica completa
- Actualizar datos existentes
- Eliminar registros obsoletos
- Búsqueda por nombre común o científico

### 2. Sistema de Clasificación
- Organización jerárquica de organismos
- Visualización de árboles taxonómicos
- Filtrado por niveles de clasificación
- Relaciones entre especies

### 3. Búsqueda Avanzada
- Búsqueda por múltiples criterios
- Filtros por reino, filo, clase, etc.
- Ordenamiento alfabético y por relevancia
- Sugerencias automáticas

### 4. Reportes y Estadísticas
- Generación de reportes por clasificación
- Estadísticas de especies por categoría
- Exportación de datos en múltiples formatos

---

## 📦 Requisitos del Sistema

### Requisitos Mínimos
- **Lenguaje**: Java 8 o superior (o el correspondiente a su implementación)
- **Base de Datos**: MySQL 5.7+ o SQLite
- **Memoria RAM**: 512 MB mínimo
- **Espacio en Disco**: 500 MB

### Requisitos de Desarrollo
- IDE: IntelliJ IDEA, Visual Studio Code o Eclipse
- Git para control de versiones
- Maven o Gradle (según configuración)

---

## 🛠️ Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/taxonomia-biologica-programacion3.git
cd taxonomia-biologica-programacion3
```

### 2. Configurar la Base de Datos
```bash
# Crear la base de datos
mysql -u root -p < database/schema.sql

# Insertar datos iniciales
mysql -u root -p taxonomia_db < database/initial_data.sql
```

### 3. Compilar el Proyecto
```bash
# Con Maven
mvn clean install
mvn compile

# Con Gradle
gradle build
```

### 4. Ejecutar la Aplicación
```bash
# Con Maven
mvn spring-boot:run

# Con Gradle
gradle run

# O ejecutar directamente
java -jar target/taxonomia-biologica-1.0.0.jar
```

---

## 📚 Estructura del Proyecto

```
taxonomia-biologica-programacion3/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/taxonomia/
│   │   │   │   ├── models/          # Clases modelo (Especie, Clasificación, etc.)
│   │   │   │   ├── services/        # Lógica de negocio
│   │   │   │   ├── repositories/    # Acceso a datos
│   │   │   │   ├── controllers/     # Controladores
│   │   │   │   └── utils/           # Utilidades y helpers
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │
│   └── test/                        # Pruebas unitarias e integración
│       └── java/com/taxonomia/
│
├── database/
│   ├── schema.sql                   # Esquema de base de datos
│   ├── initial_data.sql             # Datos iniciales
│   └── migrations/                  # Migraciones de datos
│
├── docs/
│   ├── MANUAL_USUARIO.md           # Manual para usuarios finales
│   ├── GUIA_DESARROLLADOR.md       # Guía para desarrolladores
│   └── DIAGRAMA_CLASES.md          # Diagramas UML
│
├── config/
│   └── database.properties          # Configuración de conexión
│
├── pom.xml                          # Configuración Maven
├── build.gradle                     # Configuración Gradle
├── .gitignore
├── LICENSE
└── README.md                        # Este archivo

```

---

## 🔄 Flujo de Trabajo

### Crear una Nueva Especie
```
1. Usuario → Interfaz de registro
   ↓
2. Validación de datos en Frontend
   ↓
3. Envío al Backend
   ↓
4. Validación de reglas de negocio
   ↓
5. Almacenamiento en base de datos
   ↓
6. Confirmación al usuario
```

### Buscar una Especie
```
1. Ingreso de criterios de búsqueda
   ↓
2. Procesamiento de filtros
   ↓
3. Consulta a base de datos
   ↓
4. Ordenamiento y paginación
   ↓
5. Presentación de resultados
```

---

## 🧪 Pruebas

### Ejecutar Todas las Pruebas
```bash
mvn test
# o
gradle test
```

### Pruebas Unitarias
```bash
mvn test -Dtest=*Test
```

### Pruebas de Integración
```bash
mvn test -Dtest=*IntegrationTest
```

### Cobertura de Código
```bash
mvn jacoco:report
# Reportes en: target/site/jacoco/index.html
```

---

## 📖 Documentación

- **[Manual de Usuario](docs/MANUAL_USUARIO.md)** - Guía completa para usuarios finales
- **[Guía del Desarrollador](docs/GUIA_DESARROLLADOR.md)** - Información técnica para desarrolladores
- **[Diagrama de Clases](docs/DIAGRAMA_CLASES.md)** - Diseño orientado a objetos
- **[API Reference](docs/API_REFERENCE.md)** - Documentación de endpoints (si aplica)

---

## 🐛 Reporte de Problemas

Si encuentras un bug o problema:

1. **Verifica** que el problema no esté ya reportado en [Issues](../../issues)
2. **Crea un nuevo issue** con:
   - Descripción clara del problema
   - Pasos para reproducir
   - Comportamiento esperado vs actual
   - Capturas de pantalla (si es relevante)
   - Información del sistema

---

## 🤝 Contribuir al Proyecto

### Guía de Contribución
1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/mi-feature`)
3. Commit tus cambios (`git commit -m 'Agregar mi feature'`)
4. Push a la rama (`git push origin feature/mi-feature`)
5. Abre un Pull Request

### Estándares de Código
- Seguir convenciones de nombres de Java
- Comentarios en español (o inglés consistentemente)
- Máximo 100 caracteres por línea
- Usar camelCase para variables y métodos
- Usar UPPER_CASE para constantes

---

## 📝 Notas Académicas

### Conceptos Implementados
- ✔️ Programación Orientada a Objetos (POO)
- ✔️ Estructuras de datos avanzadas
- ✔️ Patrones de diseño (DAO, MVC, Singleton)
- ✔️ Conexión a bases de datos
- ✔️ Interfaz gráfica/Web
- ✔️ Testing y QA

### Materia
**Curso**: Programación III  
**Institución**: [Tu Universidad]  
**Período Académico**: [Año/Semestre]

---

## 📜 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

```
MIT License

Copyright (c) 2024 Equipo de Desarrollo - Programación III

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

## 📊 Estado del Proyecto

| Aspecto | Estado | Responsable |
|--------|--------|-------------|
| Desarrollo | ✅ Completado | Estudiante 1 & 2 |
| Testing | ✅ Completado | Estudiante 4 |
| Documentación | ✅ Completado | Estudiante 4 |
| Interfaz | ✅ Completado | Estudiante 3 |

---

**Última actualización**: Mayo 2024  
**Versión**: 1.0.0  
**Mantenedores**: Equipo de Programación III

---

<div align="center">

⭐ Si este proyecto te fue útil, considera darle una estrella ⭐

</div>
