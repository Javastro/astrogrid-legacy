/*$Id: CougaarClassLoader.java,v 1.1 2004/09/30 13:57:03 nw Exp $
 * Created on 28-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.cougaar.profiler.transform;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.util.ClassLoader;
import org.apache.catalina.loader.WebappClassLoader;

/** class loader that adapts classes using {@link org.cougaar.profiler.transform.SelfProfiler}
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Sep-2004
 *
 */
public class CougaarClassLoader extends WebappClassLoader {

    /** Construct a new CougaarClassLoader
     * 
     */
    public CougaarClassLoader() {
        super();
    }

    /** Construct a new CougaarClassLoader
     * @param arg0
     */
    public CougaarClassLoader(java.lang.ClassLoader arg0) {
        super(arg0);
    }




    protected JavaClass modifyClass(JavaClass arg0) {
        return modifier.modifyClass(arg0);
    }
    
    protected final SelfProfiler modifier;
    {
        modifier = new SelfProfiler(); // NWW - may need to add in options here - see SelfProfiler for details.
    }
}


/* 
$Log: CougaarClassLoader.java,v $
Revision 1.1  2004/09/30 13:57:03  nw
checked in sources.
 
*/