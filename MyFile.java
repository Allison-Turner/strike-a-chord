import java.io.Serializable;

public class MyFile implements Serializable{
   public int chordID;
   public String fileName;

   public MyFile(String name, int id){
	fileName = name.trim();
	chordID = id;
   }

   public boolean equals(MyFile file){
	return fileName.equals(file.fileName);
   }
}
