/*$Id: EgsoQuery.java,v 1.1 2004/07/07 09:17:40 KevinBenson Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sec.querier;

import java.io.IOException;
import java.io.Writer;
import org.astrogrid.datacenter.query.Query;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.astrogrid.datacenter.sec.secdelegate.egso.EgsoDelegate;

/** Dataclass that represents a vizier cone search.
 * <p>
 * Contains fields for each of the required or optional search terms that can be passed to vizier.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class EgsoQuery implements Query {

    protected String sql;
    
    /**
     *
     */
    public EgsoQuery() {

    }
    
    /** double-dispatch thingie - chooses which query method of the vizier delegate to call, based on which search terms are currently set.
     * @param delegate - delegate to call method on
     * @return votable document returned by the delegate
     */
    public Document doDelegateQuery(EgsoDelegate delegate) throws IOException {
       try {
          return delegate.doQuery(this.sql);
       }
       catch (ParserConfigurationException e) {
          throw new RuntimeException("Egso not configured properly: "+e, e);
       }
       catch (SAXException e) {
          throw new RuntimeException("Egso error: "+e, e);
       }
    }

   
    /** Set radius of search cone
     * @param d radius
     */
    public void setSQL(String sql) {
        this.sql = sql;
    }
    
    public String getSQL(String sql) {
      return this.sql;   
    }

}


/*
$Log: EgsoQuery.java,v $
Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.3  2003/12/09 16:25:08  nw
wrote plugin documentation

Revision 1.2  2003/12/01 16:50:11  nw
first working tested version

Revision 1.1  2003/11/28 19:12:16  nw
getting there..
 
*/
