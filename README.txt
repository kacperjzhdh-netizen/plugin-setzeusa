SET ZEUSA — Paper 1.21.4

WYMAGANIA
- Java JDK 21
- Maven
- Serwer Paper 1.21.4

BUDOWANIE NA WINDOWS
1. Zainstaluj JDK 21 i Maven.
2. Otwórz terminal w folderze, w którym znajduje się pom.xml.
3. Uruchom: mvn package
4. Gotowy plik będzie w target\SetZeusa-1.0.0.jar
5. Skopiuj JAR do folderu plugins na serwerze Paper i uruchom ponownie serwer.

KOMENDA
/setzeusa

MENU
Kliknij hełm, tunikę albo miecz, aby otrzymać przedmiot.
Piorun przy uderzeniu jest wyłącznie efektem wizualnym i nie niszczy bloków.

UWAGA O GŁÓWCE ZEUSA
W projekcie główka jest standardowym PLAYER_HEAD z nazwą i opisem.
Aby pokazywała prawdziwą teksturę Zeusa, trzeba dodać prawidłową teksturę skina.

UWAGA O ENCHANTACH
Ochrona 52 i Ostrość 53 przekraczają normalne limity gry. Zachowanie ich efektów może zależeć od konfiguracji serwera i innych pluginów.

BUDOWANIE PRZEZ GITHUB (bez instalowania Javy)
1. Utworz nowe repozytorium na github.com i wgraj do niego cala zawartosc tego folderu
   (razem z ukrytym folderem .github).
2. Wejdz w zakladke Actions i poczekaj, az zakonczy sie "Build SetZeusa".
3. Otworz zakonczony przebieg i pobierz plik SetZeusa-jar z sekcji Artifacts.
4. Rozpakuj go i skopiuj SetZeusa-1.0.0.jar do folderu plugins na serwerze.
