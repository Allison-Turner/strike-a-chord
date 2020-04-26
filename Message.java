
public abstract class Message{
  public String messageType;

  public Message(String messageType){
    this.messageType = messageType;
  }

  abstract boolean equals(Message message);
  abstract String serializeMessage();
  abstract static Message deserializeMessage(String message) throws UnknownHostException;

}
