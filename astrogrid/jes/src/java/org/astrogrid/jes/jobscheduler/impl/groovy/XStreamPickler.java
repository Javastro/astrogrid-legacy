/*$Id: XStreamPickler.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 28-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Reader;
import java.io.Writer;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class XStreamPickler implements Pickler {

    /** Construct a new XStreamPickler
     * 
     */
    public XStreamPickler() {
        xstream = new XStream(new PureJavaReflectionProvider(),new DomDriver());
        xstream.alias("interpreter",GroovyInterpreter.class);
        xstream.alias("rules",RuleStore.class);
        xstream.alias("rule",Rule.class);
        xstream.alias("state",ActivityStatus.class);
        xstream.alias("states",ActivityStatusStore.class);
        xstream.alias("vars",Vars.class);
    }
    protected final XStream xstream;

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#marshallInterpreter(java.io.Writer, org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreter)
     */
    public void marshallInterpreter(Writer out, GroovyInterpreter interp) {
        xstream.toXML(interp,out);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#unmarshallInterpreter(java.io.Reader)
     */
    public GroovyInterpreter unmarshallInterpreter(Reader in){
        return (GroovyInterpreter)xstream.fromXML(in);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#unmarshallRuleStore(java.io.Reader)
     */
    public RuleStore unmarshallRuleStore(Reader reader)  {
        return (RuleStore)xstream.fromXML(reader);
    }

}


/* 
$Log: XStreamPickler.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/