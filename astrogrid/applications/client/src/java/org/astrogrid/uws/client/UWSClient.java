/*
 * $Id: UWSClient.java,v 1.2 2008/09/25 00:16:27 pah Exp $
 * 
 * Created on 21 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.uws.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.ivoa.uws.ExecutionPhase;
import net.ivoa.uws.JobList;
import net.ivoa.uws.JobSummary;
import net.ivoa.uws.ResultList;
import net.ivoa.uws.ShortJobDescription;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * Standard implementation of a generic UWS client. This implementation uses <a
 * href="http://hc.apache.org/httpcomponents-client/">apache httpcomponents</a>.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 23 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class UWSClient implements UWS {
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(UWSClient.class);

    private final URI UWSRoot;
    private final DefaultHttpClient httpClient;

    public UWSClient(URI uwsroot) {
        this.UWSRoot = uwsroot;
        httpClient = new DefaultHttpClient();

    }

    public JobSummary createJob(String jobParams)
            throws UWSJobCreationException {

        try {
            HttpPost post = new HttpPost(UWSRoot);
            HttpEntity ent = new StringEntity(jobParams);
            post.setEntity(ent);
            post.setHeader("Accept", "application/xml");
            httpClient.setRedirectHandler(new DefaultRedirectHandler());
            // IMPL really should have a specialized response handler
            JobSummary retval = httpClient.execute(post, new UWSObjectResponseHandler<JobSummary>(JobSummary.class));
            return retval;
        } catch (Exception e) {
            logger.debug("error creating job", e);
            throw new UWSJobCreationException("error creating job", e);

        }

    }

    public void deleteJob(String jobId) throws UWSException {

        try {
            HttpDelete del = new HttpDelete(makeJobURI(jobId));
            httpClient.execute(del);
        } catch (Exception e) {
            throw new UWSException("problem deleting job=" + jobId, e);
        }

    }

    private URI makeJobURI(String jobId) throws URISyntaxException {
        return makeJobURI(jobId, null);
    }

    public ExecutionPhase getPhase(String jobId) throws UWSException {
        try {
            HttpGet get = new HttpGet(makeJobURI(jobId, "phase"));
            get.setHeader("Accept", "application/xml");

            ExecutionPhase retval = httpClient.execute(get, new UWSObjectResponseHandler<ExecutionPhase>(ExecutionPhase.class));
            return retval;
        } catch (Exception e) {
            throw new UWSException("cannot get phase", e);
        }

    }

    private URI makeJobURI(String jobId, String string)
            throws URISyntaxException {

        StringBuffer jobStr = new StringBuffer(UWSRoot.toString());
        jobStr.append("/");
        jobStr.append(jobId);
        if (string != null) {
            jobStr.append("/");
            jobStr.append(string);
        }
        URI jobURI = new URI(jobStr.toString());
        return jobURI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.uws.client.UWS#getResults(java.lang.String)
     */
    public ResultList getResults(String jobId) throws UWSException {
        // TODO want to have more sophisticated dealing with results - wait for
        // them to be ready
        try {
            HttpGet get = new HttpGet(makeJobURI(jobId, "results"));
            get.setHeader("Accept", "application/xml");
            ResultList retval = httpClient.execute(get, new UWSObjectResponseHandler<ResultList>(ResultList.class));
            return retval;
        } catch (Exception e) {
            throw new UWSException("cannot get resultList", e);
        }

    }

    public JobSummary jobDetail(String jobId) throws UWSException {

        try {
            HttpGet get = new HttpGet(makeJobURI(jobId));
            get.setHeader("Accept", "application/xml");
            JobSummary retval = httpClient.execute(get, new UWSObjectResponseHandler<JobSummary>(JobSummary.class));
            return retval;
        } catch (Exception e) {
            throw new UWSException("cannot get job detail", e);
        }

    }

    public List<String> listJobs() throws UWSException {
        List<String> retval = new ArrayList<String>();
        try {
            HttpGet get = new HttpGet(UWSRoot);
            get.setHeader("Accept", "application/xml");
            JobList joblist = httpClient.execute(get, new UWSObjectResponseHandler<JobList>(JobList.class));
            for (ShortJobDescription jobdesc : joblist.getJobref()) {
                retval.add(jobdesc.getId());
            }
        } catch (Exception e) {
            throw new UWSException("cannot get job detail", e);
        }

        return retval;
    }

    public DateTime setDestruction(String jobId, DateTime destructionTime)
            throws UWSException {
        try {
            JobSummary job = postObject(jobId, "destruction", destructionTime
                    .toString());
            return job.getDestruction();
        } catch (Exception e) {
            throw new UWSException("cannot set destruction time", e);
        }
    }

    public ExecutionPhase setPhase(String jobId, String newPhase)
            throws UWSException {
        try {
            JobSummary retval = postObject(jobId, "phase", newPhase);
            return retval.getPhase();
        } catch (Exception e) {
            throw new UWSException("cannot set phase", e);
        }

    }

    private JobSummary postObject(String jobId, String uwsobject,
            String newPhase) throws URISyntaxException,
            ClientProtocolException, IOException {
        HttpPost post = new HttpPost(makeJobURI(jobId, uwsobject));
        List<BasicNameValuePair> nvp = new ArrayList<BasicNameValuePair>();
        nvp.add(new BasicNameValuePair(uwsobject, newPhase));
        HttpEntity ent = new UrlEncodedFormEntity(nvp);
        post.setEntity(ent);
        post.setHeader("Accept", "application/xml");
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
        JobSummary retval = httpClient.execute(post,
                new UWSObjectResponseHandler<JobSummary>(JobSummary.class));
        return retval;
    }

    public Period setExecutionDuration(String jobId, Period time) throws UWSException {
        try {
            JobSummary job = postObject(jobId, "executionduration", Integer.toString(time.toStandardSeconds().getSeconds()));
            return new Period(0,0,job.getExecutionDuration(),0);
        } catch (Exception e) {
            throw new UWSException("cannot set termination time", e);
        }
 
    }

    public void abortJob(String jobId) throws UWSException {
        setPhase(jobId, "ABORT");
    }

    public ExecutionPhase runJob(String jobId) throws UWSException {
        return setPhase(jobId, "RUN");
    }

}

/*
 * $Log: UWSClient.java,v $
 * Revision 1.2  2008/09/25 00:16:27  pah
 * change termination time to execution duration
 *
 * Revision 1.1  2008/09/24 13:47:18  pah
 * added generic UWS client code
 *
 */
