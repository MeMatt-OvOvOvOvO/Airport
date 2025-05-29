# Airport App ✈️

## Opis projektu

Aplikacja webowa typu backend-only, pozwalająca na:

- Rejestrację użytkowników z rolą `ROLE_USER` lub `ROLE_ADMIN`
- Zarządzanie lotami przez administratora
- Zapisywanie się na loty przez użytkowników
- Aktualizację bagażu, przegląd historii lotów
- Generowanie raportów lotów
- Zabezpieczenie endpointów przy pomocy Spring Security
- Zarządzanie danymi przez bazę PostgreSQL i migracje Flyway
- Pokrycie testami jednostkowymi + Jacoco
- Dokumentację REST API w Swagger UI

---

## Wzorzec projektowy Strategy

Działanie
- Użytkownik podczas rejestracji wybiera klasę podróży: ECONOMY lub BUSINESS.
- Na tej podstawie przypisywana jest odpowiednia strategia:
- EconomyBaggageStrategy – ścisłe sprawdzanie limitu.
- BusinessBaggageStrategy – dozwolone przekroczenie limitu o 10 kg.

Implementacja
- Interfejs: BaggageCheckStrategy
- Implementacje:
- EconomyBaggageStrategy – userBaggage <= flightLimit
- BusinessBaggageStrategy – userBaggage <= flightLimit + 10
- Strategia jest dynamicznie wybierana na podstawie pola travelClass z obiektu User.

---

## Technologia

- Java 17
- Spring Boot 3
- Spring Security
- PostgreSQL (Docker)
- Flyway
- JUnit + Mockito + Jacoco
- Swagger UI (OpenAPI)
- Lombok

---

## 📦 Uruchamianie

1. Uruchom kontener PostgreSQL:

```bash
docker compose up -d