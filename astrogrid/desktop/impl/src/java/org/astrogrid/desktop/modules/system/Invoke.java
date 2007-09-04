package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;

import java.lang.reflect.Method;

/** classes that encapsulates a single method invocation as a runnable */
public class Invoke implements Runnable {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Invoke.class);

    private final Method meth;
    private final Object[] args;
    private final Object service;
    private Object result;
    public Invoke(Method meth, Object[] args, Object service) {
        super();
        if (meth == null) {
            throw new ApplicationRuntimeException("Null method object");
        }
        this.meth = meth;
        this.args = args;
        this.service = service;
    }
    public void run() {
        this.result = null;
        try {
            this.result = meth.invoke(service,args);
        } catch (Exception e) {
                logger.error("Failed to invoke method " + meth.getName());            
                logger.debug("Exception",e);
        }
    }
    public Object getResult() {
        return result;
    }

}