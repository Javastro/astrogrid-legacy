/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.ErrorLeaf;
import uk.ac.starlink.connect.Node;

/** @todo show some sort of curson / hourglass when myspace loads up for the first time.
 * 	- copy from the glasspane used on myspace browser.
 * @author Noel Winstanley
 * @since Nov 6, 20064:47:17 PM
 */
class MyspaceRootNode extends MyspaceBranch {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(MyspaceRootNode.class);

		public MyspaceRootNode(MyspaceInternal msi, Community comm)  {
			super(null,null);
			this.comm = comm;
			this.msi = msi;
		} 
		
		private final Community comm;
		private final MyspaceInternal msi;
		
		// overrides implementation in MyspaceBranch - lazily connects to myspace.
		public Node[] getChildren() {
			synchronized(this) {
			if (super.getNode() == null) {
				try {
					if (!comm.isLoggedIn()) {
						comm.guiLogin();
					}
					super.setNode(msi.getClient().home());
				} catch (Exception e) {
					logger.debug("Failed to connect to msypace - returning error leaf",e);
					return new Node[]{
							new ErrorLeaf(this,e)
					};
				}
			}
			}
			return super.getChildren();
			
		}
		// overides implementation on ms branch - lazily connects.
		// once either getNode() or getChildren() is called once, this object is connected to myspace.
		protected FileManagerNode getNode() {
			synchronized(this) {
				if (super.getNode() == null) {
					try {
						if (!comm.isLoggedIn()) {
							comm.guiLogin();
						}						
						super.setNode(msi.getClient().home());
					} catch (Exception e) {
						logger.debug("Failed to connect to msypace - node will be null",e);
					}					
				}
			}
			return super.getNode();
		}
		
		protected void setNode(FileManagerNode fn) {
			throw new UnsupportedOperationException("Unsupported: setting the node for a myspace root");
		}
		
		public String getName() {
			if (super.getNode() == null) { // not logged in yet.
				return "Myspace";
			} else {
				UserInformation info = comm.getUserInformation();
				return "Myspace - " + info.getId();
			}
		}
		
		public String toString() {
			return getName();
		}
}
