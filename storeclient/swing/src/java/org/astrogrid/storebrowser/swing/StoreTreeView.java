/*
 * $Id: StoreTreeView.java,v 1.1.1.1 2005/02/16 15:02:46 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.storebrowser.swing.models.RootStoreNode;
import org.astrogrid.storebrowser.swing.models.StoreFileNode;
import org.astrogrid.storebrowser.swing.models.StoreNode;

/**
 * Shows a store file tree.
 *
 */

public class StoreTreeView extends JTree {

   //the operator of this view, not (necessarily) the owner of the files
   Principal operator = null;
   
   Log log = LogFactory.getLog(StoreTreeView.class);
   
   public StoreTreeView(Principal aUser) throws IOException {
      super(new DefaultTreeModel(new RootStoreNode(aUser)));
      
      this.operator = aUser;
      setShowsRootHandles(true);
      setRootVisible(false);
   }
   
   
   public StoreFile getSelectedFile()
   {
      if (getSelectionPath() == null)  {
         return null;
      } else {
         Object node = getSelectionPath().getLastPathComponent();
         if (node instanceof StoreFileNode) {
            try {
               return ((StoreFileNode) node).getFile();
            }
            catch (URISyntaxException ioe) {
               ((StoreFileNode) node).setError(ioe);
            }
            catch (IOException ioe) {
               ((StoreFileNode) node).setError(ioe);
            }
         }
         return null;
      }
   }
   
   public String convertValueToText(Object value, boolean selected,
                                    boolean expanded, boolean leaf, int row,
                                    boolean hasFocus) {
      
      //remember that StoreNode subclases StoreFileNode so we must check it first...
      if (value instanceof StoreNode) {
         return ((StoreNode) value).getName();
      }
      else if (value instanceof StoreFileNode) {
//         try {
            return ((StoreFileNode) value).getName();
//         }
//         catch (URISyntaxException use) {
//            ((StoreFileNode) value).setError(use);
//            return use.getMessage();
//         }
//         catch (IOException ioe) {
//            ((StoreFileNode) value).setError(ioe);
//            return ioe.getMessage();
//         }
      }
      else {
         return value.toString();
      }
   }

}

/*
 $Log: StoreTreeView.java,v $
 Revision 1.1.1.1  2005/02/16 15:02:46  mch
 Initial Checkin

 Revision 1.1.2.1  2005/01/26 14:48:06  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/11/25 01:28:59  mch
 Added mime type to outputchild

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package



 */

