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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.Converter;
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
        //Need to choose default implementations if 
        //we're not given one
        if (arg0.isInterface()) {
            if (List.class.isAssignableFrom(arg0)) {
                resultType = ArrayList.class;
            } else if (Set.class.isAssignableFrom(arg0)) {
                resultType = HashMap.class;
            }
            //It could also be something obscure, but we'll
            //let it blow up with a runtimeexception later on
            //if so.
        }
        
        try {
            result = (Collection) resultType.newInstance();
        } catch (InstantiationException e) {
            logger.error("Could not convert object ",e);
            throw new RuntimeException("Can only convert to Lists" + arg0.getName());
        } catch (IllegalAccessException e) {
            logger.error("Could not convert object ",e);
            throw new RuntimeException("Could not convert object ");
        }
        
        if (arg1.equals("")) return result;//empty List in this case
        String[] tokenized = (arg1.toString()).split(",");
        result.addAll(Arrays.asList(tokenized));
        return result;
    }
 
}

/* 
$Log$
Revision 1.2  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.1.26.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/12/01 16:11:08  jdt
added a general Collections converter and registered it for Lists.

Revision 1.1  2005/11/29 11:27:55  nw
refactored converters
 
*/