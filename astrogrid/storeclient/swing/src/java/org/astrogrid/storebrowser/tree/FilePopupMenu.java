/*
 * $Id: FilePopupMenu.java,v 1.2 2005/04/04 01:10:15 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import org.astrogrid.storebrowser.tree.actions.*;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;



/**
 * Popup menu for files
 */

public class FilePopupMenu extends JPopupMenu {

   /** Initialise icons etc */
   public FilePopupMenu(SelectedFileGetter getter) {

      add(new JMenuItem(new RefreshAction(getter)));

      add(new JMenuItem(new DeleteAction(getter)));
      
      add(new JMenuItem(new UploadAction(new JFileChooser(), getter)));

      add(new JMenuItem(new DownloadAction(new JFileChooser(), getter)));

      add(new JMenuItem(new CopyFromUrlAction(getter)));
      
   }

}

