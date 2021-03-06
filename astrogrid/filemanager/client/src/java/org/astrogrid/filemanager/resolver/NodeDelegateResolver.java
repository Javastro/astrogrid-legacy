/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/NodeDelegateResolver.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: NodeDelegateResolver.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
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
import org.astrogrid.store.Ivorn;

/**
 * Public interface for a helper class to resolve an Ivron into a service
 * delegate.
 *  
 */
public interface NodeDelegateResolver {

    /**
     * Resolve an Ivorn into a delegate.
     * 
     * @param ivorn
     *            An Ivorn containing a filestore identifier.
     * @return A FileManagerDelegate for the service.
     * @throws FileManagerResolverException
     * @throws FileManagerResolverException
     *             If unable to resolve the identifier.
     *  
     */
    public NodeDelegate resolve(Ivorn ivorn) throws FileManagerResolverException;
}

