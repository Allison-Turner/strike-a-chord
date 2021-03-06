import java.util.*; 
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
  
   public MemberInfo myInfo; 
   
   private ExecutorService pool;
   
   private MemberInfo predecessor;
   private MemberInfo[] fingerTable;

   ArrayList<MyFile> files;
   int next; // for fix fingers

   /*
   Constructors
   At minimum, a Member must be created with knowledge of its receiving port number.
   */

   public Member(int receivePort, MemberInfo[] fingerEntries, MemberInfo predecessor){
	this.myInfo = new MemberInfo(receivePort);
      
	this.fingerTable = new MemberInfo[myInfo.chordIDLength];
	this.predecessor = predecessor;

	this.pool = Executors.newFixedThreadPool(20);
	this.files = new ArrayList<MyFile>();
   }

   public Member(int receivePort, MemberInfo[] fingerEntries){
	this(receivePort, fingerEntries, null);
   }

   public Member(int receivePort){
	this(receivePort, null, null);
   }
   
   /*
   Add any functions that will create a SendSocket here, whether it's the ReceivingSocket class, Stabilizer class, or the main workflow that will call them
   The ReceivingSocket and Stabilizer properties Member myself exist so they can call these functions
   */
	
	 
   //Returns a MemberInfo with the successor of the chordID if we can find it in our fingerTable
   //Else, send a message to the closest chordID in the fingerTable and return null
   public  MemberInfo findSuccessor(int chordID, MemberInfo requester) {
	   // if it's us
	   synchronized(this) {
		   if(this.compareChordIds(this.myInfo.chordID, chordID) && (this.compareChordIds(this.fingerTable[0].chordID, chordID) || this.fingerTable[0].chordID == chordID)) {
		   return this.myInfo;
		   } 
	   }
	   MemberInfo nextClosest = closestPreceding(chordID);
			 
	   RequestSuccessor message = new RequestSuccessor(chordID, requester, nextClosest);
	   this.send(message);
			 
	   // RETURNING NULL BECAUSE NO OPTION TYPE #ANGERY
	   return null; 
	
   }
	 
   public synchronized MemberInfo closestPreceding(int chordID) {
	for (int i = this.myInfo.chordIDLength - 1; i >= 1; i--) {
	   int tableID = (this.fingerTable[i].chordID); 
	   if(this.compareChordIds(this.myInfo.chordID, tableID) && (this.compareChordIds(chordID, this.myInfo.chordID) || chordID == this.myInfo.chordID)) {
		return fingerTable[i];
	   } 
	}
	return this.myInfo;
   }
   
   public String fileSearch(int key, MemberInfo requester) throws FileNotFoundException {
	   
	   System.out.println("Searching for " + key + " on " + requester.chordID);
	   synchronized(this) {
		   //if((this.myInfo.chordID > key) && (key <= this.fingerTable[0].chordID)) { 
		   if((this.compareChordIds(this.myInfo.chordID, key) && 
				   (this.compareChordIds(this.fingerTable[0].chordID, key) || (key == this.fingerTable[0].chordID)))) {
			   for (MyFile file: this.files) {
				   if(file.chordID == key) {
					   System.out.println("I have the file!");
					   return file.fileName;
				   }
			   }
			   // we didn't find the file
			   throw new FileNotFoundException(); 
		   }
	   }


	   MemberInfo nextClosest = closestPreceding(key);

	   RequestFile message = new RequestFile(key, requester, nextClosest);
	   this.send(message);

	   // RETURNING NULL BECAUSE NO OPTION TYPE #ANGERY
	   return null; 

   }
   
   // finds a machine to add a file to
   // returns null if the file should be added on this machine
   public MemberInfo findNewFileMachine(MyFile file) {
	//File is in my ID space range
	if((this.compareChordIds(file.chordID, predecessor.chordID) && 
			(this.compareChordIds(this.myInfo.chordID, file.chordID) || file.chordID == this.myInfo.chordID))) {
	   return null;
	}
	//File should be stored by another machine
	else{
	   int potentialStorer = -1;

	   //Find the machine with the lowest Chord ID that's still larger than the file's Chord ID
	   
	   for(int i = 0; i < this.fingerTable.length; i++){
		if( this.compareChordIds( this.fingerTable[i].chordID, file.chordID ) ){
		   potentialStorer = i;
		}

	   }

	   return this.fingerTable[potentialStorer];
	}
   }

   public void addFile(MyFile file, MemberInfo originator){
	MemberInfo hostMachine = findNewFileMachine(file);
	if(hostMachine == null){
	   files.add(file);
	   System.out.println("Added file " + file.fileName + " with Chord ID " + file.chordID + " to my records.");
	}
	else{
	   this.send(new AddFileMessage(file, originator, hostMachine));
	   System.out.println("Sent request to add file " + file.fileName + " with Chord ID " + file.chordID + " to "
				+ hostMachine.IP.toString() + " with Chord ID " + hostMachine.chordID);
	}
   }
	 
   // Check that things have joined correctly but we're not implementing joining
   public synchronized void stabilize(){
	   
	//Request predecessor's successor
	RequestSuccessor m = new RequestSuccessor(myInfo, predecessor); 
	this.pool.execute(new SendingSocket(m));

	//Check finger table entries
   }

   // Also part of join -- not implementing
   public void notify(int chordID) {}

   // Also part of join -- could implement but not a priority
   public synchronized void fixFingers() {}
	 
   // we will implement this
   public void checkPredecessor() {}
	 
   
   //helpers
   
   // enables us to send messages from recieving socket
   public void send(Message m) {
	this.pool.execute(new SendingSocket(m));
   }

    /* Returns our finger table */
    public synchronized MemberInfo[] getFingerTable() {
        return this.fingerTable; 
    }
    
    public synchronized void setFingerTable(MemberInfo[] ft) {
      this.fingerTable = new MemberInfo[myInfo.chordIDLength]; 
      System.arraycopy(ft, 0, this.fingerTable, 0, myInfo.chordIDLength); 
    }
   
   // if a is to the right of b, return true, else false
   public boolean compareChordIds(int a, int b) {
	   if (Math.abs(a - b) > Math.pow(2, this.myInfo.chordIDLength - 1)) {
		   return b > a; 
	   } 
	   else {
		   return a > b;
	   }
	  
   }
   
   public int findFingerTableSlot(MemberInfo newEntry){
	int slot = this.myInfo.chordIDLength;

	for(int i = 0; i < this.myInfo.chordIDLength; i++){
	   int maxI = ( (this.myInfo.chordID + ((int) Math.pow(2, i)) ) % ((int) Math.pow(2, this.myInfo.chordIDLength)) );
	   if( this.compareChordIds(maxI, newEntry.chordID)){
		slot = i;
	   }
	}
	return slot;
   }

   public void addFingerTableEntry(MemberInfo newEntry){
	int slot = findFingerTableSlot(newEntry);

	if(slot < this.myInfo.chordIDLength && ((this.fingerTable[slot] == null) || this.compareChordIds(newEntry.chordID, this.fingerTable[slot].chordID) )){
	   this.fingerTable[slot] = newEntry;
	}
   }

   public void setPredecessor(MemberInfo neighbor){
	//No null predecessor
	if(this.predecessor == null){
	   this.predecessor = neighbor;
	}
	else if( this.compareChordIds( this.myInfo.chordID, neighbor.chordID ) && this.compareChordIds(neighbor.chordID, this.predecessor.chordID) ){
	   this.predecessor = neighbor;
	}
   }

   public void setSuccessor(MemberInfo neighbor){
	//No null successor
	if(this.fingerTable[0] == null){
	   this.fingerTable[0] = neighbor;
	}
	else if( this.compareChordIds(this.fingerTable[0].chordID, neighbor.chordID) && this.compareChordIds(neighbor.chordID, this.myInfo.chordID) ){
	   this.fingerTable[0] = neighbor;
	}
   }

   public synchronized void printFingerTable(){
	System.out.println("------- Myself -------");
	System.out.println("My Chord ID: " + this.myInfo.chordID);
	System.out.println("My IP: " + this.myInfo.IP.toString());

	System.out.println("-----Predecessor -----");
	if(this.predecessor != null){
	   System.out.println("Predecessor Chord ID: " + this.predecessor.chordID);
	   System.out.println("Predecessor IP: " + this.predecessor.IP.toString());
	}
	else{
	   System.out.println("Predecessor is NULL");
	}

	System.out.println("-----Finger Table-----");
	for(int i = 0; i < this.fingerTable.length; i++){
	   if(this.fingerTable[i] != null){
		System.out.println("Entry :" + i);
		System.out.println("Chord ID: " + this.fingerTable[i].chordID);
		System.out.println("IP: " + this.fingerTable[i].IP.toString());
	   }
	   else{
		System.out.println("Entry " + i + " is NULL");
	   }
	}
   }
    
    
    public void help() {
    	System.out.println("Welcome to Chord.");
    	System.out.println("This member has id " + this.myInfo.chordID + " and max id length in bits (\"m\")" + this.myInfo.chordIDLength);
    	
    	System.out.println("Enter \"add <filename>\" to add a file to the chord ring");
    	System.out.println("Enter \"search <filename>\" to retrieve a file in the chord ring");
    	System.out.println("Enter \"successor <key>\" to find the node following that key");
    	System.out.println("Enter \"help\" to view this menu again."); 
    }
   
   /* Main workflow */
   public static void main(String[] args){
	//command line should take the format: 
	//java Member (My Receive Port) (Machine A IP) (Machine A Receive Port) (Machine B IP) ....

	Member member = new Member(Integer.parseInt(args[0]));

	for(int i = 1; i < args.length; i+=2){
	   MemberInfo newNeighbor = new MemberInfo(MemberInfo.parseIP(args[i]), Integer.parseInt(args[i+1]));
	   member.addFingerTableEntry(newNeighbor);
	   member.setPredecessor(newNeighbor);
	   member.setSuccessor(newNeighbor);
	}

	MemberInfo lastNonNull = member.fingerTable[0];

	//No null finger table entries. 
	//If no members fit into the ID space (id + 2^i) mod 2^m, 
	//then the entry for (id + 2^(j)) mod 2^m, where j was the 
	//last non-null entry, will be duplicated
	for(int i = 1; i < member.fingerTable.length; i++){
	   if(member.fingerTable[i] == null){
		member.fingerTable[i] = lastNonNull;
	   }
	   else{
		lastNonNull = member.fingerTable[i];
	   }
	} 

	member.printFingerTable();

	//We give the ReceivingSocket and Stabilizer handles on the invoking Member for when they 
	//need to invoke a process that alters the Member's connectivity info or open a SendingSocket
	member.pool.execute(new ReceivingSocket(member));
	member.pool.execute(new Stabilizer(member));

	Scanner userInput = new Scanner(System.in);
	String[] command;

		
	// UI -- 
	// Enter "add <filename>" to add a file to the chord ring
	// Enter "search <filename>" to retrieve a file in the chord ring
	// Enter "successor <key>" to find the node following that key
	// Enter "help" to view this menu again.
	while(true) {
	   System.out.println("Please enter a command");
	   command = userInput.nextLine().trim().split(" "); 
			
	   if (command.length == 0) {
		System.out.println("Please enter a valid command.  Enter \"help\" to see commands"); 
	   }

	   if (command[0].equals("help")) {
		member.help(); 
		continue;
	   } 	
	   if (command[0].equals("quit")) {
		break; 
	   }

	   if (command.length != 2) {
		System.out.println("Please enter a valid command. Enter \"help\" to see commands");
		continue;
	   } 
	   if (command[0].equals("add")) {
		String filename = command[1]; 
		int key = member.myInfo.generateChordID(filename);
		member.addFile(new MyFile(filename, key), member.myInfo);	 
		continue;
	   }
	   
	   if (command[0].equals("search")) {
		String filename = command[1];
		int key = member.myInfo.generateChordID(filename); 
		try {
			member.fileSearch(key, member.myInfo);
		} catch (FileNotFoundException e){
			System.out.println("Couldn't find the file " + filename + ". It should have been on this machine, but it isn't. Oops!");
		}
				
		continue; 
	   } 
	   
	   if (command[0].equals("successor")) {
			int key = Integer.parseInt(command[1]);
			member.findSuccessor(key, member.myInfo);
			
			continue; 
			
		}
	}
	userInput.close(); 
		
    }
}
