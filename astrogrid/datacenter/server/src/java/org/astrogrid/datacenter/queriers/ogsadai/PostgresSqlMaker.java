/*$Id: PostgresSqlMaker.java,v 1.6 2004/03/30 16:21:24 eca and mch
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.ogsadai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.w3c.dom.Element;
import org.astrogrid.datacenter.adql.generated.ogsadai.Select; 
import org.w3c.dom.Node;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Produced Postgres-specific SQL
 */
public class PostgresSqlMaker extends StdSqlMaker {

   private static final Log log = LogFactory.getLog(PostgresSqlMaker.class);

   /**
    * Constructs an SQL statement for the given ADQL
    */
   public String fromAdql(AdqlQuery query) {
      //should use appropriate xslt, but use deprecated stuff for now
	
      try {
        Element queryBody = query.toDom().getDocumentElement();
        Node node = queryBody.getFirstChild(); 
        
		String rootName = queryBody.getNodeName(); 
		Select selectQuery = (Select)Unmarshaller.unmarshal(Select.class, queryBody); 
		

		try {  
		AdqlQueryTranslator visitor = new AdqlQueryTranslator();
		String sql = visitor.translate(selectQuery);
		 return sql;
		}catch (Exception e) { 
			e.printStackTrace(); 
			return "PSM ERROR: Caught Exception " + e.getMessage(); 
		} 
	
	  } catch (Exception e) { 
		return "PSM ERROR: Caught Exception" + e.getMessage(); 
	  }
   }
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.7  2004/04/02 03:12:12  eca
PostgresSqlMaker now unmarshalls the adql query as a node, using
ADQLUtils.Unmarshall rather than org.astrogrid.datacenter.adql.generated.Select Unmarshall.

Also, removed function to convert DEC to DECL - now obsolete as 
ogsadai.AdqlQueryTranslator should do all necessary conversions.

Revision 1.6  2004/03/30 16:21:24  eca
Updated ogsadai Postgres-optimized query translator, updated class
references in PostgresSqlMaker.

30/03/04 ElizabethAuden

Revision 1.5  2004/03/24 15:57:31  kea
Updated Javadocs etc.

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor
 
*/

