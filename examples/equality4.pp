Indique si il y a 25% de A, 25% de B, 25% de C et 25% de D
TITLE: A = B = C = D
STATES: A B C D AB CD 0 1
INITIAL: A B C D
RULES:
    A B -> AB 0
    C D -> CD 0
    AB CD -> 0 1
    AB 1 -> AB 0
    CD 1 -> CD 0
    A 1 -> A 0
    B 1 -> B 0
    C 1 -> C 0
    D 1 -> D 0
    0 1 -> 1 1
YES: 1
NO: A B C D AB CD 0
CONF: A=5 B=5 C=5 D=5