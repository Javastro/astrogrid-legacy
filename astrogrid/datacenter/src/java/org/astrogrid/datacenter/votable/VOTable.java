/*
 * @(#)VOTable.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.votable;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.query.Query;

/**
 * after refactoring, this class is withering.*/
public class VOTable {

   private static final boolean
      TRACE_ENABLED = true ;

   private static final String
      SUBCOMPONENT_NAME = Util.getComponentName( VOTable.class ) ;

   private static Logger
      logger = Logger.getLogger( VOTable.class ) ;



   private static String
      VOTABLEFACTORY_KEY = "VOTABLEFACTORY" ;

   private Query
      query = null ;


   public VOTable(Query query) {
      this.query = query ;
   }

} // end of class VOTable
