/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/stress/src/junit/org/astrogrid/filestore/stress/DataOverloadTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/09/06 12:45:22 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: DataOverloadTestCase.java,v $
 *   Revision 1.2  2005/09/06 12:45:22  clq2
 *   dave-dev-200507251101
 *
 *   Revision 1.1.2.2  2005/08/08 10:59:43  dave
 *   Added OutOfMemory catch to the OutputStream.
 *   Not ideal, as it may not trap the error in time.
 *   Needs more work on this ...
 *
 *   Revision 1.1.2.1  2005/08/04 12:29:52  dave
 *   Added large data (1Gbyte) stress test.
 *   Added JDK 1.5 fix to FileStoreOutputStream.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.stress ;

import java.lang.reflect.Method ;
import java.net.URL ;
import java.net.URLConnection ;
import java.net.HttpURLConnection ;

import java.io.OutputStream ;
import java.io.BufferedWriter ;
import java.io.OutputStreamWriter ;
import java.io.BufferedOutputStream;

import junit.framework.TestCase ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.FileStoreOutputStream ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filestore.client.FileStoreSoapDelegate ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;
import org.astrogrid.filestore.common.transfer.UrlGetRequest ;
import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;
import org.astrogrid.filestore.common.transfer.UrlPutTransfer ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

/**
 * A JUnit test case to try and overload a filestore with data.
 *
 */
public class DataOverloadTestCase
	extends TestCase
	{
	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Create our test targets.
		this.target = new FileStoreSoapDelegate(
			"http://localhost:8080/astrogrid-filestore-one-SNAPSHOT/services/FileStore"
			) ;
		}

    /**
     * A reference to our target service.
     *
     */
    protected FileStore target ;

	/**
	 * Test we can call our target service.
	 *
	 */
	public void testConnect()
		throws Exception
		{
		//
		// Import the test string.
		FileProperties properties = new FileProperties(
			target.importString(
				null,
				"A test string ..."
				)
			) ;		
		}

    /**
     * Our default buffer size.
     *
     */
    public static final int BUFFER_SIZE = 1024 ;

    /**
     * Test a HTTP URL connection.
	 * This is only here for reference, the code has now been added to FileStoreOutputStream.
     *
    public void testUrlConnector()
        throws Exception
        {
        //
        // Initiate our transfer.
        TransferProperties transfer = target.importInit(
            new UrlPutTransfer()
            ) ;
		//
		// Get the import URL.
		URL url = new URL(
			transfer.getLocation()
			);
		//
		// Create our http connection.
		HttpURLConnection http = (HttpURLConnection) url.openConnection() ;
		http.setAllowUserInteraction(false);
		http.setDoInput(true);
		http.setDoOutput(true) ;
		http.setUseCaches(false);
		http.setRequestMethod("PUT");
		http.setRequestProperty("User-Agent", this.getClass().getName());
		//
		// JDK 1.5 only.
		// http.setChunkedStreamingMode(1024);

		//
		// JDK 1.4 patch.
		Class clazz = http.getClass() ;
		try {
System.out.println("");
System.out.println("Checking for extra method ....");
			Method method = clazz.getMethod(
				"setChunkedStreamingMode",
				new Class[] { int.class }
				);
System.out.println("Yep, method supported :-)");
System.out.println("Trying to call method ....");
			method.invoke(
				http,
				new Object[] { new Integer(1024) }
				);
System.out.println("Ok, done method call ....");
			}
		catch (NoSuchMethodException ouch)
			{
			//
			// ... Ok, we tried ...
System.out.println("Nope, method not supported :-(");
			//
			}

		http.connect();
		//
		// Try to overload the stream.
		overload(
			http.getOutputStream()
			);
		}
     */

    /**
     * See how long it takes to overload the target.
     *
     */
    public void testFileStoreStream()
        throws Exception
        {
        //
        // Initiate our transfer.
        TransferProperties transfer = target.importInit(
            new UrlPutTransfer()
            ) ;
        //
        // Transfer our data.
        FileStoreOutputStream stream = new FileStoreOutputStream(
            transfer.getLocation()
            ) ;
		stream.open();
		//
		// Try to overload the stream.
		try {
			overload(stream);
			}
		catch (Error ouch)
			{
System.out.println("");
System.out.println("Ouch !!");
			stream.close();
			ouch.printStackTrace();
			throw ouch;
			}
        }

	/**
	 * The loop size.
	 *
	 */
	public static final int LOOP_SIZE = 100 ;

    /**
     * See how long it takes to overload a connection.
     * With LOOP_SIZE = 100, this sends approx 1Gbyte of data to the stream.
     *
     */
    public void overload(OutputStream stream)
        throws Exception
        {
		//
		// Wrap our stream in a writer.
		BufferedWriter writer = new BufferedWriter(
			new OutputStreamWriter(
				stream
				)
			);
		//
		// Send test data ....
		for (int i = 0 ; i < LOOP_SIZE ; i++)
			{
			for (int j = 0 ; j < LOOP_SIZE ; j++)
				{
				for (int k = 0 ; k < LOOP_SIZE ; k++)
					{
					for (int l = 0 ; l < LOOP_SIZE ; l++)
						{
						StringBuffer buffer = new StringBuffer() ;
						buffer.append(i) ;
						buffer.append(" ") ;
						buffer.append(j) ;
						buffer.append(" ") ;
						buffer.append(k) ;
						buffer.append(" ") ;
						buffer.append(l) ;

				        writer.write(
				            buffer.toString(),
							0,
							buffer.length()
				            ) ;
						writer.newLine();
						}
					}
				}
			}
        writer.close() ;
        }
	}
