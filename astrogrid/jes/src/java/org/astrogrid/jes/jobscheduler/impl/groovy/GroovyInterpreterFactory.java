/*$Id: GroovyInterpreterFactory.java,v 1.6 2004/11/05 16:52:42 jdt Exp $
 * Created on 14-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.Extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;

/** Factory object that takes care of creating fresh and unpickling existing groovy interpreters.
 * @see GroovyInterpreter
 * @author Noel Winstanley nw@jb.man.ac.uk 14-May-2004
 */
public class GroovyInterpreterFactory {
    /**
     * interface to a component that actually performs the serialization / deserialization in some way.
     * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
     *
     */
    public interface Pickler {
        
        /** serialize / pickle an intepreter (i.e. all hte state of an executing workflow) to a writer
         * @param out
         * @param interp
         * @throws Exception
         */
        void marshallInterpreter(Writer out, GroovyInterpreter interp) throws Exception;
        /** unmarshal a previously pickled interpreter.
         * @param in
         * @return
         * @throws Exception
         */
        GroovyInterpreter unmarshallInterpreter(Reader in) throws Exception;     
        /** unmarshal a previously pickled rulestore (subcomponent of an intepreter).
         * @param reader
         * @return
         * @throws Exception
         */
        Map unmarshallRuleStore(Reader reader)  throws Exception;        
    }
    /** construct a new factory, passing in the pickler implementatio to use */
    public GroovyInterpreterFactory(Pickler p, TemporaryBuffer buff) {
        this.pickler = p;
        this.buff = buff;
    }
    protected final Pickler pickler;
    /** @todo maybe better to soft-reference wrap this */
    protected final TemporaryBuffer buff; //
    
    /** so our buffer can be shared amongst friends.. */
    public TemporaryBuffer getBuffer() {
        return buff;
    }
    
    private static final Log log = LogFactory.getLog(GroovyInterpreterFactory.class);
    /** key used in workflow extension to store pickled workflow.
     */
    public static final String EXTENSION_KEY = "pickled.groovy.interpreter";
    static final String EXTENSION_XPATH = "jobExecutionRecord/extension[key='" +  EXTENSION_KEY+ "']";
    
  /** write a groovy interpreter back into the extension fragment of a workflow */
    public void pickleTo(GroovyInterpreter interp,Workflow wf) throws PickleException{
        // see if its already there first..
        Extension pickleJar = (Extension)wf.findXPathValue(GroovyInterpreterFactory.EXTENSION_XPATH);
        if (pickleJar == null) {
            pickleJar = new Extension();
            pickleJar.setKey(GroovyInterpreterFactory.EXTENSION_KEY);

            wf.getJobExecutionRecord().addExtension(pickleJar);
        }
        //StringWriter writer =  new StringWriter();
        buff.writeMode();
        Writer writer = buff.getWriter();
        try {
            pickler.marshallInterpreter(writer,interp);
            writer.close();
            buff.readMode();
            pickleJar.setContent(buff.getContents());
        }catch (Exception e) {
            throw new PickleException("Could not pickle interpreter",e);
        }
    }    

    /**
     * deserialize previously created workflow interpreter. */
    public GroovyInterpreter unpickleFrom(JesInterface env) throws PickleException {        
        Extension pickleJar = (Extension)env.getWorkflow().findXPathValue(GroovyInterpreterFactory.EXTENSION_XPATH);
        StringReader sr = new StringReader(pickleJar.getContent());
        try {
        GroovyInterpreter gi = pickler.unmarshallInterpreter(sr);
        gi.setJesInterface(env);
        return gi;
        } catch (Exception e) {
            throw new PickleException("Could not unpickle interpreter",e);
        } finally {
            sr.close();
        }
    }
    
    /** create a new workflow interpreter, populated with contents of rulesDoc */
    public GroovyInterpreter newInterpreter(String rulesDoc,JesInterface env) throws PickleException{
        StringReader sr = new StringReader(rulesDoc);
        try {
        Map rs = pickler.unmarshallRuleStore(sr);
        GroovyInterpreter interp = new GroovyInterpreter(rs);
        interp.setJesInterface(env);        
        return interp;
        } catch (Exception e) {
            throw new PickleException("Could not create new interpreter",e);
        } finally {
            sr.close();
        }
    }

    
}


/* 
$Log: GroovyInterpreterFactory.java,v $
Revision 1.6  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.5.18.1  2004/11/05 16:13:08  nw
replaced stringWriter with TemporaryBuffer
updated to work with new ruleStore

Revision 1.5  2004/09/16 21:47:56  nw
made sure all streams are closed

Revision 1.4  2004/09/06 16:30:25  nw
javadoc

Revision 1.3  2004/08/09 17:32:18  nw
updated due to removing RuleStore

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.

Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/