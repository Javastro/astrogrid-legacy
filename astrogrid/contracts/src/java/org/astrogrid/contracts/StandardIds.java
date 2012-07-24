package org.astrogrid.contracts;

/**
 * URI values for the standardID attribute.
 * Registrations of IVO service based on VOResource 1.0 and later use
 * capability elements which are distinguished by the values of their
 * standardID attribute. The values are usually IVORNs pointing to
 * the registrations of standards. This class lists the known ones:
 * global standards acknowledged by IVOA and local standards defined by
 * AstroGrid.
 *
 * @author Guy Rixon
 */
public class StandardIds {
  
  /**
   * Cone search: ivo://ivoa.net/std/ConeSearch.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/ConeSearch/v1.0.
   */
  public final static String CONE_SEARCH_1_0 = "ivo://ivoa.net/std/ConeSearch";
  
  /**
   * Simple Image Access Protocol: ivo://ivoa.net/std/SIA.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/SIA/v1.0.
   */
  public final static String SIAP_1_0 = "ivo://ivoa.net/std/SIA";
  
  /**
   * Simple Spectral Access Protocol: ivo://ivoa.net/std/SSA.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/SSA/v0.2
   */
  public final static String SSAP_1_0 = "ivo://ivoa.net/std/SSA";
  
  /**
   * Open Sky Node: ivo://ivoa.net/std/OpenSkyNode.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/SkyNode/v0.2,
   */
  public final static String OPEN_SKY_NODE_0_2 = 
      "ivo://ivoa.net/std/OpenSkyNode";
  
  /**
   * IVOA registry: ivo://ivoa.net/std/Registry.
   * Taken from IVOA's standard document for the registry interface.
   */
  public final static String REGISTRY_1_0 = "ivo://ivoa.net/std/Registry";
  
  /**
   * Common Execution Architecture: ivo://org.astrogrid/std/CEA/v1.0.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * A capability for CEA v1.0 implies the CEC SOAP service.
   */
  public final static String CEA_1_0 = "ivo://org.astrogrid/std/CEA/v1.0";
  
  /**
   * Simple Temporal Access Protocol: ivo://org.astrogrid/std/STAP/v1.0.
   * Chosen by AstroGrid during cycle 1 of AG3.
   */
  public final static String STAP_1_0 = 
      "ivo://org.astrogrid/std/STAP/v1.0";
  
  /**
   * Table Access Protcol with XSAMS VAMDC : ivo://vamdc/std/TAP-XSAMS.
   *
   */
  public final static String TAPXSAMS_1_0 = 
      "ivo://vamdc/std/TAP-XSAMS";
  
  /**
   * Table Access Protcol with XSAMS VAMDC : ivo://vamdc/std/TAP-XSAMS.
   *
   */
  public final static String TAPXSAMS_1_01 = 
      "ivo://vamdc/std/VAMDC-TAP";
  
  
  /**
   * Table Access Protcol with XSAMS VAMDC : ivo://vamdc/std/TAP-XSAMS.
   *
   */
  public final static String XSAMSConsumer_1_0 = 
      "ivo://vamdc/std/XSAMS-consumer";
  
  /**
   * Table Access Protocol: ivo://ivoa.net/std/TAP.
   */
  public final static String TAP_0_1 = 
      "ivo://ivoa.net/std/TAP";
    
  /**
   * PolicyManager: ivo://org.astrogrid/std/Community/v1.0#PolicyManager.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * PolicyManager is the community's SOAP service for reporting home space.
   * This capability is obsolete.
   */
  public final static String POLICY_MANAGER_1_0 = 
      "ivo://org.astrogrid/std/Community/v1.0#PolicyManager";
  
  /**
   * SecurityService: ivo://org.astrogrid/std/Community/v1.0#SecurityService.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * SecurityService is the community's SOAP interface for checking passwords.
   * This capability is obsolete.
   */
  public final static String SECURITY_SERVICE_1_0 =
      "ivo://org.astrogrid/std/Community/v1.0#SecurityService";
  
  /**
   * MyProxy: ivo://org.astrogrid/std/Community/v1.0#MyProxy.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * V2 of the MyProxy protocol is implied.
   * IVOA may later issue a standardID for MyProxy; if so, their URI
   * should be used instead of this one.
   */
  public final static String MY_PROXY_2 = 
      "ivo://org.astrogrid/std/Community/v1.0#MyProxy";
  
  /**
   * Community accounts protocol: ivo://org.astrogrid/std/Community/accounts.
   * Chosen by AstroGrid during cycle 1 of AG3.
   */
  public final static String AG_ACCOUNTS =
      "ivo://org.astrogrid/std/Community/accounts";
  
  /**
   * VOSI service availability: ivo://org.astrogrid/std/VOSI/v0.3#availability.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's implementation of VOSI 0.3.
   */
  public final static String VOSI_AVAILABILITY_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#availability";
  
  /**
   * VOSI service capabilities: ivo://org.astrogrid/std/VOSI/v0.3#capabilities.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's implementation of VOSI 0.3.
   */
  public final static String VOSI_CAPABILITIES_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#capabilities";
  
  /**
   * VOSI service tables: ivo://org.astrogrid/std/VOSI/v0.3#tables.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's extension of VOSI 0.3.
   * The tables capability allows a client to download from a catalogue service
   * a description of the tables in that service.
   */
  public final static String VOSI_TABLES_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#tables";
  
  /**
   * VOSI service application: ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's extension of VOSI 0.3.
   * The application capability allows a client to download a description of
   * the CEA application hosted by the service.
   */
  public final static String VOSI_APPLICATION_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication";
  
}
