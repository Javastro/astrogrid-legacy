/*$Id: Myspace.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import java.net.URI;
import java.net.URL;

/** Distributed Storage System
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public interface Myspace {
    /** retreive the uri of your home folder in myspace 
     * 
     * @return uri of the home folder
     * @throws SecurityException if authentication / authorization fails
     * @throws ServiceException if error occurs calling the service
     * @throws NotFoundException if there is no home folder associated with the user
     */
    URI getHome() throws SecurityException, ServiceException, NotFoundException;
   /** verify whether a myspace resource exists
    * 
    * @param ivorn uri to check
    * @return true if the resource exists
    * @throws ServiceException if error occurs calling the service
    * @throws SecurityException if the user is not permitted to inspect this ivorn (e.g. it is private to another user)
 * @throws InvalidArgumentException
    */
    boolean exists(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException;

    /** access metadata about a resource
     * 
     * @param ivorn resource to investigate
     * @return a beanful of information
     * @throws ServiceException if error occurs calling the service
     * @throws NotFoundException if this resource does not exist
     * @throws SecurityException it the user is not permitted to inspect this resource 
     * @throws InvalidArgumentException
     */
    NodeInformation getNodeInformation(URI ivorn) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;

// creation    
    /** create a new myspace file. any parent folders that are missing will be created too.
     * 
     * @param ivorn the resource to create.
     * @throws ServiceException if error occurs calling the service
     * @throws SecurityException if the user is not permitted to create this resource.
     * @throws InvalidArgumentException if this resource already exists,,  or one of the parent resources already exists and is not a folder
     */
    public void createFile(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException;
    
    /** create a new myspace folder. any parent folders that are missing will be created too.
     * 
     * @param ivorn the resource to create.
     * @throws ServiceException if error occurs calling the service
     * @throws SecurityException if the user is not permitted to create this resource.
     * @throws InvalidArgumentException if this resource already exists,  or one of the parent resources already exists and is not a folder
     */
    
    public void createFolder(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException ;


// node based creation
    
    /** create a child folder of this resource
     * @param parentIvorn parent of the new resource (must be a folder)
     * @param name name of the new folder
     * @return the ivorn of the new folder
     * @throws NotFoundException if the parent does not exist.
     * @throws ServiceException if error occurs calling the service
     * @throws SecurityException if the user is not permitted to create folders here
     * @throws InvalidArgumentException if a child file or folder with this name already exists,
     * @throws NotApplicableException if the parent is not a folder
     * 
     */
    public URI createChildFolder(URI parentIvorn, String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException, NotApplicableException;

    /** create a child file of this resource
     * @param parentIvorn parent of the new resource (must be a folder)
     * @param name name of the new file
     * @return the ivorn of the new file
     * @throws NotFoundException if the parent does not exist
     * @throws ServiceException if error occurs calling the service
     * @throws SecurityException if the user is not permitted to create files here
     * @throws InvalidArgumentException if a child file or folder with this name already exists
     * @throws NotApplicableException if the parent is not a folder
     * 
     */
    public URI createChildFile(URI parentIvorn, String name) throws NotFoundException,
            ServiceException, SecurityException, InvalidArgumentException, NotApplicableException;

    //navigation
    
    /** retrieve the URI of the parent of a myspace resource
     * @param ivorn uri of the resource to find parent for
     * @return uri of the parent
     * @throws NotFoundException  if <code>ivorn</code> does not exist
     * @throws InvalidArgumentException if <code>ivorn</code> has no parent - i.e. is the home directory.
     * @throws ServiceException if a erorr occurs calling the service
     * @throws SecurityException if the user is not permitted to inspect this resource
     * 
     */
    public URI getParent(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;
    
     /** list the names of the contents of a myspace folder   
      * 
      * @param ivorn uri of the folder to inspect
      * @return an array of the names of the contents.
      * @throws ServiceException if an error occurs calling the service
      * @throws SecurityException if the user is not permitted to inspect this resource
      * @throws NotFoundException if <code>ivorn</code> does not exist
      * @throws InvalidArgumentException if <code>ivorn</code> is not a folder
      */
    String[] list(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

    /** list the ivorns of the contents of a myspace folder   
     * 
     * @param ivorn uri of the folder to inspect
     * @return an array of the ivorns of the contents.
     * @throws ServiceException if an error occurs calling the service
     * @throws SecurityException if the user is not permitted to inspect this resource
     * @throws NotFoundException if <code>ivorn</code> does not exist
     * @throws InvalidArgumentException if <code>ivorn</code> is not a folder
     */
   URI[] listIvorns(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
   
   /** refresh the information held on a myspace resource with the server
 * @param ivorn resource to refresh
 * @throws SecurityException if the user is not permitted to inspect this resource
 * @throws ServiceException if an error occurs calling the service
 * @throws NotFoundException if the resource does not exist.
 * @throws InvalidArgumentException
 */
void refresh(URI ivorn) throws SecurityException, ServiceException, NotFoundException, InvalidArgumentException;

   // management
   /** delete a myspace resource
 * @param ivorn uri of the resource to delete
 * @throws NotFoundException if the resource does not exist
 * @throws SecurityException if the user is not permitted to delete this resource
 * @throws ServiceException if an error occurs calling the service
 * @throws InvalidArgumentException if the resouce is a folder that contains further resources - delete these first.
 */
void delete(URI ivorn) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException;


   /** rename a myspace resource
 * @param srcIvorn uri of the resource to renam
 * @param newName new name for this resource
 * @return uri pointing to the renamed resource (original uri may now be invalid)
 * @throws NotFoundException if the resource does not exist
 * @throws SecurityException if the user is not permitted to rename this resource
 * @throws ServiceException if an error occurs calling the service
 * @throws InvalidArgumentException if the newName is already taken by anotherh resource in this folder.
 */
URI rename(URI srcIvorn, String newName) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException;

  /** move a myspace resource 
   * 
   * @param srcIvorn ivorn of the resource to move
   * @param newParentIvorn ivorn of the new parent
   * @param newName new name for this resource.
   * @return uri pointing to the moved resouce (original uri will now be invalid)
   * @throws NotFoundException if either the source or new folder resources cannot be found
   * @throws InvalidArgumentException if a resource with name =newName= already exists in the new folder  
   * @throws SecurityException if the user is not permitted to move this resource
   * @throws ServiceException if an error occurs calling the service.
 * @throws NotApplicableException if the new parent is not a folder
   */
   URI move(URI srcIvorn, URI newParentIvorn, String newName) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException, NotApplicableException;

   /** relocate this resource to another store.
 * @param srcIvorn uri of the resource to relocate
 * @param storeIvorn uri of the store server to relocat to.
 * @throws NotFoundException if the resource or store server do not exist
 * @throws InvalidArgumentException if either reference is malformed.
 * @throws ServiceException if an error occurs calling the service
 * @throws SecurityException if the user is not permitted to relocate this resource
 * @throws NotApplicableException if this resource is not a file - folders can't be relocated.
 */
void changeStore(URI srcIvorn, URI storeIvorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

   /**take a copy of a resource 
 * @param srcIvorn uri of the resource to copy
 * @param newParentIvorn uri of the folder to copy to 
 * @param newName name to copy to
 * @return uri pointing to the resource copy
 * @throws NotFoundException if the original resource or new parent do not exist
 * @throws InvalidArgumentException if the new parent is not a folder, or already contains a resource called 'newName' 
 * @throws ServiceException if an error occurs while calling the service
 * @throws SecurityException if the user is not permitted to copy this resource
 */
URI copy(URI srcIvorn, URI newParentIvorn, String newName) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;
   
   
    
    /** read the content of a myspace resource directly .
     * NB: not a good idea for large files. 
     * @param ivorn resource to read
     * @return content of this resource (as a string)
     * @throws NotFoundException if this resource does not exist.
     * @throws InvalidArgumentException if this resource is malformed
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     * @throws NotApplicableException if this resource has no data - e.g. it is a folder
     */

    String read(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    
    /** Write data to a myspace resource
     * @param ivorn resource to write to
     * @param content the data to write
     * @throws InvalidArgumentException is the resource is malformed
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder
     */
    void write(URI ivorn, String content) throws InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    
    
    /** read the content of a muspace resource directly
     * NB: not a good idea for large files.
     * @param ivorn resource to read
     * @return byte array of the contents of this resource
     * @throws NotFoundException if this resource does not exist.
     * @throws InvalidArgumentException if this resource is malformed
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder
     *     */
    byte[] readBinary(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    
    
    /**Write data to a myspace resource
     * @param ivorn resource to write to
     * @param content the data to write
     * @throws InvalidArgumentException is the resource is malformed
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     *      * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder
     */
    void writeBinary(URI ivorn,byte[] content) throws InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    

    /** access a URL from which can be read contents (i.e. data) of a myspace resource
     * @param ivorn resource to read
     * @return a url from which the contents of the resource can be read
     * @throws NotFoundException if the resource does not exist
     * @throws InvalidArgumentException if the resource is not a file
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to read the contents of this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     * 
     */
    URL getReadContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

    /** access a URL to which can be written the  contents (i.e. data) of a myspace resource
     * @param ivorn resource to write to
     * @return a url to  which the contents of the resource can be written
     * @throws NotFoundException if the resource does not exist
     * @throws InvalidArgumentException if the resource is not a file
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to write the contents of this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     * 
     */
    URL getWriteContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

    /** write the contents (data) of a myspace resource out to a URL location
     * @param ivorn the myspace resource to write out
     * @param destination a writable URL - file:/, http:/ or ftp:/ protocol
     * @throws NotFoundException if the myspace resource does not exist
     * @throws InvalidArgumentException if the destination is not writable
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to read the contents of this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     * 
     */
    void copyContentToURL(URI ivorn, URL destination) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

    /** write the contents (data) of a URL location into a myspace resource
     * @param src url to read data from - file:/, http:/ or ftp:/ protocol.
     * @param ivorn the myspace resource to store the data in.
     * @throws NotFoundException if the myspace resource does not exist
     * @throws InvalidArgumentException if the src is not readable
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to write the contents of this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     * 
     */
    void copyURLToContent(URL src, URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

 


    /** 
     * List the available filestores
     * @return an array of resource information
     * @throws ServiceException if an error occurs while retreiveing the list of stores
     */
    ResourceInformation[] listAvailableStores() throws ServiceException;

}

/* 
 $Log: Myspace.java,v $
 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/07/08 11:08:02  nw
 bug fixes and polishing for the workshop

 Revision 1.3  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.1.2.1  2005/05/09 14:51:02  nw
 renamed to 'myspace' and 'workbench'
 added confirmation on app exit.

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2.2.1  2005/04/22 10:54:36  nw
 added missing methods to vospace.
 made a start at getting applications working again.

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */