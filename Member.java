import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
  
   public MemberInfo myInfo; 
   
   private ExecutorService pool;  
   //public ServerSocket listener;
   
   private MemberInfo predecessor;
   //private MemberInfo[] successors;
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
   Add any functions that will create a SendSocket here, whether it's the ReceivingSocket class or the main workflow that will call them
   The ReceivingSocket property Member myself exists so that ReceivingSocket can call these functions
   */
	
	 
   /* Returns a MemberInfo with the successor of the chordID if we can find it in our fingerTable
   * Else, send a message to the closest chordID in the fingerTable and return null
   */
   public MemberInfo findSuccessor(int chordID, MemberInfo requester) {
	   // if its us
	if((this.myInfo.chordID > chordID) && (chordID <= this.fingerTable[0].chordID)) { 
	   return this.myInfo;
	} 
	else { 
	  MemberInfo nextClosest = closestPreceeding(chordID);
			 
	  RequestSuccessor message = new RequestSuccessor(chordID, requester, nextClosest);
	  this.send(message);
			 
	   // RETURNING NULL BECAUSE NO OPTION TYPE #ANGERY
	   return null; 
			 
	}
   }
   
   public MemberInfo closestPreceeding(int chordID) {
	for (int i = this.myInfo.chordIDLength; i >= 1; i--) {
	   int tableID = (this.fingerTable[i].chordID); 
	   if (this.myInfo.chordID > tableID && this.myInfo.chordID <= chordID) { 
		return fingerTable[i];
	   } 
	}
	return this.myInfo;
   }
   
   public String fileSearch(int key, MemberInfo requester) throws FileNotFoundException {
	   if((this.myInfo.chordID > key) && (key <= this.fingerTable[0].chordID)) { 
		   for (MyFile file: this.files) {
			   if(file.chordID == key) {
				   return file.fileName;
			   }
		   }
		   // we didn't find the file
		   
		   throw new FileNotFoundException(); 
		} 
		else { 
		  MemberInfo nextClosest = closestPreceeding(key);
				 
		  RequestFile message = new RequestFile(key, requester, nextClosest);
		  this.send(message);
				 
		   // RETURNING NULL BECAUSE NO OPTION TYPE #ANGERY
		   return null; 
				 
		}
	   
   }
   
   public void addFile(MyFile files) {
   
   }
	 
   // Unimplmemented error -- check that things have joined correctly but we're not implementing joining
   public void stabilize(){
	   
	System.out.println("Stabilizing.");
	//Request predecessor's successor
	// add chordID
	//RequestSuccessor m = new RequestSuccessor(myInfo, predecessor); 
	//this.pool.execute(new SendingSocket(m));
	//Check finger table entries
   }

   // Also part of join -- not implementing
   public void notify(int chordID) { 
	
   }

   // Also part of join -- could implement but not a priority
   public void fixFingers() {
   
   }
	 
   // we will implement this
   public void checkPredecessor() {

   }
	 
   
   //helpers
   
   // enables us to send messages from recieving socket
   public void send(Message m) {
	   this.pool.execute(new SendingSocket(m));
   }

    /* Returns our finger table */
    public MemberInfo[] getFingerTable() {
        return this.fingerTable; 
    }
    
    public synchronized void setFingerTable(MemberInfo[] ft) {
      this.fingerTable = new MemberInfo[myInfo.chordIDLength]; 
      System.arraycopy(ft, 0, this.fingerTable, 0, myInfo.chordIDLength); 
    }

   public void addFingerTableEntry(MemberInfo newEntry){
	System.out.println("IP: " + newEntry.IP.toString() + " Chord ID: " + newEntry.chordID);
	int slot = 0;
	for(int i = 0; i < myInfo.chordIDLength; i++){
	   if(newEntry.chordID < ((myInfo.chordID + Math.pow(2, i)) % Math.pow(2, myInfo.chordIDLength)) ){
		slot = i;
	   }
	}
	if((this.fingerTable[slot] == null) || (this.fingerTable[slot].chordID < newEntry.chordID) ){
	   this.fingerTable[slot] = newEntry;
	   System.out.println("Added " + newEntry.chordID + "to slot " + slot);
	}
   }

   public void printFingerTable(){
	System.out.println("My Chord ID: " + this.myInfo.chordID);
	System.out.println("-----Finger Table-----");
	for(int i = 0; i < this.fingerTable.length; i++){
	   System.out.println("Entry :" + i);
	   System.out.println(this.fingerTable[i]);
	   System.out.println("Chord ID: " + this.fingerTable[i].chordID);
	   System.out.println("IP: " + this.fingerTable[i].IP.toString());
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
	   MemberInfo newFinger = new MemberInfo(MemberInfo.parseIP(args[i]), Integer.parseInt(args[i+1]));
	   member.addFingerTableEntry(newFinger);
	}

	//No null finger table entries. 
	//If no members fit into the ID space (id + 2^i) mod 2^m, 
	//then the entry for (id + 2^(i-1)) mod 2^m will be duplicated
	for(int i = 1; i < member.fingerTable.length; i++){
	   if(member.fingerTable[i] == null){
		member.fingerTable[i] = member.fingerTable[i - 1];
		System.out.println("Copying previous finger table entry into slot " + i);
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
