import java.io.Serializable; 

public class Message implements Serializable {
   MemberInfo sender; 
   MemberInfo recipient;  
   
   // sender is not used in the actual mechanics of sending
   // usually is the original sender of the message 
   public Message(MemberInfo sender, MemberInfo recipient) { 
      this.sender = sender; 
      this.recipient = recipient; 
   }
}