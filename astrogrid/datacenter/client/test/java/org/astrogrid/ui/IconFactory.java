/*
 * $Id: IconFactory.java,v 1.1 2004/02/17 16:04:06 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.ui;

import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;
import java.util.*;
import java.net.URL;

/**
 * a cached factory of images stored off this package, for things such as
 * button icons etc
 *
 * @author Martin Hill
 *
 */


public class IconFactory
{
   public static final int SMALL = 0;   //for lines, status bars, etc
   public static final int MEDIUM = 1; //size suitable for toolbar buttons
   public static final int LARGE = 2;   //for dialog boxes, etc

   //pixel sizes for above
   private static final int[] PIXELS = new int[] { 16, 32, 64 };

   //cached icons - lookups for above
   private static final Hashtable[] cache = new Hashtable[] { new Hashtable(), new  Hashtable(), new Hashtable() };

   /** Get an icon suitable for message boxes, etc.  Looks first
    * for file, then for icons used by option pane in UI Manager
    */
   public static Icon getIcon(String iconName)
   {
      return getIcon(iconName, LARGE);
   }

   /** Get an icon suitable for display on lines, status bars, etc.
    * Looks first in hashtable, then for image file, then makes one
    * out of larger icon if necessary
    */
   public static Icon getSmallIcon(String iconName)
   {
      return getIcon(iconName, SMALL);
   }

   /** Get an icon with the size given
    * Looks first in hashtable, then for image file, then makes one
    * out of larger icon if necessary
    * @todo - this only deals with ImageIcons but it ought to not fail if there are other types
    */
   public static Icon getIcon(String iconName, int size)
   {
      assert (size >= SMALL) && (size <= LARGE) : "Size must be SMALL, MEDIUM or LARGE";
      
      //if it's in the hashtable, return that.
      ImageIcon icon = (ImageIcon) cache[size].get(iconName);
      if (icon != null)
         return icon;

      //if it's in the cache at this or a larger size, get that
      int trySize = size+1;
      while ((trySize<=LARGE) && (icon == null))
      {
         icon = (ImageIcon) cache[trySize].get(iconName);
         trySize++;
      }

      //if its not there, look for a file
      if (icon == null) {
         //look for file
         icon = loadIcon(iconName.trim());
      }

      //make sure correct size - shrink if not
      if (icon != null) {
         if (icon.getIconWidth() > PIXELS[size]) {
            icon = new ImageIcon(icon.getImage().getScaledInstance(PIXELS[size], PIXELS[size],Image.SCALE_REPLICATE));
         }
         cache[size].put(iconName, icon);
      }

      return icon;
   }

   /**
    * Loads Icon from file in the images subdirectory of this class
    */
   private static ImageIcon loadIcon(String filename)
   {
      //path is subdirectory of this package, called images
      URL url = IconFactory.class.getResource("./images/"+filename+".gif");
      if (url != null)
         return new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

      return null;
   }

   /**
    * Loads Icon from file in the images subdirectory of the given class
    */
   private static ImageIcon loadClassIcon(Class givenClass, String filename)
   {
      //path is subdirectory of this package, called images
      URL url = givenClass.getResource("./images/"+filename+".gif");
      if (url != null)
         return new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

      return null;
   }

   /**
    * Sets an ordinary icon
    *
   public static void setIcon(String iconName, Icon anIcon)
   {
      bigIcons.put(iconName, anIcon);
   }

   /**
    * Sets a small icon
    *
   public static void setSmallIcon(String iconName, Icon anIcon)
   {
      smallIcons.put(iconName, anIcon);
   }
    */

}
/*
$Log: IconFactory.java,v $
Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.3  2004/02/17 03:53:43  mch
Nughtily large number of fixes for demo

Revision 1.2  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */
