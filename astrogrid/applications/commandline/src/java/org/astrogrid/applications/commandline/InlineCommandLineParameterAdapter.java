/*$Id: InlineCommandLineParameterAdapter.java,v 1.3 2004/07/23 07:46:16 nw Exp $
 * Created on 18-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;

/** Parameter adapter for arguments that are added to the commandline directly - i.e. inline ones.
 *  - so obviously, can only be used for input parameters.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Jun-2004
 *
 */
public class InlineCommandLineParameterAdapter extends DefaultParameterAdapter {
    /**
     * Not possible for this kind of parameter.
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object arg0) throws CeaException {
        throw new CeaException("Programming Error - attempting to writeback to inline command line parameter");
    }
    /** Construct a new CommandLineParameterAdapter
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public InlineCommandLineParameterAdapter(ParameterValue arg0, CommandLineParameterDescription arg1, IndirectParameterValue arg2) {
        super(arg0, arg1, arg2);
        this.desc= arg1;
    }
    protected final CommandLineParameterDescription desc;
    
    /**
     * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
     * @return a string buffer of this arg.
     */
    public Object process() throws CeaException {
        String val =  (String)super.process(); // super implementation guarantees a string
        return desc.addCmdlineAdornment(val); // this will return a list.
    }

}


/* 
$Log: InlineCommandLineParameterAdapter.java,v $
Revision 1.3  2004/07/23 07:46:16  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:43:39  nw
final version, before merge
 
*/