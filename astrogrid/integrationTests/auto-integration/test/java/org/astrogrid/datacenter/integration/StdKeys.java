package org.astrogrid.datacenter.integration;

/**
 * Defines standard keys and endpoints for these tests
 *
 *
 */

import org.astrogrid.config.SimpleConfig;

public interface StdKeys
{
   public final static String PAL_v041_ENDPOINT = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-SNAPSHOT/services/AxisDataServer";
   public final static String PAL_v05_ENDPOINT = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_FITS_ENDPOINT = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-fits-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_SEC_ENDPOINT = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-sec-SNAPSHOT/services/AxisDataService05";
   public final static String PAL_v05_VIZIER_ENDPOINT = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-cds-SNAPSHOT/services/AxisDataService05";

   public final static String PAL_QUERYSTATUS = "http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-pal-SNAPSHOT/queryStatus.jsp?";
   public final static String MYSPACE = "myspace:http://localhost:"+SimpleConfig.getSingleton().getString("TomcatPort")+"/astrogrid-mySpace-SNAPSHOT/services/Manager";
}

