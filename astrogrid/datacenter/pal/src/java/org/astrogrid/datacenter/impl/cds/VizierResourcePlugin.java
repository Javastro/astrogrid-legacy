/*$Id: VizierResourcePlugin.java,v 1.2 2004/10/18 13:11:30 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.cds;

import java.io.IOException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.impl.cds.vizier.VizierDelegate;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;

/** returns resources for Vizier service
 */
public class VizierResourcePlugin implements VoResourcePlugin {

   
   /**
    * Returns the VOResource element of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException {
      try {
         VizierDelegate delegate = new VizierDelegate();
         String votableHeader = delegate.getAllMetadata();
         //do something to it to make it a Resource
         //@todo
         //return it
         return new String[] { votableHeader };
      }
      catch (ServiceException e) {
         throw new IOException("Could not connect to Vizier:"+e);
      }

   }
   
   
}


/*
 $Log: VizierResourcePlugin.java,v $
 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.4.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/10/05 20:26:43  mch
 Prepared for better resource metadata generators

 Revision 1.1  2004/10/05 19:19:18  mch
 Merged CDS implementation into PAL

 Revision 1.5  2004/09/29 18:45:55  mch
 Bringing Vizier into line with new(er) metadata stuff

 Revision 1.4  2004/08/14 14:35:42  acd
 Fix the cone search in the Vizier Proxy.

 Revision 1.4  2004/08/13 16:50:00  acd
 Added static final String METADATA.

 Revision 1.3  2004/08/12 17:31:00  acd
 Added static final String CATALOGUE_NAME.

 Revision 1.2  2004/03/14 04:14:20  mch
 Wrapped output target in TargetIndicator

 Revision 1.1  2004/03/13 23:40:59  mch
 Changes to adapt to It05 refactor

 Revision 1.6  2003/12/09 16:25:08  nw
 wrote plugin documentation

 Revision 1.5  2003/12/01 16:50:11  nw
 first working tested version

 Revision 1.4  2003/11/28 19:12:16  nw
 getting there..

 Revision 1.3  2003/11/25 11:14:51  nw
 upgraded to new service interface

 Revision 1.2  2003/11/20 15:47:18  nw
 improved testing

 Revision 1.1  2003/11/18 11:23:49  nw
 mavenized cds delegate

 Revision 1.1  2003/11/18 11:10:05  nw
 mavenized cds delegate
 
 */
