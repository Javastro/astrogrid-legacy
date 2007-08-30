/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.ShowOnceDialogue;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.io.Piper;

/** saves formatted details of the selection as a list to disk.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 */
public class SaveXoXoListActivity extends AbstractResourceActivity {

	protected boolean invokable(Resource r) {
		return true;
	}


	/**
	 * 
	 */
	public SaveXoXoListActivity(ResourceChooserInternal chooser,FileSystemManager vfs) {
		this.chooser = chooser;
		this.vfs = vfs;
		setText("Save Formatted List");
		setToolTipText("Save formatted details of the current selection to a textfile");
		setIcon(IconHelper.loadIcon("ascii16.png"));
	}

	private final ResourceChooserInternal chooser;
	private final FileSystemManager vfs;
	public void actionPerformed(ActionEvent e) {
		final List rs = computeInvokable();
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}	
		final URI u = chooser.chooseResourceWithParent("Save resource list",true,true,true,comp);
		if (u == null) {
			return;
		}
		(new BackgroundWorker(uiParent.get(),"Saving resource list") {
			protected Object construct() throws Exception {
				PrintWriter out = null;
				try {
				    out = new PrintWriter(new java.io.OutputStreamWriter(vfs.resolveFile(u.toString()).getContent().getOutputStream()));

					out.print("<ul class='xoxo'>");
					out.println("<!-- See http://microformats.org/wiki/xoxo for details of XoXo format -->");
					for (Iterator i = rs.iterator(); i.hasNext();) {
						Resource r = (Resource) i.next();
						String name = r.getShortName();
						if (name == null || name.trim().length() == 0) {
							name = r.getTitle();
						}
						out.println("  <li>");
						if (r.getContent().getReferenceURI() != null) {
							out.println("      <a href='" + r.getContent().getReferenceURI() + "'");
							out.println("             >" + name + "</a>" );
						}	else {
							out.println("      " + name);
						}
						out.println("       <dl>");
						out.println("         <dt>identifier</dt>");
						out.println("         <dd>" + r.getId() + "</dd>");
						out.println("         <dt>title</dt>");
						out.println("         <dd>" + r.getTitle() + "</dd>");
						out.println("         <dt>type</dt>");
						out.println("         <dd>" + r.getType() + "</dd>");	
						if (r instanceof Service) { // for roy.
							Capability[] capabilities = ((Service)r).getCapabilities();
							out.println("         <ul>");
							for (int j = 0; j < capabilities.length; j++) {
								Capability cap = capabilities[j];
								Interface[] interfaces = cap.getInterfaces();
								for (int k = 0; k < interfaces.length; k++) {
									Interface it = interfaces[k];
									out.println("          <li>" + it.getType() + "<ul>");
									AccessURL[] accessUrls = it.getAccessUrls();
									for (int index = 0; index < accessUrls.length; index++) {
										AccessURL accessURL = accessUrls[index];
										out.println("                  <li><a href='" + accessURL.getValueURI() + "'>" +accessURL.getUse()+ "</a></li>");
									}
									out.println("          </ul></li>");
								}
							}
							out.println("         </ul>");
						}
			//			out.println("         <dt>description</dt>");
			//			out.println("         <dd>");
			//			out.println(r.getContent().getDescription());
			//			out.println("         </dd>");
						out.println("      </dl>");
						out.println("  </li>");
					}
					out.println("</ul>");
				} finally {
					if (out != null) {
						out.close();
					}
				}
				return null;
			}
		}).start();
	}

}