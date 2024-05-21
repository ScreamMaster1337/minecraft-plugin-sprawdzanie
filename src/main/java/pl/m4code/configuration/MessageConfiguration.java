package pl.m4code.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import pl.m4code.notice.Notice;
import pl.m4code.notice.NoticeType;


@Getter

public class MessageConfiguration extends OkaeriConfig {
    private Notice discord = new Notice(NoticeType.MESSAGE, "&#ffffff&lS&#e5fffd&lp&#cafffc&la&#b0fffa&lc&#96fff9&le&#7bfff7&lC&#61fff6&ll&#47fff4&la&#2cfff3&ls&#12fff1&lh &8» &fdc.spaceclash.pl");
    private Notice correctUsage = new Notice(NoticeType.MESSAGE, "&4Błąd: &cPoprawne uzycie: &7[USAGE]");
    private Notice incorrectPlayer = new Notice(NoticeType.MESSAGE, "&8» &7Nie znaleziono gracza o nicku &b&l[PLAYER]");
    private Notice noPermission = new Notice(NoticeType.MESSAGE, "&8» &7Nie posiadasz uprawnien do tej komendy!");
    private Notice changedGamemode = new Notice(NoticeType.MESSAGE, "&8» &7Twoj tryb gry został zmieniony na &b&l[GAMEMODE]");
    private Notice badGamemodeName = new Notice(NoticeType.MESSAGE, "&8» &7Taki tryb gry nie istnieje!");
    private Notice playerKicked = new Notice(NoticeType.MESSAGE, "&8» &7Gracz [PLAYER] został wyrzucony!");
    private Notice teleportedtoPlayer1 = new Notice(NoticeType.MESSAGE, "&8» &7Zostales przeteleportowany do gracza: &b&l[PLAYER]");
    private Notice teleportedtoPlayer2 = new Notice(NoticeType.MESSAGE, "&8» &7Przeteleportowano gracza: &b&l[PLAYER] &7do siebie");

}
