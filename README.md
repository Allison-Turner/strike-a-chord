# strike-a-chord
CS 343 final project
Allison Turner and Lily Orth-Smith
<br>

The chord algorithm is a distributed hashing algorithm. Machines are set up in a ring topology, with integer IDs modulo 2^m that are obtained by hashing thier IP addresses. 
We chose to use the chord algorithm to store files across a distributed system. Each file has a key (chord ID) associated with it, an integer key modulo 2^m, just like the machine IDs. In our case, the files are just strings, and we use SHA-2 to hash each file, and take the result modulo 2^m to get its chord ID.

Not every integer between 0 and 2^m will have a machine associated with it. The chord algorithm stores file with chord ID n (where 0 <= n < 2^m) at the machine with the next-highest ID. 

The naive approach to finding the file with id n is to just ask our successor (the machine with the next highest ID) for it, which will then ask its successor, and so on until we get the file. 

Chord uses a distributed binary search to speed this up. Each machine records a "finger table" of m pointers to machines in the ring. The entry in the n-1 spot (for 1 <= n <= m) contains a pointer to the machine with id that is just greater than 2^n. Then, when we search for a file with key f on a machine, if the machine doesn't have it, it asks the another machine in its fingertable with the id closest to but not greater than f. This scheme enables us to find files in O(logn) time. 

The chord paper outlines ways for machines to join and leave the ring, as well as stabilization methods to account for failures. We did not write code for the join or stabilization methods, and we hardcoded the following topology for testing: 

(Note that each machine is assigned a chord ID based on its IP address). 

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
	- Enter "successor <key>" to find the node following that key (key is an integer)
	- Enter "help" to view this menu again.
5. Initially, there are no files stored in ring, so you will have to add some. Then, you can search for those files using the search function, and will be able to find them in the ring. 
	
Expected behavior: 

For each command, output is expected on the machine the command was run on. 

- Add: 
	- The machine requesting to add a file should output that it is requesting to add a file with a specific key. Then, the machine the file was added to will report that it added the file. You should now be able to use search to find this file. 
- Search: 
	- If the file cannot be found in the ring, the machine sending the search message will print the machine where the file should have been. If the file can be found in the ring, it will print that it found the file, and also the chord ID of the machine it found the file on. 
- Successor: 
	- Searches for the successor of the inputted key, and prints the successor. This is not really used as a part of the search and add functionality, but we used it for debugging and figuring out how searching should work. 
- Help:
 	- Prints the help menu

Implementation:
- Each machine is represented as an instance of the Member class. MemberInfo contains the IP of a VM and the chord ID of it. This is a serializable class, and we send MemberInfo in our messages. 
- The Message class is a parent class for all messages sent in the system. We have different message types for every kind of message sent, even if some of those have the same data. Each Message has a sender and a reciever of type MemberInfo, and subclasses have additional information. We follow the convention that the sender field is not updated when messages are forwarded, which enables us to send messages back to the original requester easily. 
- SendingSocket takes a message in its constructor, opens a socket to the machine in the reciever MemberInfo field, sends the message, and closes the socket. 
- RecievingSocket listens for messages from other machines. One is open on every instance of Member. On recieving an object, it figures out what type of message it is, and based on that gives the appropriate response. 

As an example, I'll walk you through how fileSearch works:
1. We hash the file we're looking for, and pass it into fileSearch along with our own MemberInfo. 
2. fileSearch checks if the file is on this computer. If it is, we return the name of the file to the main method, and print that we have the file. If not, we find the closest preceeding machine (let's call it n) in our finger table using the closestPreceeding method, construct a RequestFile message from us to machine n, and return null to main because we forwarded the message. 
3. The RecieveSocket on machine n recieves the RequestMessage. It then calls fileSearch, but instead of passing in itself as the requester, it passes in the sender of the FileRequest message. Again, if the result of fileSearch is null, the RecieveSocket does nothing since the message was forwarded. Otherwise, we construct a new FileRequestResponse, and send it back to the original requester. 
4. The RecieveSocket of the machine that requested the file gets the FileRequestResponse, and prints that it found the file. 

Implementation challenges:
- Remotely designing the project and working together was difficult
- Figuring out how to compare chord IDs was difficult, especially across the zero. (i.e., if m = 3, then the chord ID space is 2^3 = 8. In this space, 2 could be a successor of 7, but 2 is strictly less than 7.) To deal with this, we wrote a function called "compareChordIds" which determines whether a key is "less than" another key in the chord ID space. 
