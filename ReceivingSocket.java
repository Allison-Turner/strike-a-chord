/* Receive all messages, process them, and handle lookup (file search or member search) logic. */
public static class ReceivingSocket implements Runnable{
  private Member myself;
  private long lastStabilize;

  public ReceivingSocket(Member me){
    this.myself = me;
    this.lastStabilize = 0;
  }

  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(myself.receivePort);

      while(true) {
        Neighbor sender = new Neighbor(serverSocket.accept());
        Message received = Message.deserialize(neighbor.getMessage());

        //Call a message processing function
        if(received.messageType.equals("FILESEARCH")){
          processFileSearch(received);
        }
        else if(received.messageType.equals("WHO-IS-SUCCESSOR")){
          processSuccessorRequest(received);
        }
        else if(received.messageType.equals("SUCCESSOR-UPDATE")){
          processSuccessorUpdate(received);
        }
        else if(received.messageType.equals("READ-FINGERTABLE")){
          processFingerTableReadRequest(received);
        }

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

  //Process new successor notification (includes moving files to new successor)
  private void processSuccessorUpdate(RoutingInfoRequest message){

  }

  //Process request for my finger fingerTable
  private void processFingerTableReadRequest(RoutingInfoRequest message){

  }

  //Process request for my immediate successor
  private void processSuccessorRequest(RoutingInfoRequest message){

  }

  //Process file search request
  private void processFileSearch(FileSearchMessage message){

  }
}
