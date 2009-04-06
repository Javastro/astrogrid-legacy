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
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;

/** A 'dispatcher' filesystem - implements the 'workspace:' url scheme.
 * 
 *  <p/>
 *  When the user accesses the workspace: url, the user 
 * is asked to authenticate, and then rewriting to the homespace for that user (at the
 * moment myspace). The 'workspace:' scheme is more convenient to write than, eg,
 * ivo://uk.ac.le.star/mublefred and also allows user-independent scripts to be written
 * more easily (as workspace: gets resolved to the homespace of the user running
 * the script). 
 * 
 * <p>
 * Furthermore, it will provide a way to invisibly migrate to vospace - once a community
 * has replaced it's myspace service with a vospace service, this could be detected by 
 * this provider (using some at-the-moment unknown registry metadata) and from then on
 * workspace: for that user will redirect to their vospace storage. 
 * 
 * @todo when vospace is integrated with community, this will need a rewrite to redirect to 
 * myspace or vospace as appropriate
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 200712:47:40 PM
 */
public class WorkspaceProvider extends AbstractFileProvider implements VfsFileProvider{

	private final Community comm;
	protected final static Collection<Capability> caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
			Capability.DISPATCHER
	}));
	
	public WorkspaceProvider(final Community comm) {
		super();
		this.comm = comm;
	}
	public Collection<Capability> getCapabilities() {
		return caps;
	}
	// at the moment, requests a login, and then rewrites to the users myspace home.
	public FileObject findFile(final FileObject baseFile, String uri,
			final FileSystemOptions fileSystemOptions) throws FileSystemException  {
          final String homespace = getMySpaceHome(comm.getUserInformation());
	  try {
	    if (uri.equals("workspace:") || 
                uri.equals("workspace://") ||
                uri.equals("workspace:///")) {
	      // uri parser chokes on these forms.
	      uri = "workspace:/";
	    }
	    final URI u = new URI(uri);
           
	    final String target = homespace + u.getSchemeSpecificPart();
	    //@todo add in alternate machinery for resolving to vospace where available.
	    return getContext().getFileSystemManager().resolveFile(target);
	  } catch (final URISyntaxException e) {
	    throw new FileSystemException(e);
	  }
	}
        
        /**
         * Determines the homespace IVORN in MySpace. from a qualified user-name.
         * The IVORN is in the old style, i.e. ivo://authority/user# where the
         * authority part is taken from the IVORN for the community where the
         * user is registered.
         *
         * @param user All the user information provided by the community.
         * @return The IVORN for the homespace in string form.
         * @throws FileSystemException If the community information is insufficient.
         */
        private String getMySpaceHome(final UserInformation user) throws FileSystemException {
          
          // Extract the IVOA authority of the community in which 
          // the user is registered.
          final String c1 = user.getCommunity();
          if (c1 == null || !c1.startsWith("ivo://")) {
            throw new FileSystemException("Community name " + c1 + " is invalid");
          }
          final String c2 = c1.substring(6); // Strip ivo://
          final int slash = c2.indexOf('/');
          final String authority = (slash == -1)? c2 : c2.substring(0,slash);
          
          // Form the old-style IVORN for the home space.
          return "ivo://" + authority + '/' + user.getName() + '#';
        }

}
