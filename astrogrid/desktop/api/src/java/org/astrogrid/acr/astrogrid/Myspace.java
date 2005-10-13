/*$Id: Myspace.java,v 1.7 2005/10/13 18:22:32 nw Exp $
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

import java.io.InputStream;

/** Service Interface to Myspace - a distributed storage system, AstroGrid's implementation of VOSpace
 * <p>
 * All resources in myspace are uniquely identified by a myspace resource identifier - which is an URI of form
 *  * <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#<i>File-Path</i></tt>. However, for convenience methods in this interface also accept an
 * abridged form of reference - <tt>#<i>File-Path</i></tt> - this is resolved relative to the currently logged-in user. The abridged
 * form is more concise, and means hard-coded file references can be avoided if needed.   
 * 
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaGridAndWebServices#VO_Store_Proposal">IVOA VOStore</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
  * @service astrogrid.myspace
  * @see org.astrogrid.acr.ui.MyspaceBrowser
  * @see org.astrogrid.acr.dialogs.ResourceChooser
  * @see org.astrogrid.acr.astrogrid.NodeInformation
 */
public interface Myspace {
    /** retreive the uri of the currently logged-in user's home folder in myspace 
     * <p>
     * Each user has a single root folder. this method returns the name of it.
     * @return uri of the home folder - typically has form  <tt>ivo://<i>Community-Id</i>/<i>User-Id</i>#</tt>
     * @throws SecurityException if authentication / authorization fails
     * @throws ServiceException if error occurs calling the service
     * @throws NotFoundException if there is no home folder associated with the user
     */
    URI getHome() throws SecurityException, ServiceException, NotFoundException;
   /** verify whether a myspace resource exists
    * 
    * @param ivorn uri to check (full or abridged form)
    * @return true if the resource exists
    * @throws ServiceException if error occurs calling the service
    * @throws SecurityException if the user is not permitted to inspect this ivorn (e.g. it is private to another user)
 * @throws InvalidArgumentException
 * 
    */
    boolean exists(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException;

    /** access metadata about a myspace resource
     * 
     * @param ivorn resource to investigate
     * @return a beanful of information
     * @throws ServiceException if error occurs calling the service
     * @throws NotFoundException if this resource does not exist
     * @throws SecurityException it the user is not permitted to inspect this resource 
     * @throws InvalidArgumentException if the ivorn is malformed.
     * @xmlrpc will return a map. see {@link NodeInformation} for available keys.
     */
    NodeInformation getNodeInformation(URI ivorn) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;

// creation    
    /** create a new myspace file. any parent folders that are missing will be created too.
     * 
     * @param ivorn the resource to create.
     * @throws ServiceException if error occurs calling the service
     * @throws SecurityException if the user is not permitted to create this resource.
     * @throws InvalidArgumentException if this resource already exists,,  or one of the parent resources already exists and is not a folder
     * @example
     *   <pre>
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Myspace;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Myspace ms = (Myspace)acr.getService(Myspace.class);
     * URI file =new URI("#votable/a-new-file.vot");
     * if (! ms.exists(file)) {
     *    ms.createFile(file)
     * }
     * </pre>
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
    
     /** list the names of the children (files and folders) of a myspace folder   
      * 
      * @param ivorn uri of the folder to inspect
      * @return an array of the names of the contents.
      * @throws ServiceException if an error occurs calling the service
      * @throws SecurityException if the user is not permitted to inspect this resource
      * @throws NotFoundException if <code>ivorn</code> does not exist
      * @throws InvalidArgumentException if <code>ivorn</code> is not a folder
      */
    String[] list(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

    /** list the identifiers of the children ( files and folders)  of a myspace folder   
     * 
     * @param ivorn uri of the folder to inspect
     * @return an array of the ivorns of the contents.
     * @throws ServiceException if an error occurs calling the service
     * @throws SecurityException if the user is not permitted to inspect this resource
     * @throws NotFoundException if <code>ivorn</code> does not exist
     * @throws InvalidArgumentException if <code>ivorn</code> is not a folder
     */
   URI[] listIvorns(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
   

   /** list the node information objects for the children ( files and folders)  of a myspace folder   
    * 
    * @param ivorn uri of the folder to inspect
    * @return an array of the node information objects.
    * @throws ServiceException if an error occurs calling the service
    * @throws SecurityException if the user is not permitted to inspect this resource
    * @throws NotFoundException if <code>ivorn</code> does not exist
    * @throws InvalidArgumentException if <code>ivorn</code> is not a folder
    */
   NodeInformation[] listNodeInformation(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
   /** refresh the information held on a myspace resource with the server
    * <p>
    * For performance, metadata about myspace resources is used in a LRU cache. This method forces the ACR to re-query the myspace server
    * about this resource. 
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
    * <p>
    * The relocated file remains in the same position in the user's myspace filetree. However, this method moves the data associated with the 
    * file from one filestore to another.
 * @param srcIvorn uri of the resource to relocate
 * @param storeIvorn uri of the store server to relocat to.
 * @throws NotFoundException if the resource or store server do not exist
 * @throws InvalidArgumentException if either reference is malformed.
 * @throws ServiceException if an error occurs calling the service
 * @throws SecurityException if the user is not permitted to relocate this resource
 * @throws NotApplicableException if this resource is not a file - folders can't be relocated.
 * @see #listAvailableStores()
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
     * <p>
     * NB: not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
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
     * <p>
     * NB : not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
     * @param ivorn resource to write to
     * @param content the data to write
     * @throws InvalidArgumentException is the resource is malformed
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder
     */
    void write(URI ivorn, String content) throws InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    
    
    /** read the content of a myspace resource directly
     * <p>
     * NB: not a good idea for large files. in this case use {@link #copyContentToURL(URI, URL) } or {@link #getReadContentURL(URI) }
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
     * <p>
     * NB : not a good idea for large files. In this case use {@link #copyURLToContent(URL, URI) }
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
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     
     * @example
     *   <pre>
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Myspace;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Myspace ms = (Myspace)acr.getService(Myspace.class);
     * URI file =new URI("#results/datafile.vot");
     * URL dataUrl = ms.getReadContentURL(file);
     * InputStream is = dataUrl.openStream();
     *   // read in data..
     * </pre>     
     */
    URL getReadContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;

    /** access a URL to which can be written the  contents (i.e. data) of a myspace resource
     * <p>
     * @param ivorn resource to write to
     * @return a url to  which the contents of the resource can be written
     * @throws NotFoundException if the resource does not exist
     * @throws InvalidArgumentException if the resource is not a file
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to write the contents of this resource
     * @throws NotApplicableException if this resource cannot contain data - e.g. it is a folder     
     * @see #transferCompleted
     * @example For the current filestore impleentation the result returned is a <tt>http://</tt> url, to which data should be <b>PUT</b> (not POST). 
     * <b>NB</b>After the data has been written to the filestore, the filemanager needs to be notified that the data for this node has changed - by calling 
     * {@link #transferCompleted}
     * Here's how to do this in java
     * <pre>
     * import org.astrogrid.acr.*;
     * import java.net.*;
     * import org.astrogrid.acr.astrogrid.Myspace;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Myspace ms = (Myspace)acr.getService(Myspace.class);
     * URI file =new URI("#results/datafile.vot");
     *  //get the output url
     * URL url = ms.getWriteContentURL(file); 
      * HttpURLConnection conn  = (HttpURLConnection) url.openConnection() ;
      * conn.setDoOutput(true) ;
      * conn.setRequestMethod("PUT");
      *   // connect
      * conn.connect();
      * OutputStream os = conn.getOutputStream(); 
      *  //write the data
      *  ...
      *   //close
      * os.close();
      *  // important - the URL connection class won't tranfer data unless you ask for a response - this is a nasty gotcha, not clear from the javadocs.
      * conn.getResponseCode() // necessary to force the whole thing to happen
      * ms.transferCompleted(file); // tell the filemanager that the content for this resource has changed.
     * </pre> 
     */
    URL getWriteContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
//      *   //configure the connection
    //* conn.setChunkedStreamingMode(1024); // this method is only available in java 1.5 - omit for 1.4and beware of transferring large files
//    
    /**
     * Notify the filemanager  server that the data for a filestore node has been changed. 
     * <p>
     * This method must be called after storing data in a myspace file via the URL returned by {@link #getWriteContentURL}.
     * There's no need to call this method when storing data using any other method
     * @param ivorn the myspace resource just written to
     * @throws NotFoundException if the resource does not exist
     * @throws InvalidArgumentException if the resource is not writable
     * @throws ServiceException if an error occurs while calling the service
     * @throws SecurityException if the user is not permitted to access this resource
     * @throws NotApplicableException if this resouce cannot contain data - e.g. it is a folder
     * @see #getWriteContentURL
     * @since 1.2.4
     */
    void transferCompleted(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException;
    
    
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
     * @see #changeStore
     */
    ResourceInformation[] listAvailableStores() throws ServiceException;

}

/* 
 $Log: Myspace.java,v $
 Revision 1.7  2005/10/13 18:22:32  nw
 fixed getWriteContentURL documentation

 Revision 1.6  2005/10/07 12:11:13  KevinBenson
 moved a method that is not possible for xmlrpc (serialization) down to MyspaceInternal.java interface and took it out of Myspace.java interface

 Revision 1.5  2005/10/06 09:19:26  KevinBenson
 Added a writeStream method to pass in a inputstream for storing into myspace.

 Revision 1.4  2005/08/25 16:59:44  nw
 1.1-beta-3

 Revision 1.3  2005/08/16 13:14:24  nw
 added missing method

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

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