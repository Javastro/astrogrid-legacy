/*$Id: GroovyTransformers.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 11-May-2004
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

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Test;

/** Default implementation of the transformers interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 *
 */
public class GroovyTransformers implements GroovySchedulerImpl.Transformers, ComponentDescriptor {
    /** Construct a new DefaultTransformers
     * 
     */
    public GroovyTransformers() throws TransformerConfigurationException {
        TransformerFactory fac = TransformerFactory.newInstance();
        Source compilerSource = new StreamSource(this.getClass().getResourceAsStream(COMPILER_XSL));        
        compilerTemplate = fac.newTemplates(compilerSource);
        Source annotatorSource = new StreamSource(this.getClass().getResourceAsStream(ANNOTATOR_XSL));
        annotatorTemplate = fac.newTemplates(annotatorSource);
    }
    public static final String COMPILER_XSL = "workflow-compiler.xsl";
    public static final String ANNOTATOR_XSL = "id-annotate.xsl";
    
    protected final Templates compilerTemplate;
    protected final Templates annotatorTemplate; 
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.ScriptedSchedulerImpl.Transformers#getCompiler()
     */
    public Transformer getCompiler() throws TransformerConfigurationException {
        return compilerTemplate.newTransformer();
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.ScriptedSchedulerImpl.Transformers#getWorkflowAnnotator()
     */
    public Transformer getWorkflowAnnotator() throws TransformerConfigurationException {
        return annotatorTemplate.newTransformer();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Groovy transformers";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "XSLT transformers used in Groovy Scheduler impl\n"
         + this.getClass().getResource(COMPILER_XSL).toString() + "\n"
         + this.getClass().getResource(ANNOTATOR_XSL).toString();
    }
    /** @todo add test to verify all xslts are present and compile.
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: GroovyTransformers.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/27 23:37:59  nw
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