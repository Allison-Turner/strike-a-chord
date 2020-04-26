import java.util.*;
import java.io.*;
import java.net.*;

public class Neighbor{
  public InetAddress IP;

  public int receivePort;
  public Socket receiveSocket;
  public PrintWriter to;

  public int sendPort;
  public Socket sendSocket;
  public BufferedReader from;

	private String chordID;

  //Initialize Neighbor with connection accepted by Member's receiving socket
  public Neighbor(Socket socket) throws IOException{
    this.sendSocket = socket;
    this.IP = sendSocket.getInetAddress();
    this.sendPort = sendSocket.getPort();
    this.from = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
    this.chordID = Member.generateChordID(this.IP);
  }

  //Inititalize Neighbor with information to initiate a connection from the Member
  public Neighbor(String IP, int receivePort) throws UnknownHostException{
    this.IP = InetAddress.getByName(IP);
    this.receivePort = receivePort;
    this.chordID = Member.generateChordID(this.IP);
  }

  //Define Neighbor equality as having the same IP address
  public boolean equals(Neighbor neighbor){
    return IP.getHostAddress().equals(neighbor.IP.getHostAddress());
  }

  public String serializeNeighbor(){
    return "["+ +"]";
  }


  /*Change socket connection state*/

  //Store a new connection initiated by the Neighbor
  public void receive(Socket socket) throws IOException{
    sendSocket = socket;
    IP =sendSocket.getInetAddress();
    sendPort = sendSocket.getPort();
    from = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
  }

  //Memberer inititate connection with Neighbor's server socket
  public void connect() throws IOException{
    receiveSocket = new Socket(IP, receivePort);
    to = new PrintWriter(receiveSocket.getOutputStream(),true);
  }

  //Terminate any connections or message streams between Member and Neighbor
  public void disconnect() throws IOException{
    if(to != null){
      to.close();
    }
    if(listening()){
      receiveSocket.close();
    }
    if(from != null){
      from.close();
    }
    if(sending()){
      sendSocket.close();
    }
  }


  /*Message Passing*/

  //Member send a string to Neighbor's receiving socket
  public void pushMessage(String message) throws IOException{
    if(!listening()){
      connect();
    }
    to.println(message);
  }

  //Pull new message from Neighbor's connection to Peer's receive socket
  public String getMessage() throws IOException{
    if(sending()){
      return from.readLine();
    }
    else{
      return null;
    }
  }


  /*Socket Status Checks*/

  //Check if Member's connection to Neighbor's receive socket is live
  public boolean listening(){
    return (receiveSocket != null && receiveSocket.isConnected());
  }

  //Check if Neighbor's connection to Member's receive socket is live
  public boolean sending(){
    return (sendSocket != null && sendSocket.isConnected());
  }

}
