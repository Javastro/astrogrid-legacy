/*
 * $Id: NumericExpression.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A Numeric Expression is one that, when evaluated, returns a number
 */

public interface NumericExpression  extends Expression {

}

/*
$Log: NumericExpression.java,v $
Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc



 */

