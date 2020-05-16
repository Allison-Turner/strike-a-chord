/* class containing information that will need to be sent with each message */ 

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress; 
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 
import java.nio.ByteBuffer;

public class MemberInfo implements Serializable { 
   public InetAddress IP;
   public int receivePort;
   public int sendPort; 

   public int chordID;
   public static final int chordIDLength = 3; // m in chord paper
	
   private MessageDigest md;

   //Info about another machine
   public MemberInfo(InetAddress IP, int receivePort){
	this.receivePort = receivePort; 
	this.sendPort = 4000; 
	this.IP = IP;
      
	try { 
       md = MessageDigest.getInstance("SHA-256");
	} 
	catch (NoSuchAlgorithmException e) {
	   System.out.println("Couldn't find the algorithm \"SHA-256\""); 
	}
   	this.chordID = generateChordID(this.IP.toString());
   }

   //Info about myself
   public MemberInfo(int receivePort){
	this.receivePort = receivePort; 
	this.sendPort = 4000; 
	getLocalIP();
      
	try { 
    	  md = MessageDigest.getInstance("SHA-256");
	} 
	catch (NoSuchAlgorithmException e) {
	   System.out.println("Couldn't find the algorithm \"SHA-256\""); 
	}
   	this.chordID = generateChordID(this.IP.toString());
   }

   public boolean equals(MemberInfo other){
	return this.IP.toString().equals(other.IP.toString());
   }
   
   //Publicly available function to generate Chord ID for any value
   public int generateChordID(String value){
	this.md.update(value.getBytes());
	byte[] valueDigest = md.digest();
	md.reset(); 
	ByteBuffer wrapped = ByteBuffer.wrap(valueDigest); 
	int chordID = wrapped.getInt(); 
		
	// take modulo 2^chordIDLength
	chordID = chordID % (int) Math.pow(2,  this.chordIDLength); 
	if (chordID < 0){
	    chordID += (int) Math.pow(2, this.chordIDLength);
	}
	return chordID; 
   }

   public static InetAddress parseIP(String address){
	try{
	   return InetAddress.getByName(address);
	}
	catch(Exception e){
	   System.out.println("Error parsing IP from string: " + address);
	   return null;
	}
   }

   //Connect to a public IP lookup service and read what my public IP is
   private void getLocalIP(){
	try{
	   URL whatismyip = new URL("http://checkip.amazonaws.com");
	   BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

	   this.IP = InetAddress.getByName(in.readLine());
	}
	catch(Exception e){
	   System.out.println("Error retrieving local IP");
	   e.printStackTrace();
	}
   }

}
