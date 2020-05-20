public class AddFileMessage extends Message{
   public MyFile file;

   public AddFileMessage(MyFile file, MemberInfo sender, MemberInfo possibleHost){
	super(sender, possibleHost);
	this.file = file;
   }
}
