/*
 Options.java

 Date         Author      Changes
 8 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.tools.util;

import java.util.Properties;
import java.io.*;
import java.awt.Point;
import java.awt.Dimension;

/**
 * Persistent set of options.  Currently implemented as a wrapper around a
 * Properties class, but should probably be XML format at some point.
 *
 * @author M Hill
 */

public class Options
{
   protected Properties properties = null;
   protected String filename = null;
   protected String header = null;

   /**
    * Default - uses file "options.ini"
    */
   public Options() throws IOException
   {
      this("options.ini","Application Options");
   }

   /**
    * Constructor using given file as store
    */
   public Options(String givenFilename, String givenHeader) throws IOException
   {
      super();

      this.filename = givenFilename;
      this.header = givenHeader;
      properties = new Properties();
      load();
   }

   public void load() throws IOException
   {
      try
      {
         properties.clear();
         InputStream in = new FileInputStream(filename);
         properties.load(in);
         in.close();
      }
      catch (FileNotFoundException fnfe) {} // ignore
   }

   public void store()
   {
      try
      {
         OutputStream out = new FileOutputStream(filename);
         properties.store(out, header);
         out.close();
      }
      catch (IOException ioe)
      {
         org.astrogrid.tools.log.Log.logError("Could not store configuration",ioe);
      }
   }

   public void setProperty(String key, String value)
   {
      properties.setProperty(key, value);
   }

   public String getProperty(String key)
   {
      return properties.getProperty(key);
   }

   public Point getPoint(String key)
   {
      try
      {
         int x = Integer.parseInt(getProperty(key+"X"));
         int y = Integer.parseInt(getProperty(key+"Y"));
         return new Point(x,y);
      }
      catch (NumberFormatException nfe)
      {
         return null;
      }
   }

   public void setPoint(String key, Point value)
   {
      setProperty(key+"X", ""+value.x);
      setProperty(key+"Y", ""+value.y);
   }

   public Dimension getDimension(String key)
   {
      try
      {
         int w = Integer.parseInt(getProperty(key+"Width"));
         int h = Integer.parseInt(getProperty(key+"Height"));
         return new Dimension(w,h);
      }
      catch (NumberFormatException nfe)
      {
         return null;
      }
   }

   public void setDimension(String key, Dimension value)
   {
      setProperty(key+"Width", ""+value.width);
      setProperty(key+"Height", ""+value.height);
   }
}


