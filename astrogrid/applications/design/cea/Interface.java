/*
 * $Id: Interface.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 *
 * Created on 09 August 2004 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package cea;

public class Interface {
   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   private String name;

   /**
    * @link aggregationByValue
    * @supplierCardinality 0..* 
    */
   private InputParameterValue lnkInputParameterValue;

   /**
    * @link aggregationByValue
    * @supplierCardinality 0..* 
    */
   private OutputParameterValue lnkOutputParameterValue;
}
