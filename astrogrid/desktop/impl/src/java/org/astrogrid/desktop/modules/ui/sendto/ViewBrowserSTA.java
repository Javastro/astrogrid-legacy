package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Pipe;

import org.apache.commons.collections.Transformer;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;


/** consumer that displays result in a browser */
public class ViewBrowserSTA extends AbstractSTA {
	/**
	 * 
	 */
	public ViewBrowserSTA(BrowserControl browser, Transformer trans) {
		super("Browser","View in system web browser","Resource.gif");
		this.browser = browser;
		this.trans = trans;
	}
	private final BrowserControl browser;
	private final Transformer trans;
	
	public void actionPerformed(ActionEvent e) {        
		(new BackgroundWorker(getParent(),"Displaying Result") {
        protected Object construct() throws Exception {
        	/** OTT - does a lot of copying, and only transforms xml. 
        	 * furthermore, have only got a specialized stylesheet for workflow transcript.
        	 * browser will probably do a pretty good job at displaying all other xml anyhow.
        	 * would be better
        	 * if we just pass the url to the browser.
        	 */
//            File f = File.createTempFile("browserView",".html");
//            f.deleteOnExit();
//			Writer out = new FileWriter(f);
//			    Object result;
//			    if (getAtom().isDataFlavorSupported(VoDataFlavour.XML_STRING)) {
//			    	Document source = DomHelper.newDocument((String)getAtom().getTransferData(VoDataFlavour.XML_STRING));
//			    	out.write((String)trans.transform(source));
//			    } else if (getAtom().isDataFlavorSupported(VoDataFlavour.XML)) {
//			    	Document source = DomHelper.newDocument((InputStream)getAtom().getTransferData(VoDataFlavour.XML));
//			    	out.write((String)trans.transform(source));					    	
//			    } else if (getAtom().isDataFlavorSupported(VoDataFlavour.HTML_STRING)) {
//			    	out.write((String)getAtom().getTransferData(VoDataFlavour.HTML_STRING));
//			    } else {
//			    	Reader r = new InputStreamReader((InputStream)getAtom().getTransferData(VoDataFlavour.HTML));
//			    	Piper.pipe(r,out);
//			    	r.close();
//			    }
//			out.close();
//			URL url = f.toURL();
        	URL url = (URL)getAtom().getTransferData(VoDataFlavour.URL);
            browser.openURL(url);
            return null;
        }                    
    }).start();  
	
	}

	protected boolean checkApplicability(PreferredTransferable atom) {
		return
		      atom.isDataFlavorSupported(VoDataFlavour.URL);
	/*	    ||atom.isDataFlavorSupported(VoDataFlavour.XML)
			|| atom.isDataFlavorSupported(VoDataFlavour.XML_STRING)
			|| atom.isDataFlavorSupported(VoDataFlavour.HTML)
			|| atom.isDataFlavorSupported(VoDataFlavour.HTML_STRING);*/
	}
	
}