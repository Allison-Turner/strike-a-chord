import java.net.ServerSocket;
import java.net.Socket; 
import java.io.ObjectInputStream;


/* Receive all messages, process them, and handle lookup (file search or member search) logic. */
public class ReceivingSocket implements Runnable {
  private Member myself;

  public ReceivingSocket(Member me){
    this.myself = me;
  }

   public void run() {
	try {
	   ServerSocket listener = new ServerSocket(myself.myInfo.receivePort);
      
	   while(true) {
		Socket newConnection = listener.accept();

		// are these blocking??? (probably)
		ObjectInputStream in = new ObjectInputStream(newConnection.getInputStream());
		Object inObject = in.readObject();  
        
		if (inObject instanceof RequestSuccessor) {
		   RequestSuccessor rs = (RequestSuccessor) inObject;
        	
		   MemberInfo successor = myself.findSuccessor(rs.chordID, rs.sender); 
		   if (successor == null) {
			// do nothing -- findSuccessor already forwarded the message
		   }
		   else { 
			// send a requestSuccessorResponse to rs.sender with the successor
			RequestSuccessorResponse message = new RequestSuccessorResponse(rs.chordID, successor, this.myself.myInfo, rs.sender); 
			this.myself.send(message); 
		   }
		}
        	// .. put more receiving messages here  
		else if (inObject instanceof RequestSuccessorResponse){
			RequestSuccessorResponse rsr = (RequestSuccessorResponse) inObject;
			System.out.println("The successor of " + rsr.chordID + " is " + rsr.successor.chordID);
		} else if (inObject instanceof RequestFile) {
			
		}
		else {
		   System.err.println("Recieved an object of unknown message type"); 
		}
        
	   }
	}
	catch(Exception e) {
	   System.err.println("Receive socket error");
	   e.printStackTrace();
	}
   }

  /*
  //Process new successor notification (includes moving files to new successor)
  private void processSuccessorUpdate(RoutingInfoRequest message){

  }

  //Process request for my finger fingerTable
  private void processFingerTableReadRequest(RoutingInfoRequest message){
  
      Message m = new RequestFingerTable(myInfo, predecessor); 
      this.pool.execute(new SendingSocket(m));
  }

  //Process request for my immediate successor
  private void processSuccessorRequest(RoutingInfoRequest message){

  }

  //Process file search request
  private void processFileSearch(FileSearchMessage message){

  }
  
  //Process finger table update request
  */
 
}
