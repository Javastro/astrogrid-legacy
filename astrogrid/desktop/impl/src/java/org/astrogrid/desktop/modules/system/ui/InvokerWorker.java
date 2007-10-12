/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.contributions.UIActionContribution;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
/**
 * specialized background worker that executes a ui-contribution action.
 * takes care of parsinng the parameters to correct types, and transforming and displaying 
 * the results if needed.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 200711:17:14 AM
 */
public class InvokerWorker extends BackgroundWorker {
    private final UIActionContribution action;
    public InvokerWorker(UIComponent parent,Converter conv, Transformer trans,UIActionContribution action) {
        super(parent,"Running " + action.getName());
        this.action = action; 
        this.conv = conv;
        this.trans = trans;
    }
    protected final Converter conv;
    protected final Transformer trans;          

    protected Object construct() throws Exception {
        Method method = ReflectionHelper.getMethodByName(action.getObject().getClass(),action.getMethodName());                        
        Class[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        // convert parameter values to correct types.
        Iterator i =action.getParameters().iterator();
        for (int j = 0; j < parameterTypes.length && i.hasNext(); j++) {
            final String pValue = i.next().toString();
            if ("null".equals(pValue)) {
            	args[j] = null;
            } else {
            	args[j] = conv.convert(parameterTypes[j],pValue);
            }
        }                
        //Object result = MethodUtils.invokeMethod(action.getObject(),action.getMethodName(),args);
        Object result = MethodUtils.invokeExactMethod(action.getObject(), action.getMethodName(), args, parameterTypes);
            if (result != null) { // display results, if this method produced any.
                return trans.transform(result);
            } else {
                return null;
            }
    }
    
      protected void doFinished(Object r) {
            if (r != null) {                
                    ResultDialog rd = new ResultDialog(parent.getComponent(),r);
                    rd.show();
                    rd.toFront();
                    rd.requestFocus();
            }    
     }
    
}