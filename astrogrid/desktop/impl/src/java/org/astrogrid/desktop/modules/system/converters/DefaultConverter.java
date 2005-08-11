/*$Id: DefaultConverter.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Default implementation of the converter interface - uses convertUtils.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
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
    private DefaultConverter() {
        super();
    }

    
    public static Converter getInstance() {
        return theInstance;
    }
    
    private static final DefaultConverter theInstance = new DefaultConverter();

    /**
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    public Object convert(Class c, Object o) {
        if (c.isAssignableFrom(o.getClass())) { // no need to change.
            return o;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(c.getName() + " " + o.getClass().getName());
        }
        return ConvertUtils.lookup(c).convert(c,o);
    }

}


/* 
$Log: DefaultConverter.java,v $
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