/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Generate Hivedoc documentation.
 * 
 * <p/>
 * A specialized subclass of Launcher - just generates the documentation, then exits.
 * 
 * @author Noel Winstanley
 * @since Jan 2, 20076:24:46 PM
 * 
 */
public class GenerateHivedoc extends Launcher {
	

    public void run() { 
    	spliceInDefaults();
    	// build registry
		final RegistrySerializer serializer = new RegistrySerializer(); 
		serializer.addModuleDescriptorProvider(createModuleDescriptorProvider());
		final Document result = serializer.createRegistryDocument();
		final File targetDir = new File("hivedoc");
		try {
		final File tmpResult = File.createTempFile("hivedoc",".xml");
		// seems to be a problem in the sun impl of DOMSource - so serialize to a file, and read back in.
		final OutputStream fos = FileUtils.openOutputStream(tmpResult);
		DomHelper.DocumentToStream(result,fos);
		fos.close();
		// create hivedoc.
		targetDir.mkdir();
		//Source source = new DOMSource(result);
		final Source source = new StreamSource(FileUtils.openInputStream(tmpResult));
		final OutputStream sw = FileUtils.openOutputStream(new File(targetDir,"index.html"));
		final Result sink = new StreamResult(sw);
		final InputStream styleStream = GenerateHivedoc.class.getResourceAsStream("hivedoc/hivemind.xsl");
		
		final TransformerFactory fac = TransformerFactory.newInstance();
			final Templates template = fac.newTemplates(new StreamSource(styleStream));
			final Transformer trans = template.newTransformer();
			trans.setParameter("base.dir",targetDir.getAbsolutePath());
			trans.transform(source,sink);
		} catch (final Exception x) {
			x.printStackTrace();
		}
		// copy resources to target directory.
		try {
		FileUtils.copyURLToFile(GenerateHivedoc.class.getResource("hivedoc/hivemind.css")
				,new File(targetDir,"hivemind.css"));
		    FileUtils.copyURLToFile(GenerateHivedoc.class.getResource("hivedoc/public.png")
				,new File(targetDir,"public.png"));
		    FileUtils.copyURLToFile(GenerateHivedoc.class.getResource("hivedoc/private.png")
				,new File(targetDir,"private.png"));		
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
