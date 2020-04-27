
public abstract class Message{
  public String messageType;

  public Message(String messageType){
    this.messageType = messageType;
  }

  public static Message deserialize(String message){
    int start;
    int end;

    start = message.indexOf("(");
    end = message.indexOf(")");
    String type = message.substring(start, end);

    if(type.equals("FILESEARCH")){
      return FileSearchMessage.deserializeMessage(message);
    }
    else if(type.equals("WHO-IS-SUCCESSOR") || type.equals("READ-FINGERTABLE") || type.equals("SUCCESSOR-UPDATE")){
      return RoutingInfoRequest.deserializeMessage(message);
    }
    else{
      return null;
    }
  }

  abstract boolean equals(Message message);
  abstract String serializeMessage();
  abstract static Message deserializeMessage(String message) throws UnknownHostException;

}
