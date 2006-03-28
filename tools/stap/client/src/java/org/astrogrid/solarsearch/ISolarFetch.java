
package org.astrogrid.solarsearch;

import java.util.Calendar;
import java.util.Map;
import java.io.PrintWriter;
import java.io.IOException;


public interface ISolarFetch {
    
    public void fetch(Map info, PrintWriter output) throws IOException;
    
}