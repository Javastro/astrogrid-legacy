/*$Id: EdtInvocationInterceptorFactory.java,v 1.1 2007/09/04 13:38:38 nw Exp $
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.ArrayUtils;
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
/** an interceptor that ensures a method is being invoked on the EDT.
 * if not on the EDT, it will call 'invokeLater' for void typed methods, and 
 * 'invokeAndWait' for methods which return a value with a Runnable encapsulating
 * the required method.
 * 
 * 
 * not possible to create 'inner classes' in the expression language of JavaAssist
 *so using reflection to do the actual invocation - hopefully shouldn't be 
 *too inefficient, as none of this is in tight loops.
 * */
public class EdtInvocationInterceptorFactory implements ServiceInterceptorFactory {

    public EdtInvocationInterceptorFactory(ClassFactory factory) {
        this._factory = factory;
        runSignature = (new MethodIterator(Runnable.class)).next();        
    }
    
    protected final ClassFactory _factory;
    private final MethodSignature runSignature;

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

    protected void addServiceMethodImplementation(Class iface,ClassFab classFab, MethodSignature sig)
    {
        Class returnType = sig.getReturnType();
        String methodName = sig.getName();

        boolean isVoid = (returnType == void.class);
        
        // now create the interceptor method.
        BodyBuilder builder = new BodyBuilder();
        String su = ClassFabUtils.getJavaClassName(SwingUtilities.class);
        String invoke = ClassFabUtils.getJavaClassName(Invoke.class);
        String ret = ClassFabUtils.getJavaClassName(returnType);
        
        builder.begin();
        builder.add("if (");
        builder.add(su);
        builder.addln(".isEventDispatchThread()) { ");
        if (!isVoid)
        {
            builder.add(ret);
            builder.add(" result = ");
        }

        builder.add("_delegate.");
        builder.add(methodName);
        builder.addln("($$);");
                 
        if (!isVoid)  {
            builder.addln("return result;");
        }
        builder.addln("} else {");
        builder.add(invoke);
        builder.add(" invoke = new ");
        builder.add(invoke);
        builder.add("(");
        builder.add(ClassFabUtils.getJavaClassName(iface));
        builder.add(".class.getMethod(");
        builder.addQuoted(methodName);
        builder.addln(",$sig),$args,_delegate);");
        
        if (isVoid) {
            builder.add(su);
            builder.addln(".invokeLater(invoke);");
        } else {
            builder.add(su);
            builder.addln(".invokeAndWait(invoke);");
            builder.add("return (");
            builder.add(ret);
            builder.add(")invoke.getResult();");
        }
        builder.addln("}");
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

            if (includeMethod(matcher, sig)) {
                addServiceMethodImplementation(stack.getServiceInterface(),fab, sig);
            }
            else {
                addPassThruMethodImplementation(fab, sig);
            }
        }

        if (!mi.getToString()) {
            addToStringMethod(stack, fab);
        }
    }

    /**
     * Creates a toString() method that identify the interceptor service id,
     * the intercepted service id, and the service interface class name).
     */
    protected void addToStringMethod(InterceptorStack stack, ClassFab fab)
    {
        ClassFabUtils.addToStringMethod(
            fab,
            "<EdtInvocationInterceptor for "
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

        classFab.addField("_delegate", topClass);

        classFab.addConstructor(
            new Class[] {  topClass},
            null,
            "{  _delegate = $1;"
            + " }");
        
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
                    "Couldn't produce EDTInvocation interceptor",ex);
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

        return c.newInstance(new Object[] {  stackTop });
    }

   
}


/* 
$Log: EdtInvocationInterceptorFactory.java,v $
Revision 1.1  2007/09/04 13:38:38  nw
added debugging for EDT, and adjusted UI to not violate EDT rules.

Revision 1.3  2006/06/15 09:51:30  nw
added code to this interceptor to trap unknown runtime exceptions, and replace them with runtime exceptions whose class is known on the client - otherwise rmi clients will choke.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.2.1  2006/04/04 10:31:26  nw
preparing to move to mac.
 
*/