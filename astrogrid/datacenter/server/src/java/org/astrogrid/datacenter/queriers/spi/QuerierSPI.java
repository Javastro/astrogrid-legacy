/*$Id: QuerierSPI.java,v 1.2 2003/11/28 16:10:30 nw Exp $
 * Created on 26-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.Workspace;

/** Interface to the plugin system
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
 *
 */
public interface QuerierSPI {

    /** access the translator mapping for this plugin */
    public TranslatorMap getTranslatorMap();
    
    /** self-description 
     * can be used for name, description, version, etc.
     * may retrun a richer type here later.
     * */
    public String getPluginInfo();
    
    /** pass config properties into the plugin 
     * called before any other methods are called on the plugin */
    public void receiveConfig(Config conf);

    /** pass workspace object to the plugi
     * again called before any of the querying methods
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
     *
     */
    public void receiveWorkspace(Workspace ws);
    
    
    

    /** perform a query
     * 
     * @param o the intermediate form of the query
     * @param type the class of the intermediate form
     * @return a query results object
     */
    public QueryResults doQuery(Object o,Class type) throws Exception ;

    /** close resources associated with the query just performed
     * called at the end of a QuerySPI objects lifecycle.
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
     *
     */
    public void close() throws Exception;

    /** interface that encapsulates a configuration thingie.
     *would have liked to use something from astrogrid.common, but no clean interfaces there.
     * @author Noel Winstanley nw@jb.man.ac.uk 26-Nov-2003
     *
     */
    public interface Config {
        /** retrive a property associated with a key, or null*/
        public String getProperty(String key);
        /** retreive a property associated with a key, or default value*/
        public String getProperty(String key,String defaultVal);

    }
    

}


/* 
$Log: QuerierSPI.java,v $
Revision 1.2  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.1  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/