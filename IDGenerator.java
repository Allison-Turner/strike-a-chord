import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;

public class IDGenerator{

   public IDGenerator(){}

   public static int generateChordID(String value, int chordIDLength){
	MessageDigest md;
	int chordID = -1;
	try { 
    	   md = MessageDigest.getInstance("SHA-256");
	   md.update(value.getBytes());
	   byte[] valueDigest = md.digest();
	   md.reset(); 
	   ByteBuffer wrapped = ByteBuffer.wrap(valueDigest); 
	   chordID = wrapped.getInt(); 
		
	   // take modulo 2^chordIDLength
	   chordID = chordID % (int) Math.pow(2,  chordIDLength); 
	   if (chordID < 0){
		chordID += (int) Math.pow(2, chordIDLength);
	   }
	} 
	catch (NoSuchAlgorithmException e) {
	   System.out.println("Couldn't find the algorithm \"SHA-256\""); 
	}
	return chordID; 
   }

}
