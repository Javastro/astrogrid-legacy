/*
 * $Id: SimpleError.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

/**
 * Represents an absolute simple error; ie +/- x. Assumed to be in the
 * same units & scale as associated value
 *
 * @author M Hill
 */

public class SimpleError implements ValueError
{
   private double error;
   
   public SimpleError(double givenAbsoluteError) {
      this.error = givenAbsoluteError;
   }

   public double getAbsoluteError() {
      return error;
   }
   
}

/*
$Log: SimpleError.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */

