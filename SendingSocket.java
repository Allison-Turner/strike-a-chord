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
      recipient.pushMessage(message.serializeMessage());
      recipient.disconnect();
      //Keep all complexity in ReceivingSocket and Member so things don't get too confusing
    }
    catch(Exception e) {
      System.err.println("Send socket error");
      e.printStackTrace();
    }
  }
}
