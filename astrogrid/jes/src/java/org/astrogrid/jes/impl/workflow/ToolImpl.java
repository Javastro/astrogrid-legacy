/*$Id: ToolImpl.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.jes.job.ConversionException;
import org.astrogrid.jes.job.Tool;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringWriter;

/** Implemenation of a Tool as a thin wrapper around {@link org.astrogrid.workflow.beans.v1.Tool}
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class ToolImpl implements Tool {
    /** Construct a new ToolImpl
     * 
     */
    public ToolImpl(org.astrogrid.workflow.beans.v1.Tool t) {
        this.tool = t;
    }
    protected org.astrogrid.workflow.beans.v1.Tool tool;
    /**
     * @see org.astrogrid.jes.job.Tool#getName()
     */
    public String getName() {
        return tool.getName();
    }
    /**
     * @see org.astrogrid.jes.job.Tool#toJESXMLString()
     * @todo implement this method
     */
    public String toXML() throws ConversionException {
        try {
        StringWriter sw = new StringWriter();
       tool.marshal(sw);
       sw.close();
       return sw.toString();
        } catch (CastorException e) {
            throw new ConversionException("Could not marshall tool to XML",e);
        } catch (IOException e) {
            throw new ConversionException("Could not marshall too to XML",e);
        }
    }
}


/* 
$Log: ToolImpl.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/27 00:24:23  nw
tidied interface names

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 01:14:01  nw
castor implementation of jes object model
 
*/