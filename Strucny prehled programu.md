# Přehled programu

Program se zakládá na swing-ové aplikaci, tedy aplikace jako taková extenduje JFrame a "Board", který se dále stará o celou hru, extenduje JPanel

## Main class "Bulanci"

Vytvoří novou třídu "Bulanci", dále vytvoří hrací plochu "Board" a nastaví ji velikost a další parametry:

``` java
setDefaultCloseOperation(EXIT_ON_CLOSE);
setResizable(false);
setLocationRelativeTo(null);
```

## class "Board"

Při vytváření přidá ```keyListener(new TAdapter())``` který se stará o uživatelský vstup, TAdapter je vlastní třída která extenduje KeyAdapter (více níže).

Dále nastaví dimenze a pozadí třídy "Board"

```java
setFocusable(true);
d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
setBackground(Color.black);
```

Začne timer ```timer = new Timer(Commons.DELAY, new GameCycle());``` a GameCycle extenduje `ActionListener`, ze kterého se následně volá `doGameCycle()` který pak aktualizuje celou aplikaci po určité době (`Commons.DELAY`).

A zinicializuje menu. `menuInit();`

### TAdapter

Vlastně předává vstup z klávesnice instanci třídy "player". Až na střelbu, protože stav board a čas mi připadá lepší řešit rovnou v třídě "Board", než posílat odkazy do hráče aby se sám rozhodl. Ale asi do budoucna, kdyby tato aplikace měla být složitější tak bych tuto logiku do třídy "player" přesunul.

### gameCycle

Zvedne hodnotu timeru o 1. zavolá update() (provede jeden krok) a pak překreslí plochu.

#### update()

Zkontroluje jestli nemá náhodou skončit hru:

``` java
if (state != states.INGAME)
  return;
for (final var player : players){
  if (player.isDying()) {
    setState(states.ENDGAME);
    break;
  }
}
if (kills == maxKills) {
  setState(states.ENDGAME);
  timer.stop();
  message = "Game won!";
  return;
}
else if (time > maxTime) {
  setState(states.ENDGAME);
  timer.stop();
  message = "Game lost!, no time left";
  return;
}
else if (players.size() == 0) {
  setState(states.ENDGAME);
  timer.stop();
  message = "Game lost!";
  return;
}

```

Odstraní mrtvoly.

Pohne hráčem o normalizovaný vektor jeho pohybu, to stejné pro kulky.

Zkontroluje zda kulka něco netrefila a případně udělí poškození hráči, zničí nepřítele nebo se odstraní ze hry po nárazu/opuštění mapy.

A pak se pohnou nepřátelé a případně vystřelí.

## sprites

Základní třídu mám Sprite od které všechny ostatní Sprity dědí.

### načítání obrázků - loadImages()

Jako vstup si bere String s lokací obrázku s předpřipraveným charem pro nahrazení čísly ("%d"), který se následně zformátuje do 4 různých jmen souborů, které se následně nahrají.

### get/set X/Y

Setry nastaví novou pozici s ohledem na velikost mapy a pokud nekoliduje s ostatními objekty `board.collideWithOthers(this)`.

Getry jen vrátí pozici

### getShootingPoint / LookingPoint

Vrátí vektor kterým se postavička naposledy pohla.

### collide

nějaká černá kolizní magie.

## Zbraně - Pistol

Základní třída pro zbraně s jednoduchou pistolí jako základ.
V konstruktoru se nahrají obrázky podobně jako v spritech.

### tryCreateShoots()

Pokusí se ze zbraně vypálit = zajistí poslední čas výstřelu a dostatek nábojů.
A vrátí seznam instancí nábojů

## struktura Obrázků

každý objekt má 4 instance pojmenované "něco(0-3).png" které odpovídají směru kterým se postavička dívá. Jediná výjimka jsou statické předměty které se neotáčí.