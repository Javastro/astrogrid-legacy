package org.cougaar.profiler.astrogrid;

import org.cougaar.profiler.Options;
/** configuration class for profiling astrogird - 
 * passing in module parameter 'minimal' gives minimal profiling of just astrogird code,
 * passing in anything else (including null) gives  profiles all org.astrogrid code extensively, everything else less so 
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2004
 *
 */
public class Config {

  /**
   * Option that will track 100% of allocations,
   * record allocation timestamp,
   * record allocation stacktrace,
   * record size/capacity/context.
   * <p>
   * This is the best detail but the most overhead.
   */
  private static final Options FULL_DETAIL =
    new Options(
        true, // time
        true, // stack
        true, // size
        true, // capacity
        true, // context
        1.00  // sampleRatio
        );

  /**
   * Track only 1% of allocations,
   * record only the instance itself,
   * allow size/capacity.
   * <p>
   * This is very low overhead but only samples a small
   * subset of the instances.  This is ideal for the
   * initial profiling runs, to get a rough idea of the
   * allocation counts.
   */
  private static final Options MIN_DETAIL =
    new Options(
        false, // time
        false, // stack
        true,  // size
        true,  // capacity
        false, // context
        0.01   // sampleRatio
        );

  /*
   * The profiled class will call this method to get its options.
   *
   * @param module optional module name, e.g. "xerces" or null
   * @param classname non-null class name, e.g.
   *   "org.apache.xerces.dom.DocumentImpl$LEntry"
   * @return profiling options for the class, or null if the
   *   class shouldn't be tracked.
   */
  public static final Options getOptions(
      String module,
      String classname) {
      System.out.print("getting options " + classname);
      if (module != null && module.equalsIgnoreCase("minimal")) {
          if (classname.startsWith("org.astrogrid")) {
              System.out.println(" - MIN");
              return MIN_DETAIL;
          } else {
              System.out.println(" - NONE");
              return null; // signifies no profiling
          }      
      } else {
          if (classname.startsWith("org.astrogrid")) {
              System.out.println(" - FULL");
              return FULL_DETAIL;
          } else {
              System.out.println(" - MIN");
              return MIN_DETAIL;
          }
      }
  }
}
