/*
 * Copyright  2003-2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.ws.security.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Load resources (or images) from various sources.
 *
 * @author Davanum Srinivas (dims@yahoo.com).
 */
public class Loader {
    private static Log log = LogFactory.getLog(Loader.class.getName());

    /**
     * This method will search for <code>resource</code> in different
     * places. The rearch order is as follows:
     * <ol>
     * <li>Search for <code>resource</code> using the thread context
     * class loader under Java2. If that fails, search for
     * <code>resource</code> using the class loader that loaded this
     * class (<code>Loader</code>).
     * <li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is
     * using the system class loader in JDK 1.2 and virtual machine's
     * built-in class loader in JDK 1.1.
     * </ol>
     */
    static public URL getResource(String resource) {
        ClassLoader classLoader = null;
        URL url = null;
        try {
            // We could not find resource. Ler us now try with the
            // classloader that loaded this class.
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                log.debug("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        } catch (Throwable t) {
            log.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
    
        // Last ditch attempt: get the resource from the class path. It
        // may be the case that clazz was loaded by the Extentsion class
        // loader which the parent of the system class loader. Hence the
        // code below.
        log.debug("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }

    /**
     * Get the Thread context class loader.
     */
    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * If running under JDK 1.2 load the specified class using the
     * <code>Thread</code> <code>contextClassLoader</code> if that
     * fails try Class.forname.
     */
    static public Class loadClass(String clazz) throws ClassNotFoundException {
        try {
            Class c = getTCL().loadClass(clazz);
            if (c != null)
                return c;
        } catch (Throwable e) {
          log.debug("Throwable was ignored: " + e.getMessage());
        }
        // we reached here because tcl was null or because of a
        // security exception, or because clazz could not be loaded...
        // In any case we now try one more time
        return Class.forName(clazz);
    }
}
