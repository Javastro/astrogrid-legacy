/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.Resource;

/** factory for the simplified app launcher.
 * @author Noel Winstanley
 * @since Jun 21, 20067:48:11 PM
 */
public class TaskRunnerFactory implements TaskRunnerInternal{

	public TaskRunnerFactory(TypesafeObjectBuilder builder) {
		this.builder = builder;
	}
	private final TypesafeObjectBuilder builder;

	protected TaskRunnerInternal newInstance() {
		return builder.createTaskRunner();
	}
	
	public void invokeTask(Resource r) {
		TaskRunnerInternal tr = newInstance();
		tr.show();
		tr.invokeTask(r);
	}


	public void hide() {
		// ignored.
	}


	public void show() {
		newInstance().show();
	}


	public Object create() {
		TaskRunnerInternal tr = newInstance();
		tr.show();
		return tr;
	}

    public void edit(FileObject o) {
        TaskRunnerInternal tr = newInstance();
        tr.show();        
        tr.edit(o);
    }

}
