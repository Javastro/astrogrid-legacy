
package org.astrogrid.solarsearch;

import java.util.Calendar;
import java.util.Map;
import java.io.OutputStream;
import java.io.IOException;


public interface ISolarFetch {
    
    public void fetch(Map info, OutputStream output) throws IOException;
    
}