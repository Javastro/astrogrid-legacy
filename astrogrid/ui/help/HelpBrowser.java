package org.astrogrid.ui.help;

import java.net.URL;
import java.io.File;

/** HelpBrowser.java
 *
 * Defines the interface that all help browsers must define
 *
 * @Created          : Feb 2001
 * @Last Update      :
 *
 * @author           : M Hill
 *
 *
 */

public interface HelpBrowser extends HelpKeys
{
  
   /**
    * Go to particular url
    */
   public void showHelp(URL url) throws HelpNotFoundException;

   /**
    * Factory method for creating more instances
    */
   public HelpBrowser createNewBrowser();
   
   /**
    * Set whether help is editable
    */
   public void setEditable(boolean b);
}
