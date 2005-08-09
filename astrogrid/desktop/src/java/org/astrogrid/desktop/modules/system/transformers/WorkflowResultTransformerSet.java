/*$Id: WorkflowResultTransformerSet.java,v 1.5 2005/08/09 17:33:07 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;


/** Special case for a workflow - apply a stylesheet to make it look all lovely.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class WorkflowResultTransformerSet extends DocumentResultTransformerSet {

    /** Construct a new WorkflowResultTransformerSet
     * 
     */
    public WorkflowResultTransformerSet() {
        super();
        setHtmlTransformer( Workflow2XhtmlTransformer.getInstance());
    }

    public static class Workflow2XhtmlTransformer extends Xml2XhtmlTransformer {
        protected Workflow2XhtmlTransformer(Source styleSource) throws TransformerConfigurationException {
            super(styleSource);
    }
        public static Source getStyleSource() {
            return new StreamSource(WorkflowResultTransformerSet.class.getResourceAsStream("workflow.xsl"));
        }
        
        private static Transformer theInstance;
        public static Transformer getInstance() {
            if (theInstance == null) {
                try {
                    Source styleSoutce = getStyleSource();
                    theInstance = new Xml2XhtmlTransformer(styleSoutce);
                } catch (Exception e) {
                    logger.error("Could not load stylesheet ",e);
                    theInstance = IDTransformer.getInstance();
                }                
            }
            return theInstance;
        }
    }
    
}


/* 
$Log: WorkflowResultTransformerSet.java,v $
Revision 1.5  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.4  2005/05/12 15:59:12  clq2
nww 1111 again

Revision 1.2.20.1  2005/05/11 14:25:23  nw
javadoc, improved result transformers for xml

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/