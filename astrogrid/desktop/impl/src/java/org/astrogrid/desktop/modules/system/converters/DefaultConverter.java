/*$Id: DefaultConverter.java,v 1.8 2008/12/10 21:02:09 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Implementation of the converter interface using commons beansutils convertUtils, and previously registered convertors.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 01-Feb-2005
 *
 */
public class DefaultConverter implements Converter {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DefaultConverter.class);

    /** Construct a new ConvertUtilsTransformer
     * 
     */
    public DefaultConverter() {
        super(); 
    }


    /**
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(final Class c, final Object o) {
        if (c.isAssignableFrom(o.getClass())) { // no need to change.
            return o;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(c.getName() + " " + o.getClass().getName());
        }
        final Converter lookup = ConvertUtils.lookup(c);
        if (lookup == null) {
        	logger.error("No Convertor able to produce " + c.getName());
        	throw new ConversionException("No convertor to produce " + c.getName());
        }
        return lookup.convert(c,o);
    }

}


/* 
$Log: DefaultConverter.java,v $
Revision 1.8  2008/12/10 21:02:09  nw
Complete - taskadd input convertor to produce Date
altered other input convertors to throw correct exception.

Revision 1.7  2008/11/04 14:35:54  nw
javadoc polishing

Revision 1.6  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.5  2006/06/15 10:07:18  nw
improvements coming from unit testingadded new convertors.

Revision 1.4  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.3.30.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3.30.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.3  2005/11/29 11:27:55  nw
refactored converters

Revision 1.2  2005/11/23 19:15:30  jdt
Extruded plastic.

Revision 1.1.16.2  2005/11/23 17:34:57  jdt
added a note to Noel

Revision 1.1.16.1  2005/11/20 18:30:45  jdt
Added a converter for Strings to Vectors, but it needs putting in the right place.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.2  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/