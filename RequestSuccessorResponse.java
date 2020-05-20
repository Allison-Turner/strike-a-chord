
public class RequestSuccessorResponse extends Message { 
	
   MemberInfo successor; 
   int chordID;
   // chordID: id of node whose successor we're looking for
   // successor: successor of chordID
   // sender: node that sent this
   // reciever: recipient
   public RequestSuccessorResponse(int chordID, MemberInfo successor, MemberInfo sender, MemberInfo requester) { 
      super(sender, requester); 
      this.chordID = chordID; 
      this.successor = successor;
   }
}