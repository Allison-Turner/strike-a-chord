
public class RequestSuccessor extends Message { 
	int chordID; 
	
	// chordID: id whose successor we're looking for
	// sender: who sent the message (initially -- doesn't change during binary search)
	// receiver: who we're sending the message to 
   public RequestSuccessor(int chordID, MemberInfo sender, MemberInfo receiver) { 
      super(sender, receiver); 
      this.chordID = chordID; 
   }
}