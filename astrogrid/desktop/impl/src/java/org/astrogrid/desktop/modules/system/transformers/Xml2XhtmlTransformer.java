/*$Id: Xml2XhtmlTransformer.java,v 1.8 2008/11/04 14:35:49 nw Exp $
 * Created on 11-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.contributions.StylesheetsContribution;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Transforms a result by finding an applying a suitable stylesheet.
 * input is expected to be xml, output is expected to be xhtml
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-May-2005
 */
public class Xml2XhtmlTransformer implements Transformer{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(Xml2XhtmlTransformer.class);

    /** Construct a new Xml2XhtmlTransformer
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * 
     */
    public Xml2XhtmlTransformer(final List sheets) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        super();
        this.sheets = (StylesheetsContribution[])sheets.toArray(new StylesheetsContribution[sheets.size()]);
    }
    private final StylesheetsContribution[] sheets;
    
    
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     * 
     */
    public Object transform(final Object arg0) {
    	if (arg0 == null) {
    		return "<html><body>null</body></html>";
    	}
    	if (! (arg0 instanceof Document)) {
    		throw new IllegalArgumentException("Can only transform XML Documents: " + arg0.getClass().getName());
    	}
    	final Document d = (Document)arg0;
    	for (int i = 0; i < sheets.length ; i++) {
    		if (sheets[i].isApplicable(d)) {    	
    			final Source source = new DOMSource(d);
    			final StringWriter sw = new StringWriter();
    			final Result sink = new StreamResult(sw);
    			try {
    				sheets[i].createTransformer().transform(source,sink);
    				return sw.toString();
    			} catch (final TransformerException e) {
    				logger.error("TransformerException - falling back",e);
    				// will continue iterating - may find a fallback.
    			}
    		}
        }
    	// just return the XML.
    	return DomHelper.DocumentToString(d);

    }

    
}


/* 
$Log: Xml2XhtmlTransformer.java,v $
Revision 1.8  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.7  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.6  2006/06/15 09:58:18  nw
improvements coming from unit testing

Revision 1.5  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.3.34.3  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.3.34.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3.34.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.3  2005/11/11 15:25:49  pjn3
new stylesheet

Revision 1.2.32.1  2005/11/11 15:14:13  pjn3
registry stylesheet

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/05/12 15:59:13  clq2
nww 1111 again

Revision 1.1.2.2  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.1.2.1  2005/05/11 14:25:23  nw
javadoc, improved result transformers for xml
 
*/