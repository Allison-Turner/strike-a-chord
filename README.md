# strike-a-chord
CS 343 final project
Allison Turner and Lily Orth-Smith
<br>
We did not write code for the join processes, so we hardcoded the following topology for testing:
```
	a - b - c
	|       |
	h       d
	|       |
	g - f - e
```
a's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | b       |
| 1                            | c       |
| 2                            | e       |

b's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | c       |
| 1                            | d       |
| 2                            | f       |

c's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | d       |
| 1                            | e       |
| 2                            | g       |

d's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | e       |
| 1                            | f       |
| 2                            | h       |

e's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | f       |
| 1                            | g       |
| 2                            | a       |

f's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | g       |
| 1                            | h       |
| 2                            | b       |

g's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | h       |
| 1                            | a       |
| 2                            | c       |

h's finger table:

| 2<sup>i</sup> ID Spaces Away | Machine |
| ---------------------------- | ------- |
| 0                            | a       |
| 1                            | b       |
| 2                            | d       |
