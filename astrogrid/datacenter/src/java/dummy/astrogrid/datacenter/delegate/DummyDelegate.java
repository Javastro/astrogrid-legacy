package dummy.astrogrid.datacenter.delegate;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtils;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class DummyDelegate extends DatacenterDelegate {

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#setTimeout(int)
   */
  public void setTimeout(int givenTimeout) {
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#query(org.w3c.dom.Element)
   */
  public Element query(Element adql) throws IOException {
    Element result = null;
    
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    try {
      result = utils.getDomElement("<rod><jane><freddy/></jane></rod>");
    }
    catch(ParserConfigurationException e) {
    }
    catch(SAXException e) {
    }

    return result;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#adqlCountDatacenter(org.w3c.dom.Element)
   */
  public int adqlCountDatacenter(Element adql) throws IOException {
    return 0;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#makeQuery(org.w3c.dom.Element)
   */
  public Element makeQuery(Element adql) throws IOException {
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#startQuery(java.lang.String)
   */
  public Element startQuery(String queryId) throws IOException {
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getResults(java.lang.String)
   */
  public Element getResults(String queryId) throws IOException {
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getRegistryMetadata()
   */
  public Element getRegistryMetadata() throws IOException {
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getQueryStatus(java.lang.String)
   */
  public QueryStatus getQueryStatus(String queryId) {
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#registerListener(java.lang.String, org.astrogrid.datacenter.delegate.DatacenterStatusListener)
   */
  public void registerListener(String queryId, DatacenterStatusListener listener) {
  }

}
