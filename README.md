# Tires Shop App

System sprzedaży opon, felg i akcesoriów motoryzacyjnych planowo(z panelem 
klienta i administracyjnym). Projekt stworzony jako aplikacja webowa REST 
z wykorzystaniem Spring Boot, Spring Security, Hibernate, JWT 
oraz dokumentacją Swagger UI.

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Hibernate (JPA)
- PostgreSQL
- Flyway (migracje)
- Swagger OpenAPI (Springdoc)
- Maven
- Docker (docelowo)
- IntelliJ IDEA

---

## Uruchomienie lokalne

1. Skonfiguruj bazę danych PostgreSQL:
    ```sql
    CREATE DATABASE tires_shop;
    CREATE USER myuser WITH PASSWORD 'mypass';
    GRANT ALL PRIVILEGES ON DATABASE tires_shop TO myuser;
    ```

2. Ustaw dane dostępowe w `src/main/resources/application.properties`

3. Uruchom aplikację:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

4. Wejdź na:
    ```
    http://localhost:8080/swagger-ui/index.html
    ```

---

## Bezpieczeństwo i autoryzacja

Aplikacja wykorzystuje:
- JWT do logowania i autoryzacji
- Role użytkowników: `USER`, `ADMIN`
- Role przypisywane podczas rejestracji (domyślnie: `USER`)
- Endpoints zabezpieczone adnotacjami `@PreAuthorize`

---

## Dokumentacja API

(Jescze nie ma całego)

---

## Struktura projektu

<pre>
TiresShopApp/
├── pom.xml                        # Plik konfiguracyjny Maven
├── README.md                      # Dokumentacja projektu
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/tireshop/tiresshopapp/
│   │   │       ├── TiresShopAppApplication.java    # Główna klasa aplikacji
│   │   │       ├── config/                         # Konfiguracje (Security, Swagger)
│   │   │       ├── controller/                     # Kontrolery REST (Auth, User itd.)
│   │   │       ├── dto/                            # Obiekty żądań i odpowiedzi (DTO)
│   │   │       ├── entity/                         # Encje JPA (baza danych)
│   │   │       ├── exception/                      # Obsługa wyjątków globalnych
│   │   │       ├── repository/                     # Repozytoria JPA
│   │   │       └── service/                        # Logika biznesowa
│   │   │           └── security/                   # JWT, filtrowanie, UserDetailsService
│   │   └── resources/
│   │       ├── application.properties              # Konfiguracja aplikacji (porty, DB)
│   │       └── db/
│   │           └── migration/                      # Migracje bazy danych (Flyway)
└── src/test/
    └── java/org/tireshop/tiresshopapp/
        └── TiresShopAppApplicationTests.java       # Testy aplikacji

</pre>
---

## Testy

---

## Autor

Projekt stworzony jako projekt zaliczeniowy.  
Daniel Ryż 
---

## Status

Projekt w trakcie rozwoju

