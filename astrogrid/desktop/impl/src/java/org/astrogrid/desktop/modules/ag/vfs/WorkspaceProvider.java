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

/** A 'dispatcher' filesystem - implements the 'workspace:' url scheme by 
 * asking the user to authenticate, and then rewriting to the homespace for that user (at the
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
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 5, 200712:47:40 PM
 */
public class WorkspaceProvider extends AbstractFileProvider implements VfsFileProvider{

	private final Community comm;
	protected final static Collection caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
			Capability.DISPATCHER
	}));
	
	public WorkspaceProvider(final Community comm) {
		super();
		this.comm = comm;
	}
	public Collection getCapabilities() {
		return caps;
	}
	// at the moment, requests a login, and then rewrites to the users myspace home.
	public FileObject findFile(FileObject baseFile, String uri,
			FileSystemOptions fileSystemOptions) throws FileSystemException  {
		UserInformation user = comm.getUserInformation();
		String homespace = user.getId().toString();
		try {
			if (uri.equals("workspace:") || uri.equals("workspace://")) {
				// uri parser chokes on these forms. if there's no path following
				uri = "workspace:/";
			}
		URI u = new URI(uri);
		String target = homespace + u.getSchemeSpecificPart();
		//@todo add in alternate machinery for resolving to vospace where available.
		return getContext().getFileSystemManager().resolveFile(target);
		} catch (URISyntaxException e) {
			throw new FileSystemException(e);
		}
	}

}
