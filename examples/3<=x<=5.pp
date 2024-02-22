TITLE: 3<=x<=5
STATES: x x2 x3 x4 x5 1 0 00
INITIAL: x
YES: 1 x3 x4 x5
NO: x x2 0 00
RULES:
    x x -> x2 0
    x x2 -> x3 1
    x x3 -> x4 1
    x x4 -> x5 1
    x x5 -> 00 00
    x2 x2 -> x4 1
    x2 x3 -> x5 1
    x2 x4 -> 00 00
    x2 x5 -> 00 00
    x3 x3 -> 00 00
    x3 x4 -> 00 00
    x3 x5 -> 00 00
    x4 x4 -> 00 00
    x4 x5 -> 00 00
    x5 x5 -> 00 00
    1 0 -> 1 1
    x3 0 -> x3 1
    x4 0 -> x4 1
    x5 0 -> x5 1
    00 1 -> 00 00
    00 x3 -> 00 00
    00 x4 -> 00 00
    00 x5 -> 00 00