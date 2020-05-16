

public class RequestFile extends Message { 
   int key;
   
   public RequestFile(int key, MemberInfo sender, MemberInfo recipient) { 
      super(sender, recipient); 
      this.key = key;  
   }
}
