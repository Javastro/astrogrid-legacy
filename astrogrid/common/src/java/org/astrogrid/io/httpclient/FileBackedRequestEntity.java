/*
 * $Id: FileBackedRequestEntity.java,v 1.2 2008/09/17 08:16:06 pah Exp $
 * 
 * Created on Nov 10, 2006 by Paul Harrison (pharriso@eso.org)
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

import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * @TODO actually implement as a workaround until NGAS becomes properly http 1.1 compliant
 * @author Paul Harrison (pharriso@eso.org) Nov 10, 2006
 * @version $Name:  $
 * @since VOTech Stage 4
 */
public class FileBackedRequestEntity implements RequestEntity {

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.methods.RequestEntity#getContentLength()
	 */
	public long getContentLength() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"FileBackedRequestEntity.getContentLength() not implemented");
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.methods.RequestEntity#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"FileBackedRequestEntity.getContentType() not implemented");
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.methods.RequestEntity#isRepeatable()
	 */
	public boolean isRepeatable() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"FileBackedRequestEntity.isRepeatable() not implemented");
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.methods.RequestEntity#writeRequest(java.io.OutputStream)
	 */
	public void writeRequest(OutputStream arg0) throws IOException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(
				"FileBackedRequestEntity.writeRequest() not implemented");
	}

}


/*
 * $Log: FileBackedRequestEntity.java,v $
 * Revision 1.2  2008/09/17 08:16:06  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.1  2007/01/10 10:34:37  pharriso
 * entity requester for http client
 *
 */
