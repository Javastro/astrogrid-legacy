/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;

/** Download and install certificates..
 * never overwrites a certificate which is already there. 
 * @author Noel Winstanley
 * @since Sep 7, 200611:27:55 AM
 */
public class InstallCertificates implements Runnable {

	private final File certDir;
	private final List certList;
	private final UIInternal ui;
	public InstallCertificates(final UIInternal ui, final String certDir, final List certList) {
		super();
		this.certDir = new File(certDir);
		this.certList = certList;
		this.ui = ui;
		
	}
	public void run() {
		(new BackgroundWorker(ui,"Checking for new certificates") {
			protected Object construct()  {
				if (! certDir.exists()) {
					certDir.mkdirs();
				}
				for (Iterator i = certList.iterator(); i.hasNext(); ) {
					InputStream is = null;
					OutputStream os = null;
					try {
					URL url = new URL((String)i.next());
					String certName = StringUtils.substringAfterLast(url.toString(),"/");
					File cert = new File(certDir,certName);
					if (!cert.exists()) { // missing - download it.
						os = new FileOutputStream(cert);
						is = url.openStream();
						Piper.pipe(is,os);
					}
					} catch (IOException e) {
						if (is != null) {
							try {
								is.close();
							} catch (IOException x) {
								// oh-well.
							}
						}
						if (os != null) {
							try {
								os.close();
							} catch (IOException x) {
								// never mind
							}
						}
					}
				}
				return null;
			}
		}).start();
	}

}
