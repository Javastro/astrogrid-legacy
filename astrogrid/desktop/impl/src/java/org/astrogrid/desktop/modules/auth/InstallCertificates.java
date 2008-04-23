/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;

/** Download and install certificates..
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
				XMLInputFactory fac = XMLInputFactory.newInstance();
				int count = 0;
				int max = certLists.size();
				setProgress(count,max);
				for (Iterator i = certLists.iterator(); i.hasNext(); ) {
					// fetch and parse each of these certificate lists.
				    
					XMLStreamReader in = null;
					String certList = (String)i.next();
					reportProgress("Reading certificate list from " + certList);
					try {
						in = fac.createXMLStreamReader((new URL(certList)).openStream());
						while (in.hasNext()) {
							in.next();
							if (in.isStartElement() && in.getLocalName().equals("item")) {
								handleSingleCertificate(in.getElementText().trim());
							}
						}
					} catch (XMLStreamException x) {
						reportProgress("Failed to parse " + certList );
					} catch (IOException x) {
						reportProgress("Failed to parse " + certList );						
					} finally {
					    setProgress(++count,max);
						try {
							if (in != null) {
								in.close();
							}
						} catch (XMLStreamException x) {
						    //meh
						}
					}					
				}
				return null;
			}

			// checks whether we've already downloaded a particular certificate.
			// and fetches it if not.
			private void handleSingleCertificate(final String certificateURL) {
				InputStream is = null;
				OutputStream os = null;
				URL url = null;
				try {
					url = new URL(certificateURL);
					String certName = StringUtils.substringAfterLast(url.toString(),"/");
					File cert = new File(certDir,certName);
					if (!cert.exists()) { // missing - download it.
						logger.info("Downloading new certificate " + url);
						os = new FileOutputStream(cert);
						is = url.openStream();
						Piper.pipe(is,os);
					}
				} catch (IOException e) {
					logger.warn("Failed to downlooad certificate " + url);
				} finally {
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
		}).start();
	}

}
