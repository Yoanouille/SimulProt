Indique si x + 2y = z
TITLE: x + 2y = z
STATES: x y z xy zz zzz 0 1
INITIAL: x y z
YES: 1
NO: x y z xy zz zzz 0
RULES:
    x y -> xy 0
    z z -> zz 0
    zz z -> zzz 0
    zz zz -> zzz z
    xy zzz -> 1 1
    x 1 -> x 0
    y 1 -> y 0
    z 1 -> z 0
    xy 1 -> xy 0
    zz 1 -> zz 0
    zzz 1 -> zzz 0
    1 0 -> 1 1
CONF: x=2 y=2 z=6