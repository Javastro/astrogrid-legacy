/*
 * $Id: MySpaceFileType.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate.myspace;

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
   
   public static final MySpaceFileType UNKNOWN = new MySpaceFileType("0", "Unknown");
   public static final MySpaceFileType FOLDER = new MySpaceFileType("1", "Folder");
   public static final MySpaceFileType VOTABLE = new MySpaceFileType("2", "Folder");
   public static final MySpaceFileType QUERY = new MySpaceFileType("3", "Folder");
   public static final MySpaceFileType WORKFLOW = new MySpaceFileType("4", "Workflow");
   public static final MySpaceFileType XML = new MySpaceFileType("5", "Workflow");

   
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
Revision 1.2  2004/06/14 23:08:52  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:20  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.3  2004/05/03 08:55:53  mch
Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

Revision 1.2  2004/04/23 11:38:19  mch
Fixes to return correct AGSL plus change to File model for It05 delegate

Revision 1.1  2004/03/04 12:51:31  mch
Moved delegate implementations into subpackages

Revision 1.2  2004/03/01 22:50:08  mch
added finals, oops

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

