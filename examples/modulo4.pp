Indique si le nombre de A1 est divisible par 4
TITLE: 4 / A1
STATES: A0 A1 A2 A3 B0 B1 B2 B3
INITIAL: A1
YES: A0 B0
NO: A1 A2 A3 B1 B2 B3
RULES:
	A0 A0 -> A0 B0
	A0 A1 -> A1 B1
	A0 A2 -> A2 B2
	A0 A3 -> A3 B3
	A1 A1 -> A2 B2
	A1 A2 -> A3 B3
	A1 A3 -> A0 B0
	A2 A2 -> A0 B0
	A2 A3 -> A1 B1
	A3 A3 -> A2 B2
	A0 B1 -> A0 B0
	A0 B2 -> A0 B0
	A0 B3 -> A0 B0
	A1 B0 -> A1 B1
	A1 B2 -> A1 B1
	A1 B3 -> A1 B1
	A2 B0 -> A2 B2
	A2 B1 -> A2 B2
	A2 B3 -> A2 B2
	A3 B0 -> A3 B3
	A3 B1 -> A3 B3
	A3 B2 -> A3 B3

CONF: A1=0