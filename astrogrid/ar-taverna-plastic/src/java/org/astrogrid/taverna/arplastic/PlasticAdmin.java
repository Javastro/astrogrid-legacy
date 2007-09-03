/**
 * 
 */
package org.astrogrid.taverna.arplastic;


import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collection;
import java.net.URI;
import javax.swing.*;
import java.awt.Component;
import java.awt.Color;

import java.util.Vector;
import java.util.Iterator;
//import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.ExtendableHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.incoming.messages.votable.VOTableLoader;
import org.votech.plastic.incoming.messages.votable.VotableLoadFromUrlMessageInvoker;
import org.votech.plastic.managers.AbstractObservableManager.ManagerObserver;
import org.votech.plastic.outgoing.messages.info.EchoMessage;
import org.votech.plastic.outgoing.messages.info.GetDescriptionMessage;
import org.votech.plastic.managers.PlasticRegistry;
import org.votech.plastic.managers.PlasticConnectionManagerImpl;
import org.votech.plastic.managers.PlasticConnectionManager;
import org.votech.plastic.managers.PlasticApplication;

import org.votech.plastic.managers.PlasticRegistryListener;

import org.apache.log4j.Logger;


/** Singleton static that contains a reference to ACR.
 * seems only way to share context object between processor / factory / etc.
 * @todo replace with a better-architected solution - in particular, something
 * that will reconnect to ACR if connection was lost.
 * @author Noel Winstanley
 * @since May 30, 20064:36:02 PM
 */
public class PlasticAdmin implements ManagerObserver, PlasticRegistryListener {
	
	private static Logger logger = Logger.getLogger(PlasticAdmin.class);
	
	
    private static final String NAME = "Taverna";
    private static final String DESCRIPTION = "Taverna used for sending messages to other apps on the Hub";
    private static final String IVORN = "ivo://taverna";
    private static final String LOGOURL = "http://taverna.sourceforge.net/images/taverna.png";
    private PlasticConnectionManager manager;
    private PlasticRegistry registry;
    private JList plasicAppList;
    private static boolean pasticInitialized = false;


	public PlasticAdmin() {
		logger.warn("plasticadmin constructor initPlastic");
		initPlastic();
		
	}
	
	 /**
     * Set up the {@link PlasticConnectionManager} that is responsible for housekeeping tasks
     * such as keeping track of whether the hub is up or down, registering us, and responding
     * to common informational messages.
     *
     */
    private void initPlastic() {
    	if(pasticInitialized) {
    		return;
    	}

        //Create a message handler for the core informational messages
        StandardHandler handler = new StandardHandler(NAME, DESCRIPTION, IVORN, LOGOURL);

        //Create another message handler for the loadVOTable message.  This type of handler can 
        //handle any number of different messages.
        /*
        ExtendableHandler exhandler = new ExtendableHandler();
        exhandler.addMessageInvoker(new VotableLoadFromUrlMessageInvoker(new VOTableLoader() {
            //A VOTable loader is something capable of loading a votable!  In this case, our
            //anon inner class just has a dummy method.
            public void loadVOTable(URI sender, String id, URL url) throws Exception {
                System.out.println("Application "+sender+" has asked us to load a table from "+url+" and give it ID "+id);
            }
        }));
        */

        //Handlers can be then chained together.  Some handlers "absorb" messages that they have dealt with, while
        //other types of handlers such as the LoggingHandler, deal with them and pass them on to the next handler.
        //handler.appendHandler(exhandler);

        //Create the Plastic manager, tell it your name, the handler at the beginning of the chain, the fact
        //that we don't want to connect immediately, and that should we lose the hub we want to retry to
        //connect every 10 seconds.
        manager = new PlasticConnectionManagerImpl(NAME, handler, false, 10000 );

        //Register ourselves as being interested in the hub starting and stopping
        //See ManagerObserver interface
        manager.addObserver(this);

        //Instruct the manager to connect to the hub.
        manager.connect(); // Blocks waiting for connection.  See also manager.connectWhenReady()

        //While a PlasticConnectionManager keeps track of the hub, a PlasticRegistry keeps track of
        //other plastic applications.
        registry = new PlasticRegistry(manager);
        // Register ourselves as being interested in knowing when applications register and deregister
        registry.addListener(this);
        makeList();
    }
    
    public boolean isHubUp() {
    	return plasticHubisUp;
    }
    
    public JList getPlasticList() {
    	return this.plasicAppList;
    }
    

    /**
     * Called when the hub goes down
     * @see org.votech.plastic.managers.AbstractObservableManager.ManagerObserver
     */
    public void down() {
        logger.warn("The hub has gone down");
//        System.exit(0);
        plasticHubisUp = false;
    }

    /**
     * Called when the hub comes up
     * @see org.votech.plastic.managers.AbstractObservableManager.ManagerObserver
     */
    public void up() {
        logger.warn("The hub is up");
        //logger.warn("This hub's ID is "+manager.getHub().getHubId());
        logger.warn("My ID is "+ manager.getId());
        plasticHubisUp = true;
    }
    
    /**
     * Called when an application registers or unregisters.
     * @see PlasticRegistryListener
     */
    public void applicationsChanged() {
    	
        //System.out.println("An application has registered/deregistered");
    	//rebuild our Plastic List
    	makeList();
    }    
    
    
    private JList makeList() {
    	logger.warn("makeList()");
        Collection apps =  registry.getPlasticApplications();
        Iterator it = apps.iterator();
        Vector plasticApps = new Vector(apps.size());
        PlasticAppForList []pafl = new PlasticAppForList[apps.size()];
        logger.warn("makeList PlasticAppForList array size = " + pafl.length);
        while (it.hasNext()) {
            PlasticApplication app = (PlasticApplication) it.next();
            plasticApps.add(new PlasticAppForList(app));
        }
        JList list = new JList(plasticApps);
        list.setCellRenderer(new SimpleCellRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        this.plasicAppList = list;
        return this.plasicAppList;
    }
    
    public PlasticRegistry getPlasticRegistry() {
    	logger.warn("getPlasticRegistry()");
    	return this.registry;
    }


	private static boolean plasticHubisUp = true;
	
	class PlasticAppForList
    {
		PlasticApplication pa;
		public PlasticAppForList(PlasticApplication pa) {
			this.pa = pa;
		}
		
		public PlasticApplication getPlasticApplication() {
			return this.pa;
		}
    }	
	
	class SimpleCellRenderer extends JLabel implements ListCellRenderer
    {
        public SimpleCellRenderer()
        {
            setOpaque(true);
        }
        
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
        	PlasticAppForList val = (PlasticAppForList)value;
        	PlasticApplication pa = val.getPlasticApplication();
        	
            setText(pa.getName());
            setIcon(new ImageIcon(pa.getIconUrl()));
            setToolTipText(pa.getDescription());
            setBackground(isSelected ? Color.red : (index & 1) == 0 ? Color.cyan : Color.green);
            setForeground(isSelected ? Color.white : Color.black);
            return this;
        }
    }
}