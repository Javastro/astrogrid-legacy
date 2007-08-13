/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.File;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.CacheStrategy;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.impl.DefaultFileReplicator;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.commons.vfs.operations.FileOperationProvider;
import org.apache.commons.vfs.provider.FileProvider;
import org.astrogrid.acr.builtin.ShutdownListener;

/** Extension of default VFS filesystemmanager that adds some hooks to be configured by hivemind.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20072:50:59 PM
 */
public class HivemindFileSystemManager extends DefaultFileSystemManager implements ShutdownListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(HivemindFileSystemManager.class);

	/**
	 * @throws FileSystemException 
	 * 
	 */
	public HivemindFileSystemManager(DefaultFileReplicator tmpFileStore,Map providers, Map operations, Map extensions, Map mimetypes) throws FileSystemException {
		setTemporaryFileStore(tmpFileStore);
		for (Iterator i = providers.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			logger.info("Adding provider for " + e.getKey());					
			addProvider((String)e.getKey(),(FileProvider)e.getValue());
		}
		for (Iterator i = operations.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			logger.info("Adding operations for " + e.getKey());				
			addOperationProvider((String)e.getKey(),(FileOperationProvider)e.getValue());
		}		

		for (Iterator i = extensions.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			logger.info("Adding extension mapping for " + e.getKey());				
			addExtensionMap((String)e.getKey(),(String)e.getValue());
		}

		for (Iterator i = mimetypes.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			logger.info("Adding mime mapping for " + e.getKey());				
			addMimeTypeMap((String)e.getKey(),(String)e.getValue());
		}		
		
		// much more efficient if no auto-refresh happens. need to call 'refresh()' programmatically in this setting.
		setCacheStrategy(CacheStrategy.MANUAL);
		//setCacheStrategy(CacheStrategy.ON_RESOLVE); -- much too slow for myspace
	}


	public void halting() {
		close();
	}

	public String lastChance() {
		return null;
	}
	
	public void setBaseFileString(String arg0) throws FileSystemException {
		super.setBaseFile(new File(arg0));
	}
	
	/** extended - to initialize the results virtual file system too */
	public void init() throws FileSystemException {
	    super.init();
	}

	
	/** Overridden - as default implementatio causes a StackOverflow - as it delegates back to the
	 * system protocol handlers.
	 */ 
	public URLStreamHandlerFactory getURLStreamHandlerFactory() {
		return new StackOverflowAvoidingStreamHandlerFactory(super.getURLStreamHandlerFactory());
	}
	
	/** wrap the stream handler retruned from the core implementaiton, 
	 * and intercept calls to it which will result in a stack overflow.
	 * 
	 * necessary to go this way, as because most classes in VFS are private or
	 * final, there's no other way to override.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 17, 20074:15:25 PM
	 */
	final class StackOverflowAvoidingStreamHandlerFactory implements URLStreamHandlerFactory {

		private final URLStreamHandlerFactory orig;
		
		public StackOverflowAvoidingStreamHandlerFactory(
				URLStreamHandlerFactory orig) {
				this.orig = orig;
		}

		public URLStreamHandler createURLStreamHandler(String protocol) {
			if (!hasProvider(protocol)) {
				return null;
			} else {
				return orig.createURLStreamHandler(protocol);
			}
		}
		
	}

}
