/*$Id: URIConverter.java,v 1.7 2008/12/10 21:02:09 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.converters;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/** convets strings to URI
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Mar-2005
 *
 */
public class URIConverter implements Converter {

  

    /**
     * @throws URISyntaxException
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(final Class arg0, final Object arg1) {
        if (arg0 != URI.class) {
            throw new ConversionException("Can only convert to URI" + arg0.getName());
        }      
        if (arg0 == null) {
        	throw new ConversionException("Null argument");
        }
        try {
            return new URI(arg1.toString().trim());
        } catch (final URISyntaxException e) {
            throw new ConversionException("Cannot convert " + arg1 + " to URI");
        }
    }
    
  
}


/* 
$Log: URIConverter.java,v $
Revision 1.7  2008/12/10 21:02:09  nw
Complete - taskadd input convertor to produce Date
altered other input convertors to throw correct exception.

Revision 1.6  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.5  2006/07/19 15:48:15  nw
improved error capture.

Revision 1.4  2006/06/15 10:07:18  nw
improvements coming from unit testingadded new convertors.

Revision 1.3  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.2.66.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.66.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/