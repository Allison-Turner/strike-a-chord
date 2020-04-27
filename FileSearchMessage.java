public class FileSearchMessage extends Message{
  public String fileName;
  public String chordID;
  public Neighbor searchOriginator;

  /* Message types
  FILESEARCH
  SEARCHRESPONSE
  */
  public FileSearchMessage(String type, String fileName, Neighbor searchOriginator){
    super(type);
    this.fileName = fileName;
    this.searchOriginator = searchOriginator;
    this.chordID = Member.generateChordID(this.fileName);
  }

  public boolean equals(FileSearchMessage message){
    return true;
  }

  public String serializeMessage(){
    return "[" + this.messageType + "] from originator " + searchOriginator.serializeNeighbor() + " for file [" + this.fileName + "]"
  }

  public static FileSearchMessage deserializeMessage(String message) throws UnknownHostException{
    int start;
    int end;

    start = message.indexOf("[");
    end = message,indexOf("]");
    String mType = message.substring(start, end).trim();

    start = message.indexOf("[", end);
    end = message.indexOf("]", start);
    Neighbor sender = Neighbor.deserializeNeighbor(message.substring(start, end).trim());

    start = message.indexOf("[", end);
    end = message.indexOf("]", start);
    String file = message.substring(start, end).trim();

    return new FileSearchMessage(mType, file, sender);
  }
}
