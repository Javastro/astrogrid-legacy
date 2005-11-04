/*$Id: DefaultStoreFacade.java,v 1.3 2005/11/04 17:31:05 clq2 Exp $
 * Created on 27-Feb-2005
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
import org.astrogrid.filestore.client.FileStoreDelegate;
import org.astrogrid.filestore.common.exception.FileStoreException;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException;
import org.astrogrid.filestore.common.file.FileProperties;
import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.transfer.TransferProperties;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolver;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Default implementation of store facade - uses the standard file store delegate resolvers
 * to resolve to filestore dleegates as needed.
 *
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2005
 *
 */
public class DefaultStoreFacade implements StoreFacade {

    /** Construct a new DefaultStoreFacade
     * @param resolver resolver used to map identifiers to filestore delegates.
     *
     */
    public DefaultStoreFacade(FileStoreDelegateResolver resolver) {
        super();
        this.resolver = resolver;
    }

    protected final FileStoreDelegateResolver resolver;

    /**
     * @see org.astrogrid.filemanager.datastore.StoreFacade#resolve(org.apache.axis.types.URI)
     */
    public Store resolve(URI locationIvorn) throws FileStoreException {
        try {
            FileStoreDelegate del = resolver.resolve(new Ivorn(locationIvorn.toString()));
            return new DefaultStore(locationIvorn.toString(),del);
        } catch (URISyntaxException e) {
            throw new FileStoreIdentifierException("URISyntaxException " + e.getMessage());
        }
    }

    /** implementation of te StoreFacade.Store */
    static class DefaultStore implements StoreFacade.Store {
        /** Construct a new DefaultStore
         * @param location ivorn endpoint of the delegate we're wrapping
         * @param fs the filestore delegate to wrap.
         */
        public DefaultStore(String location,FileStoreDelegate fs) {
            this.fs = fs;
            this.location = location;
        }
        protected final FileStoreDelegate fs;
        protected final String location;
        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#delete(java.lang.String)
         */
        public void delete(String ident) throws FileStoreException {
            FileProperty[] arr= fs.delete(ident);
        }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#duplicate(java.lang.String)
         */
        public String duplicate(String ident) throws FileStoreException {
            FileProperty[] origArr =new FileProperty[0];
            FileProperty[] arr = fs.duplicate(ident,origArr);
            FileProperties fps = new FileProperties(arr);
            return fps.getStoreResourceIdent();
        }

        /**
         * Sets up a read from the FileStore. The returned object tells the caller
         * where and how to write the data: URL, method. Currently, the
         * FileStore supports only HTTP-GET, so this is hard-coded.
         *
         * @param ident The file to be read, in the store's namespace.
         * @return The recipe for reading the data from the store.
         * @throws FileStoreException If errors are trapped in this class.
         * @throws FileStoreException If the FileStore service faults.
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#requestReadFromStore(java.lang.String)
         */
        public TransferInfo requestReadFromStore(String ident) throws FileStoreException {
            TransferProperties tps = identToTransferProperties(ident);
            tps.setMethod("GET");
            TransferProperties tps1 = fs.exportInit(tps);
            TransferInfo ti = this.transferPropertiesToTransferInfo(tps1);
            ti.setMethod("GET");
            return ti;
        }

        /**
         * Sets up a write to the FileStore. The returned object tells the caller
         * where and how to write the data: URL, method. Currently, the
         * FileStore supports only HTTP-PUT, so this is hard-coded.
         *
         * @param ident The file to be written, in the store's namespace.
         * @param overwrite If true, replace existing data in the store; otherwise append data.
         * @return The recipe for writing the data to the store.
         * @throws FileStoreException If errors are trapped in this class.
         * @throws FileStoreException If the FileStore service faults.
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#requestWriteToStore(java.lang.String, boolean)
         */
        public TransferInfo requestWriteToStore(String ident, boolean overwrite) throws FileStoreException {
            TransferProperties tps = identToTransferProperties(ident);
            tps.setMethod("PUT");
            TransferProperties tps1;
            if (overwrite) {
                tps1 = fs.importInit(tps);
            } else {
              //* no implementation for append */
                throw new FileStoreException("Append not supported");
            }
            TransferInfo ti = this.transferPropertiesToTransferInfo(tps1);
            ti.setMethod("PUT");
            return ti;
        }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#requestWriteToStore()
         */
        public NewResource requestWriteToStore() throws FileStoreException {
                TransferProperties tps = new TransferProperties();
                TransferProperties tps1 = fs.importInit(tps);
                NewResource nr = new NewResource();
                nr.ident = (new FileProperties(tps1.getFileProperties())).getStoreResourceIdent();
                nr.transfer = transferPropertiesToTransferInfo(tps1);
                return nr;
            }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#readIn(java.lang.String,
         org.astrogrid.filemanager.common.TransferInfo)
         */
        public void readIn(String ident, TransferInfo info) throws FileStoreException {
            TransferProperties tp = transferInfoAndIdentToTransferProperties(ident,info);
            fs.importData(tp);
        }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#readIn(org.astrogrid.filemanager.common.TransferInfo)
         */
        public String readIn(TransferInfo info) throws FileStoreException {
            TransferProperties tp = transferInfoToTransferProperties(info);
            TransferProperties tp1 = fs.importData(tp);
            return  (new FileProperties(tp1.getFileProperties())).getStoreResourceIdent();
        }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#writeOut(java.lang.String,
         org.astrogrid.filemanager.common.TransferInfo)
         */
        public void writeOut(String ident, TransferInfo info) throws FileStoreException {
            TransferProperties tp = transferInfoAndIdentToTransferProperties(ident,info);
            fs.exportData(tp);
        }

        /**
         * @see org.astrogrid.filemanager.datastore.StoreFacade.Store#getAttributes(java.lang.String)
         */
        public Map getAttributes(String ident) throws FileStoreException {
            FileProperty[] arr = fs.properties(ident);
            if (arr == null) {
                return Collections.unmodifiableMap(Collections.EMPTY_MAP);
            }
            Map m = new HashMap(arr.length);
            for (int i = 0; i < arr.length; i++) {
                m.put(arr[i].getName(),arr[i].getValue());
            }
            return Collections.unmodifiableMap(m);
        }

        /** convert a transger into to a transfer properties.
         * @param info
         * @return
         */
        private TransferProperties transferInfoToTransferProperties(TransferInfo info) {
            TransferProperties tp = new TransferProperties();
            tp.setLocation(info.getUri().toString());
            tp.setProtocol("HTTP");
            tp.setMethod(info.getMethod());
            return tp;
        }
        /** construct a transfer properties from a transfer info and an ident.
         * @param info
         * @return
         * @throws FileStoreException
         */
        private TransferProperties transferInfoAndIdentToTransferProperties(String ident,TransferInfo info) throws
        FileStoreException {
            try {
            FileProperties props = new FileProperties();
            props.setStoreResourceIvorn(new Ivorn(location +"#" + ident));
            TransferProperties tp = new TransferProperties(props);
            tp.setIdent(ident);
            tp.setLocation(info.getUri().toString());
            tp.setProtocol("HTTP");
            tp.setMethod(info.getMethod());
            tp.setIdent(ident);
            return tp;
            } catch (URISyntaxException e) {
                throw new FileStoreException("Could not build valid ident " + e.getMessage());
            }
        }


        /** convert a transfer propertires to a transfer info
         * @param tps1
         * @return
         * @throws MalformedURIException
         */
        private TransferInfo transferPropertiesToTransferInfo(TransferProperties tps1) throws FileStoreException {
            try {
            TransferInfo result = new TransferInfo();
            result.setMethod(tps1.getMethod());
                result.setUri(new URI(tps1.getLocation()));
            return result;
        } catch (MalformedURIException e) {
            throw new FileStoreException("File store returned invalid access URL " + e.getMessage());
        }
        }


        /** construct a transfer properties from a resource ident.
         * @param ident
         * @return
         * @throws URISyntaxException
         */
        private TransferProperties identToTransferProperties(String ident) throws FileStoreException {
            try {
            FileProperties props = new FileProperties();
            props.setStoreResourceIvorn(new Ivorn(location +"#" + ident));
            TransferProperties tps = new TransferProperties(props);
            tps.setIdent(ident);
            return tps;
            } catch (URISyntaxException e) {
                throw new FileStoreException("Could not build valid ident " + e.getMessage());
            }
        }




    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DefaultStoreFacade:");
        buffer.append(" resolver: ");
        buffer.append(resolver);
        buffer.append("]");
        return buffer.toString();
    }
}


/*
$Log: DefaultStoreFacade.java,v $
Revision 1.3  2005/11/04 17:31:05  clq2
axis_gtr_1046

Revision 1.2.64.1  2005/10/19 13:46:27  gtr
I added code to make sure that the method property of a TransferInfo bean is always set. Axis 1.3 requires this in order to serialize the bean.

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:39  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:36  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

*/
