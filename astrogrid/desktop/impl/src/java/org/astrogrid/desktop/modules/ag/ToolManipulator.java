/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.util.ArrayList;
import java.util.List;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

/** methods to create and manipulate tool documents.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 16, 20083:45:08 PM
 */
public class ToolManipulator {


    /**
     * @param applicationsImpl
     */
    public ToolManipulator() {
    }

    public static ParameterBean findParameter(ParameterBean[] arr,String name) {
    	for (int i =0; i < arr.length; i++) {
    		if (arr[i].getName().equals(name)){
    			return arr[i];
    		}
    	}
    	return null;
    }

    public static InterfaceBean findInterface(String interfaceName, InterfaceBean[] ifaces) {
    	InterfaceBean iface = null;
    	for (int i = 0; i < ifaces.length; i++) {
    	    if (ifaces[i].getName().equals(interfaceName)) {
    	        iface =ifaces[i];
    	    }
    	}
    	return iface;
    }

    public Tool createTemplateTool(String interfaceName, CeaApplication descr) throws IllegalArgumentException {
        if (interfaceName != null && (! interfaceName.equals("default"))) {
            InterfaceBean[] ifaces = descr.getInterfaces();
            InterfaceBean iface = ToolManipulator.findInterface(interfaceName, ifaces);
            if (iface == null) {
                throw new IllegalArgumentException("Cannot find interface named " + interfaceName );
            }
             return createTool(descr,iface);
        } else {
            return createTool(descr,null);
        }
    }

    private Tool createTool(CeaApplication descr,InterfaceBean iface) {
        if (iface == null) {
            iface = descr.getInterfaces()[0];
        }
        Tool t = new Tool();
        t.setInterface(iface.getName());
        t.setName(descr.getId().getAuthority() + descr.getId().getPath()); //@todo should I drop the 'ivo' part.? - yes. for now. until cea accept this
        Input input = new Input();
        Output output = new Output();
        t.setInput(input);
        t.setOutput(output);
        
        // populate inputs
        ParameterReferenceBean[] refs = iface.getInputs();
        ParameterBean[] parameters = descr.getParameters();
        ParameterValue[] arr = new ParameterValue[refs.length];
        for (int i = 0; i < refs.length; i++) {
            ParameterBean pb =ToolManipulator.findParameter(parameters,refs[i].getRef());
            arr[i] = createParameterValue(pb);         
        }
        input.setParameter(arr);
        
        // populate outputs.
        refs = iface.getOutputs();
        arr = new ParameterValue[refs.length];
        for (int i = 0; i < refs.length; i++) {
            ParameterBean pb =ToolManipulator.findParameter(parameters,refs[i].getRef());
            arr[i] = createParameterValue(pb);
            
        }   
        output.setParameter(arr);
        
        // fill in the blanks.
        return t;
    }

    private ParameterValue createParameterValue(ParameterBean pb) {
          ParameterValue pv = new ParameterValue();
          pv.setName(pb.getName());
          if (pb.getDefaultValue()!= null) {
              pv.setValue(pb.getDefaultValue());
          } else {
              pv.setValue("");
          }
          return pv;
          
      }

    /** returns true if this app has an adql parameter */
    public static String[] listADQLParameters(String interfaceName,Resource r) {
    	if (! (r instanceof CeaApplication)) {
    		return new String[0];
    	}
    	CeaApplication info = (CeaApplication)r;
        InterfaceBean ib = null;
        List results = new ArrayList();
        for (int i = 0; i < info.getInterfaces().length; i++) {        
            if (info.getInterfaces()[i].getName().equals(interfaceName)) {
                ib = info.getInterfaces()[i];
            }            
        }
        if (ib == null) {
            return new String[]{};
        }
        ParameterBean[] arr = info.getParameters();
        for (int i =0; i < ib.getInputs().length; i++) {
            ParameterReferenceBean prb = ib.getInputs()[i];
            ParameterBean pb = findParameter(arr,prb.getRef());
            if (pb ==null) {
                return new String[]{};
            }
            if (pb.getType() != null && pb.getType().equalsIgnoreCase("adql")) {
                results.add(pb.getName());
            }
        }
        return (String[])results.toArray(new String[results.size()]);
        
    }

}
