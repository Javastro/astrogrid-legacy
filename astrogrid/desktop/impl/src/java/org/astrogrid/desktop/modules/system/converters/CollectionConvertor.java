/*$Id$
 * Created on 29-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convert a comma-delimited string into a Collection
 * @author John Taylor
 *
 */
public class CollectionConvertor implements Converter {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CollectionConvertor.class);

    public Object convert(Class arg0, Object arg1) {
        Collection result;
        Class resultType = arg0;
        if (!Collection.class.isAssignableFrom(arg0)) {
        	throw new IllegalArgumentException("Can only convert to collections: " + arg0.getName());
        }
        //Need to choose default implementations if 
        //we're not given one
        if (arg0.isInterface()) {
            if (List.class.isAssignableFrom(arg0)) {
                resultType = ArrayList.class;
            } else if (Set.class.isAssignableFrom(arg0)) {
            	//nww - surely HashSet??
                //resultType = HashMap.class;
            	resultType = HashSet.class;
            } else if (Collection.class.isAssignableFrom(arg0)) { // fallback position.
            	resultType = ArrayList.class;
            }
            //It could also be something obscure, but we'll
            //let it blow up with a runtimeexception later on
            //if so.
            
        }
        
        try {
            result = (Collection) resultType.newInstance();
        } catch (InstantiationException e) {
            logger.error("Could not convert object ",e);
            throw new IllegalArgumentException("Can't create collection type: " + arg0.getName());
        } catch (IllegalAccessException e) {
            logger.error("Could not convert object ",e);
            throw new IllegalArgumentException("Can't create collection type : " + arg0.getName());
        }
        
        String s= arg1.toString();
        if (s.trim().length() == 0) return result;//empty List in this case
        String[] tokenized = StringUtils.split(s,',');
        result.addAll(Arrays.asList(tokenized));
        return result;
    }
 
}

/* 
$Log$
Revision 1.6  2007/01/29 16:45:09  nw
cleaned up imports.

Revision 1.5  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.4  2006/06/15 10:07:18  nw
improvements coming from unit testingadded new convertors.

Revision 1.3  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.2  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.1.26.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/12/01 16:11:08  jdt
added a general Collections converter and registered it for Lists.

Revision 1.1  2005/11/29 11:27:55  nw
refactored converters
 
*/