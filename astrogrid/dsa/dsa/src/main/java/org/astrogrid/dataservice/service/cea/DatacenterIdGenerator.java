package org.astrogrid.dataservice.service.cea;

import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.dataservice.queriers.Querier;

/**
 * A generator of job identifiers. This one supplies identifiers in the
 * synatx used for TAP jobs rather than the default syntax of the CEA library.
 * The latter syntax includes slashes and therefore clashes with the RESTful
 * arrangements for viewing the jobs.
 *
 * @author Guy Rixon
 */
public class DatacenterIdGenerator implements IdGen {

  /**
   * Supplies an identifier.
   *
   * @return The identifier.
   */
  public String getNewID() {
    return Querier.generateQueryId();
  }

}
