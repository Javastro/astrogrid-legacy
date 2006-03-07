
package org.astrogrid.solarsearch;

import java.util.Calendar;
import java.util.Map;
import java.io.PrintWriter;
import java.io.IOException;


public interface ISolarSearch {
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException;
    
}