package org.astrogrid.registry.xquery;

import java.io.*;

/**
 * This class is used to open an Xquery file and transfer
 * a fileImputStrean to a String
 *
 */

public class Transform {

  public static String fileToString(String filePath) {

	String xquery = new String();

	try {
	  // create a file instance and a file InputStream
	  File fileXQuery = new File(filePath);
	  InputStream fileIn = new FileInputStream(fileXQuery);

	  // read file leght and change data type from double to byte
	  double fileLeghtDouble = fileXQuery.length();
	  byte fileLeght = (byte) fileLeghtDouble;
	  byte buff[] = new byte[fileLeght];
	  int i = fileIn.read(buff);
	  xquery = new String(buff);
	}
	catch (IOException io) {
	  io.printStackTrace();
	}
	return (xquery);
  }
}