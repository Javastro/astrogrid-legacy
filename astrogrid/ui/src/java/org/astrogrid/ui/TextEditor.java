package org.astrogrid.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

/**
 * A simple text editor field in a dialog box with OK, Open, Save, Save As
 * and Cancel
 *
 */
public class TextEditor extends StandardDialog implements ActionListener
{
   private JButton okButton = null;    // the 'ok' button indicating task is to proceed
   private JButton cancelButton = null;  // the 'cancel' button indicating task is to be abandoned
   
   private JButton openButton = null;
   private JButton saveButton = null;
   private JButton saveAsButton = null;

   private JEditorPane editorPane = null;
   
   private JFileChooser chooser = new JFileChooser();
   
   /**
    * Creates an editor with the given parent frame
    * and title.
    *
    * @param parent The parent frame for the dialog.
    * @param title The title to display in the dialog.
    */
   public TextEditor(Frame parent, String title)
   {
      super(parent, title);
      
      init();
      
      if (parent != null)
      {
         centerAndSizeOn(parent);
      }
   }
   
   /**
    * Creates an editor with the given parent dialog
    * and title.
    *
    * @param parent The parent dialog for the dialog.
    * @param title The title to display in the dialog.
    */
   public TextEditor(Dialog parent, String title)
   {
      super(parent, title);
      
      init();
      
      if (parent != null)
      {
         centerAndSizeOn(parent);
      }
   
   }
   

   /**
    * assembles components and sets properties
    */
   private final void init()
   {
      //make up tool bar
      openButton = new JButton(IconFactory.getIcon("Open"));
      openButton.setToolTipText("Opens configuration file");
      openButton.addActionListener(this);

      saveAsButton = new JButton(IconFactory.getIcon("SaveAs"));
      saveAsButton.setToolTipText("Saves current configuration to given file");
      saveAsButton.addActionListener(this);

      JToolBar toolbar = new JToolBar();
      toolbar.add(openButton);
      //toolbar.add(saveButton);
      toolbar.add(saveAsButton);
      getContentPane().add(toolbar, BorderLayout.NORTH);

      editorPane = new JEditorPane();
      JScrollPane scroller = new JScrollPane(editorPane);
      
      getContentPane().add(scroller, BorderLayout.CENTER);
   }

   /**
    * Centers on component & should size to something appropriate
    */
   public void centerAndSizeOn(Component parent)
   {
      setLocation(parent.getLocation());
      setSize(parent.getSize());
   }
   
   /**
    * Loads text area with given text
    */
   public void setText(String text)
   {
      editorPane.setText(text);
   }

   /**
    * Gets text area's contents
    */
   public String getText()
   {
      return editorPane.getText();
   }
   
   
   
   /**
    * Invoked when an action occurs.
    */
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == openButton)
      {
         int resp = chooser.showOpenDialog(this);
         if (resp == chooser.APPROVE_OPTION)
         {
            loadFile(chooser.getSelectedFile());
         }
      }
      else if (e.getSource() == saveAsButton)
      {
         int resp = chooser.showSaveDialog(this);
         if (resp == chooser.APPROVE_OPTION)
         {
            saveFile(chooser.getSelectedFile());
         }
      }
      
   }

   /**
    * Loads text from given file into text field
    */
   public void loadFile(File aFile)
   {
      //not the most efficient implementation, but it will do for now
      try
      {
         InputStream in = new BufferedInputStream(new FileInputStream(aFile));
         int c = 0;
         StringBuffer textBuffer = new StringBuffer();
         while ( (c = in.read()) != -1)
         {
            textBuffer.append(c);
         }
         editorPane.setText(textBuffer.toString());
         in.close();
      }
      catch (IOException ioe)
      {
         JOptionPane.showMessageDialog(this, "Could not load file "+aFile+": "+ioe.getMessage());
      }
   }
   
   /**
    * Saves text into given file
    */
   public void saveFile(File aFile)
   {
      try
      {
         FileOutputStream out = new FileOutputStream(aFile);
         out.write(editorPane.getText().getBytes());
         out.close();
      }
      catch (IOException ioe)
      {
         JOptionPane.showMessageDialog(this, "Could not save file "+aFile+": "+ioe.getMessage());
      }
      
   }
   
   /**
    * Test harness
    */
   public static void main(String [] args)
   {
      TextEditor editor = new TextEditor((Frame) null, "Editor");

      editor.show();
   }
   
   
}
