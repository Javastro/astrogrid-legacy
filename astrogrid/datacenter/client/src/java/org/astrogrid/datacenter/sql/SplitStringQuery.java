package  org.astrogrid.datacenter.sql;

import java.net.MalformedURLException;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class SplitStringQuery {
  public String[] splitString(String theString, String theDelimiter){
    int delimiterLength;
    int stringLength = theString.length();

    if (theDelimiter == null || (delimiterLength = theDelimiter.length()) == 0) {
      return new String[] {
          theString};
    }

    int count, start, end;
    count = 0;
    start = 0;

    while ( (end = theString.indexOf(theDelimiter, start)) != -1) {
      count++;
      start = end + delimiterLength;
    }
    count++;

    String[] result = new String[count];

    count = 0;
    start = 0;

    while ( (end = theString.indexOf(theDelimiter, start)) != -1) {
      result[count] = (theString.substring(start, end));
      count++;
      start = end + delimiterLength;
    }

    end = stringLength;
    result[count] = theString.substring(start, end);

    return (result);

  }

  public static void main(String[] args) throws MalformedURLException {
    //String query = Transform.LocalFileToString("D://httpd//files//sql");
    //System.out.print(SplitStringQuery.splitString(query));
  }

}
