/*
 * $Id: DatacenterSelector.java,v 1.1 2004/02/17 03:39:13 mch Exp $
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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.JHistoryComboBox;
import org.astrogrid.ui.JPasteButton;

public class DatacenterSelector extends JPanel  {
   
   JHistoryComboBox serverEntryField = new JHistoryComboBox();
   JPasteButton serverPasteBtn = null;
   JLabel validLabel = new JLabel("Valid");

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

      //layout components - grid
      setLayout(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();

      constraints.gridy ++;
      
      GridBagHelper.setLabelConstraints(constraints); add(new JLabel("Server"), constraints);
      GridBagHelper.setEntryConstraints(constraints); add(serverEntryField, constraints);
      GridBagHelper.setControlConstraints(constraints); add(serverPasteBtn, constraints);
      
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
}

/*
$Log: DatacenterSelector.java,v $
Revision 1.1  2004/02/17 03:39:13  mch
New Datacenter UIs

 */

