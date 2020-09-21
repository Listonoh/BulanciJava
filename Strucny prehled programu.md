# Přehled programu

Program se zakládá na swing-ové aplikaci, tedy aplikace jako taková extenduje JFrame a "Board", který se dále stará o celou hru, extenduje JPanel

## Main class "Bulanci"

V metodě main se vytvoří nová instance třídy "Bulanci".
Tím se v konstruktoru třídy "Bulanci" vytvoří a přidá instance hrací plochy "Board" a nastaví si velikost a další parametry.

## class "Board"

Při vytváření přidá ```keyListener(new TAdapter())``` který se stará o uživatelský vstup, TAdapter je vlastní třída která extenduje KeyAdapter (více níže).

Dále nastaví dimenze a pozadí třídy "Board"

Hra postupuje periodicky v metodě doGameCycle, která vyhodnocuje stav hry a vyvolá překreslení, dále se periodicky volá v intervalu který určuje `Commons.DELAY`.

A zinicializuje menu. `menuInit();`

### TAdapter

Vlastně předává vstup z klávesnice instanci třídy "player". Až na střelbu, protože stav board a čas mi připadá lepší řešit rovnou v třídě "Board", než posílat odkazy do hráče aby se sám rozhodl. Ale asi do budoucna, kdyby tato aplikace měla být složitější tak bych tuto logiku do třídy "player" přesunul.

### Stavy hry (states)

Určují v jakém je Board stavu, tedy jestli je hráč v menu, ve hře, nebo na konci hry.
Tento stav se mění v metodě `update()` při splnění některé z podmínek na konec hry (více v update).

### gameCycle

Zvedne hodnotu timeru o 1. zavolá update() (provede jeden krok) a pak překreslí plochu.

#### update()

Zkontroluje jestli náhodou nebyla splněna podmínka pro ukončení hry:

- byli zabiti všichni nepřátelé
- došel čas
- všichni hráči umřel

Když nastane nějaká z možností tak se stav změní na `ENDGAME` a skryje se hrací plocha a objeví se tlačítka pro spuštění hry nebo její ukončení (je to nepěkně schované v metodě `setState()`). A díky tomu že hra není ve stavu `INGAME`, tak se metoda `update()` hned po zavolání vrátí, bez toho aby něco dalšího dělala.

Odstraní mrtvoly.

Pohne hráčem o normalizovaný vektor jeho pohybu, to stejné pro kulky.

Zkontroluje zda kulka něco netrefila a případně udělí poškození hráči, zničí nepřítele nebo se odstraní ze hry po nárazu/opuštění mapy.

A pak se pohnou nepřátelé a případně vystřelí.

## Sprites

"Sprites" je základní třída ve hře od které všechny herní objekty dědí (všechny jí implementují).

### načítání obrázků - loadImages()

Jako vstup si bere String s lokací obrázku s předpřipraveným charem pro nahrazení čísly ("%d"), který se následně zformátuje do 4 různých jmen souborů, které se následně nahrají.

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

## struktura Obrázků

každý objekt má 4 instance pojmenované "něco(0-3).png" které odpovídají směru kterým se postavička dívá. Jediná výjimka jsou statické předměty které se neotáčí.
