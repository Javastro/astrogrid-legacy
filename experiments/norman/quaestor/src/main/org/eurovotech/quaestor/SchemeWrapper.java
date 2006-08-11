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
import sisc.interpreter.Interpreter;
import sisc.interpreter.Context;
import sisc.interpreter.AppContext;
import sisc.interpreter.SchemeException;
import sisc.interpreter.SchemeCaller;
import sisc.io.StreamInputPort;

/**
 * Wrapper for SISC.
 * 
 * <p>There are examples of how to use SISC for a servlet
 * in the <code>contrib</code> section of the SISC CVS tree.
 */
public class SchemeWrapper {

    private static SchemeWrapper instance;
    private static java.util.Set loadedOnce;

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
        props.setProperty("sisc.maxStackTraceDepth", "16");

        // The following should be the defaults            
        //         props.setProperty("sisc.emitAnnotations", "true");
        //         props.setProperty("sisc.emitDebuggingSymbols", "true");
        //         props.setProperty("sisc.stackTraceOnError", "true");
        
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
     * {@link #eval(String,Object[])}.
     * (XXX It's not currently used by Quaestor.java, which is why the
     * 'public' declaration is commented out)
     *
     * <p>The value returned is converted from a Scheme value to a Java value
     * as follows:
     * <table border="1">
     * <tr><th>Scheme value</th><th align="left">Java value</th></tr>
     * <tr><td>boolean</td><td>{@link Boolean}</td></tr>
     * <tr><td>string</td><td>{@link String}</td></tr>
     * <tr><td>void</td><td>null</td></tr>
     * <tr><td>otherwise</td><td>a String representation of the object</td></tr>
     * </table>
     *
     * @param expr a scheme expression, which should not be null
     * @return a Java Object (String, Boolean or null) representing the 
     * value of the expression; if <code>expr</code> is null, then return
     * <code>Boolean.FALSE</code>
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if one there was a problem executing
     * the expression
     */
    /* public */ private Object eval(final String expr)
            throws IOException, SchemeException {
        if (expr == null)
            return Boolean.FALSE;
        
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i)
                         throws SchemeException {
                     try {
                         return schemeToJava(i.eval(expr));
                     } catch (IOException e) {
                         return e;
                     }
                 }
             });
        if (o instanceof Exception) {
            assert o instanceof IOException;
            throw (IOException)o;
        }
        return o;
    }

    /**
     * Evaluates a named procedure, with arguments.  This is
     * equivalent to the expr <code>proc args...</code>, but wrapped
     * in an error-handler (procedure <code>APPLY-WITH-TOP-FC</code>
     * which translates errors and exceptions to a suitable string,
     * which it returns.
     *
     * @param proc the name of a Scheme procedure defined in the top level
     * @param args an array of Java objects
     * @return a Java Object (String, Boolean or null) representing the
     * value of the expression
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if one there was a problem executing
     * the expression
     * @throws ServletException if the SISC procedure returns one
     */
    public Object eval(final String proc, final Object[] args)
            throws IOException, SchemeException, ServletException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter r)
                         throws SchemeException {
                     Procedure apply =
                             (Procedure)r.eval(Symbol.get("apply-with-top-fc"));
                     Procedure p = (Procedure)r.eval(Symbol.get(proc));
                     Value[] v = r.createValues(args.length+1);
                     v[0] = p;
                     for (int i=0; i<args.length; i++)
                         v[i+1] = new sisc.modules.s2j.JavaObject(args[i]);
                     return schemeToJava(r.eval(apply, v));
                 }
                });
        if (o instanceof Exception) {
            if (o instanceof IOException)
                throw (IOException)o;
            else if (o instanceof ServletException)
                throw (ServletException)o;
            else
                assert false : "Impossible assertion from SchemeCaller: " + o;
        }
        return o;
    }

    /**
     * Evals the given input stream.  This is not (currently) wrapped
     * in the error-handling code of {@link #eval(String,Object[])}.
     *
     * @return a Java Object (String, Boolean or null) representing the
     * value of the expression
     * @throws IOException if the input stream cannot be parsed
     * @throws SchemeException if there is a syntax error reading the 
     * Scheme input
     */
    public Object evalInput(final java.io.InputStream in)
            throws IOException, SchemeException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i)
                         throws SchemeException {
                     try {
                         return schemeToJava(i.evalInput
                                             (new StreamInputPort(in)));
                     } catch (IOException e) {
                         return e;
                     }
                 }
             });
        if (o instanceof Exception) {
            assert(o instanceof IOException);
            throw (IOException)o;
        }
        return o;
    }
    
    /**
     * Loads the given source file into the interpreter.
     * @param loadFile the full path of a file to load
     * @return true if the load succeeded; throws descriptive exception
     * if there are errors reading the file
     * @throws IOException if the file cannot be parsed
     * @throws SchemeException if there is a scheme error when reading the file
     */
    public boolean load(final String loadFile)
            throws IOException, SchemeException {
        // Load the file using the LOAD procedure.  This is different from
        // Interpreter.loadSourceFiles since that just returns true or false,
        // whereas this will throw a SchemeException if there's a problem
        // loading the file.  It's also different from just "(load loadFile)",
        // since that displays the error-record structure, which takes
        // some parsing by eye.
        //
        // The following expression evaluates to either #t on success,
        // or a string on error.
        //
        // We use the elaborate eval, rather than using
        // eval(String,Object[]), because this is used to load
        // quaestor.scm, before which the APPLY-WITH-TOP-FC procedure
        // is not defined.
        Object o = eval("(with/fc (lambda (m e) (define (show-err r) (let ((parent (error-parent-error r))) (format #f \"Error at ~a: ~a~a\" (error-location r) (error-message r) (if parent (string-append \" :-- \" (show-err parent)) \"\")))) (show-err m)) (lambda () (load \"" + loadFile + "\") #t))");
        if (o instanceof String) {
            // Contains an error message from the failure-continuation.
            // We can't create SchemeExceptions (no useful constructor),
            // so hijack the IOException instead.  Ought this to be a
            // ServletException?
            throw new IOException("Error reading file " + loadFile
                                  + ": " + o);
        }

        return true;
    }
    
    /**
     * Loads the given source file at least once.  This works like
     * {@link #load}, except that if it is called a second time with
     * the same <code>loadFile</code>, it will do nothing and return
     * true.
     * @param loadFile the full path of a file to load
     * @return true if the load succeeded, or if the load previously
     * succeeded with this filename; false otherwise
     * @throws IOException if the file cannot be parsed
     * @throws SchemeException if there is a scheme error when reading the file
     * scheme error when reading the file
     */
    public boolean loadOnce(final String loadFile)
            throws IOException, SchemeException {
        if (loadedOnce == null)
            loadedOnce = new java.util.HashSet();
        if (loadedOnce.contains(loadFile)) {
            return true;
        } else if (load(loadFile)) {
            loadedOnce.add(loadFile);
            return true;
        } else {
            return false;
        }
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
     * @return a Java Object, which will be Boolean or String
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
        } else {
            ret = v.toString();
        }
        return ret;
    }

    /**
     * Main function, for testing.  I'm not sure this still works -- I
     * haven't tested it for a while.
     */
//     public static void main(String args[]) {
//         try {
//             SchemeWrapper sw = new SchemeWrapper();
//             if (args.length < 1) {
//                 System.out.println("Usage: SchemeWrapper expr");
//                 System.exit(1);
//             } else {
//                 for (int i=0; i<args.length; i++) {
//                     String expr = args[i];
//                     if (expr.startsWith("(")) {
//                         System.out.println(sw.eval(expr));
//                     } else {
//                         System.out.println(sw.eval(expr, new Object[] { }));
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println("Exception: " + e);
//             e.printStackTrace();
//         }
//     }
}
