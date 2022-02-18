Indique si il y a plus de Y que de N dans la population
STATES: Y y N n
INITIAL: Y N
RULES:
    Y N -> y n
    Y n -> Y y
    N y -> N n
    y n -> y y
YES: Y y
NO: N n