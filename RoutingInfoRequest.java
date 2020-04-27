public class RoutingInfoRequest extends Message{
  /*
  Message types and role of sender
  WHO-IS-SUCCESSOR - info requestor
  READ-FINGERTABLE - info requestor
  SUCCESSOR-UPDATE - new successor
  */
  public Neighbor originator;

  public RoutingInfoRequest(String messageType, Neighbor originator){
    super(messageType);
    this.originator = originator;
  }

  public boolean equals(RoutingInfoRequest message){
    return (this.messageType.equals(message.messageType) && this.originator.equals(message.originator));
  }

  public String serializeMessage(){
    return "(" + this.messageType + ") from originator [" + this.originator.IP.toString() + ", " + this.originator.receivePort + "]";
  }

  public static RoutingInfoRequest deserializeMessage(String message) throws UnknownHostException{
    int start;
    int end;

    start = message.indexOf("(");
    end = message.indexOf(")");
    String type = message.substring(start, end);

    start = message.indexOf("[");
    end = message.indexOf(",");
    String originatorIP = message.substring(start, end);

    start = end;
    end = message.indexOf("]");
    int originatorReceivePort = Integer.parseInt(message.substring(start, end));

    return new RoutingInfoRequest(type, new Neighbor(originatorIP, originatorReceivePort));
  }
}
