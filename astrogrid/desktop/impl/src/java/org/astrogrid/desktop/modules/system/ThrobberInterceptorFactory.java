/*$Id: ThrobberInterceptorFactory.java,v 1.2 2006/04/18 23:25:44 nw Exp $
 * Created on 31-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.InterceptorStack;
import org.apache.hivemind.ServiceInterceptorFactory;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.methodmatch.MethodMatcher;
import org.apache.hivemind.service.BodyBuilder;
import org.apache.hivemind.service.ClassFab;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.hivemind.service.ClassFactory;
import org.apache.hivemind.service.MethodContribution;
import org.apache.hivemind.service.MethodFab;
import org.apache.hivemind.service.MethodIterator;
import org.apache.hivemind.service.MethodSignature;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.acr.system.UI;
/** an interceptor that causes things to throb when messages are called 
 * 
 * */
public class ThrobberInterceptorFactory implements ServiceInterceptorFactory {

    public ThrobberInterceptorFactory(UI ui, SystemTray tray,ClassFactory factory) {
        this.ui = ui;
        this.tray = tray;
        this._factory = factory;
    }
    protected final UI ui;
    protected final SystemTray tray;

    
    protected final ClassFactory _factory;

    /**
     * Creates a method that delegates to the _delegate object; this is used for
     * methods that are not throbbed
     */
    private void addPassThruMethodImplementation(ClassFab classFab, MethodSignature sig)
    {
        BodyBuilder builder = new BodyBuilder();
        builder.begin();

        builder.add("return ($r) _delegate.");
        builder.add(sig.getName());
        builder.addln("($$);");

        builder.end();

        classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());
    }

    protected void addServiceMethodImplementation(ClassFab classFab, MethodSignature sig)
    {
        Class returnType = sig.getReturnType();
        String methodName = sig.getName();

        boolean isVoid = (returnType == void.class);

        BodyBuilder builder = new BodyBuilder();

        builder.begin();
        builder.addln("_ui.startThrobbing();");
        builder.addln("_tray.startThrobbing();");
        builder.addln(" try { ");


        if (!isVoid)
        {
            builder.add(ClassFabUtils.getJavaClassName(returnType));
            builder.add(" result = ");
        }

        builder.add("_delegate.");
        builder.add(methodName);
        builder.addln("($$);");
        
         
        if (!isVoid)  {
            builder.addln("return result;");
        }
        
        builder.addln(" } finally { ");
        builder.addln("_ui.stopThrobbing();");
        builder.addln("_tray.stopThrobbing();");
        builder.addln("  } ");

        builder.end();

        MethodFab methodFab = classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());


    }

    protected void addServiceMethods(InterceptorStack stack, ClassFab fab, List parameters)
    {
        MethodMatcher matcher = buildMethodMatcher(parameters);

        MethodIterator mi = new MethodIterator(stack.getServiceInterface());

        while (mi.hasNext())
        {
            MethodSignature sig = mi.next();

            if (includeMethod(matcher, sig))
                addServiceMethodImplementation(fab, sig);
            else
                addPassThruMethodImplementation(fab, sig);
        }

        if (!mi.getToString())
            addToStringMethod(stack, fab);
    }

    /**
     * Creates a toString() method that identify the interceptor service id,
     * the intercepted service id, and the service interface class name).
     */
    protected void addToStringMethod(InterceptorStack stack, ClassFab fab)
    {
        ClassFabUtils.addToStringMethod(
            fab,
            "<ThrobbingInterceptor for "
                + stack.getServiceExtensionPointId()
                + "("
                + stack.getServiceInterface().getName()
                + ")>");

    }

    private MethodMatcher buildMethodMatcher(List parameters)
    {
        MethodMatcher result = null;

        Iterator i = parameters.iterator();
        while (i.hasNext())
        {
            MethodContribution mc = (MethodContribution) i.next();

            if (result == null)
                result = new MethodMatcher();

            result.put(mc.getMethodPattern(), mc);
        }

        return result;
    }

    private Class constructInterceptorClass(InterceptorStack stack, List parameters)
    {
        Class serviceInterfaceClass = stack.getServiceInterface();
        
        String name = ClassFabUtils.generateClassName(serviceInterfaceClass);

        ClassFab classFab = _factory.newClass(name, Object.class);

        classFab.addInterface(serviceInterfaceClass);

        createInfrastructure(stack, classFab);

        addServiceMethods(stack, classFab, parameters);

        return classFab.createClass();
    }

    private void createInfrastructure(InterceptorStack stack, ClassFab classFab)
    {
        Class topClass = ClassFabUtils.getInstanceClass(stack.peek(), stack.getServiceInterface());

        classFab.addField("_ui", UI.class);
        classFab.addField("_tray",SystemTray.class);

        // This is very important: since we know the instance of the top object (the next
        // object in the pipeline for this service), we can build the instance variable
        // and constructor to use the exact class rather than the service interface.
        // That's more efficient at runtime, lowering the cost of using interceptors.
        // One of the reasons I prefer Javassist over JDK Proxies.

        classFab.addField("_delegate", topClass);

        classFab.addConstructor(
            new Class[] { UI.class,SystemTray.class, topClass },
            null,
            "{ _ui = $1; _tray = $2; _delegate = $3; }");
    }

    /**
     * Creates the interceptor.
     * The class that is created is cached; if an interceptor is requested
     * for the same extension point, then the previously constructed class
     * is reused (this can happen with the threaded service model, for example,
     * when a thread-local service implementation is created for different threads).
     */
    public void createInterceptor(
        InterceptorStack stack,
        Module contributingModule,
        List parameters)
    {
        Class interceptorClass = constructInterceptorClass(stack, parameters);

        try
        {
            Object interceptor = instantiateInterceptor(stack, interceptorClass);

            stack.push(interceptor);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(
                    "Couldn't produce throbbing interceptor",ex);
        }
    }



    private boolean includeMethod(MethodMatcher matcher, MethodSignature sig)
    {
        if (matcher == null)
            return true;

        MethodContribution mc = (MethodContribution) matcher.get(sig);

        return mc == null || mc.getInclude();
    }

    private Object instantiateInterceptor(InterceptorStack stack, Class interceptorClass)
        throws Exception
    {
        Object stackTop = stack.peek();

        Constructor c = interceptorClass.getConstructors()[0];

        return c.newInstance(new Object[] { this.ui, this.tray, stackTop });
    }



}


/* 
$Log: ThrobberInterceptorFactory.java,v $
Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.
 
*/