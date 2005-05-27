/*$Id$
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.xdbserver.xql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.RawPipeResults;
import org.astrogrid.io.mime.MimeTypes;

/**
 * A results wrapper around an XML document stream
 *
 */
public class XmlResults extends RawPipeResults {
   
    /**  Std Constructor. xmlIn is a stream containing the xml document
     */
    public XmlResults(Querier querier, InputStream xmlIn) {
      super(querier, xmlIn, MimeTypes.XML);
    }

    /**  Constructor where the xml doc is in the given File
     */
    public XmlResults(Querier querier, File xmlFile) throws IOException {
      this(querier, new FileInputStream(xmlFile));
    }

    public static String[] listFormats() {
       return new String[] { MimeTypes.XML };
    }
    
}


/*
$Log$
Revision 1.2  2005/05/27 16:21:18  clq2
mchv_1

Revision 1.1.24.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.24.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 
*/
