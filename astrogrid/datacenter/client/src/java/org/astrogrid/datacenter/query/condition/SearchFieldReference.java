/*
 * $Id: SearchFieldReference.java,v 1.1 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * For storing a reference to a field that can be searched on.  Might be a UCD
 * or a column name, etc
 *
 * Rather naughtily implements both NumbericExpression and StringExpression,
 * whereas I think there ought to be separate types...
 */

public interface SearchFieldReference extends NumericExpression, StringExpression {
   
}

/*
$Log: SearchFieldReference.java,v $
Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */

