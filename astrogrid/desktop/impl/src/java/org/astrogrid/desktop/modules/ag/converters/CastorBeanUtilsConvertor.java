/*$Id: CastorBeanUtilsConvertor.java,v 1.5 2008/12/10 21:02:09 nw Exp $
 * Created on 03-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.converters;

import java.io.StringReader;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Convertor for the  BeanUtils framework to unmarshall DOM Documents
 * into castor objects.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Feb-2005
 *
 */
public final class CastorBeanUtilsConvertor implements Converter {
    public Object convert(final Class arg0, final Object arg1) {
        
        try {
            return Unmarshaller.unmarshal(arg0,new StringReader(arg1.toString()));
        } catch (final MarshalException e) {
            throw new ConversionException("Failed to unmarshall from " + arg1.toString(),e);
        } catch (final ValidationException e) {
            throw new ConversionException("Document to unmarshall was not valid " + arg1.toString(),e);
        }
        
    }

}

/* 
$Log: CastorBeanUtilsConvertor.java,v $
Revision 1.5  2008/12/10 21:02:09  nw
Complete - taskadd input convertor to produce Date
altered other input convertors to throw correct exception.

Revision 1.4  2008/11/04 14:35:53  nw
javadoc polishing

Revision 1.3  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:02  nw
finished code.extruded plastic hub.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/