/**
 * MySpaceManagerSoapBindingStub.java
 *
 * Fake version used for JUnit testing of the delegate.
 */

package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;

public class MySpaceManagerSoapBindingStub
{
    public Vector allMssUrls() throws java.rmi.RemoteException
    {  Vector vec = new Vector();
       vec.add("http://www.roe.ac.uk/acdwww/index.html");
       vec.add("http://www.roe.ac.uk/acdwww/frequent.html");
       return vec;
    }

    public java.lang.String lookupDataHolderDetails
      (java.lang.String jobDetails) throws java.rmi.RemoteException
    {
       return "lookupDataHolderDetails";
    }

    public java.lang.String lookupDataHoldersDetails
      (java.lang.String jobDetails) throws java.rmi.RemoteException
    {
       return "lookupDataHoldersDetails";
    }

    public java.lang.String copyDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "copyDataHolder";
    }

    public java.lang.String exportDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "http://www.roe.ac.uk/acdwww/index.html";
    }

    public java.lang.String moveDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "moveDataHolder";
    }

    public java.lang.String deleteDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "deleteDataHolder"; 
    }

    public java.lang.String extendLease(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "extendLease";
    }

    public java.lang.String publish(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "publish";
    }

    public java.lang.String createContainer(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "createContainer";
    }

    public java.lang.String upLoad(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "upLoad";
    }

    public boolean createUser(String useId, String communityId,
      Vector servers) throws java.rmi.RemoteException
    {
       return true;
    }

    public boolean deleteUser(String useId, String communityId) 
      throws java.rmi.RemoteException
    {
       return true;
    }

    public java.lang.String changeOwner(String userId,
      String communityId, String dataHolderName, String newOwnerId)
      throws java.rmi.RemoteException
    {
       return "changeOwner";
    }

    public java.lang.String upLoadURL(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "upLoadURL";
    }
}
