/**
 * 
 */
package org.astrogrid.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.Doc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import com.sun.tools.javadoc.Main;

/** cribbed exclusion doclet
 * @see <a href="http://www.sixlegs.com/blog/java/exclude-javadoc-tag.html">Writeup</a>
 *  @exclude*/
public class ExcludeDoclet
{
    public static void main(final String[] args)
    {
        final String name = ExcludeDoclet.class.getName();
        Main.execute(name, name, args);
    }

    public static boolean validOptions(final String[][] options, final DocErrorReporter reporter)
    throws java.io.IOException
    {
        return Standard.validOptions(options, reporter);
    }
    
    public static int optionLength(final String option)
    {
        return Standard.optionLength(option);
    }
        
    public static boolean start(final RootDoc root)
    throws java.io.IOException
    {
        return Standard.start((RootDoc)process(root, RootDoc.class));
    }

    private static boolean exclude(final Doc doc)
    {
        if (doc instanceof ProgramElementDoc) {
            if (((ProgramElementDoc)doc).containingPackage().tags("exclude").length > 0) {
                return true;
            }
        }
        return doc.tags("exclude").length > 0;
    }

    private static Object process(final Object obj, final Class expect)
    {
        if (obj == null) {
            return null;
        }
        final Class cls = obj.getClass();
        if (cls.getName().startsWith("com.sun.")) {
            return Proxy.newProxyInstance(cls.getClassLoader(),
                                          cls.getInterfaces(),
                                          new ExcludeHandler(obj));
        } else if (obj instanceof Object[]) {
            final Class componentType = expect.getComponentType();
            final Object[] array = (Object[])obj;
            final List list = new ArrayList(array.length);
            for (int i = 0; i < array.length; i++) {
                final Object entry = array[i];
                if ((entry instanceof Doc) && exclude((Doc)entry)) {
                    continue;
                }
                list.add(process(entry, componentType));
            }
            return list.toArray((Object[])Array.newInstance(componentType, list.size()));
        } else {
            return obj;
        }
    }

    private static class ExcludeHandler
    implements InvocationHandler
    {
        private final Object target;
        
        public ExcludeHandler(final Object target)
        {
            this.target = target;
        }

        public Object invoke(final Object proxy, final Method method, final Object[] args)
        throws Throwable
        {
            if (args != null) {
                final String methodName = method.getName();
                if (methodName.equals("compareTo") ||
                    methodName.equals("equals") ||
                    methodName.equals("overrides") ||
                    methodName.equals("subclassOf")) {
                    args[0] = unwrap(args[0]);
                }
            }
            try {
                return process(method.invoke(target, args), method.getReturnType());
            } catch (final InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        private Object unwrap(final Object proxy)
        {
            if (proxy instanceof Proxy) {
                return ((ExcludeHandler)Proxy.getInvocationHandler(proxy)).target;
            }
            return proxy;
        }
    }
}

