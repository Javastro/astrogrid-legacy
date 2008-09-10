/* $Id: Preprocessor.java,v 1.1 2008/09/10 23:27:19 pah Exp $
 * Created on Aug 10, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.applications.http.script;

import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.impl.WebHttpApplicationSetup;
import org.astrogrid.applications.description.impl.WebHttpCall;
import org.astrogrid.applications.http.exceptions.HttpParameterProcessingException;

/**
 * Processes the tool and application documents into a calling document.
 * The tool document contains info on which interface is being used, the
 * actual parameter values etc, while the applicaiton document will contain
 * info about the app itself, such as its URL.  These used to construct (e.g. by xslt)
 * a simple calling document containing all the info that the HttpClient requires. 
 * @author jdt
 * 
 */
public interface Preprocessor {
    public WebHttpCall process(Tool tool, WebHttpApplicationSetup appSetup) throws HttpParameterProcessingException;
}
