/**
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Tom Oinn, EMBL-EBI
 */
package org.astrogrid.taverna.arplastic;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.io.IOException;

import java.net.URISyntaxException;

import uk.ac.starlink.votable.VOTableBuilder;
//import org.embl.ebi.escience.scuflui.renderers.AbstractRenderer.ByMimeType;
import org.embl.ebi.escience.scuflui.renderers.*;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;

import org.apache.log4j.Logger;


import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.BorderLayout;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Collection;
import java.util.Vector;
import org.w3c.dom.Document;
import java.net.URI;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

import org.embl.ebi.escience.baclava.DataThing;
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


/**
 * Renders using the Jmol software for chemical structures
 * @author Tom Oinn
 */
public class PlasticRenderer extends AbstractRenderer.ByMimeType implements ActionListener {
	
    private static final int VOTABLEMIME = 0;
    private static final int FITSMIME = 1;
    private static final int UNKNOWNFORPLASTICSMIME = 2;
    
    private int determinedMimeType  = UNKNOWNFORPLASTICSMIME;
    

	private JList objList;
	
    boolean siaAvailable = false;
    boolean spectraAvailable = false;

    private JCheckBox votableCheck;
    private JCheckBox siaCheck;
    private JCheckBox specCheck;

    private static Logger logger = Logger.getLogger(PlasticRenderer.class);
    
    
	PlasticAdmin pa;
    public PlasticRenderer() {
    	super("PlasticDispatcher");
    	logger.warn("inside plasticrenderer constructor");
    	pa = new PlasticAdmin();
    }
    
    public boolean isTerminal() {
    	return true;
    }
    
    public boolean canHandle(RendererRegistry renderers,
			     Object userObject,
			     String mimeType) {
        if (mimeType.matches(".*text/votable*")) {
        	determinedMimeType = VOTABLEMIME;
        	logger.warn("mime type votable return true in canhandle");
        	return true;
        }
        if(mimeType.matches(".*images/fits*")) {
        	logger.warn("mime type fits return true in canhandle");        	
        	determinedMimeType = FITSMIME;
        	return true;
        }
    	URL checkURL;
    	int type;
        if (userObject instanceof Map) {
			//ok look into the map for values  of string xml or w3c.dom.Document
			//look at the xml and see if it starts with <VOTABLE>
			
			//also look at string instances to see if they have votable.
			Map obj = (Map)userObject;
			Collection valColl = obj.values();
			Object []valObjs = valColl.toArray();
			for(int i = 0;i < valObjs.length;i++) {
				if(valObjs[i] instanceof String) {
					if(((String)valObjs[i]).indexOf("<VOTABLE") != -1 &&
					   ((String)valObjs[i]).indexOf("</VOTABLE") != -1) {
						determinedMimeType = VOTABLEMIME;
						return true;
					}else {
						try {
							checkURL = new URL((String)valObjs[i]);
							determinedMimeType = checkURLForKnownType(checkURL);
							if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;
						}catch(MalformedURLException me) {}
					}
				}else if(valObjs[i] instanceof URL) {
					determinedMimeType = checkURLForKnownType((URL)valObjs[i]);
					if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;					
				}else if(valObjs[i] instanceof File) {
					try {
						determinedMimeType = checkURLForKnownType(  ((File)valObjs[i]).toURL() );
						if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;
					}catch(MalformedURLException me) {}						
				}
			}//for
        }//if
        
        if(userObject instanceof URL) {
			determinedMimeType = checkURLForKnownType((URL)userObject);
			if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;
        }
        
        if(userObject instanceof File) {
        	try {
	        	determinedMimeType = checkURLForKnownType(  ((File)userObject).toURL() );
				if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;
			}catch(MalformedURLException me) {}				
        }
        
		if(userObject instanceof String) {
			if(((String)userObject).indexOf("<VOTABLE") != -1 &&
			   ((String)userObject).indexOf("</VOTABLE") != -1) {
				determinedMimeType = VOTABLEMIME;
				return true;
			}else {
				try {
					checkURL = new URL((String)userObject);
					determinedMimeType = checkURLForKnownType(checkURL);
					if(determinedMimeType == VOTABLEMIME || determinedMimeType == FITSMIME) return true;
				}catch(MalformedURLException me) {}
			}
		}//if
        return false;
    }
    
    
    private int checkURLForKnownType(URL url) {
    	byte []input = new byte[200];
    	InputStream is = null;
    	try {
	    	is = url.openStream();
	    	is.read(input);
	    	String checkInput = new String(input);
	    	if(checkInput.indexOf("<VOTABLE") != -1) {
	    		return VOTABLEMIME;
	    	}else if(checkInput.indexOf("FITS") != -1) {
	    		return FITSMIME;
	    	}
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	finally {
    		try {
    			if(is != null)
    				is.close();
        	}catch(IOException ioe) {
        		ioe.printStackTrace();
        	}
    	}
    	return -1;
    }
    
    public boolean hasSIA(String xml) {        	
    	if(xml.indexOf("VOX:AccessRef") != -1) {
    		if(!siaAvailable)
    			siaAvailable = true;
			return true;
		}
    	return false;
    }
    
    public boolean hasSIA(StarTable table) {
    	for(int i = 0;i < table.getColumnCount();i++) {
    		if(table.getColumnInfo(i).getUCD().equals("VOX:AccessRef") ||
    		   table.getColumnInfo(i).getName().equals("VOX:AccessRef")) {
    			if(table.getRowCount() > 0) {
    				return true;
    			}
    		}
    	}//for
    	return false;
    }

    public boolean hasSpectra(StarTable table) {
    	for(int i = 0;i < table.getColumnCount();i++) {
    		if(table.getColumnInfo(i).getUCD().equals("DATA_LINK") ||
    		   table.getColumnInfo(i).getName().equals("DATA_LINK")) {
    			if(table.getRowCount() > 0) {
    				return true;
    			}
    		}
    	}//for
    	return false;
    }

    
    public boolean hasSpectra(String xml) {
    	if(xml.indexOf("DATA_LINK") != -1) {
    		if(!spectraAvailable)
    			spectraAvailable = true;
			return true;
		}
    	return false;
    }
    
    public StarTable getStarTable(String votableXML) {
    	try {
	    	ByteArrayInputStream bai = new ByteArrayInputStream(votableXML.getBytes());
	    	StarTable table = (new StarTableFactory()).makeStarTable(bai, new VOTableBuilder());
	    	return table;
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	return null;
    }
        
    public StarTable getStarTable(URL url) {
    	try {
    		return ((new StarTableFactory()).makeStarTable(url));
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	return null;
    }
    
    public URL[] getData(StarTable table, String column) throws IOException {
    	int rowCount = (int)table.getRowCount();
    	URL []urls = new URL[rowCount];
    	int j = 0;
    	for(int i = 0;i < table.getColumnCount();i++) {
    		if(table.getColumnInfo(i).getUCD().equals(column) ||
    		   table.getColumnInfo(i).getName().equals(column)) {
    		    RowSequence rseq = table.getRowSequence();
    		     try {
    		         while ( rseq.next() ) {
    		             Object val = rseq.getCell(i);
    		             if(val instanceof URL) {
    		            	 urls[j] = (URL)val;
    		             }else if(val instanceof String) {
    		            	try {
    		            		 urls[j] = new URL((String)val);
    						}catch(MalformedURLException me) {}    		            		 
    		             }
    		         }
    		         return urls;
    		     }finally {
    		         rseq.close();
    		     }//finally
    		}//if
    	}//for
    	return null;
    }
    
    class ResultListObject {
    	
    	private String key;
    	private URL url;
    	private String listDisplay;
    	public ResultListObject(String key, String xml) {
    		this.key = key;
    		this.url = null;
    		if(key == null) {
    			listDisplay = "VOTABLE (SIA =-" + hasSIA(xml) + " Spectra - " + hasSpectra(xml) + ")";
    		}else {
    			listDisplay = key + " (SIA =-" + hasSIA(xml) + " Spectra - " + hasSpectra(xml) + ")";
    		}
    	}
    	
    	public ResultListObject(String key, URL url) {
    		this.key = key;
    		this.url = url;
    		StarTable table = getStarTable(url);
    		if(key == null) {
    			listDisplay = "VOTABLE (SIA =-" + hasSIA(table) + " Spectra - " + hasSpectra(table) + ")";
    		}else {
    			listDisplay = key + " (SIA =-" + hasSIA(table) + " Spectra - " + hasSpectra(table) + ")";
    		}
    		table = null;
    	}
    	
    	public URL getURL() {
    		return this.url;
    	}
    	
    	public String getKey() {
    		return this.key;
    	}

        public String toString() {
        	return this.listDisplay;
        }    	
    }
    
    private void makeListFromDataObjectForFits(Object dataObject) {
    	Vector vList = new Vector();
		URL checkURL;
    	if(dataObject instanceof String) {
    		try {
	    		checkURL = new URL((String)dataObject);
	    		vList.add(checkURL);
			}catch(MalformedURLException me) {}
    		//later use a StarTable to show image urls.
    	}else if(dataObject instanceof URL) {
    		vList.add( ((URL)dataObject).toString());
    	}else if(dataObject instanceof File) {
    		try {
    			vList.add( ((File)dataObject).toURL());
			}catch(MalformedURLException me) {}    			
    	}else if(dataObject instanceof Map) {
    		Object []vals = ((Map)dataObject).values().toArray();
    		for(int i = 0;i < vals.length;i++) {
    		  if(vals[i] instanceof String) {
    			  try {
	    			  checkURL = new URL((String)vals[i]);
	    			  vList.add(checkURL);
    			  }catch(MalformedURLException me) {}
    		  }else if(vals[i] instanceof URL) {
    			  vList.add((URL)vals[i]);
    		  }
    		}//for
    	}//else
    }
    
            
    private void makeListFromDataObjectForVOTABLE(Object dataObject) {
    	Vector vList = new Vector();
		URL checkURL;
		/*
    	if(dataObject instanceof String) {
    		vList.add(new ResultListObject(null,(String)dataObject));
    		//later use a StarTable to show image urls.
    	}else if(dataObject instanceof URL) {
    		vList.add(new ResultListObject(null,(URL)dataObject));
    	}else if(dataObject instanceof File) {
    		vList.add(new ResultListObject(null,( (File)dataObject.toURL())));
    	}
    	else if(dataObject instanceof Map) {
    	*/
		if(dataObject instanceof Map) {
    		Map mp = (Map)dataObject;
    		Set keys = mp.keySet();
    		Iterator keyIter = keys.iterator();
    		String key;
    		Object temp;
    		Object mapVal;
    		vList.add("All");
    		while(keyIter.hasNext()) {
    			temp = (Object)keyIter.next();
    			if(temp instanceof String) {
    				key = (String)temp;
    				mapVal = mp.get(key);
    				if(mapVal instanceof String) {
    					if(  ((String)mapVal).indexOf("<VOTABLE") != -1) {    				
    						vList.add(new ResultListObject(key,(String)mapVal));
	    				}else{
	    					try {
	    						checkURL = new URL((String)mapVal);
	    						vList.add(new ResultListObject(key,checkURL));
	    					}catch(MalformedURLException me) { }
	    				}
    				}else if(mapVal instanceof URL) {
   						vList.add(new ResultListObject(key,(URL)mapVal));
    				}else if(mapVal instanceof File) {
    					try {
    						vList.add(new ResultListObject(key,((File)mapVal).toURL()));
    					}catch(MalformedURLException me) { }
    				}
    			}
    		}//while
    	}//elseif
    	objList = new JList(vList);
    }
    
    /*
    private Collection makeCollectionFromVOTable(String xml) {
    	
    	
    }
    */    
    
    private void broadcastVOTables(Object []resInfo ,Object []pas) {
    	//pretty much simple if resInfo has a url then use it
    	//if not then Object must be a string of the xml so use that.
    	//pas are objects of PlasticApplication
    	
    	Vector argList = new Vector();
    	String message;
    	StarTable table = null;
    	for(int j = 0;j < resInfo.length;j++) {
    		ResultListObject resultInfo = (ResultListObject)resInfo[j];    	
			if(votableCheck.isSelected()) {
				
				if(resultInfo.getURL() != null) {
					table = getStarTable(resultInfo.getURL());
					argList.add(resultInfo.getURL());
					message = "ivo://votech.org/votable/loadFromURL";
				} else {
					
				    if(dataObj instanceof String) {
				    	argList.add((String)dataObj);
				    }else if(dataObj instanceof Map) {
				    	argList.add((String)((Map)dataObj).get(resultInfo.getKey()));
				    }
					table = getStarTable((String)argList.get(0));
				    argList.add("votableid");
					message = "ivo://votech.org/votable/load";
				}//else
				if(pas == null) {
					try {
					pa.getPlasticRegistry().broadcastAsynch(
							new URI(message),argList);
					}catch(URISyntaxException ue) {}
				}else {
					for(int i = 0;i < pas.length;i++) {
						try {
							((PlasticApplication)pas[i]).sendMessageAsynch(new URI(message),argList);
						}catch(URISyntaxException ue) {}							
					}
				}
			}
			if(table != null && (siaCheck.isSelected() || specCheck.isSelected())) {
				if(siaCheck.isSelected()) {
					try {
						URL []siaURLs = getData(table,"VOX:AccessRef");
						broadcastImages(siaURLs,pas);
					}catch(IOException ioe) {
						ioe.printStackTrace();
					}
				}//if
				if(specCheck.isSelected()) {
					try {
						URL []specURLs = getData(table,"DATA_LINK");
						broadcastImages(specURLs,pas);
					}catch(IOException ioe) {
						ioe.printStackTrace();
					}
						
				}//if
			}//if
    	}//if
    }
    
    private void broadcastImages(Object []urls,Object []pas) {
    	Vector argList = new Vector();
    	for(int j = 0;j < urls.length;j++) {											
			argList.set(0,(URL)urls[j]);
			if(pas == null) {
				try {
				pa.getPlasticRegistry().broadcastAsynch(
					 new URI("ivo://votech.org/images/loadFromURL"),argList);
				}catch(URISyntaxException ue) {}				
			}else {
				for(int i = 0;i < pas.length;i++) {
					try {
						((PlasticApplication)pas[i]).sendMessageAsynch(new URI("ivo://votech.org/images/load"),argList);
					}catch(URISyntaxException ue) {}						
				}
			}//else
		}//for
    }
    
    private Object dataObj;
    
    public JComponent getComponent(RendererRegistry renderers,
				   DataThing dataThing)
	throws RendererException {
   
    dataObj = (Object)dataThing.getDataObject();    
    String []tempMimes = dataThing.getMetadata().getMIMETypes();
    String mimes = new String(tempMimes[0]);
    for(int i = 1;i < tempMimes.length;i++) {
       	mimes += "," + tempMimes[i];
    }
    canHandle(renderers, dataObj,mimes);
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());
	if(dataObj instanceof Map) {
		panel.add(pa.getPlasticList(),BorderLayout.NORTH);
		if(determinedMimeType == VOTABLEMIME)
			makeListFromDataObjectForVOTABLE(dataObj);
		else 
			makeListFromDataObjectForFits(dataObj);
		
		panel.add(objList,BorderLayout.CENTER);
	}else {
		panel.add(pa.getPlasticList(),BorderLayout.CENTER);
	}
	JPanel buttonPanel = new JPanel();
	
	JButton okButton = new JButton("Dispatch");
	//JButton cancelButton = new JButton("Cancel");
	buttonPanel.add(okButton);
	if(spectraAvailable || siaAvailable) {
		votableCheck = new JCheckBox("VOTables",true);
		siaCheck = new JCheckBox("SIA");
		specCheck = new JCheckBox("Spectra");
		buttonPanel.add(new JLabel("Send"));
		buttonPanel.add(votableCheck);
		buttonPanel.add(siaCheck);
		buttonPanel.add(specCheck);
	}
	panel.add(buttonPanel,BorderLayout.SOUTH);
	//buttonPanel.add(cancelButton);
    okButton.addActionListener(this);
    /*
    cancelButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		try {
				browser.openURL(registerURL);
			} catch (ACRException x) {
				// not expected to fail.
				logger.error("Failed to open register link",x);
			}
			//@future close the dialog?
    	}
    });
    */
	
	
	return panel;	
    }
    
    
    public void actionPerformed(ActionEvent e) {
		JList plasticList = pa.getPlasticList();
		URL checkURL;
		Vector argList = new Vector();
		URL testURL;
		Object []plasticBroadcastArray = null;
		Object []objectsToSend = null;
		if(plasticList.getSelectedIndex() == -1) {
			 JOptionPane.showMessageDialog(null, "Incorrect Selection", "Please Select App(s) or All and hit Dispatch", JOptionPane.INFORMATION_MESSAGE);					 
		}else if(plasticList.getSelectedIndex() == 0) {
			plasticBroadcastArray = null;
		}else {
		   plasticBroadcastArray = plasticList.getSelectedValues();	
		}
			//No object list hence the dataObject is not a Map
		    //Meaning it must be some other kind of raw instance value to a
		    //VOTABLE OR FITS IMAGE.
			if(objList == null) {
				objectsToSend = new Object[1];
				if(determinedMimeType == VOTABLEMIME) {
					if(dataObj instanceof String) {
						if(  ((String)dataObj).indexOf("<VOTABLE") != -1) {
							objectsToSend[0] = new ResultListObject(null,(String)dataObj);
						}else {
						   try {
							   checkURL = new URL((String)dataObj);
							   objectsToSend[0] = new ResultListObject(null,checkURL);
						   }catch(MalformedURLException me) {}
						}
					}else if(dataObj instanceof URL) {
						objectsToSend[0] = new ResultListObject(null,(URL)dataObj);
					}else if(dataObj instanceof File) {
						try {
							objectsToSend[0] = new ResultListObject(null,((File)dataObj).toURL());
						}catch(MalformedURLException me) {}							
					}
				}else {
					if(dataObj instanceof String) {
						   try {
							   checkURL = new URL((String)dataObj);
							   objectsToSend[0] = checkURL;
						   }catch(MalformedURLException me) {}
					}else if(dataObj instanceof URL) {
						objectsToSend[0] = (URL)dataObj;
					}else if(dataObj instanceof File) {
						try {
							objectsToSend[0] = ((File)dataObj).toURL();
						}catch(MalformedURLException me) {}
					}
				}
			}else if(objList.getSelectedIndex() == 0) {
			   ListModel model = objList.getModel();
			   objectsToSend = new Object[(model.getSize() -1)];
			   for(int i = 1;i < model.getSize();i++) {
				   objectsToSend[(i-1)] = model.getElementAt(i);   
			   }
			}else {
				objectsToSend = objList.getSelectedValues();
			}
			
			if(determinedMimeType == FITSMIME) {
				broadcastImages(objectsToSend,plasticBroadcastArray);							
			} else {	
				broadcastVOTables(objectsToSend,plasticBroadcastArray);
			}
    } 
    
  
}
