/**
 * MySpaceManagerSoapBindingStub.java
 *
 * Fake version used for JUnit testing of the delegate.
 */

package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;

public class MySpaceManagerSoapBindingStub
{
    public Vector getServerURLs() throws java.rmi.RemoteException
    {  Vector vec = new Vector();
       vec.add("http://www.roe.ac.uk/acdwww/index.html");
       vec.add("http://www.roe.ac.uk/acdwww/frequent.html");
       return vec;
    }

    public java.lang.String lookupDataHolderDetails
      (java.lang.String jobDetails) throws java.rmi.RemoteException
    {  return "lookupDataHolderDetails";

    }

    public java.lang.String lookupDataHoldersDetails
      (java.lang.String jobDetails) throws java.rmi.RemoteException
    {  String xmlFragment =
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<results>\n" +
         "    <status>\n" +
         "        <status>{0}</status>\n" +
         "        <details>{1}</details>\n" +
         "        <currentDate>{2}</currentDate>\n" +
         "    </status>\n" +
         "    <dataItemRecords>\n" +
         "      <dataItemRecord>\n" +
         "        <dataItemName>/acd/serv1/conta/myfile</dataItemName>\n" +
         "        <dataItemID>{4}</dataItemID>\n" +
         "        <ownerID>{5}</ownerID>\n" +
         "        <creationDate>{6}</creationDate>\n" +
         "        <expiryDate>{7}</expiryDate>\n" +
         "        <size>{8}</size>\n" +
         "        <type>{9}</type>\n" +
         "        <permissionsMask>{10}</permissionsMask>\n" +
         "        <dataHolderURI>{11}</dataHolderURI>\n" +
         "      </dataItemRecord>\n" +
         "    </dataItemRecords>\n" +
         "</results>\n";

       return xmlFragment;
    }

    public java.lang.String copyDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {
       return "copyDataHolder";
    }

    public java.lang.String exportDataHolder(java.lang.String jobDetails) 
      throws java.rmi.RemoteException
    {  String xmlFragment =
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<results>\n" +
         "    <status>\n" +
         "        <status>{0}</status>\n" +
         "        <details>{1}</details>\n" +
         "        <currentDate>{2}</currentDate>\n" +
         "    </status>\n" +
         "    <dataItemRecords>\n" +
         "      <dataItemRecord>\n" +
         "        <dataItemName>{3}</dataItemName>\n" +
         "        <dataItemID>{4}</dataItemID>\n" +
         "        <ownerID>{5}</ownerID>\n" +
         "        <creationDate>{6}</creationDate>\n" +
         "        <expiryDate>{7}</expiryDate>\n" +
         "        <size>{8}</size>\n" +
         "        <type>{9}</type>\n" +
         "        <permissionsMask>{10}</permissionsMask>\n" +
         "        <dataHolderURI>http://www.roe.ac.uk/acdwww/index.html" +
                     "</dataHolderURI>\n" +
         "      </dataItemRecord>\n" +
         "    </dataItemRecords>\n" +
         "</results>\n";

       return xmlFragment;
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
