import java.net.ServerSocket;
import java.net.Socket; 
import java.io.ObjectInputStream;
import java.io.FileNotFoundException; 


/* Receive all messages, process them, and handle lookup (file search or member search) logic. */
public class ReceivingSocket implements Runnable {
  private Member myself;

  public ReceivingSocket(Member me){
    this.myself = me;
  }

   public void run() {
	try {
	   ServerSocket listener = new ServerSocket(this.myself.myInfo.receivePort);
      
	   while(true) {
		Socket newConnection = listener.accept();

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
        	else if (inObject instanceof RequestSuccessorResponse){
			RequestSuccessorResponse rsr = (RequestSuccessorResponse) inObject;
			System.out.println("The successor of " + rsr.chordID + " is " + rsr.successor.chordID);
		} 
		else if (inObject instanceof RequestFile) {
			RequestFile rf = (RequestFile) inObject; 
			
			try { 
				String file = myself.fileSearch(rf.key, rf.sender); 
				if(file == null) {
					System.out.println("Forwarded..");
					// message has been forwarded via finger table; do nothing
				} else {
					System.out.println("Sending requested file to " + rf.sender.chordID);
					// send the file back
					RequestFileResponse message = new RequestFileResponse(file, this.myself.myInfo, rf.sender); 
					this.myself.send(message);
				}
				
			} 
			catch (FileNotFoundException e) { 
				// no file is stored at the key -- 
				// SEND A FILE NOT FOUND MESSAGE
				
			}
		} 
		else if (inObject instanceof RequestFileResponse) {
			System.out.println(myself.myInfo.chordID + " got a request file response!");

			//FileNotFoundResponse is a subclass of RequestFileResponse
			if (inObject instanceof FileNotFoundResponse) {
				
				FileNotFoundResponse fnfr = (FileNotFoundResponse) inObject;
				System.out.println("File " + fnfr.filename + " could not be found. It should have been located at node " + 
				fnfr.sender.chordID + " , but it wasn't there. You can try again with a new file name, or add the file to the system. ");
			
			} 
			else {
				RequestFileResponse rfr = (RequestFileResponse) inObject;
				System.out.println("File " + rfr.filename + " was found at " + rfr.sender.chordID + "!");
			}
		}
		else if(inObject instanceof AddFileMessage){
		   AddFileMessage addRequest = (AddFileMessage) inObject;
		   this.myself.addFile(addRequest.file, addRequest.sender);
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
}
