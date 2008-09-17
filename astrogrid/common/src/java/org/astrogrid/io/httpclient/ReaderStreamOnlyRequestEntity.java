/*
 * $Id: ReaderStreamOnlyRequestEntity.java,v 1.2 2008/09/17 08:16:06 pah Exp $
 * 
 * Created on Nov 8, 2006 by Paul Harrison (pharriso@eso.org)
 * Copyright 2006 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.io.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.CharBuffer;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.astrogrid.io.mime.MimeType;


public class ReaderStreamOnlyRequestEntity implements RequestEntity {

	private Reader in;

	public ReaderStreamOnlyRequestEntity(Reader inStream) {
		this.in = inStream;
	}

	public long getContentLength() {
		return -1; //do not know
	}

	public String getContentType() {
		return MimeType.unspecified_mime_type;
	}

	public boolean isRepeatable() {
		return false;
	}

	public void writeRequest(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub
		throw new  UnsupportedOperationException("ReaderStreamOnlyRequestEntity.writeRequest() not implemented");

/* FIXME	need to find out the best way to do this
		try{
	        int l;
	        byte[] buffer = new byte[8192];
	        CharBuffer target= new CharBuffer;
			int i = in.read(target);
			
		}
		finally {
			in.close();
		}
	
*/
	}

}


/*
 * $Log: ReaderStreamOnlyRequestEntity.java,v $
 * Revision 1.2  2008/09/17 08:16:06  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:14  pah
 * safety checkin before interop
 *
 * Revision 1.1  2006/11/10 07:09:40  pharriso
 * attempt to get streaming only request entities for httpclient
 *
 */
