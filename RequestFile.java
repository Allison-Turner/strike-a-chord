

public class RequestFile extends Message { 
   String filename; 
   
   public RequestFile(String filename, MemberInfo sender, MemberInfo recipient) { 
      super(sender, recipient); 
      this.filename = filename; 
   }
}
