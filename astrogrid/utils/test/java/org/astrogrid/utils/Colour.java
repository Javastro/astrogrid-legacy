/*
 * $Id: Colour.java,v 1.1 2005/03/22 13:00:41 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.utils;


/**
 * For testing TypeSafeEnumerator
 *
 * @author M Hill
 */

public class Colour extends TypeSafeEnumerator
{
   public static final Colour RED = new Colour("Red", "#xxxxx");
   public static final Colour BLUE = new Colour("Blue", "#xxxxx");
   public static final Colour GREEN = new Colour("Green", "#xxxxx");

    
   public Colour(String text, String hash) {
       super(text);
   }
}

/*
$Log: Colour.java,v $
Revision 1.1  2005/03/22 13:00:41  mch
Seperated utils from common

Revision 1.1  2004/03/01 22:59:29  mch
Increased test coverage

 */

