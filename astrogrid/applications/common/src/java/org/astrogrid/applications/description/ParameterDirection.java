/*$Id: ParameterDirection.java,v 1.1 2009/02/26 12:25:48 pah Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

/** constants class that defines parameter direction (input / output).
 * @todo dunno if this is needed really anymore s
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 *
 */
public class ParameterDirection
{
   private final String direction;
   private ParameterDirection(String dir)
   {
      direction = dir;
     
   }

   public static final ParameterDirection INPUT = new ParameterDirection("input");
   public static final ParameterDirection OUTPUT = new ParameterDirection("output");
   public static final ParameterDirection NOTFOUND = new ParameterDirection("not found");
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
public String toString() {
     return direction;

  }

}

/* 
$Log: ParameterDirection.java,v $
Revision 1.1  2009/02/26 12:25:48  pah
separate more out into cea-common for both client and server

Revision 1.4  2008/09/13 09:51:05  pah
code cleanup

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/