import java.io.Serializable; 

public class Message implements Serializable {
   MemberInfo sender; 
   MemberInfo recipient;  
   
   public Message(MemberInfo sender, MemberInfo recipient) { 
      this.sender = sender; 
      this.recipient = recipient; 
   }
}