# Nutikas Restorani Reserveerimissüsteem

CGI Suvepraktika Ülesanne

## Projekti Ülevaade

Veebirakendus, mis võimaldab restorani külastajatel broneeerida laudu ja saada soovitusi sobivate laudade kohta. Süsteem pakub intelligentset lauade soovitamist põhinedes seltskonna suurusel, olevusel ja kliendi eelistustel, koos visuaalse restorani saaliplaaniga.

## Tehnoloogiad

- **Backend**: Spring Boot koos Java LTS-iga
- **Frontend**: Vue 3 TypeScript-i ja Vite-iga
- **Ehitustööriist**: Maven (Backend), npm (Frontend)
- **Versioonihaldus**: Git

## Projekti Struktuur

```
restaurant-reservation-api/          # Backend (Spring Boot)
├── src/main/java/com/example/restaurantreservationapi/
│   ├── controller/                  # REST API lõpp-punktid
│   ├── service/                     # Äriloogika
│   ├── repository/                  # Andmebaasi juurdepääs
│   ├── entity/                      # Domeenikasutajad
│   └── config/                      # Konfigureerimine ja initsialiseerimine
├── pom.xml                          # Maven sõltuvused
└── mvnw/mvnw.cmd                    # Maven wrapper

restaurant-reservation-ui/           # Frontend (Vue 3)
├── src/
│   ├── components/                  # Vue komponendid
│   ├── views/                       # Lehevaated
│   ├── stores/                      # Seisundi haldus
│   ├── api/                         # API klient
│   └── types/                       # TypeScript tüübid
├── package.json                     # npm sõltuvused
└── vite.config.ts                   # Vite konfigureerimine
```

## Eeltingimused

- Java 21 LTS (või viimane LTS versioon)
- Node.js 18+ (frontend jaoks)
- npm või yarn
- Git

## Alustamine

### Backend Seadistamine

1. **Navigeeri backend kaustale**:
   ```bash
   cd restaurant-reservation-api
   ```

2. **Ehita projekt**:
   ```bash
   # Linux/Mac-il:
   ./mvnw clean package
   
   # Windows-il:
   mvnw.cmd clean package
   ```

3. **Käivita rakendus**:
   ```bash
   # Linux/Mac-il:
   ./mvnw spring-boot:run
   
   # Windows-il:
   mvnw.cmd spring-boot:run
   ```

   Backend käivitub aadressil `http://localhost:8080`

### Frontend Seadistamine

1. **Navigeeri frontend kaustale**:
   ```bash
   cd restaurant-reservation-ui
   ```

2. **Paigalda sõltuvused**:
   ```bash
   npm install
   ```

3. **Käivita arendusserver**:
   ```bash
   npm run dev
   ```

   Frontend käivitub aadressil `http://localhost:5173` (või mõnes muus vabas pordis)


## API Lõpp-punktid

### Broneerings
- `GET /api/reservations` - Hankige kõik broneerings
- `GET /api/reservations/{id}` - Hankige konkreetne broneeritus
- `POST /api/reservations` - Looge uus broneeritus
- `PUT /api/reservations/{id}` - Uuendage broneerimiust
- `DELETE /api/reservations/{id}` - Tühista broneeritus

### Lauad
- `GET /api/tables` - Hankige kõik laudu
- `GET /api/tables/available` - Hankige vabad laudu (filtrite abil)
- `GET /api/tables/{id}` - Hankige konkreetse laua üksikasjad
- `POST /api/tables` - Lisage uus laud (admin)
- `PUT /api/tables/{id}` - Uuendage lauda (admin)

## Konfigureerimine

### Backend (`src/main/resources/application.properties`)
- Andmebaasi ühenduse sätted
- Serveri pordi konfigureerimine
- Spring Boot omadused

### Frontend (`src/api/api.ts`)
- API põhi-URL ja lõpp-punktid
- HTTP kliendi konfigureerimine

## Andmebaas

Rakendus kasutab mälusse jäävat või konfigureeritud andmebaasi (H2).
- Algandmed laadakse `DataInitializer` klassi kaudu


## Dokumentatsioon

- Vaadake koodis olevaid kommentaare algoritmi selgitamiseks
- Kontrollige git ajalugu arenduse edenemise jaoks
- Vaadake commit teadaandeid funktsioonide üksikasjuste jaoks

## Aja Jälgimine ja Märkmeid

- Algusaeg: 13 Märts
- Kokku kulunud tunnid: u 30h
- Tekkinud väljakutsed: Springbooti õppimine 
- Kasutatud lahendused/Ressursid: Kasutatud on siin projektis ka Gemini 3 LLM'i  
