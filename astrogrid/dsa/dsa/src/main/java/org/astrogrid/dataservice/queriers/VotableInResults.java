/*$Id: VotableInResults.java,v 1.2 2010/03/25 10:25:52 gtr Exp $
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.io.Piper;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.out.VoTableWriter;

/**
 * A results wrapper around results that are already of VOTable form; eg
 * those returned from another service, where the table is referred to by a
 * stream rather than a parsed document.
 *
 */
public class VotableInResults extends TableResults {
   
    protected final InputStream in;
   
    /**  Std Constructor. votableIn is a stream containing the votable
     */
    public VotableInResults(Querier querier, InputStream votableIn) {
      super(querier);
      this.in = votableIn;
    }

    /**  Constructor where the votable is in the given string.
     */
    public VotableInResults(Querier querier, String votableDoc) {
      super(querier);
      in = new ByteArrayInputStream(votableDoc.getBytes());
    }

    /**  Constructor where the votable is in the given File
     */
    public VotableInResults(Querier querier, File votableFile) throws IOException {
      super(querier);
      this.in = new FileInputStream(votableFile);
    }
    
    public int getCount() {
       return -1;
    }
    
   /** Writes results to the given TableWriter    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {
       if (tableWriter instanceof VoTableWriter) {
          Piper.bufferedPipe(new InputStreamReader(in), ((VoTableWriter) tableWriter).getOut());
       }
       else {
          throw new UnsupportedOperationException("Can only write to votables");
       }
   }
   
    
}