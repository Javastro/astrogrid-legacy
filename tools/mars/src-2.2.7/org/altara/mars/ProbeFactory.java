/* MARS Network Monitoring Engine
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002 Leapfrog Research & Development, LLC

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

package org.altara.mars;

import java.util.*;
import java.net.*;
import java.io.*;
import org.altara.util.*;
import org.altara.mars.engine.*;
import org.apache.oro.text.regex.*;

/** ProbeFactory creates probes of an appropriate class for a
	given type of service. ProbeFactory is considered part of the
	data model, since a Service's ProbeFactory encapsulates
	how to test the service, and therefore what type of
	service it is.
	<p>
	ProbeFactory itself maps service type names ("http", "smtp", etc.)
	to ProbeFactory instances, and handles dynamic loading of
	probes from JAR files in the home directory
*/

public abstract class ProbeFactory implements java.io.Serializable {

	public static final String PROBE_JAR_PREFIX = 
        "probe_";
    public static final String PROBE_MANIFEST_KEY = 
        "MarsProbeFactoryClass";
    
    private static Map factoryMap = new TreeMap();

	protected static Perl5Compiler recompiler = new Perl5Compiler();

	public static void registerFactory(ProbeFactory fac) {
		factoryMap.put(fac.getName(),fac);
	}

	public static Iterator getRegisteredServiceTypes() {
		return factoryMap.keySet().iterator();
	}

	public static ProbeFactory getFactory(String name) {
		return (ProbeFactory)factoryMap.get(name);
	}

	public static void loadDynamic(File homeDir) {
        ProbeLoadExceptionHandler pleh = new ProbeLoadExceptionHandler();
        ProbeFilenameFilter pff = new ProbeFilenameFilter();
        Main.getMain().showStatus("Scanning for probes in "+homeDir.getAbsolutePath()+"...");
        Iterator probes = ExtensionLoader.scanExtensions(homeDir,
            pff, PROBE_MANIFEST_KEY, pleh).iterator();
        while (probes.hasNext()) {
            try {
                Class nextProbeFactoryClass = (Class)probes.next();
                ProbeFactory nextProbeFactory =
                        (ProbeFactory)nextProbeFactoryClass.newInstance();
                registerFactory(nextProbeFactory);
                Main.getMain().showStatus("Loaded probe "+nextProbeFactoryClass);
            } catch (Exception ex) {
                pleh.handleLoadException(null,ex);
            }
        }
    }

    private static class ProbeLoadExceptionHandler
            implements LoadExceptionHandler {
        public void handleLoadException(File file, Exception ex) {
            if (file == null) {
                Main.getMain().showStatus("Error loading probe: "+ex.getMessage());
            } else {
                Main.getMain().showStatus("Error loading probe from "+
                    file.getAbsolutePath()+": "+ex.getMessage());
            }
            ex.printStackTrace();
        }
    }

    private static class ProbeFilenameFilter
            implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.startsWith(PROBE_JAR_PREFIX) && name.endsWith(".jar"));
        }
    }

	private String name;

	protected ProbeFactory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract Probe createProbe(Service service);
	public abstract int getDefaultPort();

	public String[] getServiceParamNames() {
		return null;
	}

	public String[] getServiceParamLabels() {
		return null;
	}

	public String getServiceParamDefault(Service service, String name) {
		return null;
	}
}
