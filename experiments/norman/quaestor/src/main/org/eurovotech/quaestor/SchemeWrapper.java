package org.eurovotech.quaestor;

import java.lang.ClassNotFoundException;
import java.io.InputStream;
import java.io.IOException;

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
    private static java.net.URL heap;
    private static java.util.Set loadedOnce;

    /**
     * Constructs a new wrapper for SISC.  Private constructor.
     * @throws IOException if there are problems locating the heap
     */
    private SchemeWrapper()
            throws IOException {
        // The complication below is because the AppContext.findHeap()
        // method can't find sisc.shp in its classpath.  If
        // sisc-heap.jar is installed then findHeap() _can_ find the
        // heap in that, though this is more costly in memory, since
        // it has to load the entire heap file at once.  It's probably
        // not a big deal, however, and it's certainly neater.  The
        // only problem is that sisc-heap.jar isn't included in the
        // binary SISC distribution, as of sisc-1.13.4, so the
        // quaestor build.xml can't include it in the .war unless I
        // build SISC from source and hand-install the jar file, which
        // isn't complicated, but requires leaving notes to myself,
        // and READMEs in any Quaestor distribution.  So leave this as
        // it is for now, and possibly revisit it if SISC starts
        // distributing sisc-heap.jar in some future version.
        if (heap != null) {
            AppContext ctx = new AppContext();
            sisc.ser.SeekableInputStream heapstream = null;
            try {
                heapstream = AppContext.openHeap(heap);
            } catch (IOException e) {
                throw new IOException("Failed to load SISC heap from " + heap
                                      + " (" + e + ")");
            }
            try {
                if (!ctx.addHeap(heapstream)) {
                    throw new IOException
                            ("Failed to load SISC heap from " + heap
                             + " (opened heap file but loading failed)");
                }
            } catch (ClassNotFoundException e) {
                // it's not clear to me why addHeap throws this,
                // but presumably the detail message explains all...
                throw new IOException("Failed to load SISC heap from " + heap
                                      + " (" + e + ")");
            }
            Context.setDefaultAppContext(ctx);
        }
    }

    /**
     * Returns the single instance of the SchemeWrapper.
     */
    public static SchemeWrapper getInstance()
            throws IOException {
        if (instance == null)
            instance = new SchemeWrapper();
        return instance;
    }

    /**
     * Specify the heap to be used when creating the instance.
     * This is required since the heap file is not in the Tomcat classpath,
     * and so will not be found by SISC's mechanism for finding the
     * default heap.
     * @param heapURL a URL indicating the location of the heap
     */
    public static void useHeap(java.net.URL heapURL) {
        heap = heapURL;
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
     * Evaluates the string, returning the value as a String.
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
     * @param expr a scheme expression
     * @return a Java Object (String, Boolean or null) representing the 
     * value of the expression 
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if one there was a problem executing
     * the expression
     */
    public Object eval(final String expr)
            throws IOException, SchemeException {
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
            assert(o instanceof IOException);
            throw (IOException)o;
        }
        return o;
    }

    /**
     * Evaluates a named procedure, with arguments.  This is
     * equivalent to the expr <code>proc args...</code>.
     *
     * @param proc the name of a Scheme procedure defined in the top level
     * @param args an array of Java objects
     * @return a Java Object (String, Boolean or null) representing the
     * value of the expression
     * @throws IOException if there was a problem parsing the expression
     * @throws SchemeException if one there was a problem executing
     * the expression
     */
    public Object eval(final String proc, final Object[] args)
            throws IOException, SchemeException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter r)
                         throws SchemeException {
                     Procedure p = (Procedure)r.eval(Symbol.get(proc));
                     Value[] v = r.createValues(args.length);
                     for (int i=0; i<args.length; i++)
                         v[i] = new sisc.modules.s2j.JavaObject(args[i]);
                     return schemeToJava(r.eval(p, v));
                 }
             });
        if (o instanceof Exception) {
            assert(o instanceof IOException);
            throw (IOException)o;
        }
        return o;
    }

    /**
     * Evals the given input stream.
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
        // some parsing by eye.  The following expression evaluates to
        // either #t on success, or a string on error.
        Object o = eval("(with/fc (lambda (m e) (define (show-err r) (let ((parent (error-parent-error r))) (format #f \"Error at ~a: ~a (~a)\" (error-location r) (error-message r) (if parent (show-err parent) \"thatsall\")))) (show-err m)) (lambda () (load \"" + loadFile + "\") #t))");
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
     * <dt>{@link SchemeString}</dt>
     * <dd>return a Java String using the {@link SchemeString#asString}
     * method, which does suitable conversions of newlines</dd>
     * <dt>scheme void</dt>
     * <dd>return null</dd>
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
