# Library Manager V2

Konsolowa aplikacja do zarządzania biblioteką napisana w Javie, wykorzystująca framework Spring (Core) oraz relacyjną bazę danych H2 (In-Memory) obsługiwaną przez JDBC.

## Wymagania
* Java 17 (lub nowsza)
* IDE (Zalecane: IntelliJ IDEA)

## Jak uruchomić projekt w IntelliJ IDEA

1. **Sklonuj repozytorium**
    * Otwórz terminal w wybranym folderze i wpisz komendę:
   ```bash
   git clone git@github.com:wedzichakonrad/library-manager-v2.git
2. **Otwórz projekt**
    * Wybierz `File` -> `Open...` i wskaż główny folder projektu (ten zawierający plik `pom.xml`).
3. **Pobierz zależności**
    * Po otwarciu projektu, IntelliJ powinien automatycznie wykryć plik `pom.xml`.
    * Kliknij prawym przyciskiem myszy na plik `pom.xml` -> `Maven` -> `Reload project`, aby pobrać wszystkie niezbędne biblioteki (Spring, H2, BCrypt, Lombok).
4. **Uruchom aplikację**
    * Znajdź główną klasę startową: `src/main/java/pl/manager/library/App.java`.
    * Kliknij w zielony trójkąt (Run) obok metody `main` lub na pasku narzędzi.

## Domyślne konta testowe

**Konto Administratora (pełen dostęp):**
* Login: `admin`
* Hasło: `admin`

**Konto Użytkownika (tylko przeglądanie i wypożyczanie):**
* Login: `jan`
* Hasło: `jan`
