/*
 * $Id: PrecannedResults.java,v 1.1.1.1 2009/05/13 13:20:52 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.test;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.VotableInResults;

/**
 * For testing only; returns a dummy votable
 *
 * @author M Hill
 */

public class PrecannedResults extends VotableInResults {
   
   private String id = null;

   /**
    * Create with some identifying mark (eg timestamp) if you want to distinguish
    * it from other results.  Loads example votable that should eb in the same
    * package
    */
   public PrecannedResults(Querier parentQuerier, String someIdentifyingMark)
   {
      super(parentQuerier, PrecannedResults.class.getResourceAsStream("ExampleVotable.xml"));
      this.id = someIdentifyingMark;

//      if (getExampleStream() == null)
//         throw new RuntimeException("Could not find example votable");
   }

   /** Returns any old number */
   public int getCount() {
      return 15;
   }
   


 }
