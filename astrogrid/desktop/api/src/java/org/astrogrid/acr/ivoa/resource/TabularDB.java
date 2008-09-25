/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.astrogrid.DatabaseBean;

/**  A type of IVOA resource describing (exactly) one database independently of
        any service giving access to the database. The mandatory core of the element 
        details the database itself. There may, optionally, be additional elements 
        describing the provenance and astronomical usage of the resource.

* @exclude 
* @deprecated I suspect this type is going to be removed. Prefer DataCollection instead.
 * @author Noel.Winstanley@manchester.ac.uk
 */
@Deprecated
public interface TabularDB extends DataCollection {
	/** access a description of the database owned by this resource */
  public DatabaseBean getDatabase();
}
