package org.eurovotech.quaestor;

import java.lang.ClassNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import javax.servlet.ServletException;

import sisc.REPL;
import sisc.data.Value;
import sisc.data.Procedure;
import sisc.data.SchemeBoolean;
import sisc.data.SchemeString;
import sisc.data.SchemeVoid;
import sisc.data.Symbol;
import sisc.env.DynamicEnvironment;
import sisc.interpreter.Interpreter;
import sisc.interpreter.Context;
import sisc.interpreter.AppContext;
import sisc.interpreter.SchemeException;
import sisc.interpreter.SchemeCaller;

/**
 * Wrapper for SISC.
 * 
 * <p>The various <code>eval</code> procedures below return their
 * values as Java objects, translated from Scheme objects as described below:
 * <dl>
 * <dt>(scheme) boolean</dt>
 * <dd>return a Java {@link Boolean}</dd>
 *
 * <dt>scheme void</dt>
 * <dd>return null</dd>
 *
 * <dt>otherwise</dt>
 * <dd>convert it to a String value in the usual way (for SISC)
 * </dl>
 */
public class SchemeWrapper {

    private static SchemeWrapper instance;

    /**
     * Constructs a new wrapper for SISC.  Private constructor.
     * @throws IOException if there are problems locating the heap
     */
    private SchemeWrapper()
            throws IOException {
        // All we need do here is set the default application context
        // to one which has the non-default parameters we want.  If we
        // didn't (or don't, in the future) need this, then we don't
        // need anything at all in this method, as the
        // Context.execute() method will use the SISC-default
        // application context, which automatically loads the default
        // heap.
        //
        // The maxStackTraceDepth parameter requires SISC 1.15.1 at least.

        java.util.Properties props = new java.util.Properties();
        // Set the maximum stack-trace depth to 16.  This causes a
        // performance hit which is reportedly about 30%: see
        // <http://sourceforge.net/mailarchive/forum.php?thread_id=21786623&forum_id=7422>
        // In fact, setting this to be non-zero seems to produce longer,
        // but less-intelligible stack traces, so I might be better off with it (default) zero.
        props.setProperty("sisc.maxStackTraceDepth", "16");

        // The following should be the defaults            
        //props.setProperty("sisc.emitAnnotations", "true");
        //props.setProperty("sisc.emitDebuggingSymbols", "true");
        //props.setProperty("sisc.stackTraceOnError", "true");
        
        for (Object keyo : System.getProperties().keySet()) {
            String key = (String)keyo;
            if (key.startsWith("sisc.")) {
                props.setProperty(key, System.getProperty(key));
                System.err.println("Found property " + key + "=" + System.getProperty(key));
            }
        }

        // The following hopes to pick up additional properties, but I can't get it to work.
        java.io.InputStream swprops = ClassLoader.getSystemResourceAsStream("SchemeWrapper.properties");
        if (swprops != null) {
            System.err.println("Found resource SchemeWrapper.properties");
            props.load(swprops);
        }

        AppContext ctx = new AppContext(props);
        ctx.addDefaultHeap();
        Context.setDefaultAppContext(ctx);
    }

    /**
     * Returns the single instance of the SchemeWrapper.
     */
    public static SchemeWrapper getInstance()
            throws IOException {
        if (instance == null) {
            instance = new SchemeWrapper();
            try {
                InputStream is = SchemeWrapper.class
                        .getResourceAsStream("scheme-wrapper-support.scm");
                if (is == null)
                    throw new IOException("Failed to find scheme-wrapper-support.scm");
                instance.evalInputStream(is);

                // having loaded that, we now have to load the newly-defined module
                // (we could do this in each file which uses these routines,
                // but that's easy to forget, and in any case should really be transparent)
                instance.eval("(import quaestor-support)");
            } catch (SchemeException e) {
                throw new IOException("Error loading scheme wrapper support:" + e);
            }   
        }
        return instance;
    }

    /**
     * Tests whether a given symbol is defined.
     * @param name the symbol name to test
     * @return true if the symbol is defined
     */
    public boolean symbolIsDefined(String name) {
        return Symbol.get(name) != null;
    }

    /**
     * Evaluates the string, returning the value as a String.  This is
     * not (currently) wrapped in the error-handling code of
     * {@link #eval(Procedure,Object[])}, because it's only used within this
     * class, which we can hope would get things right (hmm).
     *
     * @param expr a scheme expression, which should not be null
     * @return a Java Object (String, Boolean or null) representing the 
     *   value of the expression; if <code>expr</code> is null, then return
     * <code>Boolean.FALSE</code>
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if one there was a problem executing
     *   the expression
     */
    private Object eval(final String expr)
            throws IOException, SchemeException {
        if (expr == null)
            return Boolean.FALSE;
        
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i)
                         throws SchemeException {
                     try {
                         return i.eval(expr);
                     } catch (IOException e) {
                         return e; // return exception as value
                     }
                 }
             });
        if (o instanceof Exception) {
            assert o instanceof IOException;
            throw (IOException)o;
        }
        assert o instanceof Value;
        return schemeToJava((Value)o);
    }

    /**
     * Evaluates a procedure, with arguments.  This is
     * equivalent to the expr <code>proc args...</code>, but wrapped
     * in an error-handler (procedure <code>APPLY-WITH-TOP-FC</code>)
     * which translates errors and exceptions to a suitable string,
     * which it returns.  This procedure is defined in
     * <code>scheme-wrapper-support.scm</code>,
     * which is found and evaluated in {@link #getInstance} above.
     *
     * @param proc a Scheme {@link Procedure} defined in the top level
     * @param args an array of Java objects
     * @return a Java Object (String, Boolean or null) representing the
     *   value of the expression, translated as described above
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if there was a problem executing
     *   the expression
     * @throws ServletException if the SISC procedure returns one
     */
    public Object eval(final Procedure proc, final Object[] args)
            throws SchemeException, ServletException {
        Object o = Context.execute(new SchemeCaller() {
                public Object execute(Interpreter i)
                        throws SchemeException {
                    // The failure-continuation within procedure
                    // APPLY-WITH-TOP-FC returns a ServletException
                    Procedure apply = (Procedure)i.eval(Symbol.get("apply-with-top-fc"));
                    Value[] v = i.createValues(args.length+1);
                    v[0] = proc;
                    for (int n=0; n<args.length; n++)
                        v[n+1] = new sisc.modules.s2j.JavaObject(args[n]);
                    return i.eval(apply, v);
                }
            });
        if (o instanceof Exception) {
            assert o instanceof ServletException;
            throw (ServletException)o;
        }
        assert o instanceof Value;
        return schemeToJava((Value)o);
    }

    /**
     * Evaluates a procedure, with arguments.
     * Equivalent to {@link #eval(Procedure,Object[])}, but with a
     * string naming the procedure.
     *
     * @param proc the name of a Scheme procedure defined in the top level
     * @param args an array of Java objects
     * @return a Java Object (String, Boolean or null) representing the
     *   value of the expression, translated as described above
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if there was a problem executing
     *   the expression
     * @throws ServletException if the SISC procedure returns one
     */
    public Object eval(final String proc, final Object[] args)
            throws SchemeException, ServletException {
        Procedure p = (Procedure)Context.execute(new SchemeCaller() {
                public Object execute(Interpreter i)
                        throws SchemeException {
                    return i.eval(Symbol.get(proc));
                }
            });
        return eval(p, args);
    }

    /**
     * Evaluate an input stream.  This is a package-only method.
     * Unlike the public methods of this class, the return value is
     * not converted to a convenient Java value.
     *
     * <p>Unlike
     * {@link #evalInputStream(java.io.InputStream,java.io.OutputStream)},
     * this is not expected to be used outside of this class.
     *
     * @param in an input stream from which s-expressions can be read
     * @returns the value of the last expression evaluated, as a SISC Value
     */
    sisc.data.Value evalInputStream(final java.io.InputStream in)
            throws IOException, SchemeException {
        Object o = Context.execute(new SchemeCaller() {
                public Object execute(Interpreter i)
                        throws SchemeException {
                    try {
                        return i.evalInput(new java.io.PushbackReader(new java.io.InputStreamReader(in)));
                    } catch (IOException e) {
                        // Smuggle it out by returning it.
                        return e;
                    }
                }
            });
        if (o instanceof Exception) {
            assert o instanceof IOException;
            throw((IOException)o);
        }
        assert o instanceof sisc.data.Value;
        return (sisc.data.Value)o;
    }
                
    /**
     * Evals the given input stream, wrapped in basic
     * error-handling code.  The method acts as a basic REPL, reading
     * expressions from the input stream, displaying any output on the
     * output stream, and returning the value of the last expression
     * evaluated.  If there is an error, it appears as a SchemeException.
     *
     * @param in a stream from which expressions are read
     * @param out a stream to which any output is written
     * @return a Java Object (String, Boolean or null) representing the
     *   value of the expression, translated as described above
     * @throws IOException if the input stream cannot be parsed
     * @throws SchemeException if there's an error evaluating the Scheme input
     */
    public Object evalInputStream(final java.io.InputStream in,
                                  final java.io.OutputStream out)
            throws IOException, SchemeException {
        DynamicEnvironment de = new DynamicEnvironment(Context.getDefaultAppContext(), in, out);
        Object o = Context.execute(de, new SchemeCaller() {
                public Object execute(Interpreter i)
                         throws SchemeException {
                     try {
                         // The following is a basic REPL, with a
                         // failure continuation which throws an improved
                         // error message as a SchemeException
                         String evalCurrentInput = "(with/fc (lambda (m e) (define (show-err r) (let ((parent (error-parent-error r))) (format #f \"Error~a: ~a~a\" (if (error-location r) (format #f \" at ~a\" (error-location r)) \"\") (error-message r) (if parent (string-append \" :-- \" (show-err parent)) \"\")))) (error (show-err m))) (lambda () (let loop ((e (read)) (last #f)) (if (eof-object? e) last (loop (read) (eval e))))))";

                         return i.eval(evalCurrentInput);
                     } catch (IOException e) {
                         // I'm not sure under what circumstances this can happen
                         return e; // return exception as value
                     }
                 }
             });
        if (o instanceof Exception) {
            assert(o instanceof IOException);
            throw (IOException)o;
        }
        assert o instanceof Value;
        return schemeToJava((Value)o);
    }
    
    /**
     * Convert a SISC Value to a Java Object.  If the value is:
     * <dl>
     * <dt>(scheme) boolean</dt>
     * <dd>return a Java {@link Boolean}</dd>
     *
     * <dt>{@link SchemeString}</dt>
     * <dd>return a Java String using the {@link SchemeString#asString}
     * method, which does suitable conversions of newlines</dd>
     *
     * <dt>scheme void</dt>
     * <dd>return null</dd>
     *
     * <dt>a Java Exception, which will be wrapped in a
     * {@sisc.modules.s2j.JavaObject}</dt>
     * <dd>return the Exception</dd>
     *
     * <dt>otherwise</dt>
     * <dd>convert it to a String value in the usual way,
     * with {@link java.lang.Object#toString}.</dd>
     * </dl>
     *
     * @param v a SISC Value, which may or may not be a SchemeString
     * @return a Java Object, which will be null, Boolean or String
     * as appropriate.
     */
    private Object schemeToJava(Value v) {
        Object ret;

        if (v instanceof SchemeString) {
            ret = ((SchemeString)v).asString();
        } else if (v instanceof SchemeBoolean) {
            SchemeBoolean sb = (SchemeBoolean)v;
            ret = Boolean.valueOf(sb.equals(sisc.data.SchemeBoolean.TRUE));
        } else if (v instanceof SchemeVoid) {
            ret = null;
        } else if (v instanceof sisc.modules.s2j.JavaObject) {
            ret = ((sisc.modules.s2j.JavaObject)v).get();
            if (ret != null)
                ret = ret.toString();
        } else {
            ret = v.toString();
        }
        assert ret == null || ret instanceof String || ret instanceof Boolean;
        return ret;
    }
}
