package org.astrogrid.ui.help;

import java.util.*;
import java.awt.Color;
import java.net.*;

import org.astrogrid.log.Log;
/**
 * A singleton class providing factory methods for help adaptors to use when
 * creating browsers, etc. There is an option for whether only one browser
 * is allowed at any one time.
 *
 * @author M Hill
 */

public class Help implements HelpKeys
{
   //holds main help browser.
   private static HelpBrowser mainBrowser = null;
   
   //holds list of other browsers
   //private static Vector browsers = new Vector();
   
   //allow only one browser - page changes when new help requested
   private static boolean onlyOneBrowser = true;
   
   //the path to the root html help page
   private static String helpRoot = ".//help//";
   
   private static String styleSheet = null;
   
   private static Color background = Color.blue;
   private static Color foreground = Color.yellow;
   private static String browserTitle = "Help";
   
   private static boolean editable = false;
   
   //some pages may refer to 'real world' urls
   private static Hashtable dictionary = new Hashtable();
   
   /** Create/reuse help browser & display it, with given help page text */
   public static HelpBrowser showHelp(String helpPage, String ref, String fallbackPage)
   {
      if (mainBrowser == null)
      {
         throw new RuntimeException("Main browser not yet set in Help - cannot create instance");
      }
      
      HelpBrowser browser = null;
      if (onlyOneBrowser)
         browser = mainBrowser;
      else
      {
         browser = mainBrowser.createNewBrowser();
         //browsers.add(browser);   //but how to remove?
      }
      
      try
      {
         browser.showHelp(makePageUrl(helpPage, ref));
         return browser;
      }
      catch (MalformedURLException mue)
      {
         Log.logError("Help ("+helpPage+") gives illegal URL ("+dictionary.get(helpPage)+") ",mue);
      }
      catch (HelpNotFoundException hnfe)
      {
         if (fallbackPage != null)
         {
            try
            {
               browser.showHelp(makePageUrl(fallbackPage, ""));
               return browser;
            }
            catch (MalformedURLException mue)
            {
               Log.logError("Help ("+helpPage+") gives illegal URL ("+dictionary.get(helpPage)+") ",mue);
            }
            catch (HelpNotFoundException hnfeAgain)
            {
               Log.logError("Help ("+helpPage+") & Help Fallback page ("+fallbackPage+") Not Found: "+hnfe,hnfe.getCause());
            }
         }
         else
         {
            Log.logError("Help Not Found: "+hnfe,hnfe.getCause());
         }
      }
      
      return null;
   }
   
   /**
    * returns full URL for given page & ref
    */
   protected static URL makePageUrl(String helpPage, String ref) throws MalformedURLException
   {
      String path = null;
      
      if (dictionary.get(helpPage) != null)
      {
         path = (String) dictionary.get(helpPage);
      }
      else
      {
         path = getRoot()+helpPage+".html";
      }
      
      if ((ref != null) && (ref.length() > 0))  path = path+"#"+ref;
         
      URL url = new URL(path);

      return url;
   }

   
   
   /**
    * Start at contents
    */
   public static HelpBrowser showHelpContents()
   {
      return showHelp(CONTENTS,"","");
   }

   /**
    * Start at given key
    */
   public static HelpBrowser showHelp(String helpPage)
   {
      return showHelp(helpPage,"","");
   }
   
   /** Help may be provided by only one browser frame, where if help on a
    * new page/component is needed, the page displayed by the browser changes
    * to the new one.  Alternatively it may be that you want to have a new
    * browser appear for each help page */
   public static void setOnlyOneBrowser(boolean b)
   {
      onlyOneBrowser = b;
   }
   
   /** Set the main browser.  This defines the type of browser used by the
    * help system, as HelpBrowsers have a factory method for creating new
    * instances.  This means we can 'plug in' swing browsers, awt browsers,
    * or wierd little browsers for pdas, etc
    */
   public static void setMainBrowser(HelpBrowser aBrowser)
   {
      mainBrowser = aBrowser;
   }

   /** Returns true if the main browser has been sote
    */
   public static boolean mainBrowserSet()
   {
      return (mainBrowser != null);
   }
   
   /**
    * Some 'pages' can refer to real world urls; rather than having to bury
    * these in the code, they can be added here for translation
    */
   public static void setRedirect(String page, String redirectUrl)
   {
      dictionary.put(page, redirectUrl);
   }
   
   /**
    * Set root path to help pages. Usually location of "index.html" file
    */
   public static void setRoot(String newRootPath)     {  helpRoot = newRootPath; }
   public static String getRoot()                     {  return helpRoot; }
   
   /** Set style sheet t obe used with the help html. */
   public static void   setCSS(String newCssFilename) {  styleSheet = newCssFilename;}
   public static String getCSS()                      {  return styleSheet; }
   
   public static Color getBackground()          {  return background;   }
   public static void  setBackground(Color bg)  {  background = bg;   }
   
   public static Color getForeground()          {  return foreground;   }
   public static void  setForeground(Color fg)  {  foreground = fg;   }
   
   public static String getBrowserTitle()          { return browserTitle; }
   public static void   setBrowserTitle(String s)  {  browserTitle = s;   }
   
   public static boolean getEditable()          { return editable; }
   public static void    setEditable(boolean b)
   {
      mainBrowser.setEditable(b);
      editable = b;
   }
}

