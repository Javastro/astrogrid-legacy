/* $Id: DatacenterProdder.java,v 1.4 2004/03/13 16:26:25 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.datacenter.ui;
import java.awt.*;
import java.io.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.IconFactory;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.Splash;
import org.astrogrid.ui.myspace.MySpaceBrowser;
import org.astrogrid.ui.myspace.VoFileSelector;
import org.astrogrid.ui.votable.JVotBox;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * A GUI for users to query datacenters with the convenience of cut and paste, etc
 *
 * The portal should eventually take over this function, but it's useful for
 * testing.
 *
 * @author mch
 */

public class DatacenterProdder extends JFrame
{
   /** so we can remember selections from instance to instance without having to type it in every time */
   Properties storedEntries = new Properties();
   
   JButton cancelButton;
   JButton kickOffSearchButton;
   JButton blockSearchButton;
   VoFileSelector queryLocator;
   VoFileSelector resultsLocator;
   DatacenterSelector datacenterLocator;
   JHistoryComboBox jobMonitorField;
   
   Account user = Account.ANONYMOUS;

   Log log = LogFactory.getLog(DatacenterProdder.class);
   
   /**
    * Initializes the frame.
    */
   public DatacenterProdder()
   {
      super("AstroGrid Datacenter Prodder");
      
      //query locator
      JPanel queryPanel = new JPanel(new BorderLayout());
      queryLocator = new VoFileSelector("Query", MySpaceBrowser.OPEN_ACTION, Account.ANONYMOUS);
      queryPanel.add(queryLocator, BorderLayout.NORTH);
      
      //datacenter locator
      JPanel datacenterPanel = new JPanel(new BorderLayout());
      datacenterLocator = new DatacenterSelector();
      datacenterPanel.add(datacenterLocator, BorderLayout.NORTH);
      
      //results locator
      JPanel resultsPanel = new JPanel(new BorderLayout());
      resultsLocator = new VoFileSelector("Results", MySpaceBrowser.SAVE_ACTION, Account.ANONYMOUS);
      resultsPanel.add(resultsLocator, BorderLayout.NORTH);
      
      //job monitor field
      jobMonitorField = new JHistoryComboBox();
      jobMonitorField.setEditable(true);
      
      jobMonitorField.setDefaultList(new String[] {
               "http://vm05.astrogrid.org:8080/astrogrid-jes/services/JobMonitorService"
            });
      
      //button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      blockSearchButton = new JButton("Query (Block)", IconFactory.getIcon("Query"));
      kickOffSearchButton = new JButton("Query (KickOff)", IconFactory.getIcon("Query"));
      cancelButton = new JButton("Exit", IconFactory.getIcon("Exit"));
      buttonPanel.add(kickOffSearchButton);
      buttonPanel.add(blockSearchButton);
      buttonPanel.add(cancelButton);
      
      kickOffSearchButton.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               kickOffSearch();
            }
         }
      );
      
      blockSearchButton.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               blockSearch();
            }
         }
      );

      cancelButton.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
         }
      );

      JPanel contents = new JPanel();
      contents.setLayout(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.fill = GridBagConstraints.BOTH;
      constraints.weightx = 1;
      constraints.weighty = 0;
      constraints.insets = new Insets(5, 5, 5, 5);

      GridBagHelper.setLabelConstraints(constraints);
      contents.add(queryLocator.getLabel(), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      contents.add(queryLocator.getEntryField(), constraints);
      GridBagHelper.setControlConstraints(constraints);
      contents.add(queryLocator.getControls(), constraints);

      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      contents.add(datacenterLocator.getLabel(), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      contents.add(datacenterLocator.getEntryField(), constraints);
      GridBagHelper.setControlConstraints(constraints);
      contents.add(datacenterLocator.getControls(), constraints);

      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      contents.add(resultsLocator.getLabel(), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      contents.add(resultsLocator.getEntryField(), constraints);
      GridBagHelper.setControlConstraints(constraints);
      contents.add(resultsLocator.getControls(), constraints);

      //job monitor
      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      contents.add(new JLabel("Monitor"), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      contents.add(jobMonitorField, constraints);
      

      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(contents, BorderLayout.NORTH);
      getContentPane().add(buttonPanel, BorderLayout.SOUTH);
      
      new EscEnterListener(this, null, cancelButton, true);
      
      setUser(new Account("avodemo","test.astrogrid.org","EmptyToken"));

      loadEntries();
   }
   
   /**
    * Shows the frame.
    */
   public void show()
   {
      Dimension size = new Dimension(800,400);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setBounds
      (
         (screenSize.width - size.width) / 2,
         (screenSize.height - size.height) / 2,
         size.width,
         size.height
      );
      
      super.show();
   }

   private void loadEntries()
   {
      try
      {
         storedEntries.load(new FileInputStream(new File("DatacenterProdderEntries")));
         
         queryLocator.setFileLoc(storedEntries.getProperty("Query",""));
         resultsLocator.setFileLoc(storedEntries.getProperty("Results",""));
         datacenterLocator.setText(storedEntries.getProperty("Datacenter",""));
         jobMonitorField.setItem(storedEntries.getProperty("JobMonitor",""));
                                    
      } catch (IOException ioe) {} //ignore
   }

   private void storeEntries()
   {
      try
      {
         storedEntries.put("Query", queryLocator.getFileLoc());
         storedEntries.put("Results", resultsLocator.getFileLoc());
         storedEntries.put("Datacenter", datacenterLocator.getText());
         storedEntries.put("JobMonitor", jobMonitorField.getText());
                                    
         storedEntries.store(new FileOutputStream(new File("DatacenterProdderEntries")), "Datacenter Prodder entries");
      } catch (IOException ioe) {} //ignore
   }
   
   /**
    * Sets the user the operations will be carried out under
    */
   public void setUser(Account aUser)
   {
      user = aUser;
      queryLocator.setOperator(aUser);
      resultsLocator.setOperator(aUser);
   }
   
   
   /**
    * Starts an asynchronous query as a FullSearcher delegate
    */
   public void kickOffSearch()  {
      
      storeEntries();
      ProgressMonitor progBox = new ProgressMonitor(this, "Submitting Query","Connecting to datacenter",0,2);
      
      try {
         
         progBox.setMillisToDecideToPopup(0);
         progBox.setMillisToPopup(0);

         //connect up to the datacenter
         log.info("Connecting to datacenter at "+datacenterLocator.getDelegateEndPoint());
         QuerySearcher querier = DatacenterDelegateFactory.makeQuerySearcher(datacenterLocator.getDelegateEndPoint());

         progBox.setProgress(1);
         progBox.setNote("Connecting to query document");

         InputStream inQuery;
         if (queryLocator.getFileLoc().startsWith("file")) {
            inQuery = new URL(queryLocator.getFileLoc()).openStream();
         } else {
            inQuery = new Agsl(queryLocator.getFileLoc()).openStream(User.ANONYMOUS);
         }

         progBox.setProgress(2);
         progBox.setNote("Loading query document");
         log.info("Loading query doc");

         //load query
         Element queryDoc = DomHelper.newDocument(inQuery).getDocumentElement();
         
         progBox.setProgress(3);
         progBox.setNote("Creating querier on server");
         log.info("Creating querier on server");

         //set the results destination
         String resultsLoc = resultsLocator.getFileLoc();
         assert resultsLoc != null : "Need to specify results!";
         
         log.info("...with results destination: "+resultsLoc);

         progBox.setProgress(4);
         progBox.setNote("Starting query");
         String id = querier.submitQuery(new AdqlQuery(queryDoc), new Agsl(resultsLoc), "VOTABLE");

         JFrame frame = new QueryPollingMonitor(new URL(datacenterLocator.getDelegateEndPoint()), id);
         frame.show();
         
         log.info("...query started.");
      }
      catch (Exception e)
      {
         log.error("Failed to kick off Query",e);
      }
      finally {
         progBox.setProgress(progBox.getMaximum()+1);   //close
      }

   }

   /**
    * Runs a synchronous (blocking query) and displays the results
    */
   public void blockSearch() {
      
      storeEntries();
      
      ProgressMonitor progBox = new ProgressMonitor(this, "Running Query","Connecting to datacenter",0,6);
      progBox.setMillisToDecideToPopup(0);
      progBox.setMillisToPopup(0);

      try {

         //connect up to the datacenter
         log.info("Connecting to datacenter at "+datacenterLocator.getDelegateEndPoint());
         QuerySearcher querier = DatacenterDelegateFactory.makeQuerySearcher(datacenterLocator.getDelegateEndPoint());

         progBox.setProgress(1);
         progBox.setNote("Connecting to query document");

         InputStream inQuery;
         if (queryLocator.getFileLoc().startsWith("file")) {
            inQuery = new URL(queryLocator.getFileLoc()).openStream();
         } else {
            inQuery = new Agsl(queryLocator.getFileLoc()).openStream(User.ANONYMOUS);
         }

         progBox.setProgress(2);
         progBox.setNote("Loading query document");
         log.info("Loading query doc");

         //load query
         Element queryDoc = DomHelper.newDocument(inQuery).getDocumentElement();
         
         progBox.setProgress(3);
         progBox.setNote("Running querier");

         //and GO!
         log.info("Running Query...");
         InputStream results = querier.askQuery(new AdqlQuery(queryDoc), querier.VOTABLE);
         log.info("...Query complete");

         if (resultsLocator.getFileLoc() != null) {

            progBox.setProgress(4);
            progBox.setNote("Query complete. Storing Results...");
         
            //send results to given location
            if (resultsLocator.getFileLoc().startsWith("file")) {
               throw new UnsupportedOperationException("Not yet implemented local file save");
            } else {
               log.info("Sending results to myspace...");
               Agsl resultsRL = new Agsl(resultsLocator.getFileLoc());

               StoreClient vos = StoreDelegateFactory.createDelegate(user.toUser(), resultsRL);
               OutputStream out = vos.putStream(resultsRL.getPath());
               log.info("...piping...");
               Piper.bufferedPipe(results, out);
               
               log.info("...results gone");
            }
         }
         else
         {
            progBox.setProgress(5);
            progBox.setNote("Displaying Results");

            //Pick out results. Rather painfully the Votable implementation
            //borrowed from some IVO folks only works with a particular
            //representation of votable, so we write it to disk first then
            //load it again... We do it to memory given it must have arrived
            //that way :-)
            JVotBox box = new JVotBox(this);
            box.getVotController().loadVot(results); //oh well try it
            box.show();
         }
         
         progBox.setProgress(progBox.getMaximum()+1);   //close
      }
      catch (Exception e)
      {
         log.error("Failed to start Query as tool",e);
      }
      finally {
         progBox.setProgress(progBox.getMaximum()+1);   //close
      }

   }
   
   
   /**
    * Runs the box
    */
   
   public static void main(String[] args) {

      
      Splash s = new Splash("Datacenter Prodder","Desktop Client",
                            "mch/1.0",
                            Color.white,Color.blue, IconFactory.getImage("AstroGrid"),"");
      
      DatacenterProdder frame = new DatacenterProdder();
      frame.pack();
      s.dispose();
      frame.show();
   }
   
   
   
   
}

/*
 $Log: DatacenterProdder.java,v $
 Revision 1.4  2004/03/13 16:26:25  mch
 Changed makeFullSearcher to makeQuerySearcher

 Revision 1.3  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.2  2004/03/07 21:13:52  mch
 Changed apache XMLUtils to implementation-independent DomHelper

 Revision 1.1  2004/03/03 10:08:01  mch
 Moved UI and some IO stuff into client

 Revision 1.5  2004/03/02 01:33:24  mch
 Updates from chagnes to StoreClient and Agsls

 Revision 1.4  2004/02/24 16:04:02  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.3  2004/02/19 23:28:55  mch
 Nicer Splash

 Revision 1.2  2004/02/17 15:03:44  mch
 Removed Group from Account

 Revision 1.1  2004/02/17 03:39:13  mch
 New Datacenter UIs

 */
