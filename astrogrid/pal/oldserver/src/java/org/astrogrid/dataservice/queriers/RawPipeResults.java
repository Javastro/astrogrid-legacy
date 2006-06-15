/*$Id: RawPipeResults.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.queriers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import org.astrogrid.dataservice.queriers.QueryResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.io.Piper;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.out.TableWriter;

/**
 * Pipes results as-they-are from the given inputstream to the target.
 * If you are confident that the results are already in the right form suitable
 * for delivering to the client, and you have an inputstream to them, this
 * is the appropriate results wrapper to use.  For example, if you are proxying
 * to a service that provides results in the right form, there's no point in
 * interpreting them here.  Of course it woulld be nice to send them direct if
 * possible, rather than routing them through here, but some stateless services
 * can't do this.
 *
 */
public class RawPipeResults implements QueryResults {
   
   protected final InputStream in;
   protected final String mimeType;
   protected final Querier querier;
   
    /**  Std Constructor. xmlIn is a stream containing the xml document
     */
    public RawPipeResults(Querier aQuerier, InputStream source, String sourceMimeType) {
      this.querier = aQuerier;
      this.in = source;
      this.mimeType = sourceMimeType;
    }

    /**
     * Sends as is - pipes out.  Mo status info yet...
     */
    public void send(ReturnSpec returnSpec, Principal user) throws IOException {
       
       TargetIdentifier target = returnSpec.getTarget();
       
       if (mimeType != null) {
          target.setMimeType(mimeType);
       }
       OutputStream out = target.openOutputStream();

       Piper.bufferedPipe(in, out);
    }
    
    /**
     * Don't know how to write this out in table form...
     */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {
           throw new UnsupportedOperationException("Can only write to votables");
       
   }
   

    
}


/*
$Log: RawPipeResults.java,v $
Revision 1.2  2006/06/15 16:50:10  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/25 08:48:44  kea
Moving Exist functionality from Kev's branch into oldserver for the
time being.

Revision 1.3.64.1  2006/04/24 13:49:59  clq2
merged with Pal_KMB_XMLDB

Revision 1.3.60.1  2006/02/09 13:30:25  KevinBenson
adding xmldb type war/service for the dsa

Revision 1.3  2005/05/27 16:21:02  clq2
mchv_1

Revision 1.2.16.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.2  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1.2.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.2.24.3  2004/11/30 01:04:02  mch
Rationalised tablewriters, reverted AxisDataService06 to string

Revision 1.2.24.2  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.2.24.1  2004/11/17 17:56:07  mch
set mime type, switched results to taking targets

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/09/29 18:43:51  mch
doc change

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/09/06 21:37:26  mch
Factored out VotableResults

Revision 1.3  2004/09/01 12:10:58  mch
added results.toHtml

Revision 1.2  2004/03/14 03:04:57  mch
Added CSV writer

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.2  2003/12/09 16:25:08  nw
wrote plugin documentation

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/
