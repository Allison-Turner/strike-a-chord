

public class RequestFile extends Message { 
   String filename; 
   
   public RequestFile(MemberInfo sender, MemberInfo recipient, String filename) { 
      super(sender, recipient); 
      this.filename = filename; 
   }
}
