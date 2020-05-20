import java.net.InetAddress; 
import java.net.Socket; 
import java.io.ObjectOutputStream;

/* Connect to a Neighbor, push a message, and disconnect */
public class SendingSocket implements Runnable {
  private Message message;

  public SendingSocket(Message message){
    this.message = message;
  }

  public void run() {
    try {
    
      InetAddress toIP = message.recipient.IP; 
      int toPort = message.recipient.receivePort; 
      
      Socket sock = new Socket(toIP, toPort); 
      ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream()); 
      out.writeObject(message); 
      sock.close(); 
      
    }
    catch(Exception e) {
      System.err.println("Send socket error");
      e.printStackTrace();
    }
  }
}
