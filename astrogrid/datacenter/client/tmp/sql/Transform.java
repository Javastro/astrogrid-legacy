package  org.astrogrid.datacenter.sql;

import java.io.*;
import java.net.*;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Data Centre Group
 *
 */

public  class Transform {
  /**
    *
    * This Class takes a file from any URL and convert that file into a Stream
    * @param fileURL URL address of a file
    * @throws MalformedURLException
    * @return fileContent - variable which stores the URLfile into a String
    *
    */

  public static String URLFileToString(String fileURL) throws MalformedURLException {
    StringBuffer stringBuffer = new StringBuffer();

    try {
      URL fileAddres = new URL(fileURL);
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

  /**
    *
    * This Class takes a local File which is converted into a String
    * @param filePath file local address
    * @throws MalformedURLException
    * @return fileContent - variable which stores the file in a String
    *
    */

  public static String LocalFileToString(String filePath) throws MalformedURLException {
    String fileContent = new String();

    try {

      File dataFile = new File( filePath );
      byte buf[] = new byte[(int)dataFile.length()];
      FileInputStream fin = new FileInputStream( dataFile );
      new DataInputStream( fin ).readFully( buf );
      fileContent = new String( buf, 0 );
    }

    catch (FileNotFoundException e) {}
    catch (IOException e) {}

    return (fileContent);
  }
}
