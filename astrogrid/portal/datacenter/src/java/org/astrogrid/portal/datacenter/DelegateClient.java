package org.astrogrid.portal.datacenter;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.rpc.ServiceException;

import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.Certification;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;

/**
 * 
 */
public class DelegateClient {

  public Element submitADQL(String username, String communityId, String endPoint, String serviceType, String query)
      throws IOException, ServiceException, MarshalException, ValidationException {
    Element votable = null;
    
    try {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier(
          new Certification(username, communityId), endPoint, serviceType);

      DatacenterResults results = querier.doQuery(
          AdqlQuerier.VOTABLE,
          Select.unmarshalSelect(new StringReader(query)));
          
      assert results.isVotable() : "results not votable";
      
      votable = results.getVotable();
    }
    catch(IOException e) {
      throw e;
    }
    catch(ServiceException e) {
      throw e;
    }
    catch(MarshalException e) {
      throw e;
    }
    catch(ValidationException e) {
      throw e;
    }
    
    return votable;
  }
}
