/*$Id: StoreFacade.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 25-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.datastore;

import org.astrogrid.filemanager.common.TransferInfo;
import org.astrogrid.filestore.common.exception.FileStoreException;

import org.apache.axis.types.URI;

import java.util.Map;

/** Facade that separates filemanager from details of particular file storage.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2005
 *
 */
public interface StoreFacade {

    Store resolve(URI locationIvorn) throws FileStoreException;
    
    public interface Store {

        /** delete a store resource
         * @param ident identifier of the resource to delete.
         */
        void delete(String ident) throws FileStoreException;
        
        /** create a new store resource, by duplicating data in an existing store resource 
         * 
         * @param ident identifier of resource to duplicate
         * @return new identifier, for new resource.
         * @throws FileStoreException
         */
        String duplicate(String ident) throws FileStoreException; 
        
        /** request a transferInfo from the store, from which data can be read
         * 
         * @param ident identifier of resource to read
         * @return details of how to read this resource
         * @throws FileStoreException
         */
        TransferInfo requestReadFromStore(String ident) throws FileStoreException;
        /**request a transferinfo from the store, to which data can be written.
         * 
         * @param ident identifier of resource to write
         * @param overwrite. if true, writes will overwrite existing data. if false, writes will append.
         * @return details of how to write this resource.
         * @throws FileStoreException
         */        
        TransferInfo requestWriteToStore(String ident, boolean overwrite) throws FileStoreException;
      
        /**request a transferinfo from the store, to which data can be written.
         * 
         * @param overwrite. if true, writes will overwrite existing data. if false, writes will append.
         * @return details of how to write this resource, and ident of new resource.
         * @throws FileStoreException
         */        
        NewResource requestWriteToStore() throws FileStoreException;        

          /** insruct store to read data into a store resource, from an external url
           * 
           * @param ident identifier of resource to read into.
           * @param info details of external data.
           * @throws FileStoreException
           */
        void readIn(String ident,TransferInfo info) throws FileStoreException;
        
        /** insruct store to read data into a store resource, from an external url
         * 
         * @return identifier of new resource created by reading in data.
         * @param info details of external data.
         * @throws FileStoreException
         */
      String  readIn(TransferInfo info) throws FileStoreException;        
        
        /** instruct store to write a store resource to an external url
         * 
         * @param ident identifier of resoutce to write out
         * @param info details of external data
         * @throws FileStoreException
         */
        void writeOut(String ident,TransferInfo info) throws FileStoreException;
        
        /** query store for set of attributes about a resource.
         * 
         * @param ident resource to lookup attributes for.
         * @return map of implememntation-dependent properties.
         * @throws FileStoreException
         */
        Map getAttributes(String ident) throws FileStoreException;
        
    }
    
    /** very simple struct, for returning two results */
    public class NewResource {
        /** identifier of the newly created resouce */
        public String ident;
        /** details of how to write data to this new resource */
        public TransferInfo transfer;
    }
}


/* 
$Log: StoreFacade.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:39  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:37  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore
 
*/