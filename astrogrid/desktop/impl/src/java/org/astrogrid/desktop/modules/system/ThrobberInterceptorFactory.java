/*$Id: ThrobberInterceptorFactory.java,v 1.6 2008/11/04 14:35:49 nw Exp $
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
/** An interceptor that causes UI to throb when a method is called.
 * <p/>
 * used to intercept calls to long-running methods - e.g. those what call external services. 
 * 
 * */
public class ThrobberInterceptorFactory implements ServiceInterceptorFactory {

    public ThrobberInterceptorFactory(final UI ui, final SystemTray tray,final ClassFactory factory) {
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

    protected void addServiceMethodImplementation(final ClassFab classFab, final MethodSignature sig)
    {
        final Class returnType = sig.getReturnType();
        final String methodName = sig.getName();

        final boolean isVoid = (returnType == void.class);

        final BodyBuilder builder = new BodyBuilder();

        builder.begin();
        builder.addln(" javax.swing.SwingUtilities.invokeLater(_startThrobbing); ");
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
        builder.addln(" javax.swing.SwingUtilities.invokeLater(_stopThrobbing); ");
        builder.addln("  } ");

        builder.end();

        final MethodFab methodFab = classFab.addMethod(Modifier.PUBLIC, sig, builder.toString());


    }

    protected void addServiceMethods(final InterceptorStack stack, final ClassFab fab, final List parameters)
    {
        final MethodMatcher matcher = buildMethodMatcher(parameters);

        final MethodIterator mi = new MethodIterator(stack.getServiceInterface());

        while (mi.hasNext())
        {
            final MethodSignature sig = mi.next();

            if (includeMethod(matcher, sig)) {
                addServiceMethodImplementation(fab, sig);
            } else {
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
            "<ThrobbingInterceptor for "
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

        createInfrastructure(stack, classFab);

        addServiceMethods(stack, classFab, parameters);

        return classFab.createClass();
    }

    private void createInfrastructure(final InterceptorStack stack, final ClassFab classFab)
    {
        final Class topClass = ClassFabUtils.getInstanceClass(stack.peek(), stack.getServiceInterface());

        classFab.addField("_ui", UI.class);
        classFab.addField("_tray",SystemTray.class);

        classFab.addField("_delegate", topClass);
        classFab.addField("_startThrobbing",Runnable.class);
        classFab.addField("_stopThrobbing",Runnable.class);

        classFab.addConstructor(
            new Class[] { UI.class,SystemTray.class, topClass ,Runnable.class,Runnable.class},
            null,
            "{ _ui = $1; _tray = $2; _delegate = $3;"
            + "_startThrobbing = $4; _stopThrobbing = $5;"
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
                    "Couldn't produce throbbing interceptor",ex);
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

        return c.newInstance(new Object[] { this.ui, this.tray, stackTop,this.startThrobber,this.stopThrobber });
    }
    
    private final Runnable startThrobber = new Runnable() {

        public void run() {
            ui.startThrobbing();
            tray.startThrobbing();
        }
    };
    private final Runnable stopThrobber = new Runnable() {

        public void run() {
            ui.stopThrobbing();
            tray.stopThrobbing();
        }
    };



}


/* 
$Log: ThrobberInterceptorFactory.java,v $
Revision 1.6  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.5  2007/10/22 10:29:21  nw
moved runtime exceptioni lifting from here to the RmiServer, where it belongs.

Revision 1.4  2007/09/04 13:38:37  nw
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