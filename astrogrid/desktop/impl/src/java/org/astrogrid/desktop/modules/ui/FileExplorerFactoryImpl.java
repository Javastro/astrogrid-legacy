/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileExplorerImpl;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** Factory for fileexplorers.
 * - also handles some messages.
 * 
 * @todo implement the plastic message handling part of this, then uncomment section in ui.xml
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 20076:44:32 PM
 */
public class FileExplorerFactoryImpl  implements FileManagerFactory {
	
	public final TypesafeObjectBuilder builder;

	private static final Log logger = LogFactory.getLog(FileExplorerFactoryImpl.class);
	
	public FileExplorerFactoryImpl(final TypesafeObjectBuilder builder) {
		this.builder = builder;
	}	
	private FileManagerInternal newWindow() {
		final FileExplorerImpl vo = builder.createFileExplorer();
		vo.setVisible(true);
		return vo;
	}

//Factory Interface
	// create a new voexplorer
	public Object create() {
		return newWindow();
	}
	
//Myspace  interface.
	public void show() {
		newWindow();
	}
	public void hide() {
		// not implemented.
	}
	

    public void show(final FileObjectView fo) {
        newWindow().show(fo);
    }
    
    public void show(final URI loc) {
        newWindow().show(loc);
    }
	
}
