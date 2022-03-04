Indique si il y a plus de 10 individus
TITLE: >= 10
STATES: x x2 x3 x4 x5 x6 x7 x8 x9 1 0
INITIAL: x
YES: 1
NO: x x2 x3 x4 x5 x6 x7 x8 x9 0
RULES:
    x x -> x2 0
    x x2 -> x3 0
    x x3 -> x4 0
    x x4 -> x5 0
    x x5 -> x6 0
    x x6 -> x7 0
    x x7 -> x8 0
    x x8 -> x9 0
    x x9 -> 1 1
    x2 x2 -> x4 0
    x2 x3 -> x5 0
    x2 x4 -> x6 0
    x2 x5 -> x7 0
    x2 x6 -> x8 0
    x2 x7 -> x9 0
    x2 x8 -> 1 1
    x2 x9 -> 1 1
    x3 x3 -> x6 0
    x3 x4 -> x7 0
    x3 x5 -> x8 0
    x3 x6 -> x9 0
    x3 x7 -> 1 1
    x3 x8 -> 1 1
    x3 x9 -> 1 1
    x4 x4 -> x8 0
    x4 x5 -> x9 0
    x4 x6 -> 1 1
    x4 x7 -> 1 1
    x4 x8 -> 1 1
    x4 x9 -> 1 1
    x5 x5 -> 1 1
    x5 x6 -> 1 1
    x5 x7 -> 1 1
    x5 x8 -> 1 1
    x5 x9 -> 1 1
    x6 x6 -> 1 1
    x6 x7 -> 1 1
    x6 x8 -> 1 1
    x6 x9 -> 1 1
    x7 x7 -> 1 1
    x7 x8 -> 1 1
    x7 x9 -> 1 1
    x8 x8 -> 1 1
    x8 x9 -> 1 1
    x9 x9 -> 1 1
    1 x -> 1 1
    1 x2 -> 1 1
    1 x3 -> 1 1
    1 x4 -> 1 1
    1 x5 -> 1 1
    1 x6 -> 1 1
    1 x7 -> 1 1
    1 x8 -> 1 1
    1 x9 -> 1 1
    1 0 -> 1 1
