/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/NodeDelegateResolverMock.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: NodeDelegateResolverMock.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.5.2.4  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.5.2.3  2005/02/11 16:03:20  nw
 *   refactoring cuts these down quite a bit. which is nice.
 *
 *   Revision 1.5.2.2  2005/02/11 14:31:47  nw
 *   still need to refactor these.
 *
 *   Revision 1.5.2.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.5  2005/02/10 12:44:10  jdt
 *   Merge from dave-dev-200502010902
 *
 *   Revision 1.4.2.1  2005/02/01 16:10:53  dave
 *   Updated FileManagerClient and factory to support full mock services ..
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

import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.ivorn.IvornParser;
import org.astrogrid.store.Ivorn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * A resolver for mock filemanagers.
 *  
 */
public class NodeDelegateResolverMock implements
        NodeDelegateResolver {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory
            .getLog(NodeDelegateResolverMock.class);

    /**
     * Public constructor.
     *  
     */
    public NodeDelegateResolverMock() {
    }

    /**
     * Our internal map of services.
     *  
     */
    private Map map = new HashMap();

    /**
     * Register a new filemanager.
     * @throws RemoteException
     * @throws FileManagerFault
     * @throws URISyntaxException
     * @throws FileManagerIdentifierException
     *  
     */
    public void register(NodeDelegate delegate) throws FileManagerFault, RemoteException, URISyntaxException{
        // Parse the ivorn.
        Ivorn ivorn = new Ivorn(delegate.getIdentifier().toString());
        String ident = new IvornParser(ivorn).getServiceIdent();
        //
        // Add the delegate to our map.
        map.put(ident, delegate);
    }

    /**
     * Resolve an Ivorn into a delegate.
     * 
     * @param ivorn
     *            An Ivorn containing a filemanager identifier.
     * @return A FileManagerDelegate for the service.
     * @throws FileManagerResolverException
     *             If unable to resolve the identifier.
     *  
     */
    public NodeDelegate resolve(Ivorn ivorn)
            throws FileManagerResolverException {
        log.debug("FileManagerDelegateResolverMock.resolve(" + ivorn + ")");
        if (null == ivorn) {
            throw new IllegalArgumentException("Null service ivorn");
        }
        //
        // Parse the ivorn.
        String ident = null;
        try {
            ident = new IvornParser(ivorn).getServiceIdent();
       } catch (URISyntaxException ouch) {
            throw new FileManagerResolverException(
                    "Unable to parse service ivorn : " + ivorn.toString());
        }
        //
        // Lookup the filemanager in our map.
        if (map.containsKey(ident)) {
            return (NodeDelegate) map.get(ident);
        } else {
            throw new FileManagerResolverException("FileManager not found");
        }
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerDelegateResolverMock:");
        buffer.append(" map: ");
        buffer.append(map);
        buffer.append("]");
        return buffer.toString();
    }
}

