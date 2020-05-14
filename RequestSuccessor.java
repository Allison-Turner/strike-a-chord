
public class RequestSuccessor extends Message { 
	int chordID; 
	
   public RequestSuccessor(int chordID, MemberInfo sender, MemberInfo receiver) { 
      super(sender, receiver); 
      this.chordID = chordID; 
   }
}