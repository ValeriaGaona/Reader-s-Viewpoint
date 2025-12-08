| **Componente**    | **Actividad / Entregable**       | **Descripción**                                                              | **Liga / Evidencia**                        |
| ----------------- | -------------------------------- | ---------------------------------------------------------------------------- | ------------------------------------------- |
| **SABER (4 pts)** | Tabla comparativa de plataformas | Comparación Play Store, App Store, App Gallery + referencias                 | [Ver PDF]()            |
| **HACER (5 pts)** | README con código documentado    | README completo, código con KDoc/JSDoc, carpeta docs/screenshots, .gitignore | [Ver PDF]()             |
|                   | Pruebas con usuarios             | Encuesta a 10 usuarios + fotos + PDF con análisis                            | [Ver PDF]() |
| **SER (1 pt)**    | Coevaluación                     | Google Forms evaluando a un compañero                                        | [Ver PDF]()      |
|                   | Autoevaluación                   | Google Forms de reflexión personal                                           | [Ver PDF]()     |




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
