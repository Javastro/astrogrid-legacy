// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DatabaseQuerier.java

package org.astrogrid.datacenter.queriers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.JobNotifyServiceListener;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

// Referenced classes of package org.astrogrid.datacenter.queriers:
//            Query, QueryStatusForwarder, DatabaseAccessException, QueryListener, 
//            QueryResults, DatabaseQuerierManager

public abstract class DatabaseQuerier
    implements Runnable
{

    public DatabaseQuerier()
    {
        serviceListeners = new Vector();
        status = QueryStatus.UNKNOWN;
        error = null;
        workspace = null;
        handle = null;
        query = null;
        communityid = null;
        credential = "";
        resultsDestination = null;
        resultsLoc = null;
        timeQueryStarted = null;
        timeQueryCompleted = null;
    }

    public String getHandle()
    {
        return handle;
    }

    void setHandle(String handle)
    {
        this.handle = handle;
    }

    void setWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
    }

    void setCommunityId(String communityid)
    {
        this.communityid = communityid;
    }

    void setUserId(String userid)
    {
        this.userid = userid;
    }

    void setCredential(String credential)
    {
        this.credential = credential;
    }

    void setQuery(Element givenDOM)
        throws ADQLException, QueryException
    {
        query = new Query(givenDOM);
        setStatus(QueryStatus.CONSTRUCTED);
    }

    void setResultsDestination(String resultsDestination)
    {
        this.resultsDestination = resultsDestination;
    }

    public void registerWebListeners(Element domContainingListeners)
        throws MalformedURLException
    {
        NodeList listenerTags = domContainingListeners.getElementsByTagName("NotifyMe");
        for(int i = 0; i < listenerTags.getLength(); i++)
        {
            WebNotifyServiceListener listener = new WebNotifyServiceListener(new URL(((Element)listenerTags.item(i)).getNodeValue()));
            registerListener(new QueryStatusForwarder(listener));
        }

        listenerTags = domContainingListeners.getElementsByTagName("JobMonitorURL");
        for(int i = 0; i < listenerTags.getLength(); i++)
        {
            JobNotifyServiceListener listener = new JobNotifyServiceListener(new URL(((Element)listenerTags.item(i)).getNodeValue()));
            registerListener(new QueryStatusForwarder(listener));
        }

    }

    public void run()
    {
        try
        {
        	//commented out by Catherine since we have proved it's working 24th Oct. 2003
            //testResultsDestination();
            QueryResults results = doQuery();
            setStatus(QueryStatus.RUNNING_RESULTS);
            sendResults(results);
            setStatus(QueryStatus.FINISHED);
        }
        catch(QueryException e)
        {
            Log.logError("Could not construct query in spawned thread ", e);
            setErrorStatus(e);
        }
        catch(DatabaseAccessException e)
        {
            Log.logError("Could not access database in spawned thread ", e);
            setErrorStatus(e);
        }
        catch(IOException e)
        {
            Log.logError("Could not create file on myspace", e);
            setErrorStatus(e);
        }
        catch(Exception e)
        {
            Log.logError("Myspace raised an undescriptive exception", e);
            setErrorStatus(e);
        }
    }

    protected void testResultsDestination()
        throws Exception
    {
        Log.affirm(resultsDestination != null, "No Result target (eg myspace) given in config file (key DefaultMySpace), and no key ResultsTarget in given DOM ");
        MySpaceManagerDelegate myspace = new MySpaceManagerDelegate(resultsDestination);
		myspace.saveDataHolding(userid, communityid, credential, "testFile", "This is a test file to make sure we can create a file in myspace, so our query results are not lost", "test", "Overwrite");
    }

    protected void sendResults(QueryResults results)
        throws Exception
    {
        Log.affirm(results != null, "No results to send");
        MySpaceManagerDelegate myspace = new MySpaceManagerDelegate(resultsDestination);
        String myspaceFilename = getHandle() + "_results";
        try
        {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            results.toVotable(ba);
            ba.close();
			myspace.saveDataHolding(userid, communityid, credential, myspaceFilename, ba.toString(), "VOTable", "Overwrite");
            resultsLoc = myspace.getDataHoldingUrl(userid, communityid, credential, myspaceFilename);
        }
        catch(SAXException se)
        {
            Log.logError("Could not create VOTable", se);
        }
    }

    public QueryResults doQuery()
        throws QueryException, DatabaseAccessException
    {
        setStatus(QueryStatus.RUNNING_QUERY);
        timeQueryStarted = new Date();
        QueryResults results = queryDatabase(query);
        timeQueryCompleted = new Date();
        setStatus(QueryStatus.QUERY_COMPLETE);
        return results;
    }

    public String getResultsLoc()
    {
        return resultsLoc;
    }

    public double getQueryTimeTaken()
    {
        if(timeQueryStarted == null)
            return -1D;
        if(timeQueryCompleted == null)
        {
            Date timeNow = new Date();
            return (double)(timeNow.getTime() - timeQueryStarted.getTime());
        } else
        {
            return (double)(timeQueryCompleted.getTime() - timeQueryStarted.getTime());
        }
    }

    public abstract QueryResults queryDatabase(Query query1)
        throws DatabaseAccessException;

    public void abort()
    {
        try
        {
            close();
        }
        catch(IOException e)
        {
            Log.logError("Aborting querier " + this, e);
        }
    }

    public String toString()
    {
        return "[" + getHandle() + "] " + getClass();
    }

    public void close()
        throws IOException
    {
        if(DatabaseQuerierManager.queriers != null && getHandle() != null)
            DatabaseQuerierManager.queriers.remove(getHandle());
        if(workspace != null)
            workspace.close();
    }

    public synchronized void setStatus(QueryStatus newStatus)
    {
        Log.affirm(status != QueryStatus.ERROR, "Trying to start a step '" + newStatus + "' when the status is '" + status + "'");
        Log.affirm(!newStatus.isBefore(status), "Trying to start a step '" + newStatus + "' that has already been completed:" + " status '" + status);
        status = newStatus;
        fireStatusChanged(status);
    }

    protected void setErrorStatus(Throwable th)
    {
        setStatus(QueryStatus.ERROR);
        error = th;
    }

    public Throwable getError()
    {
        Log.affirm(status == QueryStatus.ERROR, "Trying to get exception but there is no error, status='" + status + "'");
        return error;
    }

    public QueryStatus getStatus()
    {
        return status;
    }

    public void registerListener(QueryListener aListener)
    {
        serviceListeners.add(aListener);
    }

    private void fireStatusChanged(QueryStatus newStatus)
    {
        for(int i = 0; i < serviceListeners.size(); i++)
            ((QueryListener)serviceListeners.get(i)).serviceStatusChanged(this);

    }

    private Vector serviceListeners;
    private QueryStatus status;
    private Throwable error;
    protected Workspace workspace;
    private String handle;
    private Query query;
    private String userid;
    private String communityid;
    private String credential;
    private String resultsDestination;
    private String resultsLoc;
    private Date timeQueryStarted;
    private Date timeQueryCompleted;
}
