/*
 * $Id: VoFileSelector.java,v 1.1 2004/02/15 23:25:30 mch Exp $
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.ui.myspace.MySpaceBrowser;
import org.astrogrid.vospace.delegate.MySpaceReference;
import org.astrogrid.ui.IconFactory;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.JPasteButton;

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
   private User operator = User.ANONYMOUS;
   
   JFileChooser fileChooser = new JFileChooser();
   
   /** action is SAVE_ACTION or OPEN_ACTION from MySpaceBrowser */
   public VoFileSelector(String label, String action, User aUser ) {

      this.browserAction = action;
      this.operator = aUser;
      
      fileEntryField.setEditable(true);
      fileEntryField.addKeyListener(this);
      fileEntryField.getEditor().getEditorComponent().addKeyListener(this);

      pasteBtn = new JPasteButton(fileEntryField);
      
      myspaceBrowserBtn = makeIconButton("MySpace");
      myspaceBrowserBtn.setToolTipText("Browse MySpace");
      fileBrowserBtn = makeIconButton("Open");
      myspaceBrowserBtn.setToolTipText("Browse local files");
      validateBtn = makeIconButton("Check");
      myspaceBrowserBtn.setToolTipText("Validate reference");
      
      btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
      btnPanel.add(pasteBtn);
      btnPanel.add(myspaceBrowserBtn);
      btnPanel.add(fileBrowserBtn);
      btnPanel.add(validateBtn);
      btnPanel.add(validLabel);
      
      myspaceBrowserBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               browseServer();
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

   /** Helper conveience method for making the little icon buttons on the right */
   public  JButton makeIconButton(String title)
   {
      Icon i = IconFactory.getIcon(title);
      if (i == null)
      {
         return new JButton(title);
      }
      else
      {
         JButton iconBtn = new JButton(i);
         iconBtn.setBorder(null);
         iconBtn.setPreferredSize(new Dimension(i.getIconWidth()+4, i.getIconHeight()+4));
         return iconBtn;
      }
   }

   /** Set who the operator is who is using this view */
   public void setOperator(User aUser)
   {
      this.operator = aUser;
   }

   /** Returns the text entered in the field - ie the file picked/typed in */
   public String getFileLoc()
   {
      return fileEntryField.getText();
   }

   /** Start MySpace Browser */
   public void browseServer()
   {
      try
      {
         String loc = getFileLoc();
         
         MySpaceBrowser browser = MySpaceBrowser.showDialog(myspaceBrowserBtn, loc, operator, browserAction);
         if (!browser.isCancelled())
         {
            fileEntryField.setItem(browser.getMySpaceRef());
            validate();
         }
      } catch (IOException ioe)
      {
         ioe.printStackTrace();
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
      if (MySpaceReference.isMySpaceRef(loc))
      {
         try {
            MySpaceReference.assertValid(loc);
            setValid(true);
         } catch (AssertionError ae) {
            setValid(false);
            JOptionPane.showMessageDialog(this, "Entry is not a valid Myspace URI ("+ae+")", "Invalid Entry", JOptionPane.WARNING_MESSAGE);
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
Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */

