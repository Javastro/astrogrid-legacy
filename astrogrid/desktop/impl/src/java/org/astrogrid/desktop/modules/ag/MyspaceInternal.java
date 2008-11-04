/*$Id: MyspaceInternal.java,v 1.9 2008/11/04 14:35:47 nw Exp $
 * Created on 02-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.registry.RegistryException;

/** Internal interface to additional myspace operations.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Aug-2005

 */
public interface MyspaceInternal extends Myspace {

    /** get the internal filemanager node for a myspace ivorn (i.e. get an internal delegate)
     * <b>NB</b> use the node returned just for display / metadata - for data transfer, prefer the methods in the Myspace & MyspaceInternal interfaces
     * @param ivorn a uri of form ivo://
     * @return the node object for this ivorn
     */
    public FileManagerNode node(URI ivorn) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException ;

   
    /** Helper method to open an arbitrary uri for reading
     * @param uri http://, file://, ftp://, ivo://
     * @return appropriate input stream
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws SecurityException
     * @throws ServiceException
     *  *@deprecated use FileSystemManager (<tt>system.vfs</tt>) instead
     */
    @Deprecated
    public InputStream getInputStream(URI uri) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException;
    /** Helper method to open an abitrary uri for writing
     * NB: not a good idea for large files. - use the variant where output file size is specified.
     * @param ui http://, file://, ftp://, ivo://
     * @return a suitable output stream
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws SecurityException
     * @throws ServiceException
     *  *@deprecated use FileSystemManager (<tt>system.vfs</tt>) instead
     */
    @Deprecated
    public OutputStream getOutputStream(URI ui) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException;

    /** Helper method to open an abitrary uri for writing
     * @param ui http://, file://, ftp://, ivo://
     * @return a suitable output stream
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws SecurityException
     * @throws ServiceException
     *  *@deprecated use FileSystemManager (<tt>system.vfs</tt>) instead
     */
    @Deprecated
    public OutputStream getOutputStream(URI ui, long size) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException;
   

    /** temporary hack - not encouraged to use this */
     FileManagerClient getClient() throws CommunityException, RegistryException, URISyntaxException ;
            
    
}


/* 
$Log: MyspaceInternal.java,v $
Revision 1.9  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.8  2007/08/30 23:46:47  nw
Complete - task 73: upgrade filechooser dialogue to new fileexplorer code
replaced uses of myspace by uses of vfs where sensible

Revision 1.7  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.6  2006/11/09 12:08:33  nw
final set of changes for 2006.4.rc1

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.46.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4  2005/10/14 14:20:41  nw
work around for problems with FileStoreOutputStream

Revision 1.3  2005/10/07 12:11:13  KevinBenson
moved a method that is not possible for xmlrpc (serialization) down to MyspaceInternal.java interface and took it out of Myspace.java interface

Revision 1.2  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/