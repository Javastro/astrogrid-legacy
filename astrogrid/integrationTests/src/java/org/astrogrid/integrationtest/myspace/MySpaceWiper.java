/*
 * $Id: MySpaceWiper.java,v 1.1 2004/03/04 19:06:04 jdt Exp $ Created on Feb
 * 16, 2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.integrationtest.myspace;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Useful little utility for clearing out mySpaces @TODO we probably want this
 * to be in the mySpace component since it uses classes from the mySpace jar
 * that won't be in the delegate jar ideally the integration tests would just
 * depend on the delegate?
 * 
 * @author jdt
 * @TODO following refactoring of mySpace this needs attention
 */
public class MySpaceWiper {
    Log log = LogFactory.getLog(MySpaceWiper.class);
    private MySpaceClient mySpace;
    /**
     * Credential used with mySpace. Hardwired to "any" for now. @TODO find out
     * what this should really be.
     *  
     */
    final private String credential = "any";
    public MySpaceWiper(String endPoint) throws IOException {
        assert endPoint != null;
        MySpaceClient client = null;
        try {
            client = MySpaceDelegateFactory.createDelegate(endPoint);
            log.debug("MySpace delegate obtained: " + client);
        } catch (IOException ex) {
            log.error("Failed to obtain a mySpace delegate");
            throw ex;
        }
        assert(client != null);
        this.mySpace = client;
    }
    /**
     * Just wipe this user
     * 
     * @param user user
     * @param community community
     */
    public void clear(String user, String community)
        throws MySpaceDelegateException {
        clearHoldings(user, community);
        deleteUser(user, community);
    }
    /**
     * Delete this user - must have no holdings or this will fail
     * @param user user
     * @param community community
     */
    private void deleteUser(final String user, final String community) throws MySpaceDelegateException {
        log.trace("deleteUser: attempting to delete user="+user+", community="+community);
        try {
        boolean ok = mySpace.deleteUser(user, community, credential);
        log.debug("Result from deleteUser: "+ok);
        } catch (Exception e) {
            log.error("Exception in deleteUser: ",e);
            throw new MySpaceDelegateException(e);
        }
        
        
    }
    public static void main(String[] args)
        throws MySpaceDelegateException, IOException {
        if (args.length != 3 || args[0].equals("-h")) {
            System.out.println(
                "Wipes the given mySpace clean\n Usage:\n MySpaceWiper <endpoint> <user> <community>\n");
            return;
        }
        String endpoint = args[0];
        String user = args[1];
        String community = args[2];
        new MySpaceWiper(endpoint).clear(user, community);
    }
    /**
     * Construct the full path name of a myspace artifact
     * 
     * @param user
     *            user
     * @param community
     *            community
     * @param file
     *            filename
     * @return full path
     */
    private static String getFullPath(
        final String user,
        final String community,
        final String file) {
        return "/" + user + "@" + community + "/" + file;
    }
    /**
     * Just wipe this user's holdings
     * 
     * @param user
     * @param community
     */
    public void clearHoldings(final String user, final String community)
        throws MySpaceDelegateException {
        List holdings = getDataHoldings(user, community);
        Iterator it = holdings.iterator();
        while (it.hasNext()) {
            String holding = (String) it.next();
            deleteHolding(user, community, holding);
        }
    }
    /**
     * Delete a given holding
     * 
     * @param user
     *            user
     * @param community
     *            community
     * @param holding
     *            holding
     */
    private void deleteHolding(
        final String user,
        final String community,
        final String holding)
        throws MySpaceDelegateException {
        log.debug(
            "deleteHolding: user="
                + user
                + ", community="
                + community
                + ",holding="
                + holding);
        try {
               String result =
                    mySpace.deleteDataHolding(
                        user,
                        community,
                        credential,
                        holding);
                log.debug("result from mySpace " + result);
                if (result.indexOf("FAULT") != -1) {
                    //@TODO urgh. Should the delegate be passing back raw xml
                    // like this?
                    throw new MySpaceDelegateException("deleteHolding: Failed to delete");
                }
            
        } catch (Exception e) {
            log.error("deleteHolding: Exception from mySpace client", e);
            throw new MySpaceDelegateException(e);
        }
    }

    
    public List getDataHoldings(final String user, final String community)
        throws MySpaceDelegateException {
        log.debug("getDataHoldings: user=" + user + ", community=" + community);
        String query = getFullPath(user, community, "*");
        log.debug("query=" + query);
        Vector serversHoldings;
        try {
            serversHoldings =
                mySpace.listDataHoldingsGen(user, community, credential, query);
        } catch (Exception e) {
            log.error("getDataHoldings: Exception from mySpace client", e);
            throw new MySpaceDelegateException(e);
        }
        assert serversHoldings != null;
        if (serversHoldings.size() != 1) {
            log.error(
                "Returned vector of holdings did not have size 1.  Size was "
                    + serversHoldings.size()
                    + ". Holdings was "
                    + serversHoldings);
            throw new MySpaceDelegateException(
                "MySpace should return a vector of vectors.  Since there's only one mySpace server the size of the parent vector should be one, but was "
                    + serversHoldings.size());
        }
        List holdingsList = processReturnedXML(serversHoldings);
        return holdingsList;
    }
    /**
     * Process the xml from listDataHoldingsGen and remove all the non-file entries
     * @param serversHoldings the xml snippet
     * @return a nice list of files
     */
    private List processReturnedXML(Vector serversHoldings) {
        //Urgh
        //@TODO this is really not a good way of doing things
        
        
        // Tokens in the returned xml from listDataHoldings corresponding to the name
         // of the file.  Scarily fragile.
         
         final String DATA_ITEM_NAME_KEY="dataItemName";
          final String DATA_ITEM_TYPE_KEY="type";
          final String DATA_ITEM_TYPE_FILE = "0";
        
        
        String xmlString = (String) serversHoldings.firstElement();
        Assist assistant = new Assist();
        Vector holdings = assistant.getDataItemDetails(xmlString, DATA_ITEM_NAME_KEY);
        Vector holdingsTypes = assistant.getDataItemDetails(xmlString, DATA_ITEM_TYPE_KEY);
        assert holdings != null;
        assert holdingsTypes !=null;
        
        List holdingsListAll = new ArrayList(holdings);
        List holdingsTypesList = new ArrayList(holdingsTypes);
        List holdingsList = new ArrayList();
        assert holdingsListAll.size()==holdingsTypesList.size();
        
        log.debug("Holdings: ");
        for (int i=0;i<holdingsListAll.size();++i) {
            String type = (String) holdingsTypesList.get(i);
            boolean isFile = (DATA_ITEM_TYPE_FILE.equals(type));
            log.debug((String) holdingsListAll.get(i)+(isFile ? " " :" *NOT A FILE *"));
            if (isFile) {
                holdingsList.add(holdingsListAll.get(i));
            }
            
        }
        return holdingsList;
    }
}
/**
 * @author jdt
 * 
 * Any exception from the mySpace client gets wrapped in this. No good just
 * chucking generic exceptions all over the place.
 */
class MySpaceDelegateException extends Exception {
    /**
     * ctor
     */
    public MySpaceDelegateException() {
        super();
    }
    /**
     * ctor
     * 
     * @param message
     *            message
     */
    public MySpaceDelegateException(String message) {
        super(message);
    }
    /**
     * ctor
     * 
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public MySpaceDelegateException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * ctor
     * 
     * @param cause
     *            cause
     */
    public MySpaceDelegateException(Throwable cause) {
        super(cause);
    }
}
