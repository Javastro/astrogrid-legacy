/*
 * $Id: IconFactory.java,v 1.2 2004/02/15 23:25:30 mch Exp $
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
   private static final Hashtable smallIcons = new Hashtable();   //collection of icons
   private static final Hashtable bigIcons = new Hashtable();   //collection of icons

   public static final int BIG = 1;
   public static final int SMALL = 2;

   private static final int SMALL_SIZE = 16; // small icons are 16x16

   /** Get an icon suitable for message boxes, etc.  Looks first
    * for file, then for icons used by option pane in UI Manager
    */
   public static Icon getIcon(String iconName)
   {
      //if it's in the hashtable, return that.
      Icon icon = (Icon) bigIcons.get(iconName);
      if (icon != null)
         return icon;

      //look for file
      icon = loadIcon(iconName);

      if (icon != null)
         setIcon(iconName, icon);

      return icon;

   }

   /** Get an icon suitable for display on lines, status bars, etc.
    * Looks first in hashtable, then for image file, then makes one
    * out of larger icon if necessary
    */
   public static Icon getSmallIcon(String iconName)
   {
      //if it's in the hashtable, return that.
      Icon icon = (Icon) smallIcons.get(iconName);
      if (icon != null)
         return icon;

      //look for file
      ImageIcon imageIcon = loadIcon(iconName.trim()+SMALL_SIZE);

      //make sure correct size - shrink if not
      if (imageIcon != null)
      {
         if (imageIcon.getIconWidth() > SMALL_SIZE)
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(16,-1,Image.SCALE_REPLICATE));
      }
      icon = imageIcon;

      //if not found, create from existing big one
      if (icon == null)
      {
         Icon bigIcon = getIcon(iconName);
         if ((bigIcon != null) && (bigIcon instanceof ImageIcon))
         {
            Image bigImage = ((ImageIcon) bigIcon).getImage();
            icon = new ImageIcon(bigImage.getScaledInstance(16,-1,Image.SCALE_REPLICATE));
         }
      }

      if (icon != null)
         setSmallIcon(iconName, icon);

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
    */
   public static void setIcon(String iconName, Icon anIcon)
   {
      bigIcons.put(iconName, anIcon);
   }

   /**
    * Sets a small icon
    */
   public static void setSmallIcon(String iconName, Icon anIcon)
   {
      smallIcons.put(iconName, anIcon);
   }

}
/*
$Log: IconFactory.java,v $
Revision 1.2  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */
