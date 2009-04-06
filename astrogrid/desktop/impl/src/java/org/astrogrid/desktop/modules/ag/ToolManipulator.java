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

/** Utilities for manipulating CEA Tool documents.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 16, 20083:45:08 PM
 */
public class ToolManipulator {


    /**
     * @param applicationsImpl
     */
    public ToolManipulator() {
    }

    public static ParameterBean findParameter(final ParameterBean[] arr,final String name) {
    	for (int i =0; i < arr.length; i++) {
    		if (arr[i].getName().equals(name)){
    			return arr[i];
    		}
    	}
    	return null;
    }

    public static InterfaceBean findInterface(final String interfaceName, final InterfaceBean[] ifaces) {
    	InterfaceBean iface = null;
    	for (int i = 0; i < ifaces.length; i++) {
    	    if (ifaces[i].getName().equals(interfaceName)) {
    	        iface =ifaces[i];
    	    }
    	}
    	return iface;
    }

    public Tool createTemplateTool(final String interfaceName, final CeaApplication descr) throws IllegalArgumentException {
        if (interfaceName != null && (! interfaceName.equals("default"))) {
            final InterfaceBean[] ifaces = descr.getInterfaces();
            final InterfaceBean iface = ToolManipulator.findInterface(interfaceName, ifaces);
            if (iface == null) {
                throw new IllegalArgumentException("Cannot find interface named " + interfaceName );
            }
             return createTool(descr,iface);
        } else {
            return createTool(descr,null);
        }
    }

    private Tool createTool(final CeaApplication descr,InterfaceBean iface) {
        if (iface == null) {
            iface = descr.getInterfaces()[0];
        }
        final Tool t = new Tool();
        t.setInterface(iface.getName());
        t.setName(descr.getId().getAuthority() + descr.getId().getPath()); //@todo should I drop the 'ivo' part.? - yes. for now. until cea accept this
        final Input input = new Input();
        final Output output = new Output();
        t.setInput(input);
        t.setOutput(output);
        
        // populate inputs
        ParameterReferenceBean[] refs = iface.getInputs();
        final ParameterBean[] parameters = descr.getParameters();
        ParameterValue[] arr = new ParameterValue[refs.length];
        for (int i = 0; i < refs.length; i++) {
            final ParameterBean pb =ToolManipulator.findParameter(parameters,refs[i].getRef());
            arr[i] = createParameterValue(pb);         
        }
        input.setParameter(arr);
        
        // populate outputs.
        refs = iface.getOutputs();
        arr = new ParameterValue[refs.length];
        for (int i = 0; i < refs.length; i++) {
            final ParameterBean pb =ToolManipulator.findParameter(parameters,refs[i].getRef());
            arr[i] = createParameterValue(pb);
            
        }   
        output.setParameter(arr);
        
        // fill in the blanks.
        return t;
    }

    private ParameterValue createParameterValue(final ParameterBean pb) {
          final ParameterValue pv = new ParameterValue();
          pv.setName(pb.getName());
          if (pb.getDefaultValue()!= null) {
              pv.setValue(pb.getDefaultValue());
          } else {
              pv.setValue("");
          }
          return pv;
          
      }

    /** returns true if this app has an adql parameter */
    public static String[] listADQLParameters(final String interfaceName,final Resource r) {
    	if (! (r instanceof CeaApplication)) {
    		return new String[0];
    	}
    	final CeaApplication info = (CeaApplication)r;
        InterfaceBean ib = null;
        final List<String> results = new ArrayList<String>();
        for (int i = 0; i < info.getInterfaces().length; i++) {        
            if (info.getInterfaces()[i].getName().equals(interfaceName)) {
                ib = info.getInterfaces()[i];
            }            
        }
        if (ib == null) {
            return new String[]{};
        }
        final ParameterBean[] arr = info.getParameters();
        for (int i =0; i < ib.getInputs().length; i++) {
            final ParameterReferenceBean prb = ib.getInputs()[i];
            final ParameterBean pb = findParameter(arr,prb.getRef());
            if (pb ==null) {
                return new String[]{};
            }
            if (pb.getType() != null && pb.getType().equalsIgnoreCase("adql")) {
                results.add(pb.getName());
            }
        }
        return results.toArray(new String[results.size()]);
        
    }

}
