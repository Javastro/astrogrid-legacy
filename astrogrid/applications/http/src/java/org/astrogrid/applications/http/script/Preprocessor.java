/* $Id: Preprocessor.java,v 1.2 2004/09/01 15:42:26 jdt Exp $
 * Created on Aug 10, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.applications.http.script;

import org.astrogrid.applications.beans.v1.WebHttpCall;
import org.astrogrid.registry.beans.cea.CeaHttpApplicationType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * Processes the tool and application documents into a calling document.
 * The tool document contains info on which interface is being used, the
 * actual parameter values etc, while the applicaiton document will contain
 * info about the app itself, such as its URL.  These used to construct (e.g. by xslt)
 * a simple calling document containing all the info that the HttpClient requires. 
 * @author jdt
 */
public interface Preprocessor {
    public WebHttpCall process(Tool tool, CeaHttpApplicationType app);
}
