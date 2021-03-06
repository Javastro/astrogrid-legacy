/**
 * CEA Provider for Datacenters.
 * <p>
 * Main class : {@link org.astrogrid.datacenter.service.v06.DatacenterCEAComponentManager}
 * defines a component manager configured to provide query access to a datacenter.
 * It provides a description library containing a single application description
 * - of type {@link org.astrogrid.datacenter.service.v06.DatacenterApplicationDescription}
 * - which provides interfaces to perform a query via cone search, or via ADQL.
 * <p>
 * Invocations of this application description are represented by
 * {@link org.astrogrid.datacenter.service.v06.DatacenterApplication} objects.
 * This class maps between the native asynchronous query interface provided by
 * {@link org.astrogrid.datacenter.service.DataServer} and the protocol expected
 * for a CEA provider.
 * <p>
 * {@link org.astrogrid.datacenter.service.cea.CecServiceBinding} is an
 * extension of the normal SOAP binding in the CEA library. The extension is
 * to set handle properly the credentials such that credentials are not
 * confused between jobs. In execute(), the credentials are cached
 * in the {@link org.astrogrid.dataservice.service.cea.DatacenterApplication}
 * class (the latter has a static map of SecurityGuard instances keyed by
 * job-ID strings). The application instance uses the credentials to secure
 * its connections to VOSpace and deletes them from the map at the end of
 * its run() method.
 */
package org.astrogrid.dataservice.service.cea;