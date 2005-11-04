/*$Id: FixedXStreamWireFormat.java,v 1.1 2005/11/04 10:14:26 nw Exp $
 * Created on 02-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.activemq.transport.xstream.XStreamWireFormat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Nov-2005
 *
 */
public class FixedXStreamWireFormat extends XStreamWireFormat {

    /** Construct a new FixedXStreamWireFormat
     * 
     */
    public FixedXStreamWireFormat() {
        super();
        setXStream(new XStream(new Sun14ReflectionProvider()));
    }

}


/* 
$Log: FixedXStreamWireFormat.java,v $
Revision 1.1  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present
 
*/