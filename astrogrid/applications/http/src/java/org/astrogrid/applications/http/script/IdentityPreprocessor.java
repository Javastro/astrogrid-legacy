/* $Id: IdentityPreprocessor.java,v 1.4 2007/08/29 08:47:46 gtr Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 11, 2004

 */
package org.astrogrid.applications.http.script;

import java.util.Enumeration;

import org.astrogrid.applications.beans.v1.SimpleParameter;
import org.astrogrid.applications.beans.v1.WebHttpCall;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * A default preprocessor that simply programatically changes the format from Tool and CeaHttpApplicationType to WebHttpCall
 * without any actual processing.  Should be deprecated by the default version of XSLTPreprocessor, if I can
 * get it working.
 * @author jdt
 */
public final class IdentityPreprocessor implements Preprocessor {

    /* (non-Javadoc)
     * @see org.astrogrid.applications.http.script.Preprocessor#process(org.astrogrid.workflow.beans.v1.Tool, org.astrogrid.registry.beans.cea.CeaHttpApplicationType)
     */
    public WebHttpCall process(final Tool tool, final CeaHttpApplicationType app) {
        final WebHttpCall webCall = new WebHttpCall();
        webCall.setURL(app.getCeaHttpAdapterSetup().getURL());
        final Input inputs = tool.getInput();
        final Enumeration en = inputs.enumerateParameter();

        while (en.hasMoreElements()) {
            final ParameterValue parameter = (ParameterValue) en.nextElement();
            final SimpleParameter simpleParameter = new ConstructableSimpleParameter(parameter);
            webCall.addSimpleParameter(simpleParameter);
        }
        
        return webCall;
    }
    
    /**
     * Just another SimpleParameter (which is generated) with an extra constructor 
     * @author jdt
     */
    private static class ConstructableSimpleParameter extends SimpleParameter {
        public ConstructableSimpleParameter(ParameterValue parameter) {
            this.setName(parameter.getName().trim());
            this.setValue(parameter.getValue().trim());
        }
    }

}
