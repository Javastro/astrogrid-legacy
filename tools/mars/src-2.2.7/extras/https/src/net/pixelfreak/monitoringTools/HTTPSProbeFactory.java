/*  MARS HTTPS Probe Factory
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

/**
 *
 * @author  Scott Ahten
 */
public class HTTPSProbeFactory extends org.altara.mars.ProbeFactory {
    
    /** Creates a new instance of HTTPSProbeFactory */
    public HTTPSProbeFactory() {
        
       super("https");
    }
    
    public org.altara.mars.engine.Probe createProbe(org.altara.mars.Service service) {
        
       return new MarsHTTPSProbe(service);
    }
    
    public int getDefaultPort() {
        
        return 443;
    }
    
    public String[] getServiceParamNames(){
        
        
        String[] parms = {"host","path","content"};
        return parms;
        
    }
    
    public String[] getServiceParamLabels(){
        
        
        String[] parms = {"Hostname","Path","Content"};
        return parms;
        
    }
    
    public String getServiceParamDefault(String name){
      
        return "";
        
    }
}
