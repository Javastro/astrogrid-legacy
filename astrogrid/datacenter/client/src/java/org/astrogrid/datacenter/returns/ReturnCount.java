/*
 * $Id: ReturnCount.java,v 1.1 2004/10/05 19:07:19 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import org.astrogrid.datacenter.query.condition.NumericExpression;
import org.astrogrid.datacenter.returns.TargetIndicator;


/**
 * Used to define that we just want back the number of matches, not the matches themselves
 *
 * @author M Hill
 */

public class ReturnCount extends ReturnSpec {

   public ReturnCount(TargetIndicator aTarget) {
      setTarget(aTarget);
   }

   /** For debug & reference */
   public String toString() {
      return "[Results: target="+target+", count]";
   }
}
/*
 $Log: ReturnCount.java,v $
 Revision 1.1  2004/10/05 19:07:19  mch
 For returning the number of matches not the matches



 */



