
public class Message{
  public String messageType;

  public Message(String messageType){
    this.messageType = messageType;
  }

  public boolean equals(Message message){
    return true;
  }

  public String serializeMessage(){
    return "MESSAGETYPE " + this.messageType;
  }

  public static Message deserializeMessage(String message) throws UnknownHostException{
    return null;
  }

}
