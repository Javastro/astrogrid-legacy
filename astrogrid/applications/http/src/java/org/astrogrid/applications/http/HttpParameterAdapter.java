/*$Id: HttpParameterAdapter.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 24, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.apache.commons.beanutils.ConvertUtils;

/** ParameterAdapter for a javaClassParameter
 * 
 * uses the additional information in javaclassParameter to convert the parameter value from a string
 * to an instance of the correct type for the java method parameter.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class HttpParameterAdapter extends DefaultParameterAdapter {



    /**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public HttpParameterAdapter(ParameterValue arg0, ParameterDescription arg1, ExternalValue arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
     * @see org.astrogrid.applications.ParameterAdapter#process()
     */
    public Object process() throws CeaException {
        String result = (String)super.process(); // we know super always returns a string.
        Class targetClass = ((HttpParameterDescription)description).getTargetClass();
        return ConvertUtils.convert(result.trim(),targetClass); //remove trailing white space, as it seems to change the conversion process. (e.g. '2/n' => 0), which isn't good.
    }

}


/* 
$Log: HttpParameterAdapter.java,v $
Revision 1.2  2004/07/27 17:49:57  jdt
merges from case3 branch

Revision 1.1.4.1  2004/07/27 17:20:25  jdt
merged from applications_JDT_case3

Revision 1.1.2.1  2004/07/24 17:16:16  jdt
Borrowed from javaclass application....stealing is always quicker.

Revision 1.3  2004/07/23 08:03:05  nw
fixed bug in conversion from string

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/