/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.astrogrid.filemanager.client.delegate.CachingNodeDelegate;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.FileManagerLocator;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolver;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.store.Ivorn;

/** reimplementation of the stuff in the filemanager client to use some memoization 
 * so that it becomes a little more efficient.
 * 
 * @future extend CachingNodeDelegate so that bundled nodes are injected
 * into the VFS cache whenever new updates are received from the server.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 4, 200711:11:20 AM
 */
public class MemoizingNodeDelegateResolver implements NodeDelegateResolver {

    /**
	 * @param prefs
	 */
	public MemoizingNodeDelegateResolver(BundlePreferences pref) {
		this.pref = pref;
	}
	private final BundlePreferences pref;
	protected final FileManagerLocator locator = new FileManagerLocator();
    protected final FileManagerEndpointResolver resolver =  new MemoizingEndpointResolver();

	public NodeDelegate resolve(Ivorn ivorn) throws FileManagerResolverException {
        try {
            URL endpoint = resolver.resolve(ivorn);
            FileManagerPortType port = locator.getFileManagerPort(endpoint);
            return new CachingNodeDelegate(port, pref) ;

        } catch (ServiceException e) {
            throw new FileManagerResolverException("Could not create soap delegate", e);
        }

    }			


}
