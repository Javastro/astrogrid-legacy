/*$Id: JavaClassParameterAdapter.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

import org.apache.commons.beanutils.ConvertUtils;

/** A {@link org.astrogrid.applications.parameter.ParameterAdapter} for a {@link org.astrogrid.applications.javaclass.JavaClassParameterDescription}
 * 
 * <p>uses the additional information in the description to convert the parameter value from a string
 * to an instance of the correct type for the java method parameter.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * @see org.apache.commons.beanutils.ConvertUtils
 * @see org.astrogrid.applications.javaclass.JavaClassParameterDescription
 *
 */
public class JavaClassParameterAdapter extends DefaultParameterAdapter {

    /** Construct a new JavaClassParameterAdapter
     * @param val
     * @param description
     */
    public JavaClassParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue ipVal) {
        super(val, description,ipVal);
    }

/**
 * Calls the super class implementation (which will always return a string), and then uses beanUtils library to convert to type expected by java method 
 * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
 * @todo add handling for other types - e.g. URI / URL / Document / Node.
 */
    public Object process() throws CeaException {
        String result = (String)super.process(); // we know super always returns a string.
        Class targetClass = ((JavaClassParameterDescription)description).getTargetClass();
        return ConvertUtils.convert(result.trim(),targetClass); //remove trailing white space, as it seems to change the conversion process. (e.g. '2/n' => 0), which isn't good.
    }

}


/* 
$Log: JavaClassParameterAdapter.java,v $
Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:23:34  gtr
Javaclass-application stuff is moved to a separate project.

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/26 10:21:47  nw
javadoc

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