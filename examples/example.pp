Indique si il y a plus de Y que de N dans la population
TITLE: Y >= N
STATES: Y y N n
INITIAL: Y N
RULES:
    Y N -> y n
    Y n -> Y y
    N y -> N n
    y n -> y y
YES: Y y
NO: N n
CONF: Y=4 N=4
CONF: Y=5 N=4
CONF: Y=4 N=5
CONF: Y=2 N=4
CONF: Y=100 N=101