/*
 * Created on 18-Jul-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry;

import junit.framework.TestCase;

public class RegistryInterfaceTest3_0 extends TestCase {
	public void testSubmitQuery() throws ClassNotFoundException{
  	  RegistryInterface3_0 ri3 = new RegistryInterface3_0();
  	
	  /** Test multiple nested selectionSequences:*/
	  String queryNests = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selectionSequence><selection item='ticker' itemOp='EQ' value='SURF'/><selectionOp op='OR'/><selection item='name' itemOp='EQ' value='Carl Foley'/></selectionSequence></selectionSequence></query>";
	  String queryResponseNests = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='Solar UK Research Facility'/><recordKeyPair item='ticker' value='SURF'/><recordKeyPair item='identifier' value='http://surfwww.mssl.ucl.ac.uk/surf/data_request.html'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseNests, ri3.submitQuery(queryNests));	

	  /** Test itemOps: EQ, NE, GT, LT, GE, LE: */
	  /** Test for "NE" also includes test for "all" */
	
	  String queryEQ = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='ticker' itemOp='EQ' value='SURF'/></selectionSequence></query>";
	  String queryResponseEQ = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='Solar UK Research Facility'/><recordKeyPair item='ticker' value='SURF'/><recordKeyPair item='identifier' value='http://surfwww.mssl.ucl.ac.uk/surf/data_request.html'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseEQ, ri3.submitQuery(queryEQ));

	  String queryNE = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='rights' itemOp='NE' value='mixed'/><selectionOp op='AND'/><selection item='rights' itemOp='NE' value='public'/></selectionSequence></query>";
	  String queryResponseNE = "<queryResponse><recordKeyPair item='ERROR:' value='No matching registry entries found.'/></queryResponse>";
	  assertEquals(queryResponseNE, ri3.submitQuery(queryNE));
	  
	  String queryGT = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='resolutionSpatial' itemOp='GT' value='0.49'/></selectionSequence></query>";
	  String queryResponseGT = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='Solar UK Research Facility'/><recordKeyPair item='ticker' value='SURF'/><recordKeyPair item='identifier' value='http://surfwww.mssl.ucl.ac.uk/surf/data_request.html'/></responseRecord><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='The First XMM-Newton Serendipitous Source Catalogue: 1XMM'/><recordKeyPair item='ticker' value='1XMM'/><recordKeyPair item='identifier' value='http://xmmssc-www.star.le.ac.uk/newpages/xcat_public.html'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseGT, ri3.submitQuery(queryGT));

	  String queryLT = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='resolutionSpatial' itemOp='LT' value='0.13'/></selectionSequence></query>";
	  String queryResponseLT = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='The USNO-B Catalog'/><recordKeyPair item='ticker' value='USNO-B'/><recordKeyPair item='identifier' value='http://ledas-svc.star.le.ac.uk/blasta/usnobhelp.php'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseLT, ri3.submitQuery(queryLT));
	  	
	  String queryGE = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='resolutionSpatial' itemOp='GE' value='0.5'/></selectionSequence></query>";
	  String queryResponseGE = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='Solar UK Research Facility'/><recordKeyPair item='ticker' value='SURF'/><recordKeyPair item='identifier' value='http://surfwww.mssl.ucl.ac.uk/surf/data_request.html'/></responseRecord><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='The First XMM-Newton Serendipitous Source Catalogue: 1XMM'/><recordKeyPair item='ticker' value='1XMM'/><recordKeyPair item='identifier' value='http://xmmssc-www.star.le.ac.uk/newpages/xcat_public.html'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseGE, ri3.submitQuery(queryGE));
	  
	  String queryLE = "<query><selectionSequence><selection item='searchElements' itemOp='EQ' value='identity'/><selectionOp op='AND'/><selection item='resolutionSpatial' itemOp='LE' value='0.12'/></selectionSequence></query>";
	  String queryResponseLE = "<queryResponse><responseRecord><recordKeyPair item='metadataType' value='identity'/><recordKeyPair item='title' value='The USNO-B Catalog'/><recordKeyPair item='ticker' value='USNO-B'/><recordKeyPair item='identifier' value='http://ledas-svc.star.le.ac.uk/blasta/usnobhelp.php'/></responseRecord></queryResponse>";
	  assertEquals(queryResponseLE, ri3.submitQuery(queryLE));
	  
	  /** Test for empty query response: */
	
	  String queryEmpty = "<query><selectionSequence><selection item='id' itemOp='EQ' value='FOO'/></selectionSequence></query>";
	  String queryResponseEmpty = "<queryResponse><recordKeyPair item='ERROR:' value='No matching registry entries found.'/></queryResponse>";
	  assertEquals(queryResponseEmpty, ri3.submitQuery(queryEmpty));
	
	  /** Test for XML parsing exception (a ">" is left out of <selectionSequence*/
	
	  String queryPE = "<query><selectionSequence<selection item='id' itemOp='EQ' value='FOO'/></selectionSequence></query>";	
	  String queryResponsePE = "<queryResponse><recordKeyPair item='ERROR:' value='QUERY - org.xml.sax.SAXParseException: Whitespace required before attributes.'></queryResponse>";
	  assertEquals(queryResponsePE, ri3.submitQuery(queryPE));


	}

}
