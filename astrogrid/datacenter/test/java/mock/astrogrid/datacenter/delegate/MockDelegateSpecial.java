package mock.astrogrid.datacenter.delegate;

import java.io.IOException;

import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MockDelegateSpecial extends DatacenterDelegate {

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#setTimeout(int)
   */
  public void setTimeout(int givenTimeout) {
    // TODO Auto-generated method stub

  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#query(org.w3c.dom.Element)
   */
  public Element query(Element adql) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#adqlCountDatacenter(org.w3c.dom.Element)
   */
  public int adqlCountDatacenter(Element adql) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#makeQuery(org.w3c.dom.Element)
   */
  public Element makeQuery(Element adql) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#startQuery(java.lang.String)
   */
  public Element startQuery(String queryId) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getResults(java.lang.String)
   */
  public Element getResults(String queryId) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getRegistryMetadata()
   */
  public Element getRegistryMetadata() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#getQueryStatus(java.lang.String)
   */
  public QueryStatus getQueryStatus(String queryId) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * @see org.astrogrid.datacenter.delegate.DatacenterDelegate#registerListener(java.lang.String, org.astrogrid.datacenter.delegate.DatacenterStatusListener)
   */
  public void registerListener(String queryId, DatacenterStatusListener listener) {
    // TODO Auto-generated method stub

  }

}
