/*
 * $Id: Application.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 *
 * Created on 09 August 2004 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package cea;

public class Application {
   public String init() {
   }

   public void execute() {
   }

   public void abort() {
   }

   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   /**
    * @link aggregationByValue
    * @supplierCardinality 1..* 
    */
   private Interface lnkInterface;
   private String name;
}
