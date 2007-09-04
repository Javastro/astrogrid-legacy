package org.astrogrid.desktop.modules.system;

import java.lang.reflect.Method;

/** classes that encapsulates a single method invocation as a runnable */
public class Invoke implements Runnable {
    private final Method meth;
    private final Object[] args;
    private final Object service;
    private Object result;
    public Invoke(Method meth, Object[] args, Object service) {
        super();
        this.meth = meth;
        this.args = args;
        this.service = service;
    }
    public void run() {
        try {
            this.result = meth.invoke(service,args);
        } catch (Exception e) {
            //@fixme report to hivemind logger, etc
        }
    }
    public Object getResult() {
        return result;
    }

}