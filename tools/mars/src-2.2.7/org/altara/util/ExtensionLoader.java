/* Altara Utility Classes
   Copyright (C) 2001-2003 Brian H. Trammell

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.
	
	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public
	License along with this library; if not, it is available at
	http://www.gnu.org/copyleft/lesser.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA
*/

package org.altara.util;

import java.util.jar.*;
import java.util.*;
import java.net.*;
import java.io.*;

public final class ExtensionLoader {

	private ExtensionLoader() {
		// can never be instantiated
	}

    /*
	public static Class loadExtension(File file, String manifestKey) 
            throws IOException, MalformedURLException, ClassNotFoundException {
		// determine the class name from the manifest
		JarFile jarFile = new JarFile(file);
		Attributes manifestAttrs = jarFile.getManifest().getMainAttributes();
		String className = manifestAttrs.getValue(manifestKey);

		// construct a class loader on the jar file
		URLClassLoader extLoader =
			new URLClassLoader(new URL[] {file.toURL()});
		// load and return the class
		return extLoader.loadClass(className);
	}
    */

    public static List loadExtensions(File file, String manifestKey)
        throws IOException, MalformedURLException, ClassNotFoundException {
        List out = new LinkedList();

        // get a class loader for the jar file
        URLClassLoader extLoader = new URLClassLoader(new URL[] {file.toURL()});

        // determine the class name list from the manifest
        JarFile jarFile = new JarFile(file);
        Attributes manifestAttrs = jarFile.getManifest().getMainAttributes();
        String classNameList = manifestAttrs.getValue(manifestKey);

        // tokenize and iterate over class names in the list
        StringTokenizer classNameTok = new StringTokenizer(classNameList," \t,");
        while (classNameTok.hasMoreTokens()) {
            // load and add the next class
            out.add(extLoader.loadClass(classNameTok.nextToken()));
        }

        return out;
    }

	public static List scanExtensions(File jarDir, FilenameFilter filter,
			String manifestKey, LoadExceptionHandler eh) {
		List out = new LinkedList();
		
		// get a list of all files in the directory conforming to the filter
		File[] jarFiles = jarDir.listFiles(filter);

		// iterate over each jar file, loading the necessary class
		for (int i = 0; i < jarFiles.length; i++) {
			try {
				out.addAll(loadExtensions(jarFiles[i],manifestKey));
			} catch (Exception ex) {
				if (eh != null) eh.handleLoadException(jarFiles[i],ex);
			}
		}

		// return the list of loaded classes
		return out;
	}
}
