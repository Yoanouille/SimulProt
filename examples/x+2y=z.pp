Indique si x + 2y = z
TITLE: x + 2y = z
STATES: x y z xx zz 0 1
INITIAL: x y z
YES: 1
NO: x y z xx zz 0
RULES:
    x z -> 1 0
    z z -> zz 0
    zz y -> 1 0
    x x -> xx 0
    xx zz -> 1 0
    x 1 -> x 0
    y 1 -> y 0
    z 1 -> z 0
    zz 1 -> zz 0
    xx 1 -> xx 0
    1 0 -> 1 1
CONF: x=2 y=2 z=6
CONF: x=2 y=1 z=4
CONF: x=1 y=2 z=5