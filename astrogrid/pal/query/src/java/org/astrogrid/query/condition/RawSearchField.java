/*
 * $Id: RawSearchField.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;


/**
 * For storing a reference to a search field as a string
 */

public class RawSearchField extends SearchFieldReference {
   
   String field = null;
   

   /**  Creates a reference to a search field. */
   public RawSearchField(String aField) {
      
      this.field = aField;
   }

   public String getField() { return this.field; }
   
   /** For human debugging */
   public String toString() {
      return "[Field "+field+"]";
   }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitRawSearchField(this);
   }
   
}

/*
$Log: RawSearchField.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.2.24.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.2.24.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.2  2004/08/27 09:31:16  mch
Added limit, order by, some page tidying, etc

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

