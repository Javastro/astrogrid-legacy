//-------------------------------------------------------------------------
// FILE: AceClient.java
// PACKAGE: org.astrogrid.ace.webservice.axis
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 11/11/02   KEA       Initial prototype
//
//
// NOTE: This class is developed for use with Apache Axis 1.0 RC1.
//-------------------------------------------------------------------------


package org.astrogrid.ace.webservice.axis;

import org.w3c.dom.Element;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;


public class AceClient 
{

	// Where is the service located?
	//
	protected static final String ENDPOINT_URL = 
			"http://localhost:8080/axis/services/AceService";

			//"http://astrogrid.ast.cam.ac.uk:8080/axis/services/AceService";


	protected static SOAPBodyElement invokeAceService(String AceInputXmlFile)
				throws Exception
	{
		System.out.println("Starting invokeAceService...");
		try 
		{
			// Set up the infrastructure to contact the service
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(ENDPOINT_URL));

			// Set up the input message for the service
			// First, create an empty message body
			SOAPBodyElement[] requestSBElts = new SOAPBodyElement[1];

			// Now get the data, read it into a document and put the document
			// into the message body
			File paramFile = new File(AceInputXmlFile);
			FileInputStream fis = new FileInputStream(paramFile);
			requestSBElts[0] = 
				new SOAPBodyElement(XMLUtils.newDocument(fis).getDocumentElement());

			// Make the call to the service
			System.out.println("Making actual call...");
			Vector resultSBElts = (Vector) call.invoke(requestSBElts);
			System.out.println("...finished actual call.");

			// Get the response message, extract the return document 
			// from the message body and return it.
			//
			System.out.println("Returning response...");
			System.out.println("Number of response elts is " +
					Integer.toString(resultSBElts.size()));


			return (SOAPBodyElement)resultSBElts.get(0);
		}
		catch (AxisFault fault) {
			System.err.println("\nRECEIVED A FAULT FROM THE SERVICE:");
			System.err.println("Fault actor:   " + fault.getFaultActor());
			System.err.println("Fault code:    " + fault.getFaultCode());
			System.err.println("Fault string:\n" + fault.getFaultString());
			System.err.println("\nSTACK TRACE:");
			fault.printStackTrace();

			return null;
		}
	}

	protected static File getCheckedInputFile(String fileName) throws IOException
	{
		File f = new File(fileName);
		{
			if ( !(f.exists()) )
			{
				throw new IOException("Can't find input file " + fileName);
         }
		}
		return f;
	}
}
//-------------------------------------------------------------------------
