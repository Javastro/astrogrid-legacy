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
  public static String CONE_SEARCH_1_0 = "ivo://ivoa.net/std/ConeSearch";
  
  /**
   * Simple Image Access Protocol: ivo://ivoa.net/std/SIA.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/SIA/v1.0.
   */
  public static String SIAP_1_0 = "ivo://ivoa.net/std/SIA";
  
  /**
   * Open Sky Node: ivo://ivoa.net/std/OpenSkyNode.
   * Taken from IVOA's XML schema for http://www.ivoa.net/xml/SkyNode/v0.2,
   */
  public static String OPEN_SKY_NODE_0_2 = "ivo://ivoa.net/std/OpenSkyNode";
  
  /**
   * Common Execution Architecture: ivo://org.astrogrid/std/CEA/v1.0.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * A capability for CEA v1.0 implies the CEC SOAP service.
   */
  public static String CEA_1_0 = "ivo://org.astrogrid/std/CEA/v1.0";
  
  /**
   * PolicyManager: ivo://org.astrogrid/std/Community/v1.0#PolicyManager.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * PolicyManager is the community's SOAP service for reporting home space.
   */
  public static String POLICY_MANAGER_1_0 = 
      "ivo://org.astrogrid/std/Community/v1.0#PolicyManager";
  
  /**
   * SecurityService: ivo://org.astrogrid/std/Community/v1.0#SecurityService.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * SecurityService is the community's SOAP interface for checking passwords.
   */
  public static String SECURITY_SERVICE_1_0 =
      "ivo://org.astrogrid/std/Community/v1.0#SecurityService";
  
  /**
   * MyProxy: ivo://org.astrogrid/std/Community/v1.0#MyProxy.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * V2 of the MyProxy protocol is implied.
   * IVOA may later issue a standardID for MyProxy; if so, their URI
   * should be used instead of this one.
   */
  public static String MY_PROXY_2 = 
      "ivo://org.astrogrid/std/Community/v1.0#MyProxy";
  
  /**
   * VOSI service availability: ivo://org.astrogrid/std/VOSI/v0.3#availability.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's implementation of VOSI 0.3.
   */
  public static String VOSI_AVAILABILITY_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#availability";
  
  /**
   * VOSI service capabilities: ivo://org.astrogrid/std/VOSI/v0.3#capabilities.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's implementation of VOSI 0.3.
   */
  public static String VOSI_CAPABILITIES_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#capabilities";
  
  /**
   * VOSI service tables: ivo://org.astrogrid/std/VOSI/v0.3#tables.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's extension of VOSI 0.3.
   * The tables capability allows a client to download from a catalogue service
   * a description of the tables in that service.
   */
  public static String VOSI_TABLES_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#tables";
  
  /**
   * VOSI service application: ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication.
   * Chosen by AstroGrid during cycle 1 of AG3.
   * This refers to AstroGrid's extension of VOSI 0.3.
   * The application capability allows a client to download a description of
   * the CEA application hosted by the service.
   */
  public static String VOSI_APPLICATION_0_3 =
      "ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication";
  
}
