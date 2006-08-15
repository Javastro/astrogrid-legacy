
package org.astrogrid.stapsearch;

import java.util.Calendar;
import java.util.Map;
import java.io.PrintWriter;
import java.io.IOException;


public interface IStapSearch {
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException;
    
}