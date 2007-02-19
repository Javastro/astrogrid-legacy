/*
 * $Id: TestHelper.java,v 1.2 2007/02/19 16:20:23 gtr Exp $
 * 
 * Created on 17-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Assert;

/**
 * Static class of helper methods for testing.
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public final class TestHelper {
  
  private TestHelper() {
  }


/**
    * load a resource file into a string.
    * @param resource
    * @return
    * @throws IOException
    */
public static String getResourceAsString(String resource) throws IOException {
      InputStream is =TestHelper.class.getResourceAsStream(resource);
      Assert.assertNotNull("resource error for " + resource, is);
      String script = streamToString(is);
      return script;
   }

/**
    * Reads an input stream into a string.
    * @param is
    * @return the concatenated string that results from reading the input stream
    * @throws IOException
    */
public static String streamToString(InputStream is) throws IOException {
      BufferedReader r = new BufferedReader(new InputStreamReader(is));
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      String line = null;
      while ((line = r.readLine()) != null) {
         pw.println(line);
      }
      pw.close();
      r.close();
      String str = sw.toString();
      Assert.assertNotNull("stream empty", str);
      return str;
   }

}
