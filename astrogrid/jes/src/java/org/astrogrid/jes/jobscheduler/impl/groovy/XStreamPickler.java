/*$Id: XStreamPickler.java,v 1.8 2004/12/03 14:47:41 jdt Exp $
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

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory.Pickler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Reader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

/** pickler implementation.
 * @modified made the reference to xstream a soft reference, so it gets collected if needed. We can always create another one - and the extra memory might just get us through.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class XStreamPickler implements Pickler , ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XStreamPickler.class);


    /** made xstream static, for efficiencies sake - don't know how expensive it is to create this object,
     * but only one instance is ever needed, and is only ever called from the scheduler thread.
     * 
     * <p>
     * but then got concerned about keeping a 3rd part object around indefinately- no idea how big it is, or whether it 
     * leaks. Decided to use a 'pooling' pattern - resuse each instance a few times, and then create a new one.
     */

    private  SoftReference xstream = new SoftReference(createXstream());
    // number of times this xstream has been used.
    private int useCount = 0;
    // limit on number of uses.
    private static final int USE_LIMIT = 500;
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
    public Map unmarshallRuleStore(Reader reader)  {
        List l =  (List )getXstream().fromXML(reader);
        Map m = new HashMap();
        for (int i = 0; i < l.size(); i++) {
            Rule r = (Rule)l.get(i);
            m.put(r.getName(),r);
        }
        return m;
    }

    XStream getXstream() {
        XStream x = (XStream)xstream.get();
        if (useCount++ > USE_LIMIT || x == null) {
            logger.info("re-creating xstream");
            useCount = 0;
            xstream.clear();
            x = createXstream();
            xstream = new SoftReference(x);
        }
        return x;
    }
    private XStream createXstream() {
        XStream x = new XStream(new PureJavaReflectionProvider(),new DomDriver());
        x.registerConverter(new RuleStoreConvertor(x.getClassMapper(),"class"));
        x.alias("interpreter",GroovyInterpreter.class);
        x.alias("rules",ArrayList.class);
        x.alias("rule",Rule.class);
        x.alias("state",ActivityStatus.class);
        x.alias("states",ActivityStatusStore.class);
        x.alias("vars",Vars.class);
        return x;
    }
    
    /** subclass of map convertor, for converting rule stores.
     * and also serialzes the indexScript.
     * dunno what I'm doing here really - came up with this via reverse-engineering and trial and error.
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2004
     *
     */
    protected static class RuleStoreConvertor extends MapConverter {

        public void marshal(Object o, HierarchicalStreamWriter w, MarshallingContext cxt) {
            RuleStore rs = (RuleStore)o;
            w.startNode("indexScript");
            if (rs.indexScript == null) {
                n.marshal(rs.indexScript,w,cxt);
            } else {
                s.marshal(rs.indexScript,w,cxt);
            }
            w.endNode();            
            super.marshal(o, w, cxt);
        }
        public Object unmarshal(HierarchicalStreamReader r, UnmarshallingContext cxt) {
            r.moveDown();            
            String index = (String)cxt.convertAnother(r.getValue(),String.class);          
            r.moveUp();
            RuleStore rs = (RuleStore) super.unmarshal(r, cxt);
            if (index.trim().length() > 0) {
                rs.indexScript = index;
            }
            return rs;
        }
        public boolean canConvert(Class arg0) {
            return arg0.equals(RuleStore.class) ;
        }
        /** Construct a new RuleStoreConvertor
         * @param arg0
         * @param arg1
         */
        public RuleStoreConvertor(ClassMapper arg0, String arg1) {
            super(arg0, arg1);
            s = new StringConverter();
            n = new NullConverter();
        }
        private final Converter s;
        private final Converter n;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "XStream Pickler";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Pickler for interpreter that uses XStream to serialize interpreter state to xml";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: XStreamPickler.java,v $
Revision 1.8  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.7.14.1  2004/12/01 21:48:53  nw
organized import

Revision 1.7  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.6.18.1  2004/11/05 16:03:27  nw
optimized: caches xstream in a soft reference.
added custom serializer for rule store

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