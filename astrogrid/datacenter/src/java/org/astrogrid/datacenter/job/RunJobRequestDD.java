/*
 * @(#)RunJobRequestDD.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.job;

/** data descriptor class
 * defines constants for the elements and attributes of the JobXML schema
 */
import org.astrogrid.datacenter.query.AdqlTags;

public interface RunJobRequestDD extends AdqlTags {

   public static final String
       JOB_ELEMENT = "job",
       JOB_NAME_ATTR = "name",
       JOB_URN_ATTR = "jobURN",
       JOB_MONITOR_URL_ATTR = "jobMonitorURL";

   public static final String
       USERID_ELEMENT = "userid",
       COMMUNITY_ELEMENT = "community";

   public static final String
       JOBSTEP_ELEMENT = "jobstep",
       JOBSTEP_NAME_ATTR = "name",
       JOBSTEP_STEPNUMBER_ATTR = "stepNumber";


}
