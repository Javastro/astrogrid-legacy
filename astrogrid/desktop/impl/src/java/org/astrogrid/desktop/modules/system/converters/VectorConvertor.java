/*$Id: VectorConvertor.java,v 1.1 2005/11/29 11:27:55 nw Exp $
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

import org.apache.commons.beanutils.Converter;

import java.util.Arrays;
import java.util.Vector;

/**
 * Convert a comma-delimited string into a vector
 * @author John Taylor
 *
 */
public class VectorConvertor implements Converter {
    public Object convert(Class arg0, Object arg1) {
        //john - can we assume that arg0 IS Vector?
        //nww - I beleive so. anyhow, prefer to throw a runtime exception here.
/*        assert arg0==Vector.class;
        assert arg1 instanceof String;
*/        
        if (arg0 != Vector.class) {
            throw new RuntimeException("Can only convert to Vector" + arg0.getName());
        }
        if (arg1.equals("")) return new Vector();//empty vector in this case
        String[] tokenized = (arg1.toString()).split(",");
        Vector results = new Vector(Arrays.asList(tokenized));
        return results;
    }
    
    private static Converter theInstance = new VectorConvertor();
    public static Converter getInstance() {
        return theInstance;
    }
}

/* 
$Log: VectorConvertor.java,v $
Revision 1.1  2005/11/29 11:27:55  nw
refactored converters
 
*/