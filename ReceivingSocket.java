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
        Message received = Message.deserializeMessage(neighbor.getMessage());

        //Call a message processing function based on message type, using a generic for now
        processMessage(received);

        //Perform periodic checks defined by Chord's join, leave, and stabilization processes
        if((System.currentTimeMillis() - this.lastStabilize) > 60000){
          lastStabilize = System.currentTimeMillis();
          myself.stabilize();
        }
      }
    }
    catch(Exception e) {
      System.err.println("Receiving socket error");
      e.printStackTrace();
    }
  }

  //Generic placeholder
  private void processMessage(Message message){
    System.out.println("Unimplemented");
  }

  //Process new successor notification

  //Process request for my finger fingerTable

  //Process request for my immediate successor

  //Process file search request

}
