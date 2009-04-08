/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileProvider;

/** Experimental provider for exposing some examples, etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 8, 20093:03:32 PM
 */
public class ExampleProvider extends AbstractFileProvider implements VfsFileProvider {

    private final String examplesLocation;
    
    public FileObject findFile(final FileObject baseFile, String uri,
            final FileSystemOptions arg2) throws FileSystemException {
        try {
            if (uri.equals("examples:") || 
                    uri.equals("examples://") ||
                    uri.equals("examples:///")) {
              // uri parser chokes on these forms.
              uri = "examples:/";
            }
            final URI u = new URI(uri);
               
            final String target = examplesLocation + u.getSchemeSpecificPart();
            //@todo add in alternate machinery for resolving to vospace where available.
            return getContext().getFileSystemManager().resolveFile(target);
          } catch (final URISyntaxException e) {
            throw new FileSystemException(e);
          }
    }
    
    /**
     * 
     */
    public ExampleProvider(final String examplesLocation) {
        this.examplesLocation = examplesLocation;
    }

    protected final static Collection<Capability> caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
            Capability.DISPATCHER
    }));
    
    public Collection getCapabilities() {
        return caps;
    }

}
