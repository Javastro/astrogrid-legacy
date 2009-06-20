
package org.astrogrid.stapsearch;

import java.util.Calendar;
import java.util.Map;
import java.io.OutputStream;
import java.io.IOException;


public interface IStapFetch {
    
    public void fetch(Map info, OutputStream output) throws IOException;
    
}