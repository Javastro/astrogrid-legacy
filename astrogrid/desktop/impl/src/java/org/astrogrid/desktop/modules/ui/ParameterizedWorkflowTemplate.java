/*$Id: ParameterizedWorkflowTemplate.java,v 1.1 2005/09/12 15:21:16 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.modules.ag.ApplicationsImpl;
import org.astrogrid.desktop.modules.ag.builders.ApplicationInformationBuilder;
import org.astrogrid.desktop.modules.ag.builders.InformationBuilder;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.CachedXPathAPI;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * data class that holds all the info and metadata for a parameterized workflow template 
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
class ParameterizedWorkflowTemplate  {


    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterizedWorkflowTemplate.class);
    public ParameterizedWorkflowTemplate(InputStream is)  throws Exception{
        Document dom = XMLUtils.newDocument(is);           
        this.theWorkflow = (Workflow)Unmarshaller.unmarshal(Workflow.class,dom);     
        NodeList l = dom.getChildNodes();
        ProcessingInstruction pi = null;
        for (int i =0; i < l.getLength(); i++) {
            if (l.item(i).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                ProcessingInstruction candidate = (ProcessingInstruction)l.item(i);
                if (candidate.getTarget().equals("parameterized-workflow")) {
                    pi = candidate;
                }
            }
        }
        if (pi == null) {
            throw new IllegalArgumentException("Could not find required processing instruction");
        }
        logger.debug(pi.getData());
        Document regEntry = XMLUtils.newDocument(new ByteArrayInputStream(pi.getData().getBytes()));
        CachedXPathAPI xpath = new CachedXPathAPI();
        desc =(ApplicationInformation)builder.build(xpath,regEntry.getDocumentElement());
        
    }
    
    private static final String VR_NAMESPACE = "http://www.ivoa.net/xml/VOResource/v0.10";
    private final InformationBuilder builder = new ApplicationInformationBuilder();

    
    private final ApplicationInformation desc;
    private final Workflow theWorkflow;
   
  
    public ApplicationInformation getDesc() {
        return this.desc;
    }
    /**
     * @param account
     * @param t
     * @return
     */
    public Workflow instantiate(Account account, Tool t) {
        Workflow wf = makeClone();
        wf.getCredentials().setAccount(account);
        // do something about group here?
        List newActs = new ArrayList();
        // make a 'set' for each input parameter
        for (Enumeration e = t.getInput().enumerateParameter(); e.hasMoreElements(); ) {
            ParameterValue v = (ParameterValue)e.nextElement();
            Set s = new Set();
            s.setVar(v.getName());
            s.setValue(v.getValue());
            newActs.add(s);
        }
        // now do for each output parameter.
        for (Enumeration e = t.getOutput().enumerateParameter(); e.hasMoreElements(); ) {
            ParameterValue v = (ParameterValue)e.nextElement();
            Set s = new Set();
            s.setVar(v.getName());           
            s.setValue(v.getValue());
            newActs.add(s);
        }        
        // splice all together..
        AbstractActivity[] acts = wf.getSequence().getActivity();
        newActs.addAll(Arrays.asList(acts));
       wf.getSequence().setActivity(
               (AbstractActivity[])newActs.toArray(new AbstractActivity[]{})
               );
        return wf;
    }
    /** take a copy of the cached workflow.
     * bugfix for 1072
     * @throws ValidationException
     * @throws MarshalException
     * @throws IOException
     */
    private Workflow makeClone() throws RuntimeException {
        Writer sw = new StringWriter();
        try {
            theWorkflow.marshal(sw);
            sw.close();
            return Workflow.unmarshalWorkflow(new StringReader(sw.toString()));
        } catch (Exception e) {
            logger.error("Could not clone template",e);
            throw new RuntimeException("Could not clone template",e);
        }
        
        
        
    }
 
    public String toString() {
        return desc.getName() + " : " +desc.getDescription(); 
    }
}

/* 
$Log: ParameterizedWorkflowTemplate.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/27 13:42:40  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:53:46  nw
tidied up a little.
added 'save workflow' function

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/