/**
 * 
 */
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

/** Interceptor that reports function usage.
 * @author Noel Winstanley
 * @since May 18, 200611:51:33 PM
 */
public class SnitchInterceptorFactory implements ServiceInterceptorFactory {

	private final SnitchInternal snitch;
	private final ClassFactory _factory;
	public SnitchInterceptorFactory(final SnitchInternal snitch, final ClassFactory fac) {
		super();
		this.snitch = snitch;
		this._factory = fac;
	}
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
      //  builder.addln("java.util.Map _m = new java.util.HashMap();");
      //  builder.addln("_m.put
//        builder.addln("_snitch.snitch(\"INVOKE/\" + _interfaceName + \"/" + sig.getName() + "\");");
        builder.addln("_snitch.snitch(\"INVOKE/\" + _interfaceName );");     
 
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
            "<SnitchInterceptor for "
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

        classFab.addField("_snitch", SnitchInternal.class);

        // This is very important: since we know the instance of the top object (the next
        // object in the pipeline for this service), we can build the instance variable
        // and constructor to use the exact class rather than the service interface.
        // That's more efficient at runtime, lowering the cost of using interceptors.
        // One of the reasons I prefer Javassist over JDK Proxies.

        classFab.addField("_delegate", topClass);
        classFab.addField("_interfaceName",String.class);

        classFab.addConstructor(
            new Class[] { SnitchInternal.class, topClass, String.class },
            null,
            "{ _snitch = $1; _delegate = $2; _interfaceName = $3 ;}");
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
                    "Couldn't produce snitch interceptor",ex);
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

        return c.newInstance(new Object[] { this.snitch,  stackTop , stack.getServiceInterface().getName()});
    }

}
