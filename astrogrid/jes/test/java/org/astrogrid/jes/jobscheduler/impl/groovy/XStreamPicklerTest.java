/*$Id: XStreamPicklerTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class XStreamPicklerTest extends TestCase {
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        pickler = new XStreamPickler();
        interp = new GroovyInterpreter();
        rule = new Rule();
        rule.setBody("foo\n bar \n\t boo; i< 10 && x > 1");
        rule.setEnvId("neep");
        rule.setName("a rule");
        rule.setTrigger("trigger code here");
        interp.addRule(rule);
        actStatus = new ActivityStatus();
        Vars b = new Vars();
        b.set("name","value");
        b.set("number",new Integer(3));
        b.set("url",new URL("http://www.wibble.com"));
        actStatus.setEnv(b);
        actStatus.setKey("foo");
        actStatus.setStatus(Status.ERROR);
        interp.stateMap.states.put(actStatus.getKey(),actStatus);
        interp.setJesInterface(new MockJes()); 
    }
    protected ActivityStatus actStatus;
    protected Rule rule;
    protected XStreamPickler pickler;
    protected GroovyInterpreter interp;

    public void testRoundTripInterpreter() throws Exception {
        StringWriter out = new StringWriter();
        pickler.marshallInterpreter(out,interp);
        out.close();
        System.out.println(out.toString());
        assertNotNull(out.toString());
        assertTrue(out.toString().length() > 0);
        StringReader in = new StringReader(out.toString());
        GroovyInterpreter interp1 = pickler.unmarshallInterpreter(in);
        assertNotNull(interp1);
        assertEquals(interp,interp1); 
    }

    public void testRoundTripRuleStore() throws IOException  {
        RuleStore rs = interp.ruleStore;
        StringWriter out = new StringWriter();
        pickler.xstream.toXML(rs,out);
        out.close();
        System.out.println(out.toString());
        StringReader in = new StringReader(out.toString());
        RuleStore rs1 = pickler.unmarshallRuleStore(in);
        assertNotNull(rs1);
        assertEquals(rs,rs1);
    }
    
}


/* 
$Log: XStreamPicklerTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.2  2004/07/27 23:50:09  nw
removed betwixt (duff). replaces with xstream.

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/