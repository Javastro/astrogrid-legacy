/*$Id: SubmitJobRequestImpl.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 11-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/** Implementation of a submit job request.
 * <p />
 * little bean around a workflow object.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class SubmitJobRequestImpl implements SubmitJobRequest {
    /** Construct a new SubmitJobRequestImpl
     * 
     */
    
    public SubmitJobRequestImpl(File f) throws MarshalException, ValidationException, FileNotFoundException {
        this(new FileReader(f));
    }
    
    public SubmitJobRequestImpl(InputStream is) throws MarshalException, ValidationException {
        this(new InputStreamReader(is));
    }
    
    
    public SubmitJobRequestImpl(String requestDoc) throws MarshalException, ValidationException {
        this(new StringReader(requestDoc));
        
    }
    
    public SubmitJobRequestImpl(Reader reader) throws MarshalException, ValidationException {
        workflow = Workflow.unmarshalWorkflow(reader);
    }
    
    protected final Workflow workflow;
    public Workflow getWorkflow() {
        return workflow;
    }
}


/* 
$Log: SubmitJobRequestImpl.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.4  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.3  2004/02/12 15:41:24  nw
added extra constructors for testing

Revision 1.1.2.2  2004/02/12 12:54:47  nw
worked in inversion of control pattern - basically means that
components have to be assembled, rather than self-configuring
from properties in config files. so easier to test each component in isolation

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/