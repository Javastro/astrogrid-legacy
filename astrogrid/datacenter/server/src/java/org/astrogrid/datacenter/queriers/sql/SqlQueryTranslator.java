/*$Id: SqlQueryTranslator.java,v 1.8 2004/02/24 16:04:18 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.queriers.spi.Translator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.*;
import org.astrogrid.config.SimpleConfig;

/** Pass-through SQL translator.
 * <p>
 * Expects following documents in following format
 * &lt;sql xmlns="urn:sql" &gt;
 *   select * from ...
 * &lt;/sql &gt;
 * <p>
 * This translator must be explicitly enabled by setting
 * {@link #SQL_PASSTHRU_ENABLED_KEY} = "true" in the configuration. Otherwise
 * any SQL queries will be aborted with a {@link java.lang.SecurityException}
 * <p>
 * Override  {@link #inspectSQL(String)} to perform validation checks of the SQL before execution.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 */
public class SqlQueryTranslator implements Translator {
   /** set this key to 'true' to the config file to enable SQL passthru*/
   public static final String SQL_PASSTHRU_ENABLED_KEY = "SqlQueryTranslator.passthru.enabled";
   private static final Log log = LogFactory.getLog(SqlQueryTranslator.class);
    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#translate(org.w3c.dom.Element)
     */
    public Object translate(Element e) throws Exception {
       String val = SimpleConfig.getSingleton().getString(SQL_PASSTHRU_ENABLED_KEY,"false");
       if (val.trim().equalsIgnoreCase("true")) {
          log.debug("SQL passthru enabled");
       } else {
          log.warn("Attempt to access SQL passthru, which is disabled");
          throw new SecurityException("This datacenter feature is disabled");
       }
        if (e.getLocalName().equals("sql")) {
            return e.getFirstChild().getNodeValue();
        } else {
            NodeList nodes = e.getElementsByTagName("sql");
            if (nodes.getLength() == 0) {
                throw new IllegalArgumentException("No element named 'sql' found in document");
            }
            if (nodes.getLength() > 1) {
                throw new IllegalArgumentException("More than one element named 'sql' found in document - there can be only 0ne");
            }
            String sql = null;
            if (nodes.item(0).getFirstChild() != null) {
                sql = nodes.item(0).getFirstChild().getNodeValue();
            } else {
               sql =  nodes.item(0).getNodeValue();
            }
            inspectSQL(sql);
            log.info("Passthru sql to execute: " + sql);
            return sql;
        }
    }
       /** method that verifies the SQL statement is not malicious or harmful to the server
        * <p>
        * Blank implementation  - Can be overridden to provide additional checking.
        * @throws SecurityException if the sql is judged to be dodgy
        * @todo add sensible default implementation - blank for now.
        */
      protected void inspectSQL(String sql) throws SecurityException {
      }
    

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
     */
    public Class getResultType() {
        return String.class;
    }

}


/*
$Log: SqlQueryTranslator.java,v $
Revision 1.8  2004/02/24 16:04:18  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.7  2004/02/16 23:34:35  mch
Changed to use Account and AttomConfig

Revision 1.6  2004/01/15 14:49:47  nw
improved documentation

Revision 1.5  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.4.10.1  2004/01/08 15:36:54  nw
enabled pass-thru SQL

Revision 1.4  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.3  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.
 
*/
