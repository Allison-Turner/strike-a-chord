public class SendFile extends Message { 
   public String filename; 
   public String file; 
   
   public SendFile(MemberInfo sender, MemberInfo recipient, String filename, String file) { 
      super(sender, recipient); 
      this.filename = filename; 
      this.file = file;
   }
}