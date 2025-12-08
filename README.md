| **Componente**    | **Actividad / Entregable**       | **Descripción**                                                              | **Liga / Evidencia**                        |
| ----------------- | -------------------------------- | ---------------------------------------------------------------------------- | ------------------------------------------- |
| **SABER (4 pts)** | Tabla comparativa de plataformas | Comparación Play Store, App Store, App Gallery + referencias                 | [Ver PDF]()            |
| **HACER (5 pts)** | README con código documentado    | README completo, código con KDoc/JSDoc, carpeta docs/screenshots, .gitignore | [Ver Códigos](https://github.com/ValeriaGaona/Reader-s-Viewpoint/tree/main/ReadersViewpoint/app/src/main/java/com/libreria/app)             |
|                   | Pruebas con usuarios             | Encuesta a 10 usuarios + fotos + PDF con análisis                            | [Ver PDF](https://github.com/ValeriaGaona/Reader-s-Viewpoint/blob/main/GarciaGaonaValeriaGTID141-LaraCarrilloSusanaDafenGTID143-Pruebas-U4.pdf) |
| **SER (1 pt)**    |  Coevaluación Y Autoevaluación  | PDF de autoevaluacion y coevaluacion Susana Dafne Lara Carrillo                         | [Ver PDF](https://github.com/ValeriaGaona/Reader-s-Viewpoint/blob/main/INSTRUMENTO_DE_AUTOEVALUACI%C3%93N%5B1%5D.pdf)      |
| **SER (1 pt)**    |  Coevaluación Y Autoevaluación  | PDF de autoevaluacion y coevaluacion Valeria García Gaona                        | [Ver PDF](https://github.com/ValeriaGaona/Reader-s-Viewpoint/blob/main/INSTRUMENTO_DE_AUTOEVALUACI%C3%93N.pdf)      |






# 📚 Sistema de Gestión de Librería "Libreria App"

El Sistema de Gestión de Librería es una aplicación móvil **Android nativa** diseñada para digitalizar y optimizar la administración de inventario, ventas y personal. Su objetivo es sustituir los procesos manuales de registro de libros y transacciones por un sistema centralizado y digital que permite a la administración y a los empleados llevar un **control preciso del catálogo**, gestionar las ventas (tickets), rastrear los movimientos de inventario y facilitar la gestión del equipo de trabajo.

La aplicación utiliza un sistema de autenticación seguro, donde el personal accede con correo electrónico y contraseña. Se distinguen dos perfiles principales: el **Administrador**, con control total de la gestión de la tienda y el personal, y el **Empleado**, centrado en las operaciones diarias. Todo el sistema de permisos y el flujo de datos se gestiona en tiempo real con **Firebase Firestore**.

---

### Estructura del proyecto

```
--- app
    |--- data
    |    |--- model
    |    |    |--- Libro.kt
    |    |    |--- Movimiento.kt
    |    |    |--- Ticket.kt
    |    |    |--- TicketItem.kt
    |    |    |--- UserProfile.kt
    |    |--- remote
    |    |    |--- FirebaseService.kt
    |    |--- repository
    |    |    |--- LibreriaRepository.kt
    |--- ui
    |    |--- screens
    |    |    |--- admin
    |    |    |    |--- AdministrationScreen.kt
    |    |    |    |--- AdminMovementsScreen.kt
    |    |    |    |--- CreateAccountScreen.kt
    |    |    |    |--- EmployeeListScreen.kt
    |    |    |    |--- SalesHistoryScreen.kt
    |    |    |--- employee
    |    |    |    |--- AddBookScreen.kt
    |    |    |    |--- EmployeeDetailsScreen.kt
    |    |    |    |--- InventoryScreen.kt
    |    |    |--- guest
    |    |    |    |--- BookDetailsScreen.kt
    |    |    |    |--- CatalogScreen.kt
    |    |    |    |--- ReceiptScreen.kt
    |    |    |    |--- ShoppingCartScreen.kt
    |    |    |--- login
    |    |    |    |--- LoginScreen.kt
    |    |--- theme
    |    |    |--- Color.kt
    |    |    |--- Theme.kt
    |    |    |--- Type.kt
    |--- utils
    |    |--- CaptureComposable.kt
    |    |--- QRGenerator.kt
    |    |--- SaveImage.kt
    |--- vm
    |    |--- AdminViewModel.kt
    |    |--- AuthViewModel.kt
    |    |--- CatalogViewModel.kt
    |    |--- InventoryViewModel.kt
    |--- MainActivity.kt
    |--- pantallas.kt
    |--- SplashScreenActivity.kt
    |--- SplashScreenUI.kt
```


---

## 📱 Características Principales

### 🔐 Autenticación y Gestión de Usuarios
* **Inicio de Sesión y Registro:** Acceso seguro para el personal mediante email/contraseña.
* **Creación de Cuentas:** El **Administrador** puede dar de alta nuevas cuentas de empleados o administradores (`CreateAccountScreen`).
* **Gestión de Sesiones:** Manejo seguro de la sesión utilizando **Firebase Authentication**.

### 👥 Sistema de Roles y Permisos
* **Administrador (Admin):** Control total, acceso al `AdministrationScreen`, `Historial de Ventas`, `Historial de Movimientos` y `Gestión de Personal`.
* **Empleado (Employee):** Permisos para operaciones de caja/venta y consulta de catálogo.
* **Visualización de Personal:** El administrador puede listar (`EmployeeListScreen`) y gestionar a todo el personal.

### 📦 Gestión de Inventario y Operaciones
* **Catálogo de Libros (CRUD):** Funcionalidades completas para crear, leer, actualizar y eliminar registros de libros.
* **Registro de Movimientos:** Trazabilidad de entradas, salidas y ajustes de inventario, visibles en `AdminMovementsScreen`.
* **Gestión de Ventas:** Registro de transacciones como **Tickets** (`addTicket`).
* **Historial de Ventas:** Consulta detallada de todos los tickets registrados (`SalesHistoryScreen`).

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Elemento | Descripción |
| :--- | :--- | :--- |
| **Lenguaje y Framework** | **Kotlin** | Lenguaje de programación principal. |
| | **Jetpack Compose** | Framework declarativo para la construcción de la UI. |
| | **Coroutines & Flow** | Programación asíncrona reactiva y manejo de flujos de datos. |
| **Arquitectura** | **MVVM** | Patrón Model-View-ViewModel para la separación de lógica y UI. |
| | **Repository Pattern** | Abstracción de fuentes de datos a través de `LibreriaRepository`. |
| | **StateFlow** | Gestión de estado reactiva en los ViewModels. |
| **Persistencia de Datos** | **Firebase Firestore** | Base de datos NoSQL en tiempo real para el catálogo, movimientos y usuarios. |
| | **Firebase Authentication** | Sistema de gestión de autenticación de personal. |
| **Bibliotecas UI/Otros** | **Material 3 Components** | Componentes de diseño de última generación. |
| | **Navigation Compose** | Sistema de navegación declarativa en Jetpack Compose. |

---

## 🎯 Casos de Uso

### Escenario 1: Creación de Cuenta de Personal
1.  **Administrador** navega a la pantalla `CreateAccountScreen`.
2.  Introduce credenciales, nombre y selecciona el **Rol** ("admin" o "employee").
3.  Al presionar "Crear cuenta", se ejecuta `LibreriaRepository.createAccount`.
4.  Se registra el usuario en **Firebase Auth** y se crea su `UserProfile` en Firestore.

### Escenario 2: Consulta del Historial de Ventas
1.  **Administrador** navega a `SalesHistoryScreen`.
2.  El sistema llama a `LibreriaRepository.fetchTickets()` para obtener la lista de ventas.
3.  La lista de tickets (mostrando ID, fecha y total) se presenta en un `LazyColumn`.
4.  El Administrador puede hacer clic en cualquier `Card` de venta para ver los detalles.

### Escenario 3: Auditoría de Inventario
1.  **Administrador** navega a `AdminMovementsScreen`.
2.  El ViewModel obtiene los datos mediante `LibreriaRepository.getMovements()`.
3.  La pantalla muestra un historial cronológico de cada `Movimiento` (e.g., "Entrada de 10 unidades de 'El Quijote' por Empleado X").
4.  La información facilita la trazabilidad de los cambios de stock.

### Escenario 4: Monitoreo de Personal en Tiempo Real
1.  **Administrador** abre `EmployeeListScreen`.
2.  La pantalla consume el **Flow** devuelto por `LibreriaRepository.getUsersWithRoles()`.
3.  Se muestra una lista de empleados y administradores.
4.  Si se crea o elimina una cuenta desde otra instancia, la lista se **actualiza automáticamente** gracias a la escucha en tiempo real de Firestore.

---

## 🔒 Seguridad

| Característica | Descripción |
| :--- | :--- |
| **Autenticación Segura** | Implementada mediante **Firebase Authentication**. |
| **Validación de Roles** | La lógica de la aplicación restringe el acceso a módulos administrativos críticos basándose en el campo `role` del `UserProfile`. |
| **Separación de Lógica** | Uso del **Repository Pattern** y **FirebaseService** para encapsular las interacciones con la base de datos y la autenticación. |
| **Uso de Corrutinas** | Garantiza que las operaciones de red y base de datos se ejecuten de forma segura y no bloqueante. |

---

## 📝 Base de Datos (Firebase Firestore)

| Colección | Modelo de Datos Principal | Propósito |
| :--- | :--- | :--- |
| `libros` | **Libro** | Catálogo completo de títulos y stock. |
| `tickets` | **Ticket** | Historial de transacciones de venta (facturación). |
| `movimientos` | **Movimiento** | Logs de las acciones que afectan el inventario (entradas, ajustes). |
| `users` | **UserProfile** | Perfiles del personal, incluyendo `uid`, `email`, `displayName` y el crucial campo **`role`**. |

---

## 🤝 Contribuciones
Las contribuciones son bienvenidas para mejorar la aplicación. Para cambios importantes:

1.  **Fork** el proyecto.
2.  Crea una rama para tu feature (`git checkout -b feature/NuevaFuncionalidad`).
3.  Commit tus cambios (`git commit -m 'Add: Nueva funcionalidad de...'`).
4.  Push a la rama (`git push origin feature/NuevaFuncionalidad`).
5.  Abre un **Pull Request**.

---

## ⚜️ Sobre el Proyecto

Este sistema fue desarrollado como un proyecto con el objetivo de **digitalizar y modernizar** la gestión en entornos de librería o retail con inventario. Se priorizó la usabilidad móvil y la gestión en tiempo real, utilizando tecnologías modernas de Android para ofrecer una solución **eficiente y escalable** para la administración y los empleados.
