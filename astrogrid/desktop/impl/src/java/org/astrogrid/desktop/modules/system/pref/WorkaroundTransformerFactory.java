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

    @Override
    public Transformer newTransformer(final Source source) throws TransformerConfigurationException {
        return base.newTransformer(source);
    }

    @Override
    public Transformer newTransformer() throws TransformerConfigurationException {
        return base.newTransformer();
    }

    @Override
    public Templates newTemplates(final Source source) throws TransformerConfigurationException {
        return base.newTemplates(source);
    }

    @Override
    public Source getAssociatedStylesheet(final Source source, final String media, final String title, final String charset) throws TransformerConfigurationException {
        return base.getAssociatedStylesheet(source, media, title, charset);
    }

    @Override
    public void setURIResolver(final URIResolver resolver) {
        base.setURIResolver(resolver);
    }

    @Override
    public URIResolver getURIResolver() {
        return base.getURIResolver();
    }

    @Override
    public void setFeature(final String name, final boolean value) throws TransformerConfigurationException {

        // New method at J2SE1.6 - need to invoke using reflection to get this
        // class to compile at earlier versions.
        try {
            final Method method = TransformerFactory.class.getMethod("setFeature", new Class[] {String.class, boolean.class});
            method.invoke(base, new Object[] {name, Boolean.valueOf(value)});
        }
        catch (final Throwable e) {
            throw new TransformerConfigurationException(e);
        }
    }

    @Override
    public boolean getFeature(final String name) {
        return base.getFeature(name);
    }

    @Override
    public void setAttribute(final String name, final Object value) {
        try {
            base.setAttribute(name, value);
        }
        catch (final Exception e) {
            if ("indent-number".equals(name)) {
                // never mind
            }
        }
    }

    @Override
    public Object getAttribute(final String name) {
        return base.getAttribute(name);
    }

    @Override
    public void setErrorListener(final ErrorListener listener) {
        base.setErrorListener(listener);
    }

    @Override
    public ErrorListener getErrorListener() {
        return base.getErrorListener();
    }

    @Override
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
        final TransformerFactory tfac = TransformerFactory.newInstance();
        try {
            tfac.setAttribute("indent-number",Integer.valueOf(2));
        }
        catch (final Exception e) {
            baseClass = tfac.getClass();
            System.setProperty(TransformerFactory.class.getName(), WorkaroundTransformerFactory.class.getName());
        }
    }
}
