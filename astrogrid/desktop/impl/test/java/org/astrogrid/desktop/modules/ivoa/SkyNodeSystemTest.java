/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.FunctionBean;
import org.astrogrid.acr.ivoa.SkyNode;
import org.astrogrid.acr.ivoa.SkyNodeTableBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.adql.v1_0.beans.AllSelectionItemType;
import org.astrogrid.adql.v1_0.beans.FromTableType;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.SelectionItemType;
import org.astrogrid.adql.v1_0.beans.TableType;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Jun 13, 20062:25:04 PM
 */
public class SkyNodeSystemTest extends InARTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ACR reg = getACR();
		assertNotNull(reg);
		skynode = (SkyNode)reg.getService(SkyNode.class);
		assertNotNull(skynode);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		skynode  = null;
	}
	
	protected SkyNode skynode;
  
	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SkyNodeSystemTest.class));
	    }    


	//@todo transcribe this into tests.
	 public static void main(String[] args) {
	        try {
	        SkyNode s = new SkyNodeImpl();

	        // ESA - both esa services returns result in the WS_namespace,, and expects adql 0.74        
	        // they have a java axis implementation.
	        // * works, with translation and explicit format
	        //URI endpoint = new URI("http://esavo.esa.int/ISOSkyNode/services/SkyNodeSoap");
	        
	        // JHU - similar. .NET impelemtation, 0.74
	        // * works, with query namespace translation, and explicit format declaration.
	        //URI endpoint = new URI("http://openskyquery.net/nodes/usnob/nodeb.asmx");
	        
	        //JVO - different. axis implementation. wsdl states adql 1.0, different namespace for half of it.
	        //obviously expects adql 1.0. but still barfs
	        //URI endpoint = new URI("http://jvo.nao.ac.jp/skynode/services/SkyNodeForQSO");        
	        // doesn't like it. - fails with server-side exception
	        //URI endpoint = new URI("http://jvo.nao.ac.jp/skynode/services/SkyNodeForTWOMASS");
	        // * works - adql 1.0
	        //URI endpoint = new URI("http://jvo.nao.ac.jp/skynode/services/SkyNodeForSubaru");
	        // and there's some OpenSkyNodeJ variants - whatever they are..
	        // too big??
	        //URI endpoint = new URI("http://jvo.nao.ac.jp/skynode/services/SkyNodeForSDSSCatalog");
	        // seems to be down?
	        //URI endpoint = new URI("http://jvoe.dc.nao.ac.jp:8080/skynode/services/SkyNodeSoap");
	                
	        //STSci - .NET, adql 0.74. doesn't support gete availability.
	        URI endpoint = new URI("http://galex.stsci.edu/skynode/ogalex/nodeb.asmx");
	       // need to find how to translate my adql1.0 to 0.74
	        String[] results = s.getFormats(endpoint);
	        System.out.println(Arrays.asList(results));
	        FunctionBean[] functions = s.getFunctions(endpoint);
	        System.out.println(Arrays.asList(functions));
	        //System.out.println(s.getAvailability(endpoint));
	        /*
	        SkyNodeTableBean[] tables = null; // @todo  s.getMetadata(endpoint);
	        System.out.println(tables[0]);
	        System.err.println("first table is named " + tables[0].getName());
	        */
	        SelectDocument query = SelectDocument.Factory.newInstance();
	        SelectType sel = query.addNewSelect();
	        sel.addNewSelectionList()
	            .setItemArray(new SelectionItemType[]{
	                    AllSelectionItemType.Factory.newInstance()});
	        sel.addNewRestrict().xsetTop(XmlUnsignedInt.Factory.newValue(new Integer(100)));
	        TableType t = TableType.Factory.newInstance();
	        t.setAlias("a");
	        //t.setName(tables[0].getName());        
	        sel.addNewFrom().setTableArray(new FromTableType[]{t   });
	        if (! query.validate()) {
	            throw new RuntimeException("built query is not valid");
	        }
	        
	        // convert to old version of adql..
	        String a10 = query.xmlText();
	        String a74 = StringUtils.replace(a10,AdqlData.NAMESPACE_1_0,AdqlData.NAMESPACE_0_74);
	        
	      Document votable = s.getResults(endpoint,(Document)query.getDomNode());
	     //  Document doc = XMLUtils.newDocument(new InputSource(new StringReader(a74)));
	       // Document votable = s.getResults(endpoint,doc);
	        XMLUtils.PrettyDocumentToStream(votable,System.out);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	

	public void testGetAdqlRegistryQueryNewReg() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
		String q = skynode.getRegistryAdqlQuery();
		assertNotNull(q);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.adqlsSearch(q);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSiapResource(arr[i]);
		}
	}
	
	public void testGetXQueryRegistryQuery() throws Exception {
		String xq = skynode.getRegistryXQuery();
		assertNotNull(xq);
		org.astrogrid.acr.ivoa.Registry reg = (org.astrogrid.acr.ivoa.Registry)getACR().getService(org.astrogrid.acr.ivoa.Registry.class);
		Resource[] arr = reg.xquerySearch(xq);
		assertNotNull(arr);
		assertTrue(arr.length > 0);
		// just services for now..
		for (int i = 0; i < arr.length; i++) {
			checkSiapResource(arr[i]);
		}		
		
	}

	private void checkSiapResource(Resource r) {
		//@todo refine this later..
		assertTrue(r instanceof Service);
	}	
}
