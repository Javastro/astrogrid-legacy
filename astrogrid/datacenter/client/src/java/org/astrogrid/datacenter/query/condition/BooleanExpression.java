/*
 * $Id: BooleanExpression.java,v 1.1 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A Boolean Expression is one that returns a true/false result.  Because of this
 * it's also a Condition.
 */

public interface BooleanExpression extends Condition  {

}

/*
$Log: BooleanExpression.java,v $
Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */

