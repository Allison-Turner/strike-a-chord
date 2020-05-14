import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
  
   public MemberInfo myInfo; 
   
   private ExecutorService pool;  
   public ServerSocket listener;
   
   private MemberInfo predecessor;
   //private MemberInfo[] successors;
   private MemberInfo[] fingerTable;
	
   int next; // for fix fingers
 

	/*
	Constructors
	At minimum, a Member must be created with knowledge of its receiving port number.
	*/

	public Member(int receivePort, MemberInfo successor, MemberInfo predecessor){
	   this.myInfo = new MemberInfo(receivePort);
      
/*      try {
    	  this.listener = new ServerSocket(receivePort);
      } catch (IOException e) {
    	  System.err.println("Member " + myInfo.chordID + " failed to set up its listener correctly..");
      }*/
      
	   this.fingerTable = new MemberInfo[myInfo.chordIDLength];
      //Maintaining a list of O(log n) successors maintains fast searches even in the case of failure rates >=0.5
      //this.successors = new MemberInfo[(int) Math.floor(Math.log(Math.pow(2, myInfo.chordIDLength)))];
      //successors[0] = successor;
      this.predecessor = predecessor;

      this.pool = Executors.newFixedThreadPool(20);
	}

	public Member(int receivePort){
		this(receivePort, null, null);
	}

	public Member(int receivePort, MemberInfo successor){
		this(receivePort, successor, null);
	}
   
	/*
	Add any functions that will create a SendSocket here, whether it's the ReceivingSocket class or the main workflow that will call them
	The ReceivingSocket property Member myself exists so that ReceivingSocket can call these functions
	 */
	
	 
	/* Returns a MemberInfo with the successor of the chordID if we can find it in our fingerTable
	 * Else, send a message to the closest chordID in the fingerTable and return null
	 */
	 MemberInfo findSuccessor(int chordID, MemberInfo requester) {
		
		 if((this.myInfo.chordID > chordID) && (chordID <= this.fingerTable[0].chordID)) { 
			 return this.fingerTable[1]; 
		 } else { 
			 MemberInfo nextClosest = closestPreceeding(chordID);
			 
			 RequestSuccessor message = new RequestSuccessor(chordID, requester, nextClosest);
			 SendingSocket s = new SendingSocket(message); 
			 
			 // RETURNING NULL BECAUSE NO OPTION TYPE #ANGERY
			 return null; 
			 
		 }
		 
	 }
	 
	 MemberInfo closestPreceeding(int chordID) {
		 
		 for (int i = this.myInfo.chordIDLength; i >= 1; i--) {
			 int tableID = (this.fingerTable[i].chordID); 
			 if (this.myInfo.chordID > tableID && this.myInfo.chordID <= chordID) { 
				 return fingerTable[i];
			 } 
		 }
		return this.myInfo;
	 }
	 
	 
	 void stabilize(){
		 //Request predecessor's successor
		 // add chordID
       RequestSuccessor m = new RequestSuccessor(myInfo, predecessor); 
	   this.pool.execute(new SendingSocket(m));
		 //Check finger table entries
		 
	 }
	 void notify(int chordID) { 
		 
	 }
	 
	 void fixFingers() {
		 
		 
	 }
	 
	 
	 void checkPredecessor() {
	 }
	 
   
    // Getters and setters
    /* Returns our finger table */
    public MemberInfo[] getFingerTable() {
        return this.fingerTable; 
    }
    
    public ServerSocket getListener() {
      return listener; 
    }
    
    public synchronized void setFingerTable(MemberInfo[] ft) {
      this.fingerTable = new MemberInfo[myInfo.chordIDLength]; 
      System.arraycopy(ft, 0, this.fingerTable, 0, myInfo.chordIDLength); 
    }
    
    
    public void help() {
    	System.out.println("Welcome to Chord.");
    	System.out.println("This member has id " + this.myInfo.chordID + " and max id length in bits (\"m\")" + this.myInfo.chordIDLength);
    	
    	System.out.println("Enter \"add <filename>\" to add a file to the chord ring");
    	System.out.println("Enter \"search <filename>\" to retrieve a file in the chord ring");
    	System.out.println("Enter \"help\" to view this menu again."); 
    }
   
    
    
    
	/* Main workflow */
	public static void main(String[] args){
		Member member = new Member(4000);

		//Add any preloaded knowledge about Neighbors here

		member.pool.execute(new ReceivingSocket(member));

		Scanner userInput = new Scanner(System.in);
		String[] command;

		
		// UI -- 
		// add key filename 
		// add key 
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
				
				continue; 
			} 
		}
		
		userInput.close(); 
		
    }
}
