/* $Id: FileResourceLoader.java,v 1.2 2003/11/10 18:45:52 jdt Exp $*
 * Created on 08-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.jes.testutils.io;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * loads a resource file into a string - pinched from datacenter test code
 * @author jdt@roe.ac.uk
 *
 */
public final class FileResourceLoader {
  /**
   * Construct new FileResourceLoader
   * @param callee class sharing the same package as the resource you want to load
   */
  public FileResourceLoader(final Class callee) {
    this.callee = callee;
  }
  /**
   * Construct new FileResourceLoader
     * @param object Object whose class shares the same package as the resource you want to load
     */
  public FileResourceLoader(final Object object) {
    this.callee = object.getClass();
  }
  /**
   * Need the class to know where to look for the resource
   */
  private Class callee;
  /**
  * load a resource file into a string - pinched from datacenter test code
  * @param resource file to be found
  * @return contents of that file as a string
  * @throws IOException if something goes pear shaped trying to find the file
  */
  public final String getResourceAsString(final String resource) throws IOException {
    InputStream is = callee.getResourceAsStream(resource);
    if (is == null) {
      throw new IOException("Resource not found");
    }
    String script = streamToString(is);
    assert(script != null);
    return script;
  }
  /**
   * Takes the contents of an InputStream and turns it into a string
   * @param is said InputStream
   * @return said String
   * @throws IOException if there's a problem with the InputStream
   */
  private String streamToString(final InputStream is) throws IOException {
    assert(is != null);
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
    return str;
  }
}
/*
*$Log: FileResourceLoader.java,v $
*Revision 1.2  2003/11/10 18:45:52  jdt
*Minor bits and pieces to satisfy the coding standards
*
*Revision 1.1  2003/11/10 10:48:26  anoncvs
*Refactored out some of the nonsense into a separate class.
*
*/
