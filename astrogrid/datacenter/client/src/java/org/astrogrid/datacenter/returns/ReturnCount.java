/*
 * $Id: ReturnCount.java,v 1.3 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import org.astrogrid.datacenter.query.condition.NumericExpression;
import org.astrogrid.slinger.targets.TargetIndicator;


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
 Revision 1.3  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/10/05 19:07:19  mch
 For returning the number of matches not the matches



 */



