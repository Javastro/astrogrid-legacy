/*$Id: ADQLUtils.java,v 1.3 2004/01/13 00:32:47 nw Exp $
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

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.ApproxNum;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.adql.generated.SelectChoice;
import org.astrogrid.datacenter.adql.generated.SelectionAll;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/** some static helper methods for working with ADQL object trees.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class ADQLUtils {
   private ADQLUtils() {
   }
   
   /** temporary namespace identifier used for this version of adql */
    public static final String ADQL_XMLNS = "http://tempuri.org/adql";
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
 
    public static Document marshallSelect(Select s) throws ADQLException{
        try {
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(s, doc);                        
            return doc; 
        } catch (Exception e) {
            throw new ADQLException("Failed to marshall select",e);
        }        
    }
   
    public static ApproxNum mkApproxNum(double num) {
       ApproxNum an = new ApproxNum();
       an.setValue(num);
       return an;
    }
    
    /** convert a select object to an Element that can be used as the query body in a 
     * {@link org.astrogrid.datacenter.delegate.FullSearcher}
     * @param s
     * @return
     * @throws ADQLException
     */
   public static Element toQueryBody(Select s) throws ADQLException{
       try {
           Document doc = XMLUtils.newDocument();
           Marshaller.marshal(s, doc);            
           return doc.getDocumentElement();
       } catch (Exception e) {
           throw new ADQLException("Failed to marshall select",e);
       }        
   }
    
    
}


/*
$Log: ADQLUtils.java,v $
Revision 1.3  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.2.10.2  2004/01/08 15:36:00  nw
added xmlns constant to this class, as with SQLUtils

Revision 1.2.10.1  2004/01/08 09:10:20  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.2  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/14 12:43:42  nw
moved from parent datacenter project.

Revision 1.4  2003/09/22 16:51:24  mch
Now posts results to dummy myspace

Revision 1.3  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.2  2003/09/02 14:44:31  nw
added method to unmarshall ADQL object model from xml Node

Revision 1.1  2003/08/28 15:27:54  nw
added ADQL object model.

*/
