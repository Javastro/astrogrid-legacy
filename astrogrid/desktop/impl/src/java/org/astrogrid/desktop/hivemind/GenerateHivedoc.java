/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.io.Piper;
import org.w3c.dom.Document;

/** Specialized subclass of launcher that just generates Hivemind documentation.
 * 
 * @author Noel Winstanley
 * @since Jan 2, 20076:24:46 PM
 */
public class GenerateHivedoc extends Launcher {
	

    public void run() { 
    	spliceInDefaults();
    	// build registry
		RegistrySerializer serializer = new RegistrySerializer(); 
		serializer.addModuleDescriptorProvider(createModuleDescriptorProvider());
		Document result = serializer.createRegistryDocument();
		

		// create hivedoc.
		File targetDir = new File("hivedoc");
		targetDir.mkdir();
		try {
		Source source = new DOMSource(result);
		OutputStream sw = new FileOutputStream(new File(targetDir,"index.html"));
		Result sink = new StreamResult(sw);
		InputStream styleStream = GenerateHivedoc.class.getResourceAsStream("hivedoc/hivemind.xsl");
		
		TransformerFactory fac = TransformerFactory.newInstance();
			Templates template = fac.newTemplates(new StreamSource(styleStream));
			final Transformer trans = template.newTransformer();
			trans.setParameter("base.dir",targetDir.getAbsolutePath());
			trans.transform(source,sink);
		} catch (Exception x) {
			x.printStackTrace();
		}
		// copy resources to target directory.
		try {
		Piper.pipe(GenerateHivedoc.class.getResourceAsStream("hivedoc/hivemind.css")
				,new FileOutputStream(new File(targetDir,"hivemind.css")));
		Piper.pipe(GenerateHivedoc.class.getResourceAsStream("hivedoc/public.png")
				,new FileOutputStream(new File(targetDir,"public.png")));
		Piper.pipe(GenerateHivedoc.class.getResourceAsStream("hivedoc/private.png")
				,new FileOutputStream(new File(targetDir,"private.png")));		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
