/*
 * $Id: QueryPollingMonitor.java,v 1.2 2004/03/07 00:33:50 mch Exp $
 */

package org.astrogrid.datacenter.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.store.Ivorn;
import org.astrogrid.ui.EscEnterListener;


/**
 * Monitors the state of a particular query on a datacenter, by polling every second.
 * Runs in a seperate thread.
 *
 * QueryPollingMonitor frame = new QueryPollingMonitor();
 * frame.pack();
 * frame.show();
 *
 * @author mch
 */


public class QueryPollingMonitor extends JFrame implements Runnable
{
   DatacenterQuery query = null;

   Log log = LogFactory.getLog(QueryPollingMonitor.class);
      
   JLabel statusLabel = null;
   
   /**
    * Creates a polling monitor given an IVORN for the query, of the form:
    * ivo:<datacenter>#queryId
    */
   public QueryPollingMonitor(Ivorn ivorn)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Creates a polling monitor given an endpoint for the datacneter
    */
   public QueryPollingMonitor(URL datacenterEndpoint, String queryId)
   {
      super("QueryPollingMonitor");

      initComponents();

//    new Thread(this).start();
      throw new UnsupportedOperationException();
   }

   /**
    * Creates a polling monitor given an endpoint for the datacneter
    */
   public QueryPollingMonitor(DatacenterQuery queryToMonitor)
   {
      super("QueryPollingMonitor");
      
      this.query = queryToMonitor;
      
      initComponents();
      
      new Thread(this).start();
   }
   

   private void initComponents() {
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(new JLabel(query.getId()+":", JLabel.LEFT), BorderLayout.NORTH);
      
      statusLabel = new JLabel("Unknown", JLabel.CENTER);
      
      getContentPane().add(statusLabel, BorderLayout.CENTER);

      new EscEnterListener(this, null, null);
      
      pack();
   }

   /**
    * Shows the frame.
    */
   public void show()
   {
      // You can also use: Dimension size = new Dimension(300,200);
      Dimension size = getPreferredSize();
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
   
   /**
    * Closes the frame.
    */
   private void close()
   {
      setVisible(false);
      
      System.exit(0); // Delete this line if necessary.
   }
   
   
   /** Polling activity */
   public void run() {
      QueryState status = QueryState.UNKNOWN;
      
      while (status != QueryState.FINISHED) {
         try {
            status = query.getStatus();
            
            statusLabel.setText(status.getText());
            
            Thread.currentThread().sleep(1000);
         }
         catch (IOException ioe) {
            log.error(ioe);
         }
         catch (InterruptedException ioe) {
            log.error(ioe);
         }
      }
   }
}

