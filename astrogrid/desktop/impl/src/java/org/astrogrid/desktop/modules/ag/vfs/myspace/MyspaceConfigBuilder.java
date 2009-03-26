/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import org.apache.commons.vfs.FileSystemConfigBuilder;

/** some kind of filesystem configuraiton object. 
 * I'm cargo-cult programming here at the moment - don't know what it's for, or whether
 * it's really needed. looks nice though.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 4, 20071:26:06 AM
 */
public class MyspaceConfigBuilder extends FileSystemConfigBuilder {

	@Override
    protected Class getConfigClass() {
		return MyspaceFileSystem.class;
	}
	
	

}
