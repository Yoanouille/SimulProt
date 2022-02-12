# Format de données d'entrée d'un protocole de population

## Ce que vous avez à renseigner
Pour un protocole de population vous avez besoin de renseigner les données suivantes :
 - Les états possibles des agents
 - Les états initiaux
 - Les états considérés comme "bons"
 - Les états considérés comme "mauvais"
 - Les régles de transitions lors d'une interaction entre 2 agents (pour les transitions qui changent quelque chose)

## Le format

Il n'y a pas d'ordre particulier, on peut d'abord renseigner les règles de transitions avant les états possibles par exemple.

### Les états possibles
```
STATES: Etat1 Etat2 ... Etatp
```
Tout cela sur une ligne et chaque état séparé par un espace

### Les états initiaux
```
INITIAL: Etat1 Etat2 ... Etati
```
Tout cela sur une ligne et chaque état séparé par un espace, et il est important que tous ces états soient des états possibles.

### Les états considérée comme "bons"
```
YES: Etat1 Etat2 ... Etaty
```
Tout cela sur une ligne et chaque état séparé par un espace, et il est important que tous ces états soient des états possibles et qu'il n'y ait pas un de ces états qui soit considéré comme "mauvais".

### Les états considérée comme "mauvais"
```
NO: Etat1 Etat2 ... Etatn
```
Tout cela sur une ligne et chaque état séparé par un espace, et il est important que tous ces états soient des états possibles et qu'il n'y ait pas un de ces états qui soit considéré comme "bons".

### Les règles de transitions
```
RULES:
    Etat1 Etat2 -> Etat3 Etat4
              ....
    Etatn Etatm -> Etatk Etatl
```
Tout d'abord noter "RULES:" puis faire un retour à la ligne, puis noter toutes les règles avec quelques espaces en début de ligne ou une tabulation (peu importe le nombre), Séparer chaque états par des espaces, bien mettre des espaces autour de "->" 

## Exemples

### Exemple 1
```
STATES: Y N y n
INITIAL: Y N
YES: Y y
NO: N n
RULES:
    Y N -> y n
    Y n -> Y y
    N y -> N n
    y n -> y n
```
### Exemple 2
```
INITIAL: Y N
YES: Y y
RULES:
 Y N -> y n
 Y n -> Y y
 N y -> N n
 y n -> y n
NO: N n
STATES: Y N y n
```
Remarque : ce fichier représente le même protocole que l'exemple 1