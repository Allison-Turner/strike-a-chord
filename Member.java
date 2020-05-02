import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
  
   public MemberInfo myInfo; 
   
   private ExecutorService pool;  
   public ServerSocket listener;
   
	private MemberInfo predecessor;
	private MemberInfo[] successors;
	private MemberInfo[] fingerTable;
 

	/*
	Constructors
	At minimum, a Member must be created with knowledge of its receiving port number.
	*/

	public Member(int receivePort, MemberInfo successor, MemberInfo predecessor){
      this.myInfo = new MemberInfo(receivePort);
      
      try {
    	  this.listener = new ServerSocket(receivePort);
      } catch (IOException e) {
    	  System.err.println("Member " + myInfo.chordID + " failed to set up its listener correctly..");
      }
      
      this.fingerTable = new MemberInfo[myInfo.chordIDLength];
      //Maintaining a list of O(log n) successors maintains fast searches even in the case of failure rates >=0.5
      this.successors = new MemberInfo[(int) Math.floor(Math.log(Math.pow(2, myInfo.chordIDLength)))];
      successors[0] = successor;
      this.predecessor = predecessor;

      this.pool = Executors.newFixedThreadPool(20);
	}

	public Member(int receivePort){
		this(receivePort, null, null);
	}

	public Member(int receivePort, MemberInfo successor){
		this(receivePort, successor, null);
	}
   // can only have one of these
   /*
	public Member(int receivePort, MemberInfo predecessor){
		Member(receivePort, null, predecessor);
	}*/ 
   
	/*
	Add any functions that will create a SendSocket here, whether it's the ReceivingSocket class or the main workflow that will call them
	The ReceivingSocket property Member myself exists so that ReceivingSocket can call these functions
	 */
	 private void join(){
		 //notify new predecessor that I'm your successor now
		 //Copy successor's finger table
		 
		 System.out.println("Unimplemented");
		 
	 }

	 void stabilize(){
		 //Request predecessor's successor
       RequestSuccessor m = new RequestSuccessor(myInfo, predecessor); 
		 this.pool.execute(new SendingSocket(m));
		 //Check finger table entries
		 
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
    
    
   
	/* Main workflow */
	public static void main(String[] args){
		Member member = new Member(4000);

		//Add any preloaded knowledge about Neighbors here

		member.pool.execute(new ReceivingSocket(member));

		Scanner userInput = new Scanner(System.in);
		String command;

		do{
			command = userInput.nextLine().trim();

			//Parse user commands here

		} while(!command.equals("QUIT"));
		
		userInput.close(); 
		
    }
}
