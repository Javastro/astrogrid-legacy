/*
 * $Id: JVotController.java,v 1.1 2004/02/15 23:25:30 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.votable;

import java.io.*;

import VOTableUtil.Votable;
import com.tbf.xml.XmlElement;
import com.tbf.xml.XmlParser;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JFileChooser;
import org.astrogrid.log.Log;
import org.astrogrid.ui.ExtensionFileFilter;

/**
 * A controller for the JVot Panel
 *
 * @author mch
 */

public class JVotController implements ActionListener
{
   private JVot votView = null;
   public static final String SAVEAS_CMD = "SaveAs";
   public static final String LOAD_CMD = "Load";

   private JFileChooser chooser = new JFileChooser();

   public static final ExtensionFileFilter votFileFilter = new ExtensionFileFilter(new String[] {"vot"},"VOTable");
   public static final ExtensionFileFilter xmlFileFilter = new ExtensionFileFilter(new String[] {"xml"},"XML");

   public JVotController(JVot view)
   {
      this.votView = view;
      
      chooser.addChoosableFileFilter(xmlFileFilter);
      chooser.addChoosableFileFilter(votFileFilter);
   }


   public void loadVot(InputStream in) throws IOException
   {
         //horrible horrible Javot requiring a particular parser...
         XmlParser parser = new XmlParser();
         XmlElement rootNode = parser.parse(new BufferedInputStream(in));

         if (rootNode == null)
         {
            throw new IOException(parser.getLastError());
         }
         
         Votable votable = new Votable(rootNode);
         throwValidationErrors(votable.getValidationErrors());

         if (votable.getResourceCount()==0)
         {
            throw new IOException("Table has no Resource");
         }
//         Resource resource = votable.getResourceAt(0);
//         throwValidationErrors(resource.getValidationErrors());

//         Table table = resource.getTableAt(0);
//         throwValidationErrors(table.getValidationErrors());

         votView.setVotModel(votable);

   }

   private void throwValidationErrors(Vector errors) throws IOException
   {
      if ((errors != null) && (errors.size() > 0))
      {
         throw new IOException( ""+errors.elementAt(0)); //just get first for now
      }
   }

   public void saveVot(OutputStream out) throws IOException
   {
      votView.getVotModel().toXml(out);
   }


   /**
    * Carries out actions based on button or menu/etc events.  This means that
    * a UI that contains this component can create controls with the commands
    * given by this component (eg LOAD_CMD) and set this as the action listener,
    * eg:
    * <pre>
    *     JButton loadButton = new JButton("Load");
    *     loadButton.setActionCommand(JVot.LOAD_CMD);
    *     loadButton.addActionListener(votTable);
    *     toolbar.add(loadButton);
    * </pre>
    */
   public void actionPerformed(java.awt.event.ActionEvent e)
   {
      if (e.getActionCommand().equals(SAVEAS_CMD))
      {
         if (chooser.showSaveDialog(votView) == chooser.APPROVE_OPTION)
         {
            try
            {
               saveVot(new FileOutputStream(chooser.getSelectedFile()));
            }
            catch (IOException ioe)
            {
               Log.logError("Could not save VOTable to '"+chooser.getSelectedFile()+"'",ioe);
            }
         }
      }

      if (e.getActionCommand().equals(LOAD_CMD))
      {
         if (chooser.showOpenDialog(votView) == chooser.APPROVE_OPTION)
         {
            try
            {
               loadVot(new FileInputStream(chooser.getSelectedFile()));
            }
            catch (IOException ioe)
            {
               Log.logError("Could not load VOTable '"+chooser.getSelectedFile()+"'\n"+
                            "Are you sure it is a valid VOTable?",
                            ioe);
            }
         }
      }
   }




   /**
    * See JVotBox for a useful test harness
    *
   public static void main(String [] args)
    /**/

}

/*
$Log: JVotController.java,v $
Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */
