/*
 * $Id: SimpleError.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;

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
Revision 1.1  2005/02/17 18:37:35  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */

