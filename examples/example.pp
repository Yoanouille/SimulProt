Indique si il y a plus de Y que de N dans la population
TITLE: Y >= N
STATES: Y y N n y2
INITIAL: Y N
RULES:
    Y N -> y n
    Y n -> Y y
    N y -> N n
    y n -> y y
    y y -> y2 y2
    y2 y2 -> y y
YES: Y y y2
NO: N n
CONF: Y=4 N=4
CONF: Y=5 N=4
CONF: Y=4 N=5