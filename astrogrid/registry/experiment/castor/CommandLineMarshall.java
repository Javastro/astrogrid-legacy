package astrogrid.registry.experiment.castor;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: The Queen's University of Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */
import java.io.*;

public class CommandLineMarshall {
  public CommandLineMarshall(String cmdline) {
	try {
	 String line;
	 Process p = Runtime.getRuntime().exec(cmdline);
	 BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	 while ((line = input.readLine()) != null) {
	   System.out.println(line);
	   }
	 input.close();
	 } 
	catch (Exception err) {
	 err.printStackTrace();
	 }
   }
   
  public static void main(String[] args) {
	new CommandLineMarshall("/astrogrid/astrogrid/registry/experiment/castor/script/runAllScript.bat");
  }

}