package  org.astrogrid.datacenter.parser.test;

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

public class FlatADQL {

  public static void convert(List data, OutputStream out) throws IOException {

    Writer wout = new OutputStreamWriter(out, "utf-16");
    wout.write("<?xml version=\"1.0\" encoding=\"utf-16\"?>\r\n");
    wout.write("<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v0.7.3\">\r\n");

    Iterator records = data.iterator();
    while (records.hasNext()) {
      wout.write("  <LineItem>\r\n");
      Map record = (Map) records.next();
      Set fields = record.entrySet();
      Iterator entries = fields.iterator();
      while (entries.hasNext()) {
        Map.Entry entry = (Map.Entry) entries.next();
        String name = (String) entry.getKey();
        String value = (String) entry.getValue();
        // some of the values contain ampersands and less than
        // signs that must be escaped
        value = escapeText(value);

        wout.write("    <" + name + ">");
        wout.write(value);
        wout.write("</" + name + ">\r\n");
      }
      wout.write("  </LineItem>\r\n");
    }
    wout.write("</Budget>\r\n");
    wout.flush();

  }

  public static String escapeText(String s) {

    if (s.indexOf('&') != -1 || s.indexOf('<') != -1
        || s.indexOf('>') != -1) {
      StringBuffer result = new StringBuffer(s.length() + 4);
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c == '&') {
          result.append("&amp;");
        }
        else if (c == '<') {
          result.append("&lt;");
        }
        else if (c == '>') {
          result.append("&gt;");
        }
        else {
          result.append(c);
        }
      }
      return result.toString();
    }
    else {
      return s;
    }

  }

  public static void main(String[] args) {

    try {
      //File fi = new File("D://httpd//files//sql");
      InputStream in = new FileInputStream(
          "//home//pedro//public_html//code//sql//select.sql");
      OutputStream out;
      out = System.out;

      List results = SqlToHashMap.parse(in);
      convert(results, out);
    }
    catch (IOException e) {
      System.err.println(e);
    }

  }

}
