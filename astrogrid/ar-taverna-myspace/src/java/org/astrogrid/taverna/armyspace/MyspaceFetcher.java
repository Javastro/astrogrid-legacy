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
import java.util.LinkedHashMap;

import org.astrogrid.acr.astrogrid.NodeInformation;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;

import org.apache.log4j.Logger;


public class MyspaceFetcher {
	
	private static Logger logger = Logger.getLogger(MyspaceWriter.class);
	private Myspace myspace;
	public MyspaceFetcher(Myspace myspace) {
		this.myspace = myspace;
	}
	
	
	private Map resultMap;
	
	public Map fetchObjects(String directoryURI, boolean recurseDirectories, boolean discoverStringsOnly, boolean discoverVOTableOnly) 
	 throws NotFoundException, InvalidArgumentException, 
     ServiceException, SecurityException, NotApplicableException, IOException, java.net.URISyntaxException {
	
	  resultMap = new LinkedHashMap();
	  processFetchObjects(new URI(directoryURI),recurseDirectories,discoverStringsOnly, discoverVOTableOnly);
	  logger.warn("fetched things from method fetchObjects final map = " + resultMap.size());
	  return resultMap;
	}
	
	
	private void processFetchObjects(URI directoryURI, boolean recurseDirectories, boolean discoverStringsOnly, boolean discoverVOTableOnly) 
    throws NotFoundException, InvalidArgumentException, 
           ServiceException, SecurityException, NotApplicableException, IOException {
	Iterator keyIter;
	Iterator attrIter;
	String temp;
	logger.warn("Inside processFetchObjects");
	NodeInformation []nodeInfos;
	NodeInformation checkNodeInfo = myspace.getNodeInformation(directoryURI);
	if(checkNodeInfo.isFolder()) {
		nodeInfos = myspace.listNodeInformation(directoryURI);
	}else {
		nodeInfos = new NodeInformation[1];
		nodeInfos[0] =  checkNodeInfo;
	}
	
		for(int i = 0;i < nodeInfos.length;i++) {
			NodeInformation nodeInfo = nodeInfos[i];
			if(nodeInfo.isFolder()) {
				processFetchObjects(nodeInfo.getId(),recurseDirectories,discoverStringsOnly, discoverVOTableOnly);
			}else if(nodeInfo.isFile()) {
				if(!discoverStringsOnly && !discoverVOTableOnly) {
					//great user just wants to read everything found as binary.
					resultMap.put(nodeInfo.getName() + "-" + nodeInfo.getId().toString(), myspace.readBinary(nodeInfo.getId()));	
				}else if(!discoverStringsOnly && !discoverVOTableOnly) {
					byte []binContents = myspace.readBinary(nodeInfo.getId());
					temp = new String(binContents,0,300);
					if(temp.indexOf("VOTABLE") != -1) {
						resultMap.put(nodeInfo.getName() + "-" + nodeInfo.getId(),binContents);
					}
				}else if(discoverStringsOnly && !discoverVOTableOnly) {
					attrIter = nodeInfo.getAttributes().keySet().iterator();
					while(attrIter.hasNext()) {
						temp = (String)attrIter.next();
						logger.warn("Key for a map attr = " + temp + " and val = " + nodeInfo.getAttributes().get(temp));
					}
					//resultMap.put(nodeInfo.getName() + "-" + nodeInfo.getId().toString(), myspace.read(nodeInfo.getId()));
				}else {
					//bummer need to get as a string and check if it is a VOTable.
					attrIter = nodeInfo.getAttributes().keySet().iterator();
					while(attrIter.hasNext()) {
						temp = (String)attrIter.next();
						logger.warn("Key for a map attr = " + temp + " and val = " + nodeInfo.getAttributes().get(temp));
					}					
					String content = "";//myspace.read(nodeInfo.getId());
					if(content.indexOf("VOTABLE") != -1) {
						//resultMap.put(nodeInfo.getName() + "-" + nodeInfo.getId().toString(),content);
					}//if
				}
			}//elseif
		}//for
	}
		
}