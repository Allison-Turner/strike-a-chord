
public class RequestFileResponse extends Message { 
   String filename;
   
   public RequestFileResponse(String filename, MemberInfo sender, MemberInfo recipient) { 
      super(sender, recipient); 
      this.filename = filename;  
   }
}
