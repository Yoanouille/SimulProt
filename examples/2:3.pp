Indique si il y a  Card(Y) >= 2 * Card(N)
STATES: Y N YN 1 0
INITIAL: Y N
YES: Y 1
NO: N YN 0
RULES:
    Y N -> YN 0
    YN Y -> 1 0
    YN 1 -> YN 0
    Y 0 -> Y 1
    N 1 -> N 0
    1 0 -> 1 1
