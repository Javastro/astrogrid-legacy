/*$Id: ADQLUtils.java,v 1.4 2003/09/22 16:51:24 mch Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.adql;
import java.io.IOException;
import java.io.StringWriter;

import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.adql.generated.SelectChoice;
import org.astrogrid.datacenter.adql.generated.SelectionAll;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Node;

/** some static helper methods for working with ADQL object trees.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class ADQLUtils {
    /** write query out to string
     * -- why can't this be implemented by castor already*/
    public static String queryToString(Select q) throws IOException, CastorException {
        StringWriter out = new StringWriter();
        q.marshal(out);
        out.close();
        return out.toString();
    }

    /** throw together the minimal query that will validate
     *  - expect this to change as new versions of the schema become more polished.
     * currently boils down to <Select><SelectionAll/></Select> */
    public static Select buildMinimalQuery() {
        Select s = new Select();

        SelectChoice sel = new SelectChoice();
        s.setSelectChoice(sel);
        sel.setSelectionAll(new SelectionAll());
        // no order-by or table clauses required, according to schema.
        return s;
    }

    //add other marshalling mehtods ere -- e.g. to-from a dom.
    /**
     * Creates an ADQL object model from the given document object model.
     * Throws package ADQLException rather than castor-specific exceptions
     */
    public static Select unmarshalSelect(Node n) throws ADQLException{

      try {
         return (Select)Unmarshaller.unmarshal(Select.class,n);
      }
      catch (ValidationException e) {
         throw new ADQLException("ADQL invalid",e);
      }
      catch (MarshalException e) {
         throw new ADQLException("Failed to load adql object model",e);
      }
    }
}


/*
$Log: ADQLUtils.java,v $
Revision 1.4  2003/09/22 16:51:24  mch
Now posts results to dummy myspace

Revision 1.3  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.2  2003/09/02 14:44:31  nw
added method to unmarshall ADQL object model from xml Node

Revision 1.1  2003/08/28 15:27:54  nw
added ADQL object model.

*/
