/*
 * $Id: StoresView.java,v 1.1 2004/11/08 23:15:38 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.store;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.astrogrid.community.Account;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.delegate.StoreClient;

/**
 * For listing store points, such as homespace, ftp, and able to load stores from
 * a registry
 *
 */

public class StoresView extends JPanel {
   StoreClient delegate = null;
   
   //the operator of this view, not (necessarily) the owner of the files
   Account operator = Account.ANONYMOUS;
   JPanel listView = new JPanel();
   //JPanel buttons = new JPanel();
//   Component homespace = new JButton("HomeSpace");
   /** indicates which UI component corresponds to which store */
   Hashtable storeLookup = new Hashtable();

   JButton homespace = null;
   
   StoreBrowser parentBrowser = null;
   
   public StoresView(Account aUser, StoreBrowser browser) throws IOException {
      
      this.parentBrowser = browser;
      
      JScrollPane scrollPane = new JScrollPane();

      setLayout(new BorderLayout());
      add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(listView);
    
      listView.setLayout(new BoxLayout(listView, BoxLayout.Y_AXIS));
   
      StoreSelectorListener storeButtonListener = new StoreSelectorListener();
      
      homespace = new JButton("Homespace");
      homespace.addActionListener(storeButtonListener);
      setOperator(aUser);
      listView.add(homespace);
      
      //set the standard stores
      JButton twmbarlwm = new JButton("Twmbarlwm:8080");
      storeLookup.put(twmbarlwm, "myspace:http://twmbarlwm.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager");
      twmbarlwm.addActionListener(storeButtonListener);
      listView.add(twmbarlwm);
      
      JButton k = new JButton("Katatjuta");
      storeLookup.put(k, "myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager");
      k.addActionListener(storeButtonListener);
      listView.add(k);
   }

   /** package private for internal use only - external/subclasses should call login */
   void setOperator(Account aUser) {
      storeLookup.put(homespace, aUser.getIvorn());
      homespace.setEnabled( !aUser.equals(Account.ANONYMOUS) );
      this.operator = aUser;
   }

   private class StoreSelectorListener implements ActionListener {
      
      /**
       * Invoked when a store button is pressed
       */
      public void actionPerformed(ActionEvent e) {
         String storeIdentifier = storeLookup.get(e.getSource()).toString();
         
         if (storeIdentifier == null) {
            throw new RuntimeException("No store name associated with component "+e.getSource());
         }
         else {
            parentBrowser.setServer(storeIdentifier);
         }
      }
      
   }
   
   /** Loads the stores in the default registry (given in config) and adds them to the
    * list */
   public void loadRegistryStores() {

      //connect up to default
      RegistryService client = RegistryDelegateFactory.createQuery();
   
      //client.dosomesortofquery
      
      //for each of the ones found, add to list
   }
   
}

/*
 $Log: StoresView.java,v $
 Revision 1.1  2004/11/08 23:15:38  mch
 Various fixes for SC demo, more store browser, more Vizier stuff

 Revision 1.2  2004/05/10 15:06:22  mch
 Fixed toAgsl()

 Revision 1.1  2004/04/15 17:24:31  mch
 Moved myspace ui to store ui

 Revision 1.4  2004/03/09 11:00:39  mch
 Fixed for moved myspace delegates

 Revision 1.3  2004/03/06 19:34:21  mch
 Merged in mostly support code (eg web query form) changes

 Revision 1.1  2004/03/03 17:40:58  mch
 Moved ui package

 Revision 1.3  2004/03/02 01:33:24  mch
 Updates from chagnes to StoreClient and Agsls

 Revision 1.2  2004/02/24 16:04:02  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.1  2004/02/17 16:04:06  mch
 New Desktop GUI

 Revision 1.2  2004/02/17 03:47:04  mch
 Naughtily large lump of various fixes for demo

 Revision 1.1  2004/02/15 23:25:30  mch
 Datacenter and MySpace desktop client GUIs

 */

