import java.net.ServerSocket;
import java.net.Socket; 
import java.io.ObjectInputStream;


/* Receive all messages, process them, and handle lookup (file search or member search) logic. */
public class ReceivingSocket implements Runnable {
  private Member myself;
  private long lastStabilize;

  public ReceivingSocket(Member me){
    this.myself = me;
    this.lastStabilize = 0;
  }

  public void run() {
 
    try {
      
      Socket sock = myself.getListener().accept(); 
      while(true) {
      
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
        Object inObject = in.readObject(); 
        // are these blocking??? (probably) 
        
        if (inObject instanceof RequestSuccessor) {
        	RequestSuccessor rs = (RequestSuccessor) inObject;
        	
        	MemberInfo successor = myself.findSuccessor(rs.chordID, rs.sender); 
        	if (successor == null) {
        		 // do nothing
        	} else { // send it back to the requester
        		
        		// send a requestSuccessorResponse to rs.sender
        		
        		
        	}
        // .. put more receiving messages here 
        } else {
         System.err.println("Recieved an object of unknown message type"); 
        }
        
        // put this in a separate thread
        //Perform periodic checks defined by Chord's join, leave, and stabilization processes
        if((System.currentTimeMillis() - this.lastStabilize) > 60000){
          lastStabilize = System.currentTimeMillis();
          myself.stabilize();
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
