/*
 * $Id: DatacenterSelector.java,v 1.3 2004/10/08 15:17:54 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.datacenter.ui;


/**
 * A Panel for selecting myspace.  Consists of an input line with a history of
 * previous choices, and a button that (should) start a myspace browser.
 *
 * @author M Hill
 */

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.status.monitor.Monitor;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.JPasteButton;

public class DatacenterSelector extends JPanel implements ActionListener {
   
   JHistoryComboBox serverEntryField = new JHistoryComboBox();
   JPasteButton serverPasteBtn = null;
   JButton statusBtn = null;
   JLabel validLabel = new JLabel("Valid");
   JLabel mainLabel = new JLabel("Server");
   JPanel btnPanel = new JPanel();

   boolean isValid = false;
   
   public DatacenterSelector() {

      serverEntryField.setEditable(true);
      
      serverEntryField.setDefaultList(new String[] {
               "http://grendel12.roe.ac.uk:8080/pal-6df",
               "http://grendel12.roe.ac.uk:8080/pal",
//               "http://grendel12.roe.ac.uk:8080/PAL-6dF/services/AxisDataServer",
 //              "http://astrogrid.ast.cam.ac.uk:9080/INT-WFS/services/AxisDataServer",
//               "http://cass123.ast.cam.ac.uk:4040/pal-SNAPSHOT/services/AxisDataServer",
//               "http://virtualsky.org/servlet/cover?CAT=messier/services/AxisDataServer",
//               "http://adil.ncsa.uiuc.edu/cgi-bin/vocone?survey=f",
//               "http://vm07.astrogrid.org:8080/pal-It4.1/services/AxisDataServer",
//               "http://vm07.astrogrid.org:8080/pal-SNAPSHOT/services/AxisDataServer",
//               "http://vm07.astrogrid.org:8080/pal-05Test/services/AxisDataServer_v0_4_1"
               "http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-SNAPSHOT"
            } );
      serverPasteBtn = new JPasteButton(serverEntryField);

      statusBtn = new JButton("Status");
      statusBtn.addActionListener(this);
      
      btnPanel.add(serverPasteBtn);
      btnPanel.add(statusBtn);
      
      
      //layout components - grid
      setLayout(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();

      constraints.gridy ++;
      
      GridBagHelper.setLabelConstraints(constraints); add(mainLabel, constraints);
      GridBagHelper.setEntryConstraints(constraints); add(serverEntryField, constraints);
      GridBagHelper.setControlConstraints(constraints); add(btnPanel, constraints);
      
      constraints.gridy ++;

//      GridBagHelper.setLabelConstraints(constraints); add(new JLabel("endpoint "), constraints);
//      GridBagHelper.setEntryConstraints(constraints); add(delegateEndPointView, constraints);
//      GridBagHelper.setControlConstraints(constraints); add(validLabel, constraints);
      
   }

   /**
    * Invoked when an action occurs.
    */
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == statusBtn) {
         popUpStatus(serverEntryField.getText());
      }
   }

   public void popUpStatus(String endpoint) {

      ProgressMonitor progBox = new ProgressMonitor(this,"Getting status from "+endpoint,"Please Wait, connecting...",0,2);
      progBox.setMillisToDecideToPopup(0);
      progBox.setMillisToPopup(0);
      
      String msg = null;
      int msgType = JOptionPane.INFORMATION_MESSAGE;
      try {
         //ServiceStatus status = Monitor.getDatacenterStatus(endpoint);
         String status = Monitor.getSimpleDatacenterStatus(endpoint);

         progBox.setNote("Analysing status");
         progBox.setProgress(1);
         
         msg = status.toString();
         
      }
      catch (ServiceException e) {
         
         e.printStackTrace();
         
         msg = e.toString();
         Throwable t = e.getCause();
         while (t != null) {
            t = t.getCause();
            msg = msg+"\nCaused by "+t;
         }
         
         msgType = JOptionPane.ERROR_MESSAGE;
      }
      catch (AxisFault e) {
         e.printStackTrace();
         
         msg = "AxisFault: "+e.getFaultString()+"\n"+e.getFaultCode();
         Throwable t = e.getCause();
         while (t != null) {
            t = t.getCause();
            msg = msg+"\nCause by "+t;
         }
         
         msgType = JOptionPane.ERROR_MESSAGE;
      }
      catch (RemoteException e) {
         e.printStackTrace();
         
         msg = e.toString();
         Throwable t = e.getCause();
         while (t != null) {
            t = t.getCause();
            msg = msg+"\nCause by "+t;
         }
         
         msgType = JOptionPane.ERROR_MESSAGE;
      }
      progBox.close();
      
      JOptionPane.showMessageDialog(this, msg, "Status of "+endpoint, msgType);
   }
   
   
   public String getDelegateEndPoint()
   {
      return serverEntryField.getText();
   }

   public void setText(String newEntry)
   {
      serverEntryField.setItem(newEntry);
   }
   
   public String getText()
   {
      return serverEntryField.getText();
   }
   
   /** Returns label so that owning components can lay out differently */
   public JComponent getLabel() {   return mainLabel; }
   
   /** Returns entry field so that owning components can lay out differently */
   public JComponent getEntryField() { return serverEntryField;  }
   
   /** Returns control buttons so that owning components can lay out differently */
   public JComponent getControls() {   return btnPanel;  }

   
}

/*
$Log: DatacenterSelector.java,v $
Revision 1.3  2004/10/08 15:17:54  mch
Some updates to try and reach SSA/etc at ROE

Revision 1.2  2004/03/08 15:54:57  mch
Better exception passing, removed Metdata

Revision 1.1  2004/03/03 10:08:01  mch
Moved UI and some IO stuff into client

Revision 1.3  2004/03/02 01:33:24  mch
Updates from chagnes to StoreClient and Agsls

Revision 1.2  2004/02/24 16:04:02  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.1  2004/02/17 03:39:13  mch
New Datacenter UIs

 */

