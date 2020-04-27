/* class containing information that will need to be sent with each message */ 

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress; 
import java.net.URL; 

public class MemberInfo implements Serializable { 
   public InetAddress IP;
	public int receivePort;
	public int sendPort; 

	public String chordID;
	public static final int chordIDLength = 3; // m in chord paper
   
   public MemberInfo(int receivePort) {
      this.receivePort = receivePort; 
      this.sendPort = 4000; 
      getLocalIP();
		this.chordID = generateChordID(this.IP.toString());

   }
   
   //Publicly available function to generate Chord ID for any value
	public static String generateChordID(String value){
		String id = org.apache.commons.codec.digest.DigestUtils.sha1Hex(value); // for some reason can't find this
		return id.substring(0, chordIDLength);
	}

   
    /* Processes to define properties */
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