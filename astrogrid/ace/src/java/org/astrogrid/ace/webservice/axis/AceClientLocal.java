//-------------------------------------------------------------------------
// FILE: AceClientLocal.java
// PACKAGE: org.astrogrid.ace.webservice.axis
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 12/11/02   KEA       Initial prototype
//
//
// NOTE: This class is developed for use with Apache Axis 1.0 RC1.
//-------------------------------------------------------------------------

package org.astrogrid.ace.webservice.axis;

import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;

import java.io.File;
import java.io.IOException;

public class AceClientLocal extends AceClient
{
	public static void main(String [] args) 
	{
		try 
		{
			// Check arguments
			// 
			if (args == null || args.length != 1) {
				System.err.println(
					"\nUsage: AceClientLocal <aceInputFilename.xml>\n");
				return;
			}

			// Make sure input file exists
			//
			File f = new File(args[0]);
			{
				if ( !(f.exists()) ) 
				{
					throw new IOException("Can't find input file " + args[0]);
				}
			}

			System.out.println("Calling service...");
			SOAPBodyElement resultDoc = invokeAceService(args[0]);
			System.out.println("...got results doc");

			if (resultDoc != null)
			{
				System.out.println(XMLUtils.ElementToString(resultDoc.getAsDOM()));
			}
			else 
			{
				System.err.println("Unknown error encountered, terminating.");
			}
		}
		catch (Exception e)
		{
			System.err.println("Encountered a service exception:\n" + e);
		}
	}
}
//-------------------------------------------------------------------------
