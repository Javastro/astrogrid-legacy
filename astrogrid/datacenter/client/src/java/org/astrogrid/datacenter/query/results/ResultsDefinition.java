/*
 * $Id: ResultsDefinition.java,v 1.1 2004/08/13 08:52:23 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.results;

import org.astrogrid.datacenter.queriers.TargetIndicator;


/**
 * Abstract class containing those common things to defining what the results
 * will look like
 *
 * @author M Hill
 */

public abstract class ResultsDefinition  {

   TargetIndicator target = null;
   
   public void setTarget(TargetIndicator aTarget)  { this.target = aTarget; }

   public TargetIndicator getTarget()              { return this.target; }

}
/*
 $Log: ResultsDefinition.java,v $
 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages



 */



