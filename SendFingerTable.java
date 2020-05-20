public class SendFingerTable extends Message { 
   public MemberInfo[] fingertable;
   
   public SendFingerTable(MemberInfo sender, MemberInfo recipient, MemberInfo[] ft) { 
      super(sender, recipient); 
      this.fingertable = ft; 
   }
}