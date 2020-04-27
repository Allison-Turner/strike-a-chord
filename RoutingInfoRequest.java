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
    return "[" + this.messageType + "] from originator " + this.originator.serializeNeighbor();
  }

  public static RoutingInfoRequest deserializeMessage(String message) throws UnknownHostException{
    int start;
    int end;

    start = message.indexOf("[");
    end = message.indexOf("]");
    String type = message.substring(start, end);

    start = message.indexOf("[", end);
    end = message.indexOf("]", start);
    Neighbor sender = Neighbor.deserializeNeighbor(message.substring(start, end).trim());

    return new RoutingInfoRequest(type, sender);
  }
}
