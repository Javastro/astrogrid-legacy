/*$Id: MySpaceHelper.java,v 1.1 2003/08/22 15:49:48 nw Exp $
 * Created on 22-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.astrogrid.datacenter.myspace.AllocationException;
import org.astrogrid.datacenter.myspace.MySpaceManagerResponseDD;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** Class to handle all the xml processing associated with talking to MySpace.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
 *
 */
public class MySpaceHelper {

    public static final  String MYSPACE_SUCCESS = "success" ;
    private String errorDetails;
    
    private String status;

    /** call the mySpace service with an 'upload' operation
     * 
     * @param addr service end point
     * @param doc input document
     * @return response xml document.
     * @throws ServiceException
     * @throws RemoteException
     */
        public String doMySpaceCall(URL addr, String doc)
            throws ServiceException, RemoteException {
            String mySpaceResponse;
            Call call = (Call) new Service().createCall();
            call.setTargetEndpointAddress(addr);
            call.setOperationName("upLoad"); // Set method to invoke        
            call.addParameter(
                "jobDetails",
                XMLType.XSD_STRING,
                ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);
            mySpaceResponse = (String) call.invoke(new Object[]{doc});
            return mySpaceResponse;
        }
    
    /**
     *  Inspects a response document to find the return code. determines whetehr the operation
     * has been a succcess or failure.
     * <p>
     * populates status and ErrorDetails methods.
     * FUTURE - could this be more cleanly implemented using XPATH?
    * an error returned by MySpace.
     * @param responseXML
     * @return
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws AllocationException
     */
    public boolean checkResponse(String responseXML)
        throws
            FactoryConfigurationError,
            ParserConfigurationException,
            SAXException,
            IOException,
            AllocationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        
           DocumentBuilder builder = factory.newDocumentBuilder();   
           InputSource responseSource = new InputSource( new StringReader( responseXML ) );
          Document responseDoc = builder.parse( responseSource );		
        	
           NodeList nodeList1 = responseDoc.getChildNodes() ;
        	  
           Element element = null ;
        	   
           // Focus on the results element...   
           for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
        		if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE ){
        			continue ;						
                }
        		element = (Element) nodeList1.item(i) ;				   			
        		if( element.getTagName().equals( MySpaceManagerResponseDD.RESULTS_ELEMENT ) ){ 
        			break ;
                }
           } // end for	
        
           nodeList1 = element.getChildNodes() ;			
        
           // Focus on top level status element...   
           for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
        	   if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE ){
        		   continue ;						
               }
        	   element = (Element) nodeList1.item(i) ;		   				   			
        	   if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_01 ) ){ 
        		   break ;
               }
           } // end for	
        
           NodeList nodeList2 = element.getChildNodes() ;	
        	 
           // Focus on second level status element... 
           for( int i=0 ; i < nodeList2.getLength() ; i++ ) {
        		if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE ){
        			continue ;						
                }
        		element = (Element) nodeList2.item(i) ;								   			
        		if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_02 ) ){ 
        			break ;
                }
           } // end for	
        
        setStatus( element.getFirstChild().getNodeValue());
           // If it is not a success, we also need the reason 
           // (the value of the details element)...
           boolean returncode = true;
           if( !status.equalsIgnoreCase(MYSPACE_SUCCESS ) ) {	   			   	    
             setErrorDetails(fiindFailureDetails(element, nodeList2));
                returncode = false;
           }
           return returncode;
		      			              
    }


    /** we've seen a failure - find the details of it. */
    private String fiindFailureDetails(Element e,NodeList nodeList2) {
            Element element = e;
           for( int i=0 ; i < nodeList2.getLength() ; i++ ) {		   	
        		  if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE ) {
        			  continue ;						
                  }
        		  element = (Element) nodeList2.item(i) ;				   			
        		  if( element.getTagName().equals( MySpaceManagerResponseDD.DETAILS_ELEMENT_02 ) ){ 
        			  break ;
                  }
        	   } // end for	
        
        	   String details = element.getFirstChild().getNodeValue() ;
        	   if (details != null) {
        	         details = details.trim() ;
               }
        return details;
    }

    private void setErrorDetails(String details) {
        this.errorDetails = details;
    }

    /** text desciption of the error details, if one occurred,
     * otherwise null 
     */
    public String getErrorDetails() {
        return errorDetails;
    }

    private void setStatus(String status) {
        this.status = status;
        if (this.status != null) {
            this.status.trim();
        }
    }
    /** status code / text returned by myspace
     * for success, should be equal to {@link #MYSPACE_SUCCESS} */
    public String getStatus() {
        return status;
    }

}


/* 
$Log: MySpaceHelper.java,v $
Revision 1.1  2003/08/22 15:49:48  nw
refactored MySpace module - replaced Allocation class by
an interface and implementaiton class, calling to a helper class.
simplified interface and implementation of MySpaceFactory.
 
*/