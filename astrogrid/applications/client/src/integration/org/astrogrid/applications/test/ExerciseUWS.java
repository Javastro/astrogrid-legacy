/*
 * $Id: ExerciseUWS.java,v 1.2 2009/03/07 08:55:27 pah Exp $
 * 
 * Created on 23 Feb 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import net.ivoa.uws.JobSummary;

import org.astrogrid.uws.client.UWS;
import org.astrogrid.uws.client.UWSClient;
import org.astrogrid.uws.client.UWSException;
import org.astrogrid.uws.client.UWSJobCreationException;

public class ExerciseUWS implements Runnable {

    private URI uwsroot;
    private UWS client;
    private JobSummary job;
    private static String tool;
    
    static {
        StringBuffer toolbuf = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(ExerciseUWS.class.getClass().getResourceAsStream("/org/astrogrid/applications/test/testTool.xml")));
        String line;
        try {
            while ((line= rd.readLine())!=null) {
                toolbuf.append(line);
            }
            tool = toolbuf.toString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    public void run() {
        try {
            long time = System.currentTimeMillis();
            job = client.createJob(tool);
            long inittime = System.currentTimeMillis();
            client.runJob(job.getJobId());
            long runtime = System.currentTimeMillis();
            
            System.out.println("jobid:"+job.getJobId()+
                    ":createtime: "+(inittime-time)+" : create instant: "+ (time) + ":runtime: "+(runtime - inittime));

        } catch (UWSJobCreationException e) {
            e.printStackTrace();
        } catch (UWSException e) {
            e.printStackTrace();
        }
    }
    public ExerciseUWS()  {
        try {
            uwsroot = new URI("http://localhost:5797/astrogrid-cea-cec/uws/jobs");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client = new UWSClient(uwsroot);
 
    }
    
}


/*
 * $Log: ExerciseUWS.java,v $
 * Revision 1.2  2009/03/07 08:55:27  pah
 * stress testing code first checkin funny
 *
 * Revision 1.1  2009/02/26 11:17:41  pah
 * first attempt at a stress test
 *
 */
