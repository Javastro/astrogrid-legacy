/*
 * @(#)MySpaceFactoryImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.impl;

import org.apache.log4j.Logger;

// import org.astrogrid.datacenter.*;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import org.astrogrid.datacenter.myspace.*;

import java.io.BufferedOutputStream;
// import java.io.BufferedReader;
// import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
// import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class MySpaceFactoryImpl implements MySpaceFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( MySpaceFactoryImpl.class ) ;
		
	private static String
	    MYSPACECACHE_DIRECTORY = "MYSPACE.CACHE_DIRECTORY" ;
		
	private static String
		ASTROGRIDERROR_COULD_NOT_CREATE_ALLOCATION = "AGDTCE00100", 
	    ASTROGRIDERROR_COULD_NOT_DESTROY_COMPRESSED_STREAM = "AGDTCE00110";	
	
	// Compression constants.
	private static final String GZIP_COMPRESSION = "gzip";
	private static final String ZIP_COMPRESSION = "zip";
	
	
    public Allocation allocateCacheSpace( String jobID ) throws AllocationException { 
		if( TRACE_ENABLED ) logger.debug( "allocateCacheSpace(): entry") ;  
		
		Allocation
		    allocation = null ;

		try {
            String
                fileName = MySpaceFactoryImpl.produceFileName( jobID ) ;

            OutputStream
                out = MySpaceFactoryImpl.createCompressedOutputStream( fileName, "uncompressed" ) ;	
                
            allocation = new Allocation( fileName, out ) ;	

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_ALLOCATION, jobID ) ;
			logger.error( message.toString(), ex ) ;
			throw new AllocationException( message, ex );
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "allocateCacheSpace(): exit") ;			
		}    	  	
    	
        return allocation ;
       
    } // end of allocateCacheSpace()
    
    
	public void close( Allocation allocation ) throws AllocationException {
		if( TRACE_ENABLED ) logger.debug( "close(): entry") ; 
				
		try {
			destroyCompressedOutputStream( allocation.getOutputStream() ) ;	
		}
		catch( IOException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_DESTROY_COMPRESSED_STREAM ) ;
			logger.error( message.toString(), ex ) ;
			throw new AllocationException( message, ex );
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "close(): exit") ;			
		}
	
	}// end of close()
    
    
    private static String produceFileName( String jobID ) {
    	
    	StringBuffer
    	    buffer = new StringBuffer( 64 ) ;
    	
		buffer
//JBL Note: .append( "file://" )  // JBL Note: this is a quick fix for AstroGrid iteration 2
		    .append( DatasetAgent.getProperty( MYSPACECACHE_DIRECTORY ) ) 
		    .append( System.getProperty( "file.separator" ) )
		    .append( jobID.replace( ':', '.' ) ) ;
    	
    	return buffer.toString() ;
    	
    } // end of produceFileName()
    
    
	private static OutputStream createCompressedOutputStream( String outputfilename
	                                                        , String compression ) 
	                        throws IOException, FileNotFoundException {
	                               	
		OutputStream out =
		  new BufferedOutputStream(
			new FileOutputStream(outputfilename));
    
		if (compression.equalsIgnoreCase(MySpaceFactoryImpl.GZIP_COMPRESSION)) {
		  out = new GZIPOutputStream(out);
		}
		else if (compression.equalsIgnoreCase(MySpaceFactoryImpl.ZIP_COMPRESSION)) {
		  String internalfilename = "tmp.xml";
      
		  int index = outputfilename.indexOf('.');
		  if (index > -1) {
			internalfilename = outputfilename.substring(0, index) + ".xml";
		  }
      
		  ZipOutputStream zipOut = new ZipOutputStream(out);
		  zipOut.putNextEntry(new ZipEntry(internalfilename));
		  out = zipOut;
		}
    
		return out;
		
	}
	  
	  
	private static void destroyCompressedOutputStream( OutputStream compressedOutputStream ) throws IOException {
		
		if (compressedOutputStream instanceof ZipOutputStream) {
		  ZipOutputStream zipOutputStream = (ZipOutputStream) compressedOutputStream;
		  zipOutputStream.closeEntry();
		}
		
	}
    
    
} // end of class MySpaceFactoryImpl
