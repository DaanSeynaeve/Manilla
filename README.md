# README #

## OVER DE REPOSITORY ##

**Belangrijk**: maak een fork van deze repo en werk daarop!
Nadien maak je pull-requests om je code bij het geheel te voegen.
Als je niet meer zeker weet hoe dat werkt: ik help wel :)

## WEDSTRIJD ##

Het is nog te vroeg om competities tussen de verschillende AI's op te zetten,
maar dit is uiteindelijk de bedoeling.

## SCHRIJVEN VAN JE ALGORITME ##

Je mag zoveel klassen maken als je wilt op voorwaarde dat:

- 1 van je klassen de interface **Intelligence** implementeert
- Je plaatst je klasse in het package 'AI' of in een eigen package dat je naam draagt (indien je veel klassen zou hebben)

### API: ###
De eerder vermeldde klasse **Intelligence** is de required interface. Deze gebruikt het programma om
vragen te stellen aan je AI. De klasse **InformationHandle** geeft je toegang tot informatie over het speelveld,
zoals o.a. de kaarten die reeds gespeeld zijn, wat de troef is, ...

Deze klasses kunnen ( en zullen ) nog wijzigen, maar zullen een gelijkaardige functionaliteit behouden.
Er is een diagram aanwezig dat een totaalbeeld schetst. Mocht je vragen of kritiek op het
design hebben: laat me iets weten! :)

### Taal: ###
De code is het engels geschreven, dus zet dit liefst ook voort.
Vertaling van Manille-termen:

- slag = trick
- troef = trump
- suit = 'kleur' van de kaart (hearts, spades, ...)
- symbol = teken van de kaart (king, jack, queen, ...)

## TESTEN VAN JE ALGORITME ##

Om je algo te testen kan je de Match klasse gebruiken.
( of een eigen test-klasse schrijven )

## KERNPROGRAMMA ##

Voorlopig hou ik me bezig met de kern van het programma. Gelieve dus geen klassen
uit de packages *core*,*player* en *exception* aan te passen.

## VRAGEN/FOUTEN/VOORSTELLEN ##

Laat me iets weten :) Er ontbreekt nog een hoop functionaliteit, maar de kern zou er moeten zijn.

## DAAN, ER STAAT EEN TAALFOUT IN JE README ##

Oops! Laat het me weten en ik fix het ;)

![playing_card_suits2.png](https://bitbucket.org/repo/pn87gp/images/2613342406-playing_card_suits2.png)