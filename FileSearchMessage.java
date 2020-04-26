public class FileSearchMessage extends Message{
  public String fileName;
  public String chordID;
  public Neighbor searchOriginator;

  public FileSearchMessage(String fileName, Neighbor searchOriginator){
    super("FILESEARCH");
    this.fileName = fileName;
    this.searchOriginator = searchOriginator;
    this.chordID = Member.generateChordID(this.fileName);
  }

  public boolean equals(FileSearchMessage message){
    return true;
  }

  public String serializeMessage(){
    return "(" + this.messageType + ") from originator [" + searchOriginator.IP.toString() + ", " + searchOriginator.receivePort + "] for file {" + this.fileName + "}"
  }

  public static FileSearchMessage deserializeMessage(String message) throws UnknownHostException{
    int start;
    int end;

    start = message.indexOf("[");
    end = message.indexOf(",");
    String originatorIP = message.substring(start, end);

    start = end;
    end = message.indexOf("]");
    int originatorReceivePort = Integer.parseInt(message.substring(start, end));

    start = message.indexOf("{");
    end = message.indexOf("}");
    String file = message.substring(start, end);

    return new FileSearchMessage(file, new Neighbor(originatorIP, originatorReceivePort));
  }
}
