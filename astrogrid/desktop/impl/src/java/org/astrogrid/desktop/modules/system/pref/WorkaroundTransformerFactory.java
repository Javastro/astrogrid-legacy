package org.astrogrid.desktop.modules.system.pref;

import java.lang.reflect.Method;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

/**
 * TransformerFactory which works around java bug 
 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6519088">#6519088</a>.
 * This bug causes any writes to the <code>java.util.prefs</code> database
 * to fail at (for instance) Sun's J2SE 1.6.0_01.  Sun really ought to fix this;
 * when they do (and when we no longer expect to encounter broken JREs) 
 * this class will no longer be required.
 *
 * <p>To use the fix provided by this class, call {@link #fixJaxp} following
 * any normal JAXP setup that you would do.
 *
 * @author   Mark Taylor
 * @since    27 Feb 2008
 */
public class WorkaroundTransformerFactory extends TransformerFactory {
    private static Class baseClass;
    private final TransformerFactory base;

    /**
     * No-arg constructor used by JAXP to instantiate this factory.
     */
    public WorkaroundTransformerFactory() throws InstantiationException, IllegalAccessException {
        base = (TransformerFactory) baseClass.newInstance();
    }

    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        return base.newTransformer(source);
    }

    public Transformer newTransformer() throws TransformerConfigurationException {
        return base.newTransformer();
    }

    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        return base.newTemplates(source);
    }

    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        return base.getAssociatedStylesheet(source, media, title, charset);
    }

    public void setURIResolver(URIResolver resolver) {
        base.setURIResolver(resolver);
    }

    public URIResolver getURIResolver() {
        return base.getURIResolver();
    }

    public void setFeature(String name, boolean value) throws TransformerConfigurationException {

        // New method at J2SE1.6 - need to invoke using reflection to get this
        // class to compile at earlier versions.
        try {
            Method method = TransformerFactory.class.getMethod("setFeature", new Class[] {String.class, boolean.class});
            method.invoke(base, new Object[] {name, Boolean.valueOf(value)});
        }
        catch (Throwable e) {
            throw new TransformerConfigurationException(e);
        }
    }

    public boolean getFeature(String name) {
        return base.getFeature(name);
    }

    public void setAttribute(String name, Object value) {
        try {
            base.setAttribute(name, value);
        }
        catch (Exception e) {
            if ("indent-number".equals(name)) {
                // never mind
            }
        }
    }

    public Object getAttribute(String name) {
        return base.getAttribute(name);
    }

    public void setErrorListener(ErrorListener listener) {
        base.setErrorListener(listener);
    }

    public ErrorListener getErrorListener() {
        return base.getErrorListener();
    }

    public String toString() {
        return getClass().getName() + "[" + baseClass.getName() + "]";
    }

    /**
     * Checks whether the bug 6519088 is set to cause trouble, and if it is,
     * reconfigures JAXP to use this class as the default TransformerFactory.
     * If it has been called once to do this, there will be no effect.
     *
     * <p>This class delegates all methods to the previously-configured 
     * TransformerFactory, but overrides {@link #setAttribute} in such a way
     * as to avoid the problem with preferences.
     */
    public static void fixJaxp() {
        if (baseClass != null) {
            // already fixed - don't attempt a second time
            return;
        }
        TransformerFactory tfac = TransformerFactory.newInstance();
        try {
            tfac.setAttribute("indent-number", new Integer(2));
        }
        catch (Exception e) {
            baseClass = tfac.getClass();
            System.setProperty(TransformerFactory.class.getName(), WorkaroundTransformerFactory.class.getName());
        }
    }
}
