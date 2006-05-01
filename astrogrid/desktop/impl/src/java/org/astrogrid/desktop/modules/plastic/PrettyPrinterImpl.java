package org.astrogrid.desktop.modules.plastic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticListener;
//@todo NWW : remove dependency on browser - let infrastructure take care of displaying result instead.
public class PrettyPrinterImpl implements PrettyPrinterInternal {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(PrettyPrinterImpl.class);

	private BrowserControl browser;

	public PrettyPrinterImpl(BrowserControl browser) {
		this.browser = browser;
	}
	
	/**
	 * Give a List of ApplicationDescription , pretty print the application
	 * details and open the result in a browser. 
	 * @param applications
	 * @throws IOException 
	 */
	public void show(List nonHubApplications) throws IOException {
		File file = File.createTempFile("registeredapps",".html");
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		
		writer.println("<html><head><title>Plastic Registered Applications</title></head>");
		writer.println("<body><h2>Plastic-Registered Applications</h2>");
		writer.println("This ACR\'s hub supports <a href='http://plastic.sourceforge.net'>plastic</a> version "+PlasticListener.CURRENT_VERSION);
		writer.println("<table border='1'>");
		writer.println("<tr><th>Icon</th><th>Name</th><th>Description</th><th>Plastic Id</th><th>IVORN</th><th>Supported Messages</th><th>Plastic Version</th><th>Alive?</th></tr>");

		
		Iterator it = nonHubApplications.iterator();
		while (it.hasNext()) {
			ApplicationDescription app = (ApplicationDescription) it.next();
			String plid = app.getId();
			String name = app.getName();
			String description = app.getDescription();
			List messages = app.getUnderstoodMessages();
			boolean alive = app.isResponding();
			String version = app.getVersion();
			String icon = app.getIconUrl();
			String ivorn = app.getIvorn();
			
			writer.println("<tr>");
				writeImgIfNonNull(writer, icon);
				writeIfNonNull(writer, name);
				writeIfNonNull(writer, description);
				writeIfNonNull(writer, plid);
				writeIfNonNull(writer, ivorn);
				writer.println("<td>");
				writer.write("<ul>");
				Iterator mit = messages.iterator();
				while (mit.hasNext()) {
					URI msg = (URI) mit.next();  //TODO
					writer.write("<li>"+msg+"</li>");
				}
				writer.write("</ul>");

				writer.println("</td>");
				writeIfNonNull(writer, version);
				writer.println("<td>"+(alive ? "yes":"no")+"</td>");

			writer.println("</tr>");
			
		}
		writer.println("</table></body></html>");
		writer.close();
		try {
			browser.openURL(file.toURL());
		} catch (MalformedURLException e) {
			logger.error("This really ought not to happen ",e);
		} catch (ACRException e) {
			logger.error("This really ought not to happen ",e);
		}		
	}

	private void writeIfNonNull(PrintWriter writer, String value) {
		if (value==null ||  "".equals(value)) {
			writer.println("<td/>");
		} else {
			writer.println("<td>"+value+"</td>");
		}
	}

	private void writeImgIfNonNull(PrintWriter writer, String icon) {
		if (icon==null ||  "".equals(icon)) {
			writer.println("<td/>");
		} else {
			writer.println("<td><img src='"+icon+"'/></td>");
		}
	}
}
