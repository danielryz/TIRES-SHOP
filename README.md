# Tires Shop App

System sprzedaży opon, felg i akcesoriów motoryzacyjnych planowo(z panelem 
klienta i administracyjnym). Projekt stworzony jako aplikacja webowa REST 
z wykorzystaniem Spring Boot, Spring Security, Hibernate, JWT 
oraz dokumentacją Swagger UI.

---

## Tech Stack

- **Frontend**: React, TypeScript, Vite, Zustand, TailwindCSS, React Router
- **Backend**: Spring Boot, Spring Security, JWT, Hibernate, JPA, Maven
- **Baza danych**: PostgreSQL
- **DevOps**: Docker, Flyway, Cloudinary, GitHub Actions (CI/CD)

---

## 🚀 Getting Started

### 1. Sklonuj repozytorium

```bash
git clone https://github.com/TwojaNazwaUzytkownika/TiresShop.git
cd TIRES-SHOP
```

### 2. Ustaw dane dostępowe do cloudinary do dodawania zdjęć w clodinary (opcjonalne) `src/main/resources/application.properties`  

   Jeśli nie ustawisz nie będzie dało się korzystać z enpointu do Uploadu zdjęć.

```properties
cloudinary.cloud-name=yourCloudName
cloudinary.api-key=yourApi
cloudinary.api-secret=yourSecret
```


### 3. Uruchom aplikację:

    ```bash
      docker-compose build  
      docker-compose up
    ```

### 4. Wejdź na:
- Dokumentacja swagger: http://localhost:8080/swagger-ui/index.html
- Panel klienta: http://localhost:5174/
- Panel ADMINA: http://localhost:5174/admin

---

## Bezpieczeństwo i autoryzacja

Aplikacja wykorzystuje:
- JWT do logowania i autoryzacji
- Role użytkowników: `USER`, `ADMIN`
- Role przypisywane podczas rejestracji (domyślnie: `USER`)
- Endpoints zabezpieczone adnotacjami `@PreAuthorize`

---

## 📁 Struktura Projektu

### 🖥️ Frontend – `TiresShopFrontend`
Technologie: `React`, `TypeScript`, `Vite`, `Docker`, `Nginx`

<pre>
TiresShopFrontend/
├── public/ # Pliki statyczne (index.html, favicon, manifest)
├── src/
│ ├── api/ # Komunikacja z backendem (axios)
│ ├── assets/ # Obrazy, ikony, logotypy
│ ├── components/ # Komponenty UI (np. przyciski, modale)
│ ├── context/ # React Contexts (np. koszyk, autoryzacja)
│ ├── pages/ # Widoki strony (Home, Product, Cart, Admin itd.)
│ ├── routes/ # Trasy + ochrona (PrivateRoute, AdminRoute)
│ ├── types/ # Typy TypeScript (User, Tire, Product)
│ └── utils/ # Funkcje pomocnicze
├── vite.config.ts # Konfiguracja Vite
├── tsconfig.json # Konfiguracja TypeScript
├── Dockerfile # Obraz frontendu
└── nginx.conf # Konfiguracja Nginx do serwowania frontendu
</pre>
### 🧠 Backend – `TiresShopApp`

Technologie: `Spring Boot`, `Maven`, `JWT`, `JPA`, `Flyway`, `Cloudinary`, `Docker`
<pre>
TiresShopApp/
├── src/
│ ├── main/
│ │ ├── java/org/tireshop/tiresshopapp/
│ │ │ ├── config/ # Konfiguracje (Security, CORS, OpenAPI)
│ │ │ ├── controller/ # REST API – endpointy
│ │ │ ├── dto/ # DTO (request / response)
│ │ │ ├── entity/ # Encje JPA
│ │ │ ├── exception/ # Obsługa wyjątków
│ │ │ ├── repository/ # Interfejsy JPA
│ │ │ ├── security/ # JWT, filtry, konfiguracja Spring Security
│ │ │ ├── service/ # Logika biznesowa
│ │ │ ├── specifications/ # Dynamiczne filtrowanie i sortowanie
│ │ │ └── util/ # Narzędzia pomocnicze
│ ├── resources/
│ │ ├── application.properties # Konfiguracja aplikacji
│ │ └── db/migration/ # Migracje Flyway
│
│ └── test/ # Testy jednostkowe i integracyjne
├── Dockerfile # Obraz backendu
└── pom.xml # Zależności Maven
</pre>
---

## Testy

Aplikacja została otestowana w 82%, za pomocą `testów jednostkowych` i `testów integracyjnych`


---

## Autor

Projekt stworzony jako projekt zaliczeniowy.  
Daniel Ryż 
---

## Status

Projekt w trakcie rozwoju

