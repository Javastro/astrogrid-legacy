package org.astrogrid.datacenter.integration;

/**
 * Defines standard keys and endpoints for these tests
 *
 *
 */

public interface StdKeys
{
   public final static String PAL_v041_ENDPOINT = "http://localhost:8080/astrogrid-pal-SNAPSHOT/services/AxisDataServer";
   public final static String PAL_v05_ENDPOINT = "http://localhost:8080/astrogrid-pal-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_FITS_ENDPOINT = "http://localhost:8080/astrogrid-pal-fits-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_SEC_ENDPOINT = "http://localhost:8080/astrogrid-pal-sec-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_VIZIER_ENDPOINT = "http://localhost:8080/astrogrid-pal-cds-SNAPSHOT/services/AxisDataService05";

   public final static String PAL_QUERYSTATUS = "http://localhost:8080/astrogrid-pal-SNAPSHOT/queryStatus.jsp?";


//     public final static String PAL_v041_ENDPOINT = "http://grendel12.roe.ac.uk:8080/astrogrid-pal-SNAPSHOT/services/AxisDataServer";
//     public final static String PAL_v05_ENDPOINT = "http://grendel12.roe.ac.uk:8080/astrogrid-pal-SNAPSHOT/services/AxisDataService05";

     public final static String MYSPACE = "myspace:http://localhost:8080/astrogrid-mySpace-SNAPSHOT/services/Manager";
}

