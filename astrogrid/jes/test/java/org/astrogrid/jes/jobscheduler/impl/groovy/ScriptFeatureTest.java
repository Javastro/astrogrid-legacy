/*$Id: ScriptFeatureTest.java,v 1.3 2004/08/09 17:34:10 nw Exp $
 * Created on 09-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Workflow;

/** unit test for &lt;script&gt; tag.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jul-2004
 *
 */
public class ScriptFeatureTest extends AbstractTestForFeature {
    /** Construct a new ScriptFeatureTest
     * @param name
     */
    public ScriptFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForScriptingFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Script script = new Script();       
        script.setBody("x = 1 + 1; print(x);");
        wf.getSequence().addActivity(script);
        
        script = new Script();// in this script, test that various environmental variables are there.
        script.setBody("print( jes != null && log != null && astrogrid != null && astrogrid.getAllServices().size() == 0 && currentUser != null && currentAccount != null);");
        
        wf.getSequence().addActivity(script);
        return wf;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForScriptingFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        
        Script script =(Script)result.getSequence().getActivity(0);
        assertScriptCompletedWithMessage(script,"2");
        
         script =(Script)result.getSequence().getActivity(1);
        assertScriptCompletedWithMessage(script,"true");  
   }
}


/* 
$Log: ScriptFeatureTest.java,v $
Revision 1.3  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/