/*
 * $Id: MySpaceFileType.java,v 1.1 2004/02/24 15:59:56 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import java.util.Hashtable;
import org.astrogrid.util.TypeSafeEnumerator;

/**
 * File Types for MySpace.  Enumerated, indexed by the string returned
 * by the getDataHolding methods
 *
 * @author mch
 */


public class MySpaceFileType extends TypeSafeEnumerator
{
   private static Hashtable holdingIdx = new Hashtable();
   
   public static MySpaceFileType FOLDER = new MySpaceFileType("1", "Folder");
   public static MySpaceFileType WORKFLOW = new MySpaceFileType("0", "Workflow");
// public static MySpaceFileType QUERY = new MySpaceFileType("1", "Query");
// public static MySpaceFileType VOTABLE = new MySpaceFileType("2", "Votable");
   
   private MySpaceFileType(String holdingRef, String userDesc)
   {
      super(userDesc);
      holdingIdx.put(holdingRef, this);
   }

   public static MySpaceFileType getForHoldingRef(String holdingRef)
   {
      return (MySpaceFileType) holdingIdx.get(holdingRef);
   }
   
}

/*
$Log: MySpaceFileType.java,v $
Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

