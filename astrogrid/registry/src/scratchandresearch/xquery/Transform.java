package org.astrogrid.registry.xquery

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;


/**
 * This class is used to open an Xquery file and transfer
 * a fileImputStrean to a String
 *
 */

public  class Transform {

  public static String URLFileToString(String filePath) throws MalformedURLException {
	StringBuffer stringBuffer = new StringBuffer();

	try {
	  URL fileAddres = new URL(filePath);
	  InputStream is = fileAddres.openStream();
	  BufferedReader reader = new BufferedReader(new InputStreamReader(is));

	  while (true)
	   {
		 String string = reader.readLine();
		 if (string != null)
			stringBuffer.append(string).append("\n");
		 else
		 break;
	   }
	}
	catch (IOException e) {}

	String fileContent = stringBuffer.toString();

	return(fileContent);
  }

}