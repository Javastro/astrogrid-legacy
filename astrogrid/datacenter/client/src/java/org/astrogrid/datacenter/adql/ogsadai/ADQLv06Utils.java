
/*$Id: ADQLv06Utils.java,v 1.1 2004/04/16 16:44:50 eca Exp $
 * Created on 14-Apr-2004
 * @author Elizabeth Auden
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/

package org.astrogrid.datacenter.adql.ogsadai;
import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.generated.ogsadai.ApproxNum;
import org.astrogrid.datacenter.adql.generated.ogsadai.Select;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionListChoice;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionListChoiceItem;
import org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem;
import org.astrogrid.util.DomHelper;
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
public class ADQLv06Utils {
   private ADQLv06Utils() {
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
		SelectionList sel = new SelectionList();
		SelectionListChoice slc = new SelectionListChoice();
		SelectionListChoiceItem slci = new SelectionListChoiceItem();
		AllSelectionItem asi = new AllSelectionItem();
		slci.setAllSelectionItem(asi);
		slc.setSelectionListChoiceItem(slci);
		sel.addSelectionListChoice(slc);		
		s.setSelection(sel);

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
			Document doc = DomHelper.newDocument();
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
		   Document doc = DomHelper.newDocument();
		   Marshaller.marshal(s, doc);
		   return doc.getDocumentElement();
	   } catch (Throwable e) {
		   throw new ADQLException("Failed to marshall select",e);
	   }
   }
    
    
}