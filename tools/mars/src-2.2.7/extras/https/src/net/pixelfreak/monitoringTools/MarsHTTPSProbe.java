/*  MARS HTTPS Probe
    Copyright (C) 2003 Scott Ahten
 
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program; if not, it is available at
    http:///www.gnu.org/copyleft/gpl.html, or by writing to the
    Free Software Foundation, Inc., 59 Temple Place - Suite 330,
    Boston, MA  02111-1307, USA.
 */

package net.pixelfreak.monitoringTools;

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  Scott Ahten
 */
public class MarsHTTPSProbe extends org.altara.mars.engine.Probe {
    
    /** Creates a new instance of MarsHTTPSProbe */
    public MarsHTTPSProbe(Service service) {
        super(service);
        
    }
    
    protected org.altara.mars.Status doProbe() throws java.lang.Exception {
        
        String host = service.getParameter("host");
        int port = service.getPort();
        String path = service.getParameter("path");
        String expected = service.getParameter("content");
        
        Status probeResults = new Status(Status.PROBEFAIL);
        boolean validResponse = true;
        long responseTime=0;
        
        ClientDebugger debug = Debug.getCurrent().newDebugger("HTTPS https://"+host+":"+port+path);
        
        try{
            URL aURL = new URL("https://"+host+":"+port+path);
            URLConnection urlc = aURL.openConnection();
            Date startTime = new Date();
            urlc.connect();

            debug.message("connected");
            
            if (urlc instanceof HttpURLConnection) {
                
                HttpURLConnection hrulc = (HttpURLConnection) urlc;
                int rCode = hrulc.getResponseCode();
                
                if ((rCode > 199 && rCode < 400)) {
                    
                    // read data at URL
                    StringBuffer contentBuff= new StringBuffer();
                    String content="";
                    byte b[]= new byte[1024];
                    int nbytes;
                    BufferedInputStream in = new BufferedInputStream(urlc.getInputStream(),2048);
                    while((nbytes =in.read(b,0,1024))!=-1){
                        content = new String(b,0,nbytes);
                        contentBuff.append(content);
                    }
                    
                    // check returned data for expected content
                    if (expected != null && expected.length()>0) {
                        if (contentBuff.toString().indexOf(expected) == -1) {
                            validResponse = false;
                        } 
                    }
                    
                    
                } else {
                    debug.message("error "+rCode);
                    validResponse = false;
                    
                }
                
                Date endTime = new Date();
                responseTime = endTime.getTime()-startTime.getTime();
                
                if (validResponse) {
                    debug.message("ok "+rCode);
                    probeResults = new Status(Status.UP, responseTime);
                } else {
                    debug.message("got bad response");
                    probeResults = new Status(Status.UNEXPECTED, responseTime);
                }
                
                probeResults.setProperty("received", hrulc.getHeaderField(null));
                Map headerMap = urlc.getHeaderFields();
                
                // Set remaining HTTP headers
                for (Iterator it=headerMap.keySet().iterator(); it.hasNext(); ) {
                    Object key = it.next();
                    if (key!=null) {
                        probeResults.setProperty(key.toString(), hrulc.getHeaderField((String)key));
                    }
                }
                
            }
            
        } catch(IOException ex) {
            debug.message(ex.toString());
            probeResults = new Status(Status.DOWN);
        } finally {
            debug.close();
        }
        
        return probeResults;
        
    }
    
}
