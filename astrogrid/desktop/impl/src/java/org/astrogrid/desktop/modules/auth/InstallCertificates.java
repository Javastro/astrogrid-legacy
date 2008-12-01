/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Download and install certificates.
 * never overwrites a certificate which is already there. 
 * @author Noel Winstanley
 * @since Sep 7, 200611:27:55 AM
 * @TEST factor out parsing steps and unit-test them.
 */
public class InstallCertificates implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(InstallCertificates.class);

	private final File certDir;
	private final List certLists;
	private final UIContext ui;
	public InstallCertificates(final UIContext ui, final String certDir, final List certLists) {
		super();
		this.certDir = new File(certDir);
		this.certLists = certLists;
		this.ui = ui;
		
	}
	public void run() {
		(new BackgroundWorker(ui,"Checking for new certificates",Thread.MIN_PRIORITY) {
			protected Object construct() throws Exception  {
				if (! certDir.exists()) {
					reportProgress("Certificate directory does not exist - creating " + certDir);
					if (!certDir.mkdirs()) {
					    throw new Exception("Failed to create certificate directory - unable to install certificates");
					}
				}
				final XMLInputFactory fac = XMLInputFactory.newInstance();
				int count = 0;
				final int max = certLists.size();
				setProgress(count,max);
				for (final Iterator i = certLists.iterator(); i.hasNext(); ) {
					// fetch and parse each of these certificate lists.
				    
					XMLStreamReader in = null;
					final String certList = (String)i.next();
					reportProgress("Reading certificate list from " + certList);
					try {
						in = fac.createXMLStreamReader((new URL(certList)).openStream());
						while (in.hasNext()) {
							in.next();
							if (in.isStartElement() && in.getLocalName().equals("item")) {
								handleSingleCertificate(in.getElementText().trim());
							}
						}
					} catch (final XMLStreamException x) {
						reportProgress("Failed to parse " + certList );
					} catch (final IOException x) {
						reportProgress("Failed to parse " + certList );						
					} finally {
					    setProgress(++count,max);
						try {
							if (in != null) {
								in.close();
							}
						} catch (final XMLStreamException x) {
						    //meh
						}
					}					
				}
				return null;
			}

			// checks whether we've already downloaded a particular certificate.
			// and fetches it if not.
			private void handleSingleCertificate(final String certificateURL) {
				URL url = null;
				try {
					url = new URL(certificateURL);
					final String certName = StringUtils.substringAfterLast(url.toString(),"/");
					final File cert = new File(certDir,certName);
					if (!cert.exists()) { // missing - download it.
						logger.info("Downloading new certificate " + url);
						FileUtils.copyURLToFile(url,cert);
					}
				} catch (final IOException e) {
					logger.warn("Failed to downlooad certificate " + url);
				} 
			}
		}).start();
	}

}
