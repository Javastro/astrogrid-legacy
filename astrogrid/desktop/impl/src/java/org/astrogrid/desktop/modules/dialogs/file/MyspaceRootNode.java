/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;

import uk.ac.starlink.connect.ErrorLeaf;
import uk.ac.starlink.connect.Node;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:47:17 PM
 */
class MyspaceRootNode extends MyspaceBranch {

		public MyspaceRootNode(MyspaceInternal msi, Community comm)  {
			super(null,null);
			this.comm = comm;
			this.msi = msi;
		}
		
		private final Community comm;
		private final MyspaceInternal msi;
		public Node[] getChildren() {
			synchronized(this) {
			if (getNode() == null) {
				try {
					setNode(msi.getClient().home());
				} catch (Exception e) {
					return new Node[]{
							new ErrorLeaf(this,e)
					};
				}
			}
			}
			return super.getChildren();
			
		}
		
		public String getName() {
			UserInformation info = comm.getUserInformation();
			return "Myspace - " + info.getId();
		}
		
		public String toString() {
			return getName();
		}
}
