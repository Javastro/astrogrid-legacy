/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileProvider;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.VospaceService;

/** Experimental provider for exposing some examples, etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 8, 20093:03:32 PM
 */
public class ExampleProvider extends AbstractFileProvider implements ActivatableVfsFileProvider{

    private final String examplesLocation;
    private final boolean active;
    
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
    public ExampleProvider(final String examplesLocation, Registry reg) {
        this.examplesLocation = examplesLocation;
        boolean isActive = false;
        try {
            // check whether examples location is a vospace ref.
            URI vos = new URI(examplesLocation);
            if ("vos".equals(vos.getScheme())) {
                // ok. derive the equivalent ivo://
                URI ivo = new URI("ivo://" + vos.getAuthority().replaceAll("!", "/") );
                // now check whether this exists.
                Resource r = reg.getResource(ivo);
                // if got this far, resource exists.  check it's a vospace.
                isActive = r instanceof VospaceService;
            } else {
                // examples is something else - assume it to be ok.
                isActive = true;
            }
        } catch (NotFoundException ex) {
           //ignored
        } catch (ServiceException ex) {
           //ignored
        } catch (URISyntaxException ex) {
            // not any kind of valid url, so ignore it for now.
        }
        active = isActive;

    }

    protected final static Collection<Capability> caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
            Capability.DISPATCHER
    }));
    
    public Collection getCapabilities() {
        return caps;
    }

    public boolean isActive() {
        return active;
    }

}
