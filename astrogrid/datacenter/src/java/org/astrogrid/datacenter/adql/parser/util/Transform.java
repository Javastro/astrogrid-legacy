package  org.astrogrid.datacenter.parser.util;

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

public class Transform {

  /**
   * This Class takes a file in any URL and convert that file into a Stream
   * @param fileURL String
   * @throws MalformedURLException
   * @throws FileNotFoundException
   * @return fileContent - variable which stores the URLfile into a String
   */

  public static String URLFileToString(String fileURL) throws
      MalformedURLException, FileNotFoundException {
    StringBuffer stringBuffer = new StringBuffer();

    try {
      URL fileAddres = new URL(fileURL);
      InputStream is = fileAddres.openStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      while (true) {
        String string = reader.readLine();
        if (string != null) {
          stringBuffer.append(string).append("\n");
        }
        else {
          break;
        }
      }
    }
    catch (IOException e) {}

    String fileContent = stringBuffer.toString();
    return (fileContent);
  }

  /**
   *
   * This Class takes a local File which is converted into a String
   * @param filePath String
   * @throws IOException
   * @throws FileNotFoundException
   * @return String
   */

  public static String LocalFileToString(String filePath) throws
      IOException, FileNotFoundException {

    final int BUFLEN = 1024;
    char buf[] = new char[BUFLEN];

    FileReader in = new FileReader(filePath);
    StringWriter out = new StringWriter();

    try {
      while (true) {
        int len = in.read(buf, 0, BUFLEN);
        if (len == -1) {
          break;
        }
        out.write(buf, 0, len);
      }
    }
    finally {
      out.close();
      in.close();
    }
    return out.toString();
  }
}
