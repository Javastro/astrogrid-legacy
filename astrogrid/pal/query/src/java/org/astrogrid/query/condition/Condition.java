/*
 * $Id: Condition.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;


/**
 * A Condition expression is one that returns a true or false when applied.
 */

public interface Condition  {
   public void acceptVisitor(QueryVisitor visitor)  throws IOException ;
}

/*
$Log: Condition.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.62.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.62.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

