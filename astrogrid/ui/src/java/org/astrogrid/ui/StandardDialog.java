package org.astrogrid.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * This class implements a standard data entry dialog with "Ok" and
 * "Cancel" buttons. Subclasses can override the isDataValid(),
 * okButtonPressed(), and cancelButtonPressed() methods to perform
 * implementation specific processing.
 * <P>
 * By default, the dialog is modal, and has a JPanel with a
 * BorderLayout for its content pane.
 *
 * @author David Fraser
 * @author Michael Harris
 * @author Martin Hill
 */
public class StandardDialog extends JDialog
{
   
   /** The spacing between components in pixels */
   private static final int COMPONENT_SPACING = 10;
   
   /** The content pane for holding user components */
   private Container userContentPane;
   
   /** Flag indicating if "Cancel" button was pressed to close dialog */
   private boolean isCancelled = true;
   
   /** Flag indicating that the box should stay showing when the OK button
    * is pressed */
   private boolean persistOnOk = false;
   
   protected JPanel buttonPanel = null;
   private JButton okButton = null;    // the 'ok' button indicating task is to proceed
   private JButton cancelButton = null;  // the 'cancel' button indicating task is to be abandoned
   
   protected EscEnterListener eelistener = null;
   
   /**
    * Creates a StandardDialog with the given parent frame
    * and title.
    *
    * @param parent The parent frame for the dialog.
    * @param title The title to display in the dialog.
    */
   public StandardDialog(Frame parent, String title)
   {
      super(parent, title);
      
      sdinit();
   }
   
   /**
    * Creates a StandardDialog with the given parent dialog
    * and title.
    *
    * @param parent The parent dialog for the dialog.
    * @param title The title to display in the dialog.
    */
   public StandardDialog(Dialog parent, String title)
   {
      super(parent, title);
      
      sdinit();
   }
   
   /**
    * assembles components and sets properties.  Final so that subclasses
    * cannot override - if they do, it will get called by this classes
    * constructor, ie before the subclass constructors get a chance to
    * initialise components
    */
   private final void sdinit()
   {
      setModal(true);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      
      // Setup the internal content pane to hold the user content pane
      // and the standard button panel
      JPanel internalContentPane = new JPanel();
      
      //override inherited one to hold the panel with buttons
      super.setContentPane(internalContentPane);

      //space nicely
      /** leave to subclasses
      internalContentPane.setLayout(
         new BorderLayout(COMPONENT_SPACING, COMPONENT_SPACING));
      
      internalContentPane.setBorder(
         BorderFactory.createEmptyBorder(COMPONENT_SPACING,
                                         COMPONENT_SPACING, COMPONENT_SPACING, COMPONENT_SPACING));
       **/
      internalContentPane.setLayout(new BorderLayout());
      
      //OK button with anonymous action class - only works if data is valid
      okButton = new JButton(
         new AbstractAction("Ok")
         {
            public void actionPerformed(ActionEvent actionEvent)
            {
               if(isInputValid())
               {
                  isCancelled = false;
                  
                  validOkPerformed();

                  if (!persistOnOk)
                  {
                     dispose();
                  }
               }
               else
               {
                  invalidOkPerformed();  //normally just a beep
               }
            }
         }
      );
      
      //cancel button with anonymous action class
      cancelButton = new JButton(
         new AbstractAction("Cancel")
         {
            public void actionPerformed(ActionEvent actionEvent)
            {
               isCancelled = true;
               
               dispose();
            }
         }
      );
      
      // Create the standard button panel with "Ok" and "Cancel"
      buttonPanel = new JPanel();
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      
      //add button panel to dialog at bottom
      internalContentPane.add(buttonPanel, BorderLayout.SOUTH);
      
      //add 'esc'/'enter' and window closing listener
      eelistener = new org.astrogrid.ui.EscEnterListener(this, okButton, cancelButton);

      // Initialise the user content pane with a standard JPanel
      setContentPane(new JPanel(new BorderLayout()));

   }
   
   /**
    * returns the content pane for adding components.
    * Components should not be added directly to the dialog.
    *
    * @returns the content pane for the dialog.
    */
   public Container getContentPane()
   {
      return userContentPane;
   }
   
   /**
    * Reregisters the EscEnterListener after things have been added
    * to the dialog
    */
   public void reregisterEscEnterListener()
   {
      eelistener.registerWith(this);
   }
   
   /**
    * sets the content pane for adding components.
    * Components should not be added directly to the dialog.
    *
    * @param contentPane The content pane for the dialog.
    */
   public void setContentPane(Container contentPane)
   {
      //super.getContentPane().remove(userContentPane);

      userContentPane = contentPane;
      
      //add 'esc'/'enter' and window closing listener
      eelistener.registerWith(userContentPane);
   
      super.getContentPane().add(userContentPane, BorderLayout.CENTER);
   }
   
   /**
    * returns <code>true</code> if the User cancelled
    * the dialog otherwise <code>false</code>. The dialog is cancelled
    * if the "Cancel" button is pressed or the "Close" window button is
    * pressed, or the "Escape" key is pressed. In other words, if the
    * User has caused the dialog to close by any method other than by
    * pressing the "Ok" button, this method will return <code>true</code>.
    */
   public boolean wasCancelled()
   {
      return isCancelled;
   }
   
   /**
    * used to validate the current dialog box.
    * provides a default response of <code>true</code>.
    *
    * @returns a boolean indicating if the data is valid.
    * <code>true</code> indicates that all of the fields were validated
    * correctly and <code>false</code> indicates the validation failed
    */
   protected boolean isInputValid()
   {
      return true;
   }

   /**
    * Sets whether the box should remain visible (persist true) when the
    * ok button is pressed, or wether it should be disposed of
    */
   protected void setPersistOnOk(boolean b)
   {
      persistOnOk = b;
   }
   
   /**
    * This method should be overridden if any extra actions are required if
    * the validation fails.  Beeps by default.
    */
   protected void invalidOkPerformed()
   {
      java.awt.Toolkit.getDefaultToolkit().beep();
   }

   /**
    * Called when the OK button is pressed with valid data. override if
    * necessary - does nothing by default
    */
   protected void validOkPerformed()
   {
   }
   
   /**
    * Returns the instance of the ok button, so that its colour, etc can
    * be changed
    */
   protected JButton getOkButton()
   {
      return okButton;
   }

   /**
    * Returns the instance of the cancel button, so that its colour, etc can
    * be changed
    */
   protected JButton getCancelButton()
   {
      return cancelButton;
   }

   /**
    * Adds a button to the button panel at the given index
    */
   public void addButton(JButton button, int index)
   {
      buttonPanel.add(button, index);
   }
   
   /**
    * Adds a button to the button panel after the other buttons
    */
   public void addButton(JButton button)
   {
      buttonPanel.add(button);
   }
   
   /**
    * Test harness
    */
   public static void main(String [] args)
   {
      StandardDialog sd = new StandardDialog((Frame) null, "Test dialog");

      sd.show();
   }
   
   
}
