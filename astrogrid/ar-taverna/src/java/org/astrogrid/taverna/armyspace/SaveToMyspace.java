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
		
		private void processObjectToWrite(Object resultValue,URI parentDirectoryURI, Myspace ms, String resultName) 
		    throws NotFoundException, InvalidArgumentException, 
		           ServiceException, SecurityException, NotApplicableException {
			URL checkURL;
			URI fileURI;
			Object  o;
			Collection coll;
			Object []collArray;
			
			logger.warn("Inside processObjectToWrite"); 
			//SemanticMarkup markup = resultValue.getMetadataForObject(resultValue, false);
			
			
			
			if(resultValue instanceof String) {
				logger.warn("seems to be a string check for url just in case");
				try {
					checkURL = new URL((String)resultValue);
					fileURI = ms.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
					ms.copyURLToContent(checkURL, fileURI);
				}catch(MalformedURLException me) {
					//doesn't matter.
				}
				fileURI = ms.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
				ms.write(fileURI, (String)resultValue);
			}else if(resultValue instanceof URL) {
				logger.warn("seems to be a URL");

				fileURI = ms.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
				ms.copyURLToContent((URL)resultValue, fileURI);
			}else if(resultValue instanceof File) {
				logger.warn("seems to be a File");

				fileURI = ms.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
				try {
					ms.copyURLToContent((URL)((File)resultValue).toURL(), fileURI);
				}catch(MalformedURLException me ) {}
			}else if(resultValue instanceof Map) {
				logger.warn("seems to be a Map");

				//some kind of map just keys and values.
				for (Iterator j = ((Map)resultValue).keySet().iterator(); j.hasNext();) {
				   Object resultValueKey = j.next();
				   logger.warn("key for the map = " + resultValueKey.toString());
				   if(resultValueKey instanceof String) {
					   o = ((Map)resultValue).get(resultValueKey);
					   processObjectToWrite(o,parentDirectoryURI,ms,resultName + "_" + (String)resultValueKey);
				   }else {
					   //oh darn even the key is not a string lets just get values and
					   //save it and that is all we can do.
					   coll = ((Map)resultValue).values();
					   collArray = coll.toArray();
					   for(int k = 0;k < collArray.length;k++) {
						   o = collArray[k];						   
						   processObjectToWrite(o,parentDirectoryURI,ms,resultName + "_" + String.valueOf(k));
					   }//for
					   //this will break out of the iterator of the map
					   break;
				   }//else
				}//for
			}//else if map
			
			else if(resultValue instanceof byte[]) {
				logger.warn("it is a byte array try writing binary");
				fileURI = ms.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
				ms.writeBinary(fileURI,(byte[])resultValue);
			}
			else if(resultValue instanceof Collection) {
				logger.warn("seems to be a Collection");

				   coll = ((Collection)resultValue);
				   collArray = coll.toArray();
				   logger.warn("size of collection = " + collArray.length);
				   for(int k = 0;k < collArray.length;k++) {
					   o = collArray[k];						   
					   processObjectToWrite(o,parentDirectoryURI,ms,resultName + "_" + String.valueOf(k));						   
				   }//for				
			}else {
				logger.warn("can't find class instance lets do a tostring see what happens");
				logger.warn(resultValue.toString());
			}
			//what else might I need to save.
		}
		
		public void actionPerformed(ActionEvent e) {
			logger.warn("Inside actionPerformed in SaveToMyspace calling the resource chooser");
			System.out.println("inside actionperformed --println");
			/*
		    JFileChooser jfc = new JFileChooser();
		    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = jfc.showSaveDialog(parentComponent);
		    */
			try {			
			    URI chosenURI = SingletonACR.getURI();
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
						processObjectToWrite(resultValue.getDataObject(),chosenURI, ms, resultName);
				}//for
			}catch(NotFoundException ne) {
				ne.printStackTrace();
			}catch(ServiceException se) {
				se.printStackTrace();
			}catch(InvalidArgumentException ia) {
				ia.printStackTrace();
			}catch(SecurityException se) {
				se.printStackTrace();
			}catch(ACRException ae) {
				ae.printStackTrace();
			}
				
			
		}
	    };//new ActionListener
    }//getListener

}
