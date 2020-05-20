# strike-a-chord
CS 343 final project
Allison Turner and Lily Orth-Smith
<br>

The chord algorithm is a distributed hashing algorithm. Machines are set up in a ring topology, with integer IDs modulo 2^m that are obtained by hashing thier IP addresses. 
We chose to use the chord algorithm to store files across a distributed system. Each file has a key (chord ID) associated with it, an integer key modulo 2^m, just like the machine IDs. In our case, the files are just strings, and we use SHA-2 to hash each file, and take the result modulo 2^m to get its chord ID.

Not every integer between 0 and 2^m will have a machine associated with it. The chord algorithm stores file with chord ID n (where 0 <= n < 2^m) at the machine with the next-highest ID. 

The naive approach to finding the file with id n is to just ask our successor (the machine with the next highest ID) for it, which will then ask its successor, and so on until we get the file. Chord uses a distributed binary search to speed this up. Each machine records a 

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


How to use our code: 
1. Open 8 VMs, and number them 1 through 8. 
2. Edit start.sh:
	- change MEMBER1IP to the IP of VM 1, MEMBER2IP to the IP of VM 2, etc. 
	- you do not have to change the ports
3. On machine 1, run "./start.sh 1", on machine 2, run "./start.sh 2", etc.
	- you will see the chord ID of this machine, and the machine's fingertable
4. Now the VMs are set up. You will be prompted to input a command. You can type "help" to view all commands. 
	- Enter "add <filename>" to add a file to the chord ring
	- Enter "search <filename>" to retrieve a file in the chord ring
	- Enter "successor <key>" to find the node following that key
	- Enter "help" to view this menu again.
5. Initially, there are no files stored in ring, so you will have to add some. Then, you can search for those files using the search function, and will be able to find them in the ring. 
	
Expected behavior: 
