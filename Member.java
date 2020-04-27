import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
	private ExecutorService pool;
	private InetAddress IP;
	private int receivePort;
	private int sendPort;

	private String chordID;
	public static final int chordIDLength = 3;

	private Neighbor predecessor;
	private Neighbor[] successors;
	private Neighbor[] fingerTable;

	/*
	Constructors
	At minimum, a Member must be created with knowledge of its receiving port number.
	*/

	public Member(int receivePort, Neighbor successor, Neighbor predecessor){
		this.receivePort = receivePort;
		this.sendPort = 4000;
		getLocalIP();
		this.chordID = generateChordID(chordIDLength, this.IP);

		this.fingerTable = new Neighbor[chordIDLength];
		//Maintaing a list of O(log n) successors maintains fast searches even in the case of failure rates >=0.5
		this.successors = new Neighbors[Math.floor(Math.log(Math.pow(2, chordIDLength)))];
		successors[0] = successor;
		this.predecessor = predecessor;

		this.pool = Executors.newFixedThreadPool(20);
	}

	public Member(int receivePort){
		Member(receivePort, null, null);
	}

	public Member(int receivePort, Neighbor successor){
		Member(receivePort, successor, null);
	}

	public Member(int receivePort, Neighbor predecessor){
		Member(receivePort, null, predecessor);
	}


	/* Processes to define properties */
	//Connect to a public IP lookup service and read what my public IP is
	private void getLocalIP(){
		try{
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

			this.IP = InetAddress.getByName(in.readLine());
		}
		catch(Exception e){
			System.out.println("Error retrieving local IP");
			e.printStackTrace();
		}
	}

	//Publicly available function to generate Chord ID for any value
	public static String generateChordID(String value){
		String id = org.apache.commons.codec.digest.DigestUtils.sha1Hex(value);
		return id.substring(0, chordIDLength);
	}


	/*
	Add any functions that will create a SendSocket here, whether it's the ReceivingSocket class or the main workflow that will call them
	The ReceivingSocket property Member myself exists so that ReceivingSocket can call these functions
	 */
	 private void join(){
		 //notify new predecessor that I'm your successor now
		 //Copy successor's finger table
		 System.out.println("Unimplemented");
	 }

	 private void stabilize(){
		 //Request predecessor's successor
		 this.pool.execute(new SendingSocket(predecessor, new RoutingInfoRequest("WHO-IS-SUCCESSOR", this)));
		 //Check finger table entries
		 
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

		}while(!command.equals("QUIT"));

    }
}
