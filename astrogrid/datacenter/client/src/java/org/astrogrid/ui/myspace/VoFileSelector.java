/*
 * $Id: VoFileSelector.java,v 1.1 2004/03/03 17:40:58 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.myspace;


/**
 * A Panel for selecting files from VoSpace.  Provides buttons for browsing
 * myspace as well as local files, urls, etc.  Also includes a history of
 * previous choices.
 *
 * @author M Hill
 */

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.astrogrid.community.Account;
import org.astrogrid.ui.IconButtonHelper;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.JPasteButton;
import org.astrogrid.ui.myspace.MySpaceBrowser;
import org.astrogrid.store.Agsl;

public class VoFileSelector extends JPanel implements KeyListener {

   JHistoryComboBox fileEntryField = new JHistoryComboBox();
   JPasteButton pasteBtn = null;
   JButton myspaceBrowserBtn = null;
   JButton fileBrowserBtn = null;
   JButton validateBtn = null;
   JLabel validLabel = new JLabel("Valid");
   JLabel mainLabel = null;
   JPanel btnPanel = new JPanel();
   
   boolean isValid = false;

   private String browserAction = MySpaceBrowser.OPEN_ACTION;
   
   //the person operating the browser is not nec the same as the owner of
   //the files we are using
   private Account operator = Account.ANONYMOUS;
   
   JFileChooser fileChooser = new JFileChooser();
   
   /** action is SAVE_ACTION or OPEN_ACTION from MySpaceBrowser */
   public VoFileSelector(String label, String action, Account aUser ) {

      this.browserAction = action;
      this.operator = aUser;
      
      fileEntryField.setEditable(true);
      fileEntryField.addKeyListener(this);
      fileEntryField.getEditor().getEditorComponent().addKeyListener(this);

      pasteBtn = new JPasteButton(fileEntryField);
      
      myspaceBrowserBtn = IconButtonHelper.makeIconButton("MySpace", "World", "Browse MySpace");
      fileBrowserBtn = IconButtonHelper.makeIconButton("Files", "Open", "Browse Local Files");
      validateBtn = IconButtonHelper.makeIconButton("Check", "Question", "Validate reference");
      
      btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
      btnPanel.add(pasteBtn);
      btnPanel.add(myspaceBrowserBtn);
      btnPanel.add(fileBrowserBtn);
      btnPanel.add(validateBtn);
      btnPanel.add(validLabel);
      
      myspaceBrowserBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               browseMySpace();
            }
         }
      );
      
      fileBrowserBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               browseFiles();
            }
         }
      );
      
      validateBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               validate();
            }
         }
      );
      
      
      mainLabel = new JLabel(label+" ", JLabel.RIGHT);

      //layout components - grid
      setLayout(new BorderLayout());
      add(mainLabel, BorderLayout.WEST);
      add(fileEntryField, BorderLayout.CENTER);
      add(btnPanel, BorderLayout.EAST);

      validate();
   }

   /** Returns label so that owning components can lay out differently */
   public JComponent getLabel() {   return mainLabel; }
   
   /** Returns entry field so that owning components can lay out differently */
   public JComponent getEntryField() { return fileEntryField;  }
   
   /** Returns control buttons so that owning components can lay out differently */
   public JComponent getControls() {   return btnPanel;  }


   /** Set who the operator is who is using this view */
   public void setOperator(Account aUser)
   {
      this.operator = aUser;
   }

   /** Returns the text entered in the field - ie the file picked/typed in */
   public String getFileLoc()
   {
      return fileEntryField.getText();
   }

   /** For setting the text */
   public void setFileLoc(String newEntry)
   {
      fileEntryField.setItem(newEntry);
   }

   /** Returns the full vospace reference for the entry */
   public Agsl toAgsl() throws MalformedURLException {
        Agsl vorl = null;
        if ((getFileLoc() != null) && (getFileLoc().trim().length()>0)) {
           vorl = new Agsl(getFileLoc());
        }
        return vorl;
   }
   
   /** Start MySpace Browser */
   public void browseMySpace()
   {
      Agsl vorl;
      try {
         vorl = toAgsl();
      }
      catch (MalformedURLException mue) {} //ignore - it might be some other reference
         
      try
      {
         
         MySpaceBrowser browser = MySpaceBrowser.showDialog(myspaceBrowserBtn, toAgsl(), operator, browserAction);
         if (!browser.isCancelled())
         {
            fileEntryField.setItem(browser.getAgsl());
            validate();
         }
      } catch (IOException ioe)
      {
         JOptionPane.showMessageDialog(this, ioe, "Error starting myspace browser", JOptionPane.ERROR_MESSAGE);
      }
   }

   /** Start local file chooser */
   public void browseFiles()
   {
      int response = fileChooser.showOpenDialog(this);
      
      if ((response != JFileChooser.CANCEL_OPTION) && (response != JFileChooser.ERROR_OPTION)) {
         fileEntryField.setItem("file://"+fileChooser.getSelectedFile());
      }
   }
   
   /** Checks (1) that the given reference is valid, and (2) that it exists */
   public void validate()
   {
      String loc = getFileLoc();
      
      if ((loc == null) || (loc.trim().length()==0)) {
         setValid(false);
         return;
      }
      
      //is it valid?
      if (Agsl.isAgsl(loc))
      {
         try {
            new Agsl(loc);
            setValid(true);
         } catch (MalformedURLException me) {
            setValid(false);
            JOptionPane.showMessageDialog(this, "Entry is not a valid Myspace URI ("+me+")", "Invalid Entry", JOptionPane.WARNING_MESSAGE);
         }
      } else {
         try {
            new URL(loc);
            setValid(true);
         } catch (MalformedURLException mue) {
            setValid(false);
            JOptionPane.showMessageDialog(this, "Entry is not a valid URL ("+mue+")", "Invalid Entry", JOptionPane.WARNING_MESSAGE);
         }
      }
      
      //does it exist?
      
      //this is not always wanted so let's leave it
   }
   
   
   /**
    * sets the state to invalid
    */
   public void setValid(boolean newValid)
   {
      boolean oldValid = this.isValid; //remember - we want to have this state set before we fire the changes
      this.isValid = newValid;
      firePropertyChange("Valid", this.isValid, newValid);

      if (isValid) { validLabel.setText("Valid"); } else { validLabel.setText("INVALID"); }
   }

   public boolean getValid() { return isValid; }
   
   public boolean isValid() { return isValid; }

   /**
    * Invoked when a key has been typed.
    * See the class description for {@link KeyEvent} for a definition of
    * a key typed event.
    */
   public void keyTyped(KeyEvent ke) {   }
   
   /**
    * Invoked when a key has been pressed.
    * See the class description for {@link KeyEvent} for a definition of
    * a key pressed event.
    */
   public void keyPressed(KeyEvent ke) {
      if (ke.getKeyCode() == ke.VK_ENTER)
      {
         validate();
      }

   }
   
   /**
    * Invoked when a key has been released.
    * See the class description for {@link KeyEvent} for a definition of
    * a key released event.
    */
   public void keyReleased(KeyEvent e) {  }
   
}

/*
$Log: VoFileSelector.java,v $
Revision 1.1  2004/03/03 17:40:58  mch
Moved ui package

Revision 1.3  2004/03/02 01:33:24  mch
Updates from chagnes to StoreClient and Agsls

Revision 1.2  2004/02/24 16:04:02  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.2  2004/02/17 03:47:04  mch
Naughtily large lump of various fixes for demo

Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */

