/*$Id: DefaultTransformers.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 11-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.impl.scripting.ScriptedSchedulerImpl.Transformers;

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
public class DefaultTransformers implements Transformers, ComponentDescriptor {
    /** Construct a new DefaultTransformers
     * 
     */
    public DefaultTransformers() throws TransformerConfigurationException {
        TransformerFactory fac = TransformerFactory.newInstance();
        Source compilerSource = new StreamSource(this.getClass().getResourceAsStream(COMPILER_XSL));        
        compilerTemplate = fac.newTemplates(compilerSource);
        Source translatorSource = new StreamSource(this.getClass().getResourceAsStream(TRANSLATOR_XSL));
        translatorTemplate = fac.newTemplates(translatorSource);
        Source annotatorSource = new StreamSource(this.getClass().getResourceAsStream(ANNOTATOR_XSL));
        annotatorTemplate = fac.newTemplates(annotatorSource);
    }
    public static final String COMPILER_XSL = "workflow-compiler.xsl";
    public static final String TRANSLATOR_XSL = "workflow-translator.xsl";
    public static final String ANNOTATOR_XSL = "id-annotate.xsl";
    
    protected final Templates compilerTemplate;
    protected final Templates translatorTemplate;
    protected final Templates annotatorTemplate; 
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.ScriptedSchedulerImpl.Transformers#getCompiler()
     */
    public Transformer getCompiler() throws TransformerConfigurationException {
        return compilerTemplate.newTransformer();
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.ScriptedSchedulerImpl.Transformers#getTranslator()
     */
    public Transformer getTranslator() throws TransformerConfigurationException {
        return translatorTemplate.newTransformer();
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
        return "Default transformers";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "XSLT transformers used in Scripted Scheduler impl\n"
         + this.getClass().getResource(COMPILER_XSL).toString() + "\n"
         + this.getClass().getResource(TRANSLATOR_XSL).toString() + "\n"
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
$Log: DefaultTransformers.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/