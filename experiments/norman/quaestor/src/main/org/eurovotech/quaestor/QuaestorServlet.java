package org.eurovotech.quaestor;

/**
 * The interface which Quaestor requires of the servlet which calls it.
 * Quaestor is initialised by having the servlet call a SISC
 * initialisation function, giving itself or some proxy as argument.
 * This function is set in the servlet's configuration file, but will
 * probably be the INITIALISE-QUAESTOR function.  This function
 * requires a certain interface of its argument, namely this one.
 */
public interface QuaestorServlet {
    /**
     * Provide an initialisation parameter to a servlet.  Modelled after
     * {@link javax.servlet.ServletContext#getInitParameter}.
     * @param key the parameter name
     * @return the parameter value, or null if there is no such parameter
     */
public String getInitParameter(String key);

    /** 
     * Register a Scheme handler procedure with this servlet.
     *
     * <p>This method is called by the Scheme initialiser to register
     * a procedure which will handle a given context/method pair.
     *
     * @param method the name of the method, such as <code>GET</code>,
     *   <code>POST</code> and so on
     * @param context the webapp context, such as <code>/path</code>
     * @param proc a Scheme procedure as a (subclass of)
     *   {@link sisc.data.Procedure}
     */
    public void registerHandler(String method, String context, sisc.data.Procedure proc);

    /**
     * Log a string to the logging output
     * @param message the message to be logged
     */
    public void log(String message);
}
