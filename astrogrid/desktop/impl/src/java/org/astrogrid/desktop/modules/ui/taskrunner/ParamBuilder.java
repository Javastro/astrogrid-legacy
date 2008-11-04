package org.astrogrid.desktop.modules.ui.taskrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ag.ToolManipulator;
import org.astrogrid.workflow.beans.v1.Tool;

/** Construct a parameter model of a cea application.
 * takes care of merging parameter values, refs and descriptions
 * into a single list of containers - saves a lot of search and iteration 
 * throughout the code to do it all once upfront/
 * @TEST
 * */
public final class ParamBuilder {
    /** a struct containing the parameter name, description and reference.*/
    public static class Param {
        /** the parameter reference */
        public final ParameterReferenceBean ref;
        /** description of the parameter */
        public final ParameterBean description;
        /** values we started off with in the original tool (loaded, from state, or template*/
        public final ParameterValue[] values; // may be many-valued.
        private Param(final ParameterReferenceBean ref, final ParameterBean description,
                final ParameterValue[] values) {
            super();
            this.ref = ref;
            this.description = description;
            this.values = values;           
        }
        /** a fiddly parameter that requires further treatment, 
         * because it's optional and/or repeated.
         * @return
         */
        public boolean isFiddly() {
            return ref.getMax() != 1 || ref.getMin() != 1 || values.length != 1;
        }
    }

    public final ParamBuilder.Param[] inputs;
    public final ParamBuilder.Param[] outputs;

    public ParamBuilder(final String iName, final CeaApplication resource,final Tool t) {
    
        final InterfaceBean iface = ToolManipulator.findInterface(iName,resource.getInterfaces());
        final Map descriptions = createDescriptionMap(resource);
        
        // inputs
        ParameterReferenceBean[] refs = iface.getInputs();
        inputs = new ParamBuilder.Param[refs.length];
        for (int i = 0; i < refs.length; i++) {
            inputs[i] = new Param(
                        refs[i]
                         ,(ParameterBean)descriptions.get(refs[i].getRef())
                         , findValues(refs[i].getRef(),t.getInput().getParameter())        
                         );
        }
        
        // outputs
       refs = iface.getOutputs();
        outputs = new ParamBuilder.Param[refs.length];
        for (int i = 0; i < refs.length; i++) {
            outputs[i] = new Param(
                        refs[i]
                         ,(ParameterBean)descriptions.get(refs[i].getRef())
                         , findValues(refs[i].getRef(),t.getOutput().getParameter())        
                         );
        }	        
    }
    
    /** find the values for this parameter in the tool document
     * will never return null, but may return an empty array.
     *  */
    private final ParameterValue[] findValues(final String pName, final ParameterValue[] vals) {
        final List l = new ArrayList();
        for (int i = 0; i < vals.length; i++) {
            if (pName.equals(vals[i].getName())){
                l.add(vals[i]);
            }
        }
        return (ParameterValue[])l.toArray(new ParameterValue[l.size()]);
    }

    private final Map createDescriptionMap(final CeaApplication applicationResource) {
        final ParameterBean[] descriptions = applicationResource.getParameters();
        final Map descriptionMap = new HashMap();
        for (int i = 0; i < descriptions.length; i++) {
            descriptionMap.put(descriptions[i].getName(),descriptions[i]);
        }
        return descriptionMap;
    }
}