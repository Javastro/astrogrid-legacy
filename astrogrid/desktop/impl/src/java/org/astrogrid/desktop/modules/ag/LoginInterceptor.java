/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

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
import org.astrogrid.acr.astrogrid.Community;

/** Serrvice Interceptor that checks use has logged in before being granted
 * access to certain services / operations
 * @author Noel Winstanley
 * @since Apr 13, 20061:10:55 PM
 */
public class LoginInterceptor implements ServiceInterceptorFactory {

	public LoginInterceptor(Community comm, ClassFactory fac) {
		this.comm = comm;
		this._factory = fac;
	}
	private final Community comm;
	private final ClassFactory _factory;

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
     //   builder.addln("System.err.println(\"In \" +this +  \" \");");
        builder.addln("if (! _comm.isLoggedIn()) {");
        builder.addln("  _comm.getUserInformation();");// forced a login to retreive this.
        builder.addln(" }");

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
            "<LoginInterceptor for "
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

        classFab.addField("_comm", Community.class);

        // This is very important: since we know the instance of the top object (the next
        // object in the pipeline for this service), we can build the instance variable
        // and constructor to use the exact class rather than the service interface.
        // That's more efficient at runtime, lowering the cost of using interceptors.
        // One of the reasons I prefer Javassist over JDK Proxies.

        classFab.addField("_delegate", topClass);

        classFab.addConstructor(
            new Class[] { Community.class, topClass },
            null,
            "{ _comm = $1; _delegate = $2; }");
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
                    "Couldn't produce login interceptor",ex);
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

        return c.newInstance(new Object[] { this.comm,  stackTop });
    }


}
