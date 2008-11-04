/*$Id: EdtInvocationInterceptorFactory.java,v 1.4 2008/11/04 14:35:49 nw Exp $
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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
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
import org.astrogrid.desktop.framework.ReflectionHelper;
/** Interceptor that ensures a method is being invoked on the EDT.
 * <p />
 * if not on the EDT, it will call 'invokeLater' for void typed methods, and 
 * 'invokeAndWait' for methods which return a value with a Runnable encapsulating
 * the required method.
 * 
 * Alteration - do all in invokeAndWait - else we can have unexpected
 * timing / sequencing problems.
 * 
 * 
 * not possible to create 'inner classes' in the expression language of JavaAssist
 *so using reflection to do the actual invocation - hopefully shouldn't be 
 *too inefficient, as none of this is in tight loops.
 * */
public class EdtInvocationInterceptorFactory implements ServiceInterceptorFactory {

    public EdtInvocationInterceptorFactory(final ClassFactory factory) {
        this._factory = factory;
        runSignature = (new MethodIterator(Runnable.class)).next();        
    }
    
    protected final ClassFactory _factory;
    private final MethodSignature runSignature;

    /**
     * Creates a method that delegates to the _delegate object; this is used for
     * methods that are not throbbed
     */
    private void addPassThruMethodImplementation(final ClassFab classFab, final MethodSignature sig)
    {
        final BodyBuilder builder = new BodyBuilder();
        builder.begin();

        builder.add("return ($r) _delegate.");
        builder.add(sig.getName());
        builder.addln("($$);");

        builder.end();

        classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());
    }

    protected void addServiceMethodImplementation(final Class iface,final ClassFab classFab, final MethodSignature sig)
    {
        final Class returnType = sig.getReturnType();
        final String methodName = sig.getName();

        final boolean isVoid = (returnType == void.class);
        
        // now create the interceptor method.
        final BodyBuilder builder = new BodyBuilder();
        final String su = ClassFabUtils.getJavaClassName(SwingUtilities.class);
        final String invoke = ClassFabUtils.getJavaClassName(Invoke.class);
        final String ret = ClassFabUtils.getJavaClassName(returnType);
        
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
//        builder.add(ClassFabUtils.getJavaClassName(iface));
//        builder.add(".class.getMethod(");
//        builder.addQuoted(methodName);
//        builder.addln(",$sig),$args,_delegate);");
        builder.add((String)fieldMap.get(sig));
        builder.addln(",$args,_delegate);");
        
        if (isVoid) {
            builder.add(su);
          //  builder.addln(".invokeLater(invoke);");
            // altered - wait for all calls, even if nothin returned.
            builder.addln(".invokeAndWait(invoke);");            
        } else {
            builder.add(su);
            builder.addln(".invokeAndWait(invoke);");
            builder.add("return (");
            builder.add(ret);
            builder.add(")invoke.getResult();");
        }
        builder.addln("}");
        builder.end();

        final MethodFab methodFab = classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());


    }



    protected void addServiceMethods(final InterceptorStack stack, final ClassFab fab, final MethodMatcher matcher)
    {

        final MethodIterator mi = new MethodIterator(stack.getServiceInterface());

        while (mi.hasNext())
        {
            final MethodSignature sig = mi.next();

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
    protected void addToStringMethod(final InterceptorStack stack, final ClassFab fab)
    {
        ClassFabUtils.addToStringMethod(
            fab,
            "<EdtInvocationInterceptor for "
                + stack.getServiceExtensionPointId()
                + "("
                + stack.getServiceInterface().getName()
                + ")>");

    }

    private MethodMatcher buildMethodMatcher(final List parameters)
    {
        MethodMatcher result = null;

        final Iterator i = parameters.iterator();
        while (i.hasNext())
        {
            final MethodContribution mc = (MethodContribution) i.next();

            if (result == null) {
                result = new MethodMatcher();
            }

            result.put(mc.getMethodPattern(), mc);
        }

        return result;
    }

    private Class constructInterceptorClass(final InterceptorStack stack, final List parameters)
    {
        final Class serviceInterfaceClass = stack.getServiceInterface();
        
        final String name = ClassFabUtils.generateClassName(serviceInterfaceClass);

        final ClassFab classFab = _factory.newClass(name, Object.class);

        
        classFab.addInterface(serviceInterfaceClass);

        final MethodMatcher matcher = buildMethodMatcher(parameters);

        createInfrastructure(stack, classFab,matcher);

        addServiceMethods(stack, classFab,matcher);


        return classFab.createClass();
    }

    private void createInfrastructure(final InterceptorStack stack, final ClassFab classFab, final MethodMatcher matcher)
    {
        final Class serviceInterface = stack.getServiceInterface();
                
        final BodyBuilder builder = new BodyBuilder();
        builder.begin();
        
        // now create and initialize a member variable for each wrapped method
        final String iface = ClassFabUtils.getJavaClassName(serviceInterface);        
        final String reflectionHelper = ClassFabUtils.getJavaClassName(ReflectionHelper.class);
        final MethodIterator mi = new MethodIterator(serviceInterface);

        while (mi.hasNext())
        {
            final MethodSignature sig = mi.next();            
            if (includeMethod(matcher, sig)) {
                final String varName =(String)fieldMap.get(sig);
                classFab.addField(varName,Method.class);
                builder.add(varName);
                builder.add(" = ");
                builder.add(reflectionHelper);
                builder.add(".getMethodByName(");
                builder.add(iface);
                builder.add(".class,");
                builder.addQuoted(sig.getName());
                builder.addln(");");
            }
        }
        
        final Class topClass = ClassFabUtils.getInstanceClass(stack.peek(), serviceInterface);
        classFab.addField("_delegate", topClass);
        builder.addln(" _delegate = $1;");
        builder.end();
        classFab.addConstructor(new Class[] {topClass},null,builder.toString());
    }

    
    // map that generates new method names on demand.
    
    private final Map fieldMap = MapUtils.lazyMap(new HashMap(),new Transformer() {
        private int varName = 0;
        public Object transform(final Object arg0) {
            final MethodSignature sig = (MethodSignature)arg0;
            final String str =  sig.getName() + varName++;
            return str;
        }
    });

    /**
     * Creates the interceptor.
     * The class that is created is cached; if an interceptor is requested
     * for the same extension point, then the previously constructed class
     * is reused (this can happen with the threaded service model, for example,
     * when a thread-local service implementation is created for different threads).
     */
    public void createInterceptor(
        final InterceptorStack stack,
        final Module contributingModule,
        final List parameters)
    {
        final Class interceptorClass = constructInterceptorClass(stack, parameters);

        try
        {
            final Object interceptor = instantiateInterceptor(stack, interceptorClass);

            stack.push(interceptor);
        }
        catch (final Exception ex)
        {
            throw new ApplicationRuntimeException(
                    "Couldn't produce EDTInvocation interceptor",ex);
        }
    }



    private boolean includeMethod(final MethodMatcher matcher, final MethodSignature sig)
    {
        if (matcher == null) {
            return true;
        }

        final MethodContribution mc = (MethodContribution) matcher.get(sig);

        return mc == null || mc.getInclude();
    }

    private Object instantiateInterceptor(final InterceptorStack stack, final Class interceptorClass)
        throws Exception
    {
        final Object stackTop = stack.peek();

        final Constructor c = interceptorClass.getConstructors()[0];

        return c.newInstance(new Object[] {  stackTop });
    }

   
}


/* 
$Log: EdtInvocationInterceptorFactory.java,v $
Revision 1.4  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.3  2007/10/22 10:27:41  nw
altered behaviour - so always invoke and wait.

Revision 1.2  2007/09/04 18:50:50  nw
Event Dispatch thread related fixes.

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