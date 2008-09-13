/* $Id: IdentityPreprocessor.java,v 1.2 2008/09/13 09:51:04 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Aug 11, 2004

 */
package org.astrogrid.applications.http.script;

import org.astrogrid.applications.description.impl.WebHttpApplicationSetup;
import org.astrogrid.applications.description.impl.WebHttpCall;
import org.astrogrid.applications.description.impl.WebHttpCall.SimpleParameter;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;

/**
 * A default preprocessor that simply programatically changes the format from Tool and CeaHttpApplicationDefinition to WebHttpCall
 * without any actual processing.  Should be deprecated by the default version of XSLTPreprocessor, if I can
 * get it working.
 * @author jdt
 */
public final class IdentityPreprocessor implements Preprocessor {

    /* (non-Javadoc)
     * @see org.astrogrid.applications.http.script.Preprocessor#process(org.astrogrid.workflow.beans.v1.Tool, org.astrogrid.registry.beans.cea.CeaHttpApplicationDefinition)
     */
    public org.astrogrid.applications.description.impl.WebHttpCall process(final Tool tool, final WebHttpApplicationSetup appSetup) {
        final WebHttpCall webCall = new WebHttpCall();
        
	webCall.setURL(appSetup.getURL());
        ListOfParameterValues inputs = tool.getInput();
      
        for (ParameterValue parameter : inputs.getParameter()) {
	    
           final SimpleParameter simpleParameter = new ConstructableSimpleParameter(parameter);
            webCall.getSimpleParameter().add(simpleParameter);
        }
        
        return webCall;
    }
    
    /**
     * Just another SimpleParameter (which is generated) with an extra constructor 
     * @author jdt
     */
    private static class ConstructableSimpleParameter extends SimpleParameter {
        public ConstructableSimpleParameter(ParameterValue parameter) {
            this.setName(parameter.getId().trim());
            this.setValue(parameter.getValue().trim());
        }
    }

}
