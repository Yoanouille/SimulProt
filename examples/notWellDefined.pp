TITLE: This protocol is not well defined
STATES: y n 0 1
INITIAL: 0 1
YES: y
NO: n
RULES:
    0 0 -> n n
    1 1 -> y y
    y 0 -> y y
    y 1 -> y y
    n 0 -> n n
    n 1 -> n n
    y n -> y y

CONF: 1=2 0=2