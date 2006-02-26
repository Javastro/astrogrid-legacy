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

    static private SchemeWrapper instance;

    /**
     * Constructs a new wrapper for SISC.  Private constructor.
     * @throws SchemeException passed on from execute
     */
    private SchemeWrapper()
            throws SchemeException {
        Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i) {
                     REPL.loadDefaultHeap(i);
                     return Boolean.TRUE;
                 }
             });
    }

    /**
     * Returns the single instance of the SchemeWrapper.
     */
    static public SchemeWrapper getInstance()
            throws SchemeException {
        if (instance == null)
            instance = new SchemeWrapper();
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
     * Evaluates the string, returning the value as a String.
     *
     * @param expr a scheme expression
     * @return a Java Object (String or Boolean) representing the
     * value of the expression
     */
    public Object eval(final String expr)
            throws IOException, SchemeException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i) {
                     try {
                         return schemeToJava(i.eval(expr));
                     } catch (Exception e) {
                         return e;
                     }
                 }
             });
        if (o instanceof IOException)
            throw (IOException)o;
        else if (o instanceof SchemeException)
            throw (SchemeException)o;
        else {
            assert(!(o instanceof Exception));
            return o;
        }
    }

    /**
     * Evaluates a named procedure, with arguments.  This is
     * equivalent to the expr <code>proc args...</code>.
     *
     * @param proc the name of a Scheme procedure defined in the top level
     * @param args an array of Java objects
     * @return a Java Object (String or Boolean) representing the
     * value of the expression
     * @throws SchemeException passed on from execute
     */
    public Object eval(final String proc, final Object[] args)
            throws IOException, SchemeException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter r) {
                     try {
                         Procedure p = (Procedure)r.eval(Symbol.get(proc));
                         Value[] v = r.createValues(args.length);
                         for (int i=0; i<args.length; i++)
                             v[i] = new sisc.modules.s2j.JavaObject(args[i]);
                         return schemeToJava(r.eval(p, v));
                     } catch (Exception e) {
                         return e;
                     }
                 }
             });
        if (o instanceof IOException)
            throw (IOException)o;
        else if (o instanceof SchemeException)
            throw (SchemeException)o;
        else {
            assert(!(o instanceof Exception));
            return o;
        }
    }

    /**
     * Evals the given input stream.
     */
    public Object evalInput(final java.io.InputStream in)
            throws SchemeException {
        Object o = Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter i) {
                     try {
                         return schemeToJava(i.evalInput
                                             (new StreamInputPort(in)));
                     } catch (Exception e) {
                         return e;
                     }
                 }
             });
        if (o instanceof SchemeException)
            throw (SchemeException)o;
        else {
            assert(!(o instanceof Exception));
            return o;
        }
    }

    /**
     * Loads the given source file into the interpreter.
     * @param loadFile the full path of a file to load
     * @return true if the load succeeded; false otherwise
     * @throws SchemeException passed on from execute
     */
    public boolean load(final String loadFile)
            throws SchemeException {
        Boolean stat = (Boolean)Context.execute
            (new SchemeCaller() {
                 public Object execute(Interpreter r) {
                     return Boolean.valueOf(REPL.loadSourceFiles
                                            (r, new String[] { loadFile }));
                 }
             });
        return stat.booleanValue();
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
