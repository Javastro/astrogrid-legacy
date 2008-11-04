/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import org.astrogrid.workflow.beans.v1.Tool;

/** An interface to customization code specific to a particular service protocol.
 *  - e.g. CEA, UWS-PA, TAP
 *  an instance of this class is used to customize the task runner.
 *  @see TweaksSelector
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 22, 20083:41:58 PM
 */
public interface ProtocolSpecificTweaks {

    /** called from 'build Form' */
    public void buildForm(TaskRunnerImpl tr);
    
    /** called from other variant of 'build form' */
    public void buildForm(final Tool t,TaskRunnerImpl tr); 
    
}
