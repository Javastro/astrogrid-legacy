package org.astrogrid.ui.help;

import javax.swing.JFrame;
import java.net.URL;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

/**
 * Help Adaptors are attach to any component that you want to provide help for.
 * To do this, you can add the following lines of code to the components constructor:
 * <pre>
   new HelpAdaptor(this, <page help);
   </pre>
 * so the palette for example has:
   <pre>
   new HelpAdapator(this, palette);
   </pre>
 * and the help browser will look for the file \juice\help\palette.html.
 * <p>
 * e help adaptor is essentially a mouse and keyboard listener of the component,
 * and if the appropriate mouse events (shift-right click) or keyboard (F1) occur,
 * it starts the help browser. The help browser is an HTML document viewer; it takes
 * the name given (eg palette above), prefixes the default help path (\juice\help)
 * and postfixes .html to find the document to display.
 * <p>
 * You can add references (#ref) and fallback pages if you like, as follows:
   <pre>
   New HelpAdaptor(this, PropertyEditors, ColorEditor, PropertyPane);
   </pre>
 * Will show the help page PropertyEditors.html and go to the reference tag
 * <ref ColorEditor>.  If it cannot find that page, it will display the page
 * PropertyPane.    In the palette for example, each button has attached a
 * helpAdaptor (referring to the class the button is for) with the palette as
 * fallback, so that if you write the help for the button, thats fine, if not
 * the palette help will still appear.
 * <p>
 * In all cases, if it cannot find the first page, or fallback page if given,
 * it will try showing the NotFound.html page.
 * <p>
 * Note that if you attach an adaptor to a panel, and then components
 * on the panel, the components will obscure the panel and so prevent mouse
 * clicks from reaching it, and therefore the help adaptor.  If you want to add
 * help to a complete panel and all children, use the help adaptors
 * addChildren(top-level child) method.
 * <p>
 * Beware that if you add more than one help adaptor to a particular component,
 * you will get all the help pages appearing.
 * It might be worth doing something about this using a static list in the help
 * adaptors so that the last one added overrides all others....
 *
 * @author M Hill
 * @date Dec 2000
 */


public class HelpAdaptor implements HelpKeys, MouseListener, KeyListener
{
   Component component;                   //main component with associated help page/browser
   
   String page;                           //index to help page
   String ref;                            //index to reference tag within page
   
   private static final int HELP_KEY = KeyEvent.VK_F1;   //keyboard key that invokes help
   
   /**
   * Constructor
   */
   public HelpAdaptor(Component aComponent, String aPage, String aRef)
   {
      component = aComponent;
      page = aPage;
      ref = aRef;
   
      addComponent(aComponent);
   }
   
   /**
    * Convenience constructor
    */
   public HelpAdaptor(Component aComponent, String aPage)
   {
      this(aComponent, aPage, null);
   }

   /**
    * Add extra components to listen to.  Useful if one help
    * page/reference is used for a collection of components
    */
   public void addComponent(Component aComponent)
   {
      aComponent.addMouseListener(this);
      aComponent.addKeyListener(this);
   }
   
   /**
    * as addComponent but also recurses through all
    * chilren to listen to them.  Useful for panels, etc
    * where components on that panel are likely to obscure
    * the panel from mouse events.
    */
   public void addChildren(Container aContainer)
   {
      Component comp;
      for (int i=0;i<aContainer.getComponentCount();i++)
      {
         comp = aContainer.getComponent(i);
         addComponent(comp);;
         
         if (comp instanceof Container)
         {
            addChildren((Container) comp);
         }
      }
   }
   
   /**
    * Kick off help browser
    */
   private void doHelp()
   {
      Help.showHelp(page, ref, null);
   }
   
   /**
    * Interface implementations - blank
    */
   public void mousePressed(MouseEvent event) {}
   public void mouseEntered(MouseEvent event) {}
   public void mouseExited(MouseEvent event) {}
   public void mouseReleased(MouseEvent event) {}

   public void keyPressed(KeyEvent event) {}
   public void keyReleased(KeyEvent event) {}
   
   /**
    * Actions on - mouse middle button clicked or F1 pressed
    */
   public void mouseClicked(MouseEvent event)
   {
      if ( SwingUtilities.isMiddleMouseButton(event)
           || (SwingUtilities.isRightMouseButton(event) && event.isShiftDown()))
      {
         doHelp();
      }
   }

   public void keyTyped(KeyEvent event)
   {
      if ((event.getKeyCode() == event.VK_HELP)
               || (event.getKeyCode() == HELP_KEY))
      {
         doHelp();
      }
   }
}
