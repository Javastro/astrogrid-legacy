package net.mchill.log.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;
import java.util.*;
import java.net.URL;
import net.mchill.log.*;

/**
 * SeverityMedia stores various media properties to do with the Severity
 * class, such as associated sounds, colours, icons, etc
 *
 * @author Martin Hill
 *
 */

public class SeverityMedia
{
   private static final Hashtable smallIcons = new Hashtable();   //collection of icons
   private static final Hashtable bigIcons = new Hashtable();   //collection of icons

   public static final int BIG = 1;
   public static final int SMALL = 2;

   private static final int SMALL_SIZE = 16; // small icons are 16x16

   /** Get an icon suitable for message boxes, etc.  Looks first
    * for file, then for icons used by option pane in UI Manager
    */
   public static Icon getIcon(Severity aSeverity)
   {
      //if it's in the hashtable, return that.
      Icon icon = (Icon) bigIcons.get(aSeverity);
      if (icon != null)
         return icon;

      //look for file
      icon = loadIcon(aSeverity.toString().trim());

      //still nothing, use the option pane's...
      if (icon == null)
      {
         if ((aSeverity == Severity.ALARM) || (aSeverity == Severity.ERROR))
         {
            icon = UIManager.getIcon("OptionPane.errorIcon");
         }
         else if (aSeverity == Severity.WARNING)
         {
            icon = UIManager.getIcon("OptionPane.warningIcon");
         }
         else if (aSeverity == Severity.INFO)
         {
            icon = UIManager.getIcon("OptionPane.informationIcon");
         }
         else
         {
            //default?
            icon = UIManager.getIcon("OptionPane.questionIcon");
         }
      }

      if (icon != null)
         setIcon(aSeverity, icon);

      return icon;

   }

   /** Get an icon suitable for display on lines, status bars, etc.
    * Looks first in hashtable, then for image file, then makes one
    * out of larger icon if necessary
    */
   public static Icon getSmallIcon(Severity aSeverity)
   {
      //if it's in the hashtable, return that.
      Icon icon = (Icon) smallIcons.get(aSeverity);
      if (icon != null)
         return icon;

      //look for file
      ImageIcon imageIcon = loadIcon(aSeverity.toString().trim()+SMALL_SIZE);
      //make sure correct size
      if (imageIcon != null)
      {
         if (imageIcon.getIconWidth() > SMALL_SIZE)
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(16,-1,Image.SCALE_REPLICATE));
      }
      icon = imageIcon;

      //if not found, create from existing big one
      if (icon == null)
      {
         Icon bigIcon = getIcon(aSeverity);
         if ((bigIcon != null) && (bigIcon instanceof ImageIcon))
         {
            Image bigImage = ((ImageIcon) bigIcon).getImage();
            icon = new ImageIcon(bigImage.getScaledInstance(16,-1,Image.SCALE_REPLICATE));
         }
      }

      if (icon != null)
         setSmallIcon(aSeverity, icon);

      return icon;
   }

   /**
    * Loads Icon from file
    */
   private static ImageIcon loadIcon(String filename)
   {
      //path is subdirectory of this package, called images
      URL url = SeverityMedia.class.getResource("./images/"+filename+".gif");
      if (url != null)
         return new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

      return null;
   }

   public static void setIcon(Severity aSeverity, Icon anIcon)
   {
      bigIcons.put(aSeverity, anIcon);
   }

   public static void setSmallIcon(Severity aSeverity, Icon anIcon)
   {
      smallIcons.put(aSeverity, anIcon);
   }

}
