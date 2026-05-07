# EcoRide Android App 🛴
Proyecto Final Android — Jetpack Compose + MVVM

---

## Cómo importar en Android Studio

1. Abre **Android Studio** → "Open" → selecciona la carpeta `EcoRideApp/`
2. Espera a que Gradle sincronice (puede tardar 1-2 minutos la primera vez)
3. Conecta un emulador o dispositivo físico

---

## Configurar la URL de la API

El archivo `RetrofitInstance.kt` usa `http://10.0.2.2:5000/` que equivale a
`localhost:5000` del ordenador cuando se ejecuta en el **emulador de Android**.

- **Emulador** → no toques nada, ya funciona
- **Dispositivo físico** → cambia `10.0.2.2` por la IP local de tu ordenador
  (la que aparece al arrancar el servidor Flask, ej: `http://192.168.1.X:5000/`)

---

## Arrancar el servidor Python antes de usar la app

```bash
cd server
python application.py
```

El servidor debe estar corriendo en el puerto 5000.

---

## Estructura del proyecto

```
com.ecoride.app/
├── data/
│   ├── api/
│   │   ├── ApiService.kt          ← Interfaz Retrofit con todos los endpoints
│   │   ├── RetrofitInstance.kt    ← Singleton OkHttp + Retrofit + AuthInterceptor
│   │   └── models/
│   │       ├── AuthModels.kt      ← DTOs de Login/Register
│   │       ├── VehicleModels.kt   ← DTO de patinete
│   │       └── RentalModels.kt    ← DTOs de alquiler
│   ├── local/
│   │   ├── VehicleLocal.kt        ← @Entity VehicleEntity + @Dao VehicleDao
│   │   └── AppDatabase.kt         ← @Database Room (caché local)
│   ├── datastore/
│   │   └── TokenDataStore.kt      ← JWT token persistido en DataStore
│   └── repository/
│       ├── AuthRepository.kt      ← Login, register, logout
│       ├── VehicleRepository.kt   ← Red + caché Room (offline-first)
│       └── RentalRepository.kt    ← Iniciar/finalizar alquiler, historial
├── navigation/
│   ├── Routes.kt                  ← Rutas selladas
│   └── NavGraph.kt                ← NavHost con todas las pantallas
├── ui/
│   ├── theme/Theme.kt             ← Material3 verde EcoRide
│   ├── components/Components.kt   ← EcoRideTextField, EcoRideButton, StatusChip…
│   ├── login/                     ← LoginScreen + LoginViewModel
│   ├── register/                  ← RegisterScreen + RegisterViewModel
│   ├── vehicles/                  ← VehicleListScreen + ViewModel (LazyColumn)
│   ├── detail/                    ← VehicleDetailScreen + ViewModel
│   └── history/                   ← RentalHistoryScreen + ViewModel
└── MainActivity.kt                ← Punto de entrada, DI manual
```

---

## Flujo de la app

```
Login ──────► Lista de patinetes ──────► Detalle + Iniciar alquiler
  │                 │
  │            Botón historial
  │                 │
  └── Registro  Historial de alquileres
```

---

## Criterios del proyecto cubiertos

| Criterio | Implementación |
|---|---|
| ViewModel | Uno por pantalla, sobrevive rotación |
| State Hoisting | `StateFlow` en ViewModel, `collectAsStateWithLifecycle` en UI |
| Ciclo de vida | `viewModelScope` en todas las corrutinas |
| Estructura de paquetes | `data/` (api, local, datastore, repository) + `ui/` por feature |
| Retrofit | `ApiService` con GET/POST/PUT, manejo de errores HTTP |
| Corrutinas | `Dispatchers.IO` en repositorios, hilo principal libre |
| Room | Caché de patinetes, app funciona sin red |
| DataStore | JWT token persistido entre sesiones |
| LazyColumn | Lista de patinetes e historial |
| Composables reutilizables | `EcoRideButton`, `StatusChip`, `InfoRow`, `EcoRideTextField` |
| Navegación | NavGraph con paso de `vehicleId` como argumento |
| Indicadores de carga | `CircularProgressIndicator` + `LinearProgressIndicator` |

---

## Credenciales de prueba

```
Admin: admin@ecoride.com / Admin1234!
```
O regístrate como usuario nuevo desde la app.
