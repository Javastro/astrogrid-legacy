package org.eurovotech.quaestor;

import java.lang.ClassNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import sisc.REPL;
import sisc.data.Value;
import sisc.data.Procedure;
import sisc.data.SchemeBoolean;
import sisc.data.SchemeString;
import sisc.data.Symbol;
import sisc.interpreter.Interpreter;
import sisc.interpreter.AppContext;
import sisc.interpreter.Context;
import sisc.interpreter.SchemeException;
import sisc.interpreter.SchemeCaller;
import sisc.ser.MemoryRandomAccessInputStream;
import sisc.ser.SeekableInputStream;
import sisc.util.Util;

/**
 * Wrapper for SISC.
 *
 * <p>This follows the outline in <a href="http://www.lisperati.com/quick.html"
 * ><code>http://www.lisperati.com/quick.html</code></a>, though that
 * uses the simpler .enter/.exit mechanism described in the SISC docs,
 * which those docs warn has threading problems.  There are also
 * examples of use in the <code>contrib</code> section of the SISC CVS tree.
 */
public class SchemeWrapper {

    //AppContext ctx;

    /**
     * Constructs a new wrapper for SISC.
     * @throws SchemeException passed on from execute
     */
    public SchemeWrapper()
            throws ClassNotFoundException, IOException, SchemeException {
        //ctx = new AppContext();
        Context.execute
            (//ctx,
             new SchemeCaller() {
                 public Object execute(Interpreter i) {
                     REPL.loadDefaultHeap(i);
                     return Boolean.TRUE;
                 }
             });
    }
    
                     
//     public SchemeWrapper()
//             throws ClassNotFoundException, IOException {
//         InputStream shp = getClass().getResourceAsStream("/sisc.shp");
//         if (shp == null)
//             throw new IOException("scheme heap not found");
//         final SeekableInputStream heap = new MemoryRandomAccessInputStream(shp);

//         ctx = new AppContext();
//         Context.execute
//             (ctx,
//              new SchemeCaller() {
//                  public Object execute(Interpreter i) {
//                      try {
//                          return Boolean.valueOf(REPL.loadHeap(i, heap));
//                      } catch (ClassNotFoundException e) {
//                          return "SchemeWrapper: can't load heap: " + e;
//                      }
//                  }
//              });
//     }

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
            (//ctx,
             new SchemeCaller() {
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
            (//ctx,
             new SchemeCaller() {
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
     * Loads the given source file into the interpreter.
     * @param loadFile the full path of a file to load
     * @return true if the load succeeded; false otherwise
     * @throws SchemeException passed on from execute
     */
    public boolean load(final String loadFile)
            throws SchemeException {
        Boolean stat = (Boolean)Context.execute
            (//ctx,
             new SchemeCaller() {
                 public Object execute(Interpreter r) {
                     return Boolean.valueOf(REPL.loadSourceFiles
                                            (r, new String[] { loadFile }));
                 }
             });
        return stat.booleanValue();
    }

    /**
     * Convert a SISC Value to a Java Object.  If the value is (scheme) boolean,
     * then return a Java {@link Boolean}; otherwise, if it is a
     * {@link SchemeString}, then convert it to a Java String using the
     * {@link SchemeString#asString} method, which does
     * suitable conversions of newlines; otherwise convert it to a
     * String value in the usual way, with {@link java.lang.Object#toString}.
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
