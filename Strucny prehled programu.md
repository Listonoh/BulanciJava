# Přehled programu

Program se zakládá na swing-ové aplikaci, tedy aplikace jako taková extenduje JFrame a "Board" (který extenduje JPanel), který se dále stará o celou hru.

## Main class "Bulanci"

V metodě main se vytvoří nová instance třídy "Bulanci".
Tato nová instance si nastaví velikost a další parametry a vytvoří si v sobě instanci třídy "Board".

## class "Board"

Při vytváření přidá ```keyListener(new TAdapter())``` který se stará o uživatelský vstup, TAdapter je vlastní třída která extenduje KeyAdapter (více níže).

Dále nastaví dimenze a pozadí třídy "Board"

Hra postupuje periodicky v metodě doGameCycle, která vyhodnocuje stav hry a vyvolá překreslení, dále se periodicky volá v intervalu který určuje `Commons.DELAY`.

A zinicializuje menu (`menuInit()`), ve kterém zinicializuje tlačítka pro pohyb v UI.

### Board.states

Třída Board má také 3 stavy které určují v jakém je Board stavu, tedy jestli je hráč v menu, ve hře, nebo na konci hry.
Tento stav se mění v metodě `update()` při splnění některé z podmínek konce hry (více v update). Zároveň s měněním stavu se mění jaké prvky ui jdou vidět, tlačítka či hrací plocha.

### TAdapter

Vlastně předává vstup z klávesnice instanci třídy "player", podle které se pak  hráč pohybuje.
Střelba se vyhodnocuje rovnou ve třídě "Board", protože střely jsou zde uchovávané v listu.

### gameCycle

Zvedne hodnotu timeru o 1. zavolá update() (provede jeden krok) a pak překreslí plochu.

#### update()

Zkontroluje jestli náhodou nebyla splněna podmínka pro ukončení hry:

- byli zabiti všichni nepřátelé
- došel čas
- všichni hráči umřeli

Když nastane nějaká z možností tak se stav změní na `ENDGAME` a skryje se hrací plocha a objeví se tlačítka pro spuštění hry nebo její ukončení (je to nepěkně schované v metodě `setState()`). A díky tomu že hra není ve stavu `INGAME`, tak se metoda `update()` hned po zavolání vrátí, bez toho aby něco dalšího dělala.

Odstraní mrtvoly.

Pohne hráčem o normalizovaný vektor jeho pohybu, to stejné pro kulky.

Zkontroluje zda kulka něco netrefila a případně udělí poškození hráči, zničí nepřítele nebo se odstraní ze hry po nárazu nebo opuštění mapy.

A pak se pohnou nepřátelé a případně vystřelí pokud mají hráče před sebou.

## Sprites

"Sprites" je základní třída ve hře kterou všechny herní objekty implementují.

### načítání obrázků - loadImages()

Jako vstup si bere String s lokací obrázku s předpřipraveným znakem pro nahrazení čísly ("%d"), který se následně zformátuje do 4 různých jmen souborů, které se následně nahrají.

### get/set X/Y

Setry nastaví novou pozici s ohledem na velikost mapy a pokud nekoliduje s ostatními objekty `board.collideWithOthers(this)`.

Getry jen vrátí pozici

### LookingPoint

Vrátí vektor kterým se postavička naposledy pohla.

### getShootingPoint

Vrátí pozici odkud postavička vystřeluje, neboli na této pozici vzniká nová kulka.

### collide(Sprite collider)

Zkontroluje zda se 2 Sprity nepřekrývají (nekolidují).

## Zbraně - Pistol

Základní třída pro zbraně s jednoduchou pistolí jako základ.
V konstruktoru se nahrají obrázky podobně jako v spritech.

### tryCreateShoots()

Pokusí se ze zbraně vypálit = zajistí poslední čas výstřelu a dostatek nábojů.
A vrátí seznam instancí nábojů

## struktura uložených Obrázků

každý objekt má 4 instance pojmenované "něco(0-3).png" které odpovídají směru kterým se postavička dívá. Jediná výjimka jsou statické předměty které se neotáčí.
