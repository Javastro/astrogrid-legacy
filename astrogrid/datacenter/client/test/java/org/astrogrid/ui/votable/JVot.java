/*
 * $Id: JVot.java,v 1.1 2004/02/17 16:04:06 mch Exp $
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.votable;

import VOTableUtil.Resource;
import VOTableUtil.Table;
import VOTableUtil.Votable;
import com.tbf.xml.XmlElement;
import com.tbf.xml.XmlParser;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.astrogrid.log.Log;
import org.astrogrid.ui.ExtensionFileFilter;

/**
 * A panel for displaying a votable
 *
 * @version %I%
 * @author M Hill
 */

public class JVot extends JTable
{
   private VOTableUtil.Votable votModel = null;

   public JVot()
   {
   }

   public void setVotModel(VOTableUtil.Votable givenVot)
   {
      votModel = givenVot;

      DefaultTableModel model = (DefaultTableModel) getModel();
      VOTableUtil.Table table = votModel.getResourceAt(0).getTableAt(0);
      for(int i=0; i<table.getFieldCount(); i++)
      {
         VOTableUtil.Field field = (VOTableUtil.Field)table.getFieldAt(i);

         String ucd = "";
         if (field.getUcd() != null)
         {
            ucd = " ["+field.getUcd()+"]";
         }
         
         model.addColumn(field.getName()+ucd);
      }

      VOTableUtil.Tabledata data = table.getData().getTabledata();

      for (int r=0; r<data.getTrCount(); r++)
      {
         VOTableUtil.Tr tr = data.getTrAt(r);
         model.addRow( (Object[]) null);

         for (int c=0; c<tr.getTdCount(); c++)
         {
            VOTableUtil.Td td = tr.getTdAt(c);

            model.setValueAt(td.getPCDATA(), r, c);
         }
      }
   }


   public VOTableUtil.Votable getVotModel()
   {
      return votModel;
   }

   /**
    * Factory method that sets up this instance within a scrollable panel
    * with column headings
    */
   public JPanel newVotScrollPanel()
   {
      JPanel votPanel = new JPanel(new BorderLayout());
      votPanel.add(getTableHeader(), BorderLayout.NORTH);
      JScrollPane scroller = new JScrollPane(this);
      // scroller.setHorizontalScrollBarPolicy(scroller.HORIZONTAL_SCROLLBAR_NEVER);
      votPanel.add(scroller, java.awt.BorderLayout.CENTER);
      return votPanel;
   }





   /**
    * See JVotBox for a useful test harness
    *
   public static void main(String [] args)
    /**/

}
/*
$Log: JVot.java,v $
Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */

