/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.commons.vfs.FilesCache;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.delegate.AxisNodeWrapper;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.client.delegate.VanillaNodeDelegate;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.FileManagerLocator;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.common.Node;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.resolver.FileManagerEndpointResolver;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.store.Ivorn;

/** 
 * Memoizing immplementation of the NodeDelegate.
 * </p>
 * reimplementation of the stuff in the filemanager client to use some memoization 
 * so that it becomes a little more efficient.
 *
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 4, 200711:11:20 AM
 */
public class MemoizingNodeDelegateResolver implements NodeDelegateResolver {

	private final FilesCache vfsCache;

    public MemoizingNodeDelegateResolver(final BundlePreferences pref, final FilesCache vfsCache) {
		this.pref = pref;
        this.vfsCache = vfsCache;
	}
	private final BundlePreferences pref;
	protected final FileManagerLocator locator = new FileManagerLocator();
    protected final FileManagerEndpointResolver resolver =  new MemoizingEndpointResolver();

	public NodeDelegate resolve(final Ivorn ivorn) throws FileManagerResolverException {
        try {
            final URL endpoint = resolver.resolve(ivorn);
            final FileManagerPortType port = locator.getFileManagerPort(endpoint);
           // return new CachingNodeDelegate(port, pref) ;
            // am seeing all sorts of prblems with refresh - probably due to maintainign a separate cache 
            //- will try to replace the caching delegate with a vanilla delegate, and see if that helps.
            return new VanillaNodeDelegate(port,pref){
                // if I wanted to inject objects into the vfs cache, this would be the method to override.
                protected FileManagerNode returnFirst(final Node[] ns) {
                    FileManagerNode first = null;
                    for (int i = 0; i < ns.length; i++) {
                        final org.astrogrid.filemanager.common.Node bean = ns[i];
                        final NodeIvorn key = bean.getIvorn();
                        AxisNodeWrapper wrapper = null;
                        //@todo place the nodes in the cache - will require some restructuring.
                        // so that the filesystem can be accessed.
/*
                        if (cache.containsKey(key)) { // update internal
                                                                       // data bean
                            wrapper = (AxisNodeWrapper) cache.get(key);
                            wrapper.setBean(bean); // will only be set if
                                                                    // therer are changes. i

                        } else { // create new bean.
                        */
                            wrapper = new AxisNodeWrapper(bean, this);
                         //   cache.put(key, wrapper);
                        //}

                        if (i == 0) { //hang on to this node, if it was the first in
                                          // the bundle.
                            first = wrapper;
                        }
                    }// end for. 
                    return first;
                }
            };

        } catch (final ServiceException e) {
            throw new FileManagerResolverException("Could not create soap delegate", e);
        }

    }			


}
