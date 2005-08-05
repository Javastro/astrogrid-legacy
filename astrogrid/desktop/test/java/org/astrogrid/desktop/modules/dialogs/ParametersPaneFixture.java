/*$Id: ParametersPaneFixture.java,v 1.3 2005/08/05 11:46:55 nw Exp $
 * Created on 14-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.Input;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.Output;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.Parameters;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.beans.v1.types.ApplicationKindType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import javax.swing.JDialog;

import junit.framework.TestCase;

/** not a real test - just a fuxtyre 
 * @author Noel Winstanley nw@jb.man.ac.uk 14-May-2005
 *
 */
public class ParametersPaneFixture extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InputStream is  = this.getClass().getResourceAsStream("tool.xml");
        assertNotNull(is);
        Reader reader = new InputStreamReader(is);
        t = Tool.unmarshalTool(reader);
        ApplicationBase appBase = new ApplicationBase();
        appBase.setApplicationType(ApplicationKindType.CMDLINE);
        appBase.setName("astrogrid.mssl/trace_dsa");
        Parameters parameters = new Parameters();
        BaseParameterDefinition query = new BaseParameterDefinition();
        query.setName("Query");
        query.setDefaultValue("ADQL Query");
        query.setType(ParameterTypes.ADQL);
        query.setUI_Name("Query");
        BaseParameterDefinition format = new BaseParameterDefinition();
        format.setName("Format");
        format.setDefaultValue("VOTABLE");
        format.setType(ParameterTypes.TEXT);;
        format.setUI_Name("Result format");    
        BaseParameterDefinition result = new BaseParameterDefinition();
        result.setName("Result");
        result.setDefaultValue("");
        result.setType(ParameterTypes.ANYURI);
        result.setUI_Name("Result location");    
        parameters.setParameter(new BaseParameterDefinition[]{
                query, format, result
        });
        appBase.setParameters(parameters);
        InterfacesType interfaces = new InterfacesType();
        Interface iface = new Interface();
        iface.setName("adql");
        Input input = new Input();
        iface.setInput(input);
        Output output = new Output();
        ParameterRef q = new ParameterRef();
        q.setRef("Query");
        ParameterRef f= new ParameterRef();
        f.setRef("Format");
        input.setPref(new ParameterRef[]{
                q,f
        });
        iface.setOutput(output);
        ParameterRef r = new ParameterRef();
        r.setRef("Result");
        output.setPref(new ParameterRef[]{
                r
        });
        interfaces.set_interface(new Interface[]{
                iface
        });
        appBase.setInterfaces(interfaces);
        desc = new ApplicationDescription(appBase);        
       
    }
    Tool t;
    ApplicationDescription desc;    
    
    public void testDisplayParametersPane() throws Exception {
        JDialog f = new JDialog();
        f.setSize(400,300);
        f.setModal(true);
ParametersPanel panel = new ParametersPanel(new ResourceChooserDialog(null,false));;

        panel.populate(t,desc);
        f.setContentPane(panel);
        f.show();
        
        Tool t = panel.getTool();
        StringWriter sw = new StringWriter();
        t.marshal(sw);
        System.out.println(sw);
        
    }

}


/* 
$Log: ParametersPaneFixture.java,v $
Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/