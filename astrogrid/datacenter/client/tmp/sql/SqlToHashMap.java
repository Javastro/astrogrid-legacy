package  org.astrogrid.datacenter.sql;

import java.io.*;
import java.util.*;

/**
 * <p>Title: Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class SqlToHashMap {

  public static List parse(InputStream src) throws IOException {

    // The document as published by the OMB is encoded in Latin-1
    InputStreamReader isr = new InputStreamReader(src, "8859_1");
    BufferedReader in = new BufferedReader(isr);
    List records = new ArrayList();
    String lineItem;
    while ((lineItem = in.readLine()) != null) {
      records.add(splitLine(lineItem));
    }
    return records;

  }

  // the field names in order
  public final static String[] keys = {
    "select",
    "from",
    "where",
   };

  private static Map splitLine(String record) {

    record = record.trim();

    int index = 0;
    Map result = new HashMap();
    for (int i = 0; i < keys.length; i++) {
      //find the next space
      StringBuffer sb = new StringBuffer();
      char c;
      boolean inString = false;
      while (true) {
        c = record.charAt(index);
        if (!inString && c == '"') inString = true;
        else if (inString && c == '"') inString = false;
        else if (!inString && c == ' ') break;
        else sb.append(c);
        index++;
        if (index == record.length()) break;
      }
      String s = sb.toString().trim();
      result.put(keys[i], s);
      index++;
    }

    return result;

  }

}
