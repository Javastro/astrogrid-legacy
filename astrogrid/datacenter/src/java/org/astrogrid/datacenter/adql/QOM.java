/*$Id: QOM.java,v 1.1 2003/08/28 15:27:54 nw Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;

import java.io.IOException;
import java.io.Writer;

import org.exolab.castor.xml.CastorException;
import org.xml.sax.ContentHandler;

/** interface that describes commonality between all classes in the  Query Object Model.
 * At present this is just the marshalling / unmarshalling to XML routines. - 
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public interface QOM {
    /**
     * wrapper around {@link #validate}
     * @return true if <tt>validate()</tt> succeeds, false if it throws a <tt>CastorException</tt>
     * @see #validate
     */
    public boolean isValid() ;
    /**
     *  write xml representation out to a writer
     * @param w the writer to output to
     * @throws CastorException on error
     */
    public void marshal(Writer w) throws CastorException;
    /**
     * write xml representation out to a content handler
     * @param c handler to write out to
     * @throws CastorException on error
     */
    public void marshal(ContentHandler c) throws CastorException, IOException;
    /** 
     * verify this query object would generate an xml representation valid against the ADQL xml schema
     * @throws CastorException if validation fails.
     */
    public void validate() throws CastorException;
    
    
}


/* 
$Log: QOM.java,v $
Revision 1.1  2003/08/28 15:27:54  nw
added ADQL object model.
 
*/