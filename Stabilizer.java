import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Stabilizer implements Runnable{
   private long lastStabilize;
   private Member myself;

   public Stabilizer(Member me){
	lastStabilize = 0;
	myself = me;
   }

   public void run(){
	while(true){
	   //Class exists so we can put this in a separate thread as we don't want it to block any processes
	   if((System.currentTimeMillis() - this.lastStabilize) > 60000){
		lastStabilize = System.currentTimeMillis();
		myself.stabilize();
	   }
	}
   }
}
