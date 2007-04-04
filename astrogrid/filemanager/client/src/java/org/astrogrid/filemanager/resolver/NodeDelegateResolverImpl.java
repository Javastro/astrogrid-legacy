/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/NodeDelegateResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: nw $</cvs:author>
 * <cvs:date>$Date: 2007/04/04 08:58:38 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: NodeDelegateResolverImpl.java,v $
 *   Revision 1.3  2007/04/04 08:58:38  nw
 *   altered visibliltiy of some components, to make them easier to extend.
 *
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.4.4.4  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.4.4.3  2005/02/11 16:03:20  nw
 *   refactoring cuts these down quite a bit. which is nice.
 *
 *   Revision 1.4.4.2  2005/02/11 14:31:47  nw
 *   still need to refactor these.
 *
 *   Revision 1.4.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.4  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.resolver;

import org.astrogrid.filemanager.client.delegate.CachingNodeDelegate;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.FileManagerLocator;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.store.Ivorn;

import java.net.URL;

import javax.xml.rpc.ServiceException;

/**
 * A helper class to resolve an Ivron into a service delegate.
 * 
 * @modified nww reduced visibility of constructors just used for in-package
 *                   testing.
 * @modified nww removed constructos and tests that only used each other.
 * @modified nww removed unused methods.
 * 
 * @todo Ping the filestore to check it is available ?
 *  
 */
public class NodeDelegateResolverImpl implements NodeDelegateResolver {

    /**
     * Public constructor, using the default registry service.
     *  
     */
    public NodeDelegateResolverImpl(BundlePreferences pref) {
        this.resolver = new FileManagerEndpointResolverImpl();
        this.pref = pref;
        this.locator = new FileManagerLocator();
    }

    protected final BundlePreferences pref;

    protected final FileManagerLocator locator;

    /**
     * Our endpoint resolver.
     *  NWW - made protected so that this class is open to extension.
     */
    protected final FileManagerEndpointResolver resolver;

    /**
     * Resolve an Ivorn into a delegate.
     * 
     * @param ivorn
     *                    An Ivorn containing a filemanager identifier.
     * @return A FileManagerDelegate for the service.
     * @throws FileManagerResolverException
     * @throws FileManagerResolverException
     *                     If unable to resolve the identifier.
     *  
     */
    public NodeDelegate resolve(Ivorn ivorn) throws FileManagerResolverException {
        try {
            URL endpoint = resolver.resolve(ivorn);
            FileManagerPortType port = locator.getFileManagerPort(endpoint);
            return new CachingNodeDelegate(port, pref);

        } catch (ServiceException e) {
            throw new FileManagerResolverException("Could not create soap delegate", e);
        }

    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerDelegateResolverImpl:");
        buffer.append(" resolver: ");
        buffer.append(resolver);
        buffer.append("]");
        return buffer.toString();
    }
}

