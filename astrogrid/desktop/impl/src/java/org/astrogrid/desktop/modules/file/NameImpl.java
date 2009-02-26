/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.file.Name;

/** Implementation of names component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 31, 20083:54:28 PM
 */
public class NameImpl extends AbstractFileComponent implements Name{

    /**
     * @param vfs
     */
    public NameImpl(final FileSystemManager vfs) {
        super(vfs);
    }

    public String getScheme(final URI arg0) throws ACRException {
        nullCheck(arg0);
        return arg0.getScheme();  
    }

    public URI getRoot(final URI uri) throws ACRException{
        nullCheck(uri);
        try {
            return new URI(fn(uri).getRootURI());
        } catch (final URISyntaxException x) {
            throw new ACRException(x.getMessage());
        }     
    }
    

    public String getName(final URI arg0)throws ACRException {
        nullCheck(arg0);
        return fn(arg0).getBaseName();        
    }


    public String getExtension(final URI arg0)throws ACRException {
        nullCheck(arg0);
        return fn(arg0).getExtension();
        
    }
    
    public String getPath(final URI arg0) throws ACRException {
        nullCheck(arg0);
        final String p =  fn(arg0).getPath();
        return StringUtils.isEmpty(p) ? "/" : p;
    }
    
    public URI getParent(final URI arg0) throws ACRException{
        nullCheck(arg0);
        try {
            final FileName parent = fn(arg0).getParent();
            if (parent == null) {
                throw new InvalidArgumentException("Root has no parent");
            }
            return new URI(parent.getURI());
        } catch (final URISyntaxException x) {
            throw new ACRException(x.getMessage());
        }         
    }

    public boolean isAncestor(final URI arg0, final URI arg1)throws ACRException {
        nullCheck(arg0);
        nullCheck(arg1);
       return fn(arg0).isAncestor(fn(arg1));
    }

    public String relativize(final URI arg0, final URI arg1) throws ACRException{
        nullCheck(arg0);
        nullCheck(arg1);
        try {
            return fn(arg0).getRelativeName(fn(arg1));
        } catch (final FileSystemException x) {
          throw new ACRException(x.getMessage());
        }
    }

    public URI resolve(final URI arg0, final String arg1) throws ACRException {
        nullCheck(arg0);
        try {
         final FileObject resolved = fo(arg0).resolveFile(arg1);
         final String uri = resolved.getName().getURI();
         return new URI(StringUtils.replace(uri.trim()," ","%20"));
        } catch (final FileSystemException e) {
            throw new ACRException(e.getMessage());
        } catch (final URISyntaxException x) {
            throw new ACRException(x.getMessage());
        }
    }

}
