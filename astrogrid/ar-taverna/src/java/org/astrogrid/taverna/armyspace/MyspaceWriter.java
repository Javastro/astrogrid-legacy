package org.astrogrid.taverna.armyspace;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.votable.VOTableBuilder;

import org.apache.log4j.Logger;


public class MyspaceWriter {
	
	private static Logger logger  = Logger.getLogger(MyspaceWriter.class);
	private Object origObject;
	private URI origParentDirecortyURI;
	private Myspace myspace;
	private String resultName;
	private boolean processVOTables;
	private String[] urlCols;
	private String[] nameCols;
	public MyspaceWriter(Myspace myspace) {
		this(myspace,false,null,null);
	}
	
	public MyspaceWriter(Myspace myspace, boolean processVOTables, String []urlCols, String []nameCols) {
		this.myspace = myspace;
		this.processVOTables = processVOTables;
		this.urlCols = urlCols;
		this.nameCols = nameCols;
		savedVOTableURIs = new Vector();
		savedURIs = new Vector();
	}
	
	private Vector savedVOTableURIs;
	private Vector savedURIs;
	
	public Vector writeObject(Object resultValue, URI parentDirectoryURI, String resultName) 
	 throws NotFoundException, InvalidArgumentException, 
     ServiceException, SecurityException, NotApplicableException, IOException {
	  logger.warn("writeObject");
	  processObjectToWrite(resultValue,parentDirectoryURI,resultName);
	  Vector uriVector = new Vector();
	  uriVector.add(savedURIs);	  
	  uriVector.add(savedVOTableURIs);	
	  return uriVector;
	}
	
	
	private void processObjectToWrite(Object resultValue, URI parentDirectoryURI, String resultName) 
    throws NotFoundException, InvalidArgumentException, 
           ServiceException, SecurityException, NotApplicableException, IOException {
	URL checkURL;
	URI fileURI;
	Object  o;
	Collection coll;
	Object []collArray;
	StarTable starTable;
	TableInfo []tabInfo;
	logger.warn("Inside processObjectToWrite parentdirectoryURI = " + parentDirectoryURI.toString() + " and resultName = " + resultName); 
	//SemanticMarkup markup = resultValue.getMetadataForObject(resultValue, false);
	if(resultValue instanceof String) {
		logger.warn("seems to be a string check for url just in case");
		try {
			checkURL = new URL((String)resultValue);
			logger.warn("it is a url so write the url to myspace url = " + checkURL.toString());
			fileURI = myspace.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
			logger.warn("url to copy = " + checkURL.toString() + " to URI in myspace = " + fileURI.toString());
			myspace.copyURLToContent(checkURL, fileURI);
			if(processVOTables) {
				savedVOTableURIs.add(fileURI);
				starTable = getStarTable(checkURL);
				tabInfo = getData(starTable);
				processVOTables = false;
				for(int k = 0;k < tabInfo.length;k++) {
					for(int j = 0;j < tabInfo[k].getURLS().length;j++) {
						logger.warn("in for loop of tabInfo");
						processObjectToWrite(tabInfo[k].getURLS()[j],parentDirectoryURI,resultName + "_" + tabInfo[k].getRowUniqueName());
					}//for
				}//for
			}else {
				logger.warn("adding to vector savedURI the uri = " + fileURI.toString());
				savedURIs.add(fileURI.toString());
			}
			logger.warn("doing a return");
			return;
		}catch(MalformedURLException me) {
			//doesn't matter.
		}
		logger.warn("its a string so write it to myspace");
		fileURI = myspace.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
		logger.warn("writing to myspace uri = " + fileURI.toString() + " contents = " + (String)resultValue);
		myspace.write(fileURI, (String)resultValue);
		logger.warn("processvo table bool2 = " + processVOTables);
		if(processVOTables) {
			savedVOTableURIs.add(fileURI);
			starTable = getStarTable((String)resultValue);
			tabInfo = getData(starTable);
			processVOTables = false;
			for(int k = 0;k < tabInfo.length;k++) {
				for(int j = 0;j < tabInfo[k].getURLS().length;j++) {
					logger.warn("in for loop of tabInfo");
					processObjectToWrite(tabInfo[k].getURLS()[j],parentDirectoryURI,resultName + "_" + tabInfo[k].getRowUniqueName());
				}//for
			}//for
		}else {
			logger.warn("adding to vector savedURI the uri = " + fileURI.toString());
			savedURIs.add(fileURI.toString());
		}		
	}else if(resultValue instanceof URL) {
		logger.warn("seems to be a URL");

		fileURI = myspace.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
		myspace.copyURLToContent((URL)resultValue, fileURI);
		if(processVOTables) {
			savedVOTableURIs.add(fileURI);
			starTable = getStarTable((URL)resultValue);
			tabInfo = getData(starTable);
			processVOTables = false;
			for(int k = 0;k < tabInfo.length;k++) {
				for(int j = 0;j < tabInfo[k].getURLS().length;j++) {
					processObjectToWrite(tabInfo[k].getURLS()[j],parentDirectoryURI,resultName + "_" + tabInfo[k].getRowUniqueName());
				}//for
			}//for
		}else {
			savedURIs.add(fileURI);
		}
	}else if(resultValue instanceof File) {
		logger.warn("seems to be a File");

		fileURI = myspace.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
		try {
			myspace.copyURLToContent((URL)((File)resultValue).toURL(), fileURI);
		}catch(MalformedURLException me ) {}
		if(processVOTables) {
			savedVOTableURIs.add(fileURI);
			processVOTables = false;
			starTable = getStarTable((URL)((File)resultValue).toURL());
			tabInfo = getData(starTable);
			for(int k = 0;k < tabInfo.length;k++) {
				for(int j = 0;j < tabInfo[k].getURLS().length;j++) {
					processObjectToWrite(tabInfo[k].getURLS()[j],parentDirectoryURI,resultName + "_" + tabInfo[k].getRowUniqueName());
				}//for
			}//for
		}else {
			savedURIs.add(fileURI);
		}
	}else if(resultValue instanceof Map) {
		logger.warn("seems to be a Map");

		//some kind of map just keys and values.
		for (Iterator j = ((Map)resultValue).keySet().iterator(); j.hasNext();) {
		   Object resultValueKey = j.next();
		   logger.warn("key for the map = " + resultValueKey.toString());
		   if(resultValueKey instanceof String) {
			   o = ((Map)resultValue).get(resultValueKey);
			   processObjectToWrite(o,parentDirectoryURI,resultName + "_" + (String)resultValueKey);
		   }else {
			   //oh darn even the key is not a string lets just get values and
			   //save it and that is all we can do.
			   coll = ((Map)resultValue).values();
			   collArray = coll.toArray();
			   for(int k = 0;k < collArray.length;k++) {
				   o = collArray[k];						   
				   processObjectToWrite(o,parentDirectoryURI,resultName + "_" + String.valueOf(k));
			   }//for
			   //this will break out of the iterator of the map
			   break;
		   }//else
		}//for
	}//else if map
	
	else if(resultValue instanceof byte[]) {
		logger.warn("it is a byte array try writing binary");
		fileURI = myspace.createChildFile(parentDirectoryURI, resultName + "_" + String.valueOf(System.currentTimeMillis()));
		myspace.writeBinary(fileURI,(byte[])resultValue);
	}
	else if(resultValue instanceof Collection) {
		logger.warn("seems to be a Collection");

		   coll = ((Collection)resultValue);
		   collArray = coll.toArray();
		   logger.warn("size of collection = " + collArray.length);
		   for(int k = 0;k < collArray.length;k++) {
			   o = collArray[k];						   
			   processObjectToWrite(o,parentDirectoryURI,resultName + "_" + String.valueOf(k));						   
		   }//for				
	}else {
		logger.error("can't find class instance so do not know how to write it to myspace printing out string version of it to logs");
		logger.error(resultValue.toString());
	}
	//what else might I need to save.
}
	
    private StarTable getStarTable(String votableXML) {
    	try {
	    	ByteArrayInputStream bai = new ByteArrayInputStream(votableXML.getBytes());
	    	StarTable table = (new StarTableFactory()).makeStarTable(bai, new VOTableBuilder());
	    	return table;
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	return null;
    }
        
    private StarTable getStarTable(URL url) {
    	try {
    		return ((new StarTableFactory()).makeStarTable(url));
    	}catch(IOException ioe) {
    		ioe.printStackTrace();
    	}
    	return null;
    }
    
    private class TableInfo {
    	private URL []votableURLS;
    	private String rowUniqueName;
    	public TableInfo (URL []votableURLS, String rowUniqueName) {
    		this.votableURLS = votableURLS;
    		this.rowUniqueName = rowUniqueName;
    	}
    	
    	public String getRowUniqueName() {
    		return this.rowUniqueName;
    	}
    	
    	public URL[] getURLS() {
    		return this.votableURLS;
    	}
    }
    
    private TableInfo[] getData(StarTable table) throws IOException {
    	int rowCount = (int)table.getRowCount();
    	TableInfo []tableInfo = new TableInfo[rowCount];
    	int j = 0;
    	int []nameColSeq = new int[nameCols.length];
    	int []urlColSeq = new int[urlCols.length];    	

    	for(int k = 0;k < nameCols.length;k++) {
	    	for(int i = 0;i < table.getColumnCount();i++) {
	    		if((table.getColumnInfo(i).getUCD() != null && table.getColumnInfo(i).getUCD().equals(nameCols[k])) ||
	    		   (table.getColumnInfo(i).getName() != null && table.getColumnInfo(i).getName().equals(nameCols[k]))) {
	    			nameColSeq[j] = i;
	    		    j++;
	    		    i = table.getColumnCount();
	    		}//if
	    	}//for
    	}//for
    	j = 0;
    	for(int k = 0;k < urlCols.length;k++) {
	    	for(int i = 0;i < table.getColumnCount();i++) {
	    		if((table.getColumnInfo(i).getUCD() != null && table.getColumnInfo(i).getUCD().equals(urlCols[k])) ||
	    		   (table.getColumnInfo(i).getName() != null && table.getColumnInfo(i).getName().equals(urlCols[k]))) {
	    			urlColSeq[j] = i;
	    		    j++;
	    		    i = table.getColumnCount();
	    		}//if
	    	}//for
    	}//for    	
    	j = 0;
    	
    	RowSequence rseq = table.getRowSequence();
    	Object val;
	     try {
	         while ( rseq.next() ) {
	        	 URL []urls = new URL[urlCols.length];
	        	 String nameString = "";
	        	 for(int k = 0;k < urlColSeq.length;k++) {
		             val = rseq.getCell(urlColSeq[k]);
		             if(val instanceof URL) {
		            	 urls[k] = (URL)val;
		             }else if(val instanceof String) {
		            	try {
		            		 urls[k] = new URL((String)val);
						}catch(MalformedURLException me) {
							
						}
		             }//else
	        	 }//for
	        	 for(int k = 0;k < nameColSeq.length;k++) {
		             val = rseq.getCell(nameColSeq[k]);
		             nameString += (String)val+"_";
	        	 }
	        	 tableInfo[j] = new TableInfo(urls,nameString);
	        	 j++;
	        	 nameString = "";
	         }//while
	     }finally {
	         rseq.close();
	     }//finally  
	     return tableInfo;
    }
	
}