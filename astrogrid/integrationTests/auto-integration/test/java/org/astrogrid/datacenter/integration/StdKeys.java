package org.astrogrid.datacenter.integration;

/**
 * Defines standard keys and endpoints for these tests
 *
 *
 */

import org.astrogrid.config.SimpleConfig;

public interface StdKeys
{
   public final static String PAL_v041_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT/services/AxisDataServer";
   public final static String PAL_v05_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_FITS_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-fits-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_SEC_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-sec-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_VIZIER_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-cds-SNAPSHOT/services/AxisDataService05";

   public final static String PAL_QUERYSTATUS = "http://localhost:"+SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT/queryStatus.jsp?";
   public final static String MYSPACE = "myspace:http://localhost:"+SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-mySpace-SNAPSHOT/services/Manager";
}

