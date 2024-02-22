Indique si il y a un nombre impair de A1
TITLE: A1 = 1 mod 2
STATES: A0 A1 B0 B1
INITIAL: A1
YES: A1 B1
NO: A0 B0
RULES:
    A0 A0 -> A0 B0
    A0 A1 -> A1 B1
    A1 A1 -> A0 B0
    A0 B1 -> A0 B0
    A1 B0 -> A1 B1
