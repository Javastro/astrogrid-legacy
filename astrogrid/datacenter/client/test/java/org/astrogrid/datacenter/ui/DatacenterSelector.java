/*
 * $Id: DatacenterSelector.java,v 1.2 2004/02/24 16:04:02 mch Exp $
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.JPasteButton;

public class DatacenterSelector extends JPanel  {
   
   JHistoryComboBox serverEntryField = new JHistoryComboBox();
   JPasteButton serverPasteBtn = null;
   JLabel validLabel = new JLabel("Valid");
   JLabel mainLabel = new JLabel("Server");
   JPanel btnPanel = new JPanel();

   boolean isValid = false;
   
   public DatacenterSelector() {

      serverEntryField.setEditable(true);
      
      serverEntryField.setDefaultList(new String[] {
               "http://grendel12.roe.ac.uk:8080/PAL",
               "http://astrogrid.ast.cam.ac.uk:9080/INT-WFS",
               "http://cass123.ast.cam.ac.uk:4040/pal-SNAPSHOT",
               "http://virtualsky.org/servlet/cover?CAT=messier",
               "http://adil.ncsa.uiuc.edu/cgi-bin/vocone?survey=f",
               "http://vm07.astrogrid.org:8080/pal"
            } );
      serverPasteBtn = new JPasteButton(serverEntryField);

      btnPanel.add(serverPasteBtn);
      
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

   public String getDelegateEndPoint()
   {
      return serverEntryField.getText()+"/services/AxisDataServer";
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
Revision 1.2  2004/02/24 16:04:02  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.1  2004/02/17 03:39:13  mch
New Datacenter UIs

 */

