/*$Id: CastorBeanUtilsConvertor.java,v 1.1 2005/02/22 01:10:31 nw Exp $
 * Created on 03-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service.conversion;

import org.apache.commons.beanutils.Converter;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.StringReader;

/**
 * New converter in the BeanUtils framework to unmarshall documents
 * into castor objects.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Feb-2005
 *
 */
public final class CastorBeanUtilsConvertor implements Converter {
    public Object convert(Class arg0, Object arg1) {
        
        try {
            return Unmarshaller.unmarshal(arg0,new StringReader(arg1.toString()));
        } catch (MarshalException e) {
            throw new RuntimeException("Failed to unmarshall from " + arg1.toString(),e);
        } catch (ValidationException e) {
            throw new RuntimeException("Document to unmarshall was not valid " + arg1.toString(),e);
        }
        
    }
    
    private static final Converter theInstance = new CastorBeanUtilsConvertor();
    public static final Converter getInstance() {
        return theInstance;
    }
}

/* 
$Log: CastorBeanUtilsConvertor.java,v $
Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/