package org.astrogrid.datacenter.integration;

/**
 * Defines standard keys and endpoints for these tests
 *
 *
 */

import org.astrogrid.config.SimpleConfig;

public interface StdKeys
{
   //note that these constants are *not* static.  By leaving them as class
   //variables, they only get set when a class is instantiated, and that allows
   //us to sneakily set config settings before running the tests
   
   public final String PAL_v05_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT/services/AxisDataService05";
   public final String PAL_v05_FITS_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-fits-SNAPSHOT/services/AxisDataService05";
   public final String PAL_v05_SEC_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-sec-SNAPSHOT/services/AxisDataService05";
   public final String PAL_v05_VIZIER_ENDPOINT = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-cds-SNAPSHOT/services/AxisDataService05";

   public final String PAL_QUERYSTATUS = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT/queryStatus.jsp?";
   public final String MYSPACE = "myspace:"+SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-mySpace-SNAPSHOT/services/Manager";

   public final String PAL_STD_STEM = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-SNAPSHOT";
   public final String PAL_FITS_STEM = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-fits-SNAPSHOT";
   public final String PAL_SEC_STEM = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-sec-SNAPSHOT";
   public final String PAL_VIZIER_STEM = SimpleConfig.getSingleton().getString("tomcat.root")+"/astrogrid-pal-cds-SNAPSHOT";
}


