# Minecraft Player Check Plugin

Minecraft Player Check to plugin, który umożliwia administratorom sprawdzanie graczy na serwerze. Dzięki prostym komendom można kontrolować, którzy gracze są sprawdzani, a także ustawiać lokalizacje teleportacji dla graczy. Plugin pozwala również na przyznanie się do oszustwa z odpowiednią karą.

## Funkcje
- **Sprawdzanie graczy**: Administratorzy mogą rozpocząć proces sprawdzania gracza, który otrzyma specjalne powiadomienie.
- **Ustawianie lokalizacji**: Możliwość ustawienia lokalizacji dla teleportacji graczy podczas sprawdzania.
- **Zarządzanie komendami**: Kontrola nad tym, jakie komendy mogą być używane przez gracza w trakcie sprawdzania.
- **Przyznawanie się do oszustwa**: Gracze mogą przyznać się do używania cheatów, co skutkuje krótkotrwałym banem.

## Użycie Komend

- `/check sprawdz <gracz>`  
  **Opis:** Rozpoczyna proces sprawdzania wskazanego gracza.  
  **Przykład:** `/check sprawdz Steve` – Sprawdza gracza o nazwie Steve.

- `/check reload`  
  **Opis:** Przeładowuje konfigurację pluginu.  
  **Przykład:** `/check reload` – Przeładowuje ustawienia.

- `/check set1`  
  **Opis:** Ustawia lokalizację, gdzie ma być teleportowany gracz sprawdzany.  
  **Przykład:** `/check set1` – Ustawia lokalizację 1.

- `/check set2`  
  **Opis:** Ustawia lokalizację, gdzie ma być teleportowany gracz, który okazał się czysty.  
  **Przykład:** `/check set2` – Ustawia lokalizację 2.

- `/check czysty <gracz>`  
  **Opis:** Oznacza gracza jako czystego i teleportuje go do wcześniej ustawionej lokalizacji.  
  **Przykład:** `/check czysty Steve` – Oznacza Steve'a jako czystego.

- `/check cheater <gracz>`  
  **Opis:** Oznacza gracza jako oszusta i stosuje ban.  
  **Przykład:** `/check cheater Steve` – Oznacza Steve'a jako oszusta.

- `/przyznajesie`  
  **Opis:** Gracz przyznaje się do oszustwa i otrzymuje krótkotrwały ban.  
  **Przykład:** `/przyznajesie` – Przyznaje się do oszustwa.

## Specjalne Permisje
- `m4code.reload`: Umożliwia użycie podkomendy `/check reload`.
- `m4code.check`: Umożliwia ogólne używanie komendy `/check`.
- `m4code.check.bypass`: Jeśli posiadasz tę permisję, nikt nie może Cię sprawdzić.

## Konfiguracja

Plugin automatycznie tworzy plik konfiguracyjny, w którym można dostosować:

- **Wiadomości**: Dostosuj wiadomości wyświetlane graczom oraz administratorom w różnych sytuacjach.
- **Dozwolone komendy**: Ustaw, które komendy są dostępne dla gracza w trakcie sprawdzania.

### Przykład konfiguracji (`config.yml`):

```yaml
messages:
  no_permission: "&cNie masz uprawnień do użycia tej komendy."
  usage: "&4Błąd: &cPoprawne użycie: &7/check <reload|sprawdz|set1|set2|czysty|cheater>"
  usage_sprawdz: "&4Błąd: &cPoprawne użycie: &7/check sprawdz <gracz>"
  usage_czysty: "&4Błąd: &cPoprawne użycie: &7/check czysty <gracz>"
  usage_cheater: "&4Błąd: &cPoprawne użycie: &7/check cheater <gracz>"
  reload: "&aKonfiguracja została przeładowana."
  player_not_found: "&cGracz nie został znaleziony."
  check_location_not_set: "&cLokalizacja sprawdzania nie została ustawiona."
  clear_location_not_set: "&cLokalizacja czysta nie została ustawiona."
  check_started: "&7Sprawdzanie rozpoczęte dla &c&n%player%&r"
  check_started_broadcast: "&7Administrator &a&n%admin%&r &7rozpoczął sprawdzanie gracza &c&n%player%&r"
  check_location_set: "&aLokalizacja sprawdzania została ustawiona."
  clear_location_set: "&aLokalizacja czysta została ustawiona."
  player_czysty: "&7Gracz &a&n%player%&r &7został sprawdzony przez &a&n%admin%&r &7i jest &aczysty."
  player_cheater: "&7Gracz &c&n%player%&r &7został sprawdzony przez &a&n%admin%&r &7i jest &coszustem."
  cheater_ban_reason: "Zostałeś zbanowany za oszukiwanie."
  player_przyznajesie: "&7Gracz &c&n%player%&r &7przyznał się do oszukiwania."
  przyznajesie_ban_reason: "Zostałeś zbanowany za przyznanie się do oszukiwania."
  player_not_being_checked: "&7Gracz &a&n%player%&r &7nie jest sprawdzany."
  console_cannot_use: "&cKonsola nie może użyć tej komendy."
  check_title: "&c&lJESTEŚ SPRAWDZANY"
  check_subtitle: "&7Stosuj się do zaleceń administracji"
  command_not_allowed: "&cNie możesz używać tej komendy podczas sprawdzania."
  logout_ban_reason: "Zostałeś zbanowany za wylogowanie się podczas sprawdzania."
  cannot_check_self: "&cNie możesz sprawdzić samego siebie."
  cannot_check_exempt: "&cNie możesz sprawdzić tej osoby, ponieważ ma uprawnienia do pominięcia sprawdzania."
  player_logged_out_during_check: "&7Gracz &c&n%player%&r &7wylogował się podczas sprawdzania i otrzymał karę."

allowed_commands:
  - "/helpop"
  - "/msg"
  - "/r"
  - "/przyznajesie"

console_commands:
  logout: "ban %player% 2d Wylogowanie podczas sprawdzania"
  cheater: "ban %player% 30d Cheaty"
  przyznajesie: "ban %player% 7d Przyznanie się do cheatów"
```

  
**Wymagania:**
- Serwer Minecraft (wersja 1.16+)
- Java 8+

**Autor:** [ScreamMaster1337](https://github.com/ScreamMaster1337)  
**Licencja:** MIT
