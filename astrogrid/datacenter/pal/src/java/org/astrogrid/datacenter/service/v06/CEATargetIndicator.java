/*$Id: CEATargetIndicator.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.datacenter.returns.TargetIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;

/**Extended TargetIndirector, that redirects results of query into cea framework.
 * <p/> This is so the same CEA libraries can be used in the datacenter implementation as in the other cea servers
 * Works by setting up a nio Pipe - so results from querier are piped into cea system,
 * where they can be disposed of using the cea library framework.
 * <p />
 * As the datacenter framework can't know in general when to close the stream (as it depends on its use), the output stream into the pipe
 * must be called by the cea framework.
 * @todo check pipe limits, etc
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class CEATargetIndicator extends TargetIndicator {

    /** need to have a factory method*/
    public static CEATargetIndicator newInstance() throws IOException {
        Pipe pipe = Pipe.open();
        return new CEATargetIndicator(pipe);
    }

    private CEATargetIndicator(Pipe pipe) throws IOException {       
        super(Channels.newWriter(pipe.sink(),"UTF-8"));
        this.pipe = pipe;
    }
    protected final Pipe pipe;
    public InputStream getStream() {
        return Channels.newInputStream(pipe.source());
    }
    
    
}


/* 
$Log: CEATargetIndicator.java,v $
Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.4  2004/08/25 23:38:34  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.3  2004/08/17 20:19:36  mch
Moved TargetIndicator to client

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
