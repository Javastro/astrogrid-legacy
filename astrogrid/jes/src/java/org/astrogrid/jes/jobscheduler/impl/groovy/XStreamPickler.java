/*$Id: XStreamPickler.java,v 1.6 2004/09/16 21:43:47 nw Exp $
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
import java.util.ArrayList;
import java.util.List;

/** pickler implementation.
 * @see 
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class XStreamPickler implements Pickler {


    /** made xstream static, for efficiencies sake - don't know how expensive it is to create this object,
     * but only one instance is ever needed, and is only ever called from the scheduler thread.
     * 
     * <p>
     * but then got concerned about keeping a 3rd part object around indefinately- no idea how big it is, or whether it 
     * leaks. Decided to use a 'pooling' pattern - resuse each instance a few times, and then create a new one.
     */
    public XStreamPickler() {
        createXstream();
    }
    private  XStream xstream;
    // number of times this xstream has been used.
    private int useCount = 0;
    // limit on number of uses.
    private static final int USE_LIMIT = 20;
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#marshallInterpreter(java.io.Writer, org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreter)
     */
    public void marshallInterpreter(Writer out, GroovyInterpreter interp) {
        getXstream().toXML(interp,out);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#unmarshallInterpreter(java.io.Reader)
     */
    public GroovyInterpreter unmarshallInterpreter(Reader in){
        return (GroovyInterpreter)getXstream().fromXML(in);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler#unmarshallRuleStore(java.io.Reader)
     */
    public List unmarshallRuleStore(Reader reader)  {
        return (List)getXstream().fromXML(reader);
    }

    XStream getXstream() {
        if (useCount++ > USE_LIMIT) {
            useCount = 0;
            createXstream();
        }
        return xstream;
    }
    private void createXstream() {
        xstream = new XStream(new PureJavaReflectionProvider(),new DomDriver());
        getXstream().alias("interpreter",GroovyInterpreter.class);
        getXstream().alias("rules",ArrayList.class);
        getXstream().alias("rule",Rule.class);
        getXstream().alias("state",ActivityStatus.class);
        getXstream().alias("states",ActivityStatusStore.class);
        getXstream().alias("vars",Vars.class);        
    }
}


/* 
$Log: XStreamPickler.java,v $
Revision 1.6  2004/09/16 21:43:47  nw
made 3rd-party objects only persist for so many calls. - in case they're space leaking.

Revision 1.5  2004/09/06 16:47:04  nw
javadoc

Revision 1.4  2004/08/09 17:32:02  nw
updated due to removing RuleStore

Revision 1.3  2004/08/05 07:39:37  nw
made xstream static for same reasons

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