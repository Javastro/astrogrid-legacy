/*
 * $Id: BooleanNormalizer.java,v 1.3 2008/09/18 09:13:39 pah Exp $
 * 
 * Created on 28 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class to convert between different representations of boolean values.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 28 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class BooleanNormalizer {

    /** the recognised boolean pairs of values
     */
    private static final BoolPair[] boolpairs= {
	       new BoolPair("true", "false"),
	       new BoolPair("y","n"),
	       new BoolPair("t","f"),
	       new BoolPair("yes", "no"),
	       new BoolPair("on","off"),
	       new BoolPair("1","0"),
	       new BoolPair("in","out")
	   };
    /**
     * The class to represent a boolean pair.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    private static class BoolPair
    {
	public String truestr;
	public String falsestr;
	public BoolPair(String t, String f) {
	    truestr = t;
	    falsestr = f;
	}
	public boolean toBoolean(String s)
	{
	    return s.equals(truestr);
	}
	public String valueOf(boolean b)
	{
	    if(b){
		return truestr;
	    }else {
		return falsestr;
	    }
	}
	
    }
    private static final Map<String,BoolPair> map = new HashMap<String, BoolPair>();
    static {
	for (int i = 0; i < boolpairs.length; i++) {
	    map.put(boolpairs[i].truestr, boolpairs[i]);
	    map.put(boolpairs[i].falsestr, boolpairs[i]);
	}
    }

    /**
     * Convert a string representation of a boolean to the desired form.
     * N.B. if either the value or the template value is not recognised the original value is returned.
     * @param val The original string value.
     * @param def The desired form of the boolean - either the true of the false value may be given.
     * @return
     */
    public static String normalize(String val, String def) {
	
        if(map.containsKey(val.toLowerCase()) && map.containsKey(def.toLowerCase()))
        {
           BoolPair original = map.get(val.toLowerCase());   
           BoolPair desired = map.get(def.toLowerCase());
           return desired.valueOf(original.toBoolean(val.toLowerCase()));//TODO what about maintaining case sensitivity
        }
        else { // cannot do anything but return the original string
            return val;
        }
    }
    
    /**
     * determines if the boolean string representation is recognised by this class.
     * @param val
     * @return
     */
    public static boolean isRecognised(String val)
    {
	return map.containsKey(val);
    }

    
}


/*
 * $Log: BooleanNormalizer.java,v $
 * Revision 1.3  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:53  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:27  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/03/28 16:44:35  pah
 * RESOLVED - bug 2683: treatment of boolean values
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2683
 *
 */
