/**
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Tom Oinn, EMBL-EBI
 */
package org.astrogrid.taverna.armyspace;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URI;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;

import org.apache.log4j.Logger;

import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.InvalidArgumentException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.acr.astrogrid.NodeInformation;

import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scuflui.TavernaIcons;
import org.embl.ebi.escience.scuflui.spi.ResultMapSaveSPI;

import org.embl.ebi.escience.scufl.SemanticMarkup;


/**
 * Store the Map of DataThing objects to disk, using the
 * collection structure to drive the directory structure
 * and storing each leaf DataThing item as a single file
 * @author Tom Oinn
 */
public class SaveToMyspace implements ResultMapSaveSPI {
	
	private static Logger logger = Logger.getLogger(SaveToMyspace.class);

    /**
     * Return the standard looking save to disk icon
     */
    public Icon getIcon() {
	return TavernaIcons.saveIcon;
    }

    /**
     * Get the description for this plugin
     */
    public String getDescription() {
	return ( "Saves the complete set of results to the myspace system,\n"+
		 "writing each result into its own file or set of directories\n"+
		 "in the case of collections, the directory structure\n"+
		 "mirroring that of the collection and leaf nodes being\n"+
		 "allocated numbers as names starting at zero and incrementing.");
    }

    /**
     * Return the name for this plugin
     */
    public String getName() {
    	logger.warn("In getName() for Saving to Myspace");
    	return "Save to Myspace";
    }
    
    /**
     * Show a standard save dialog and dump the results to disk
     */
    public ActionListener getListener(Map results, JComponent parent) {
	final Map resultMap = results;
	final JComponent parentComponent = parent;
	return new ActionListener() {
		
		
		public void actionPerformed(ActionEvent e) {
			logger.warn("Inside actionPerformed in SaveToMyspace calling the resource chooser");
			System.out.println("inside actionperformed --println");
			/*
		    JFileChooser jfc = new JFileChooser();
		    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = jfc.showSaveDialog(parentComponent);
		    */
			try {			
			    URI chosenURI = SingletonACR.getURI("Save to Myspace");
			    Myspace ms = SingletonACR.getMyspace();
			    NodeInformation ni = ms.getNodeInformation(chosenURI);
			    if(!ni.isFolder()) {
			    	chosenURI = ms.getParent(chosenURI);
			    }
			    logger.warn("chosen URI = " + chosenURI);
			    System.out.println("uri chosen = " + chosenURI);
			    for (Iterator i = resultMap.keySet().iterator(); i.hasNext();) {		    	
					String resultName = (String) i.next();
					logger.warn("keyname from map = " + resultName);
					DataThing resultValue = (DataThing) resultMap.get(resultName);
					logger.warn("ok got the datathing for saving its object lets look at what mime types it has ");
					SemanticMarkup markup = resultValue.getMetadata();
					Object []mimeTypes = markup.getMIMETypeList().toArray();
					for(int k = 0;k < mimeTypes.length;k++) {
						logger.warn("mimetype k = " + k + " val = " + mimeTypes[k]);
						System.out.println("println mimetype k = " + k + " val = " + mimeTypes[k]);
					}					
					MyspaceWriter mw = new MyspaceWriter(ms);
					mw.writeObject(resultValue.getDataObject(),chosenURI, resultName);
				}//for
			}catch(NotFoundException ne) {
				logger.warn("NotFoundException " + ne.toString());
				ne.printStackTrace();
			}catch(ServiceException se) {
				logger.warn("ServiceException " + se.toString());				
				se.printStackTrace();
			}catch(InvalidArgumentException ia) {
				logger.warn("InvalidArgumentException " + ia.toString());		
				ia.printStackTrace();
			}catch(SecurityException se) {
				logger.warn("SecurityException " + se.toString());	
				se.printStackTrace();
			}catch(ACRException ae) {
				logger.warn("ACRException " + ae.toString());	
				ae.printStackTrace();
			}catch(IOException ioe) {
				ioe.printStackTrace();
				logger.warn("IOException " + ioe.toString());
			}
		}
	    };//new ActionListener
    }//getListener

}
