package org.astrogrid.portal.myspace.generation;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MySpaceHoldingsGenerator extends AbstractGenerator {

  private MySpaceClient delegate;
  private String userId;
  private String communityId;
  private String credential;
  private String query;
  private String testFile;

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);

    Logger logger = this.getLogger();
      
    ActionUtils utils = ActionUtilsFactory.getActionUtils();

    Request request = ObjectModelHelper.getRequest(objectModel);
    
    logger.debug("[setup] params:  " + params);
    logger.debug("[setup] request: " + request);
    
    String endPoint = utils.getAnyParameter("myspace-end-point", "http://localhost:8080/myspace", params, request);
    logger.debug("[setup] endPoint: " + endPoint);

    try {
      delegate = MySpaceDelegateFactory.createDelegate(endPoint);
    
      logger.debug("[setup] myspace-delegate-class: " + delegate.getClass().getName());

      userId = utils.getAnyParameter("username", params, request);
      logger.debug("[setup] userId: " + userId);

      communityId = utils.getAnyParameter("community-id", params, request);
      logger.debug("[setup] communityId: " + communityId);

      credential = utils.getAnyParameter("credential", params, request);
      logger.debug("[setup] credential: " + credential);

      query = utils.getAnyParameter("myspace-query", MySpaceHoldingsGenerator.getDefaultQuery(userId, communityId), params, request);
      logger.debug("[setup] query: " + query);

      testFile = utils.getAnyParameter("test-file", "", params, request);
      logger.debug("[setup] testFile: " + testFile);
    }
    catch(Exception e) {
      delegate = null;
    }
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate()
    throws IOException, SAXException, ProcessingException {
      
    Document sourceDoc = null;
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      if(testFile != null && testFile.length() > 0) {
        sourceDoc = builder.parse(new File(testFile));
      } 
      else if(delegate != null) {
        Vector holdings = null;
        try {
          holdings = delegate.listDataHoldingsGen(userId, communityId, credential, query);
        }
        catch(Throwable t) {
          t.printStackTrace();
        }
        
        if(holdings != null && holdings.size() > 0) {
          StringReader reader = new StringReader((String) holdings.firstElement());
          InputSource inputSource = new InputSource(reader);
          sourceDoc = builder.parse(inputSource);
        }
        else {
          String tmp = "<dataItemRecord>none</dataItemRecord>";
          StringReader reader = new StringReader(tmp);
          InputSource inputSource = new InputSource(reader);
          sourceDoc = builder.parse(inputSource);
        }
      }
      else {
        String tmp = "<dataItemRecord>none</dataItemRecord>";
        StringReader reader = new StringReader(tmp);
        InputSource inputSource = new InputSource(reader);
        sourceDoc = builder.parse(inputSource);
      }
    }
    catch(Exception e) {
      throw new ProcessingException(e);
    }

    DOMStreamer streamer = new DOMStreamer();
    streamer.setNormalizeNamespaces(false);
    streamer.setContentHandler(contentHandler);
    streamer.stream(sourceDoc);
  }
  
  private static String getDefaultQuery(String userId, String communityId) {
    return "/" + userId + "@" + communityId + "/*";
  }
}
