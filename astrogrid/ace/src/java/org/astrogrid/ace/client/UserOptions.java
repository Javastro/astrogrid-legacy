/*
 $Id: UserOptions.java,v 1.1 2003/08/25 18:36:13 mch Exp $

 Date         Author      Changes
 8 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.ace.client;

import org.astrogrid.tools.util.Options;
import java.awt.Point;
import java.awt.Dimension;
import java.io.IOException;

/**
 * Persistent set of options, using the util.Options class for persistence
 *
 * @author M Hill
 */

public class UserOptions
{
   static Options instance = newOptions();

   private static Options newOptions()
   {
      try
      {
         return new Options("options.cfg", "ACE client user options");
      }
      catch (IOException ioe)
      {
         //it'll crash now when running, so need to make this fatal
         throw new RuntimeException(""+ioe);
      }
   }

   public static String getLastImageFilename()
   {
      return instance.getProperty("Image");
   }

   public static void setLastImageFilename(String filename)
   {
      instance.setProperty("Image", filename);
      instance.store();
   }

   public static String getLastTemplateFilename()
   {
      return instance.getProperty("Template");
   }

   public static void setLastTemplateFilename(String filename)
   {
      instance.setProperty("Template", filename);
      instance.store();
   }

   public static String getLastAceServer()
   {
      return instance.getProperty("Ace");
   }

   public static void setLastAceServer(String aceServer)
   {
      instance.setProperty("Ace", aceServer);
      instance.store();
   }

   public static Dimension getDialogSize()
   {
      try
      {
         int w = Integer.parseInt(instance.getProperty("DialogWidth"));
         int h = Integer.parseInt(instance.getProperty("DialogHeight"));
         return new Dimension(w, h);
      }
      catch (NumberFormatException nfe)
      {
         return null;
      }
   }

   public static Point getDialogLocation()
   {
      Point p = instance.getPoint("Dialog");
      //botch fix because getLocation/setLocation seems to miss out the title bar,
      //so boxes gradually drift north
      if (p != null) p.y = p.y + 20;
      return p;
   }

   public static void setDialog(Dimension size, Point location)
   {
      instance.setProperty("DialogWidth", ""+size.width);
      instance.setProperty("DialogHeight", ""+size.height);
      instance.setPoint("Dialog", location);
      instance.store();
   }
}

