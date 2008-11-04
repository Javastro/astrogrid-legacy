/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;

/** Save formatted details of resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 6, 200711:54:38 AM
 * @TEST
 */
public class SaveXoXoListActivity extends AbstractResourceActivity 
    implements Activity.NoContext, Activity.NoTask{

	protected boolean invokable(final Resource r) {
		return true;
	}


	/**
	 * 
	 */
	public SaveXoXoListActivity(final ResourceChooserInternal chooser,final FileSystemManager vfs) {
	    setHelpID("activity.xoxo");
		this.chooser = chooser;
		this.vfs = vfs;
		setText("Export Abbreviated XML"+ UIComponentMenuBar.ELLIPSIS);
		setToolTipText("Export a summary of the selected resource(s) in HTML-like format");
		setIcon(IconHelper.loadIcon("ascii16.png"));
	}

	private final ResourceChooserInternal chooser;
	private final FileSystemManager vfs;
	public void actionPerformed(final ActionEvent e) {
		final List rs = computeInvokable();
		Component comp = null;
		if (e.getSource() instanceof Component) {
			comp = (Component)e.getSource();
		}	
		final URI u = chooser.chooseResourceWithParent("Choose output file",true,true,true,comp);
		if (u == null) {
			return;
		}
		(new BackgroundWorker(uiParent.get(),"Exporting summaries",BackgroundWorker.LONG_TIMEOUT) {
			protected Object construct() throws Exception {
				PrintWriter out = null;
                FileObject fo = null;
				final int max = rs.size() + 2;
				int count = 0;
				setProgress(count,max);
				try {
                    fo = vfs.resolveFile(u.toString());
				    out = new PrintWriter(new java.io.OutputStreamWriter(fo.getContent().getOutputStream()));
				    setProgress(++count,max);
					out.print("<ul class='xoxo'>");
					out.println("<!-- See http://microformats.org/wiki/xoxo for details of XoXo format -->");
					for (final Iterator i = rs.iterator(); i.hasNext();) {
						final Resource r = (Resource) i.next();
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
							final Capability[] capabilities = ((Service)r).getCapabilities();
							out.println("         <ul>");
							for (int j = 0; j < capabilities.length; j++) {
								final Capability cap = capabilities[j];
								final Interface[] interfaces = cap.getInterfaces();
								for (int k = 0; k < interfaces.length; k++) {
									final Interface it = interfaces[k];
									out.println("          <li>" + it.getType() + "<ul>");
									final AccessURL[] accessUrls = it.getAccessUrls();
									for (int index = 0; index < accessUrls.length; index++) {
										final AccessURL accessURL = accessUrls[index];
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
		                  setProgress(++count,max);
					}
					out.println("</ul>");
				} finally {
					if (out != null) {
						out.close();
                        fo.refresh();
					}
	                   setProgress(++count,max);
				}
				return null;
			}
            protected void doFinished(final Object result) {
                parent.showTransientMessage("Export complete","");
            }			
		}).start();
	}

}
