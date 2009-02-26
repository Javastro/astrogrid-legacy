/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.file.Systems;

/** implmentation of systems.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20086:07:28 PM
 */
public class SystemsImpl implements Systems {

    
    private final FileSystemManager vfs;
    
    /**
     * @param vfs
     */
    public SystemsImpl(final FileSystemManager vfs) {
        super();
        this.vfs = vfs;
    }

    public String[] listSchemes() {
         final String[] schemes = vfs.getSchemes();
         // work around - vfs in it's current config doesn't return 'http', even though is supported.
         if (ArrayUtils.contains(schemes,"http")) {
             return schemes;
         } else {
             return (String[]) ArrayUtils.add(schemes,"http");
         }
    }

}
