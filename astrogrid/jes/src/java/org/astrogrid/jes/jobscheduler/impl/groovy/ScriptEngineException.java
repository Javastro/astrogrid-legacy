/*$Id: ScriptEngineException.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 12-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.JesException;

/** exception class representing stuff thrown by scripting engine.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2004
 *
 */
public class ScriptEngineException extends JesException {
    /** Construct a new ScriptEngineException
     * @param s
     */
    public ScriptEngineException(String s) {
        super(s);
    }
    /** Construct a new ScriptEngineException
     * @param s
     * @param e
     */
    public ScriptEngineException(String s, Exception e) {
        super(s, e);
    }
}


/* 
$Log: ScriptEngineException.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.

Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/