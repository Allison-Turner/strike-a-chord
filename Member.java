import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;


public class Member {
	private ExecutorService pool;
	private InetAddress IP;

	private String chordID;
	public static final int chordIDLength = 3;

	private Neighbor predecessor;
	private Neighbor successor;
	private Neighbor[] fingerTable;

	/* Constructors */

	public Member(Neighbor successor, Neighbor predecessor){
		getLocalIP();
		this.chordID = generateChordID(chordIDLength, this.IP);

		this.fingerTable = new Neighbor[chordIDLength];
		this.successor = successor;
		this.predecessor = predecessor;

		this.pool = Executors.newFixedThreadPool(20);
	}

	public Member(){
		Member(null, null);
	}

	public Member(Neighbor successor){
		Member(successor, null);
	}

	public Member(Neighbor predecessor){
		Member(null, predecessor);
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


	/* Receive all messages, process them, and handle lookup (file search or member search) logic. */
	public static class ReceivingSocket implements Runnable{
		private Member myself;
		private int receivingPort;

		public ReceivingSocket(Member me, int receivingPort){
			this.myself = me;
			this.receivingPort = receivingPort;
		}

		public void run() {
			try {
				ServerSocket serverSocket = new ServerSocket(receivingPort);

				while(true) {
					Neighbor sender = new Neighbor(serverSocket.accept());
					Message received = Message.deserializeMessage(neighbor.getMessage());

					//Call a message processing function based on message type, using a generic for now
					processMessage(received);
				}
			}
			catch(Exception e) {
				System.err.println("Receiving socket error");
				e.printStackTrace();
			}
		}

		//Generic placeholder
		private void processMessage(Message message){
			System.out.println("Unimplemented");
		}

	}


	/* Connect to a Neighbor, push a message, and disconnect */
	public class SendingSocket implements Runnable{
		private Neighbor recipient;
		private Message message;

		public SendingSocket(Neighbor recipient, Message message){
			this.recipient = recipient;
			this.message = message;
		}

		public void run() {
			try {
				//Connect to a Neighbor, send a serialized message, and disconnect
				//Keep all complexity in ReceivingSocket so things don't get confusing
			}
			catch(Exception e) {
				System.err.println("Sending socket error");
				e.printStackTrace();
			}
		}
	}


	/* Main workflow */
	public static void main(String[] args){
		Member member = new Member();

		//Add any preloaded knowledge about Neighbors here

		member.pool.execute(new Member.ReceivingSocket(member, 4020)); //Change later

		Scanner userInput = new Scanner(System.in);
		String command;

		do{
			command = userInput.nextLine().trim();

			//Parse user commands here

		}while(!command.equals("QUIT"));

    }
}
