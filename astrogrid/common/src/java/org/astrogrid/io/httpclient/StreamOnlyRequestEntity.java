/*
 * $Id: StreamOnlyRequestEntity.java,v 1.2 2008/09/17 08:16:06 pah Exp $
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.astrogrid.io.mime.MimeType;

public final class StreamOnlyRequestEntity implements RequestEntity {
	private final InputStream content;

	public StreamOnlyRequestEntity(InputStream content) {
		this.content = content;
	}

	public void writeRequest(OutputStream out) throws IOException {
		BufferedInputStream in = new BufferedInputStream(content); // IMPL is this necessary
	    try {
	        int l;
	        byte[] buffer = new byte[8192];
	        while ((l = in.read(buffer)) != -1) {
	            out.write(buffer, 0, l);
	        }
	        out.flush();
	    } finally {
	        in.close();
		
	}
	}

	public boolean isRepeatable() {
		return false;
	}

	public String getContentType() {
		return MimeType.unspecified_mime_type;
	}

	public long getContentLength() {
		return -1; // do not know
	}
}

/*
 * $Log: StreamOnlyRequestEntity.java,v $
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
