package dummy.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MySpaceManagerDelegate extends org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate {
  public MySpaceManagerDelegate(String targetEndPoint) {
    super(targetEndPoint);
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#changeOwner(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String changeOwner(String userid, String communityid, String dataHolderName, String newOwnerID) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#copyDataHolding(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String copyDataHolding(String userid, String communityid, String serverFileName, String newDataItemName) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#createContainer(java.lang.String, java.lang.String, java.lang.String)
   */
  public String createContainer(String userid, String communityid, String newContainerName) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#createUser(java.lang.String, java.util.Vector)
   */
  public String createUser(String userID, Vector servers) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#deleteDataHolding(java.lang.String, java.lang.String, java.lang.String)
   */
  public String deleteDataHolding(String userid, String communityid, String serverFileName) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#deleteUser(java.lang.String)
   */
  public String deleteUser(String userID) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#extendLease(java.lang.String, java.lang.String, java.lang.String, int)
   */
  public String extendLease(String userid, String communityid, String serverFileName, int extentionPeriod) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#getDataHolding(java.lang.String, java.lang.String, java.lang.String)
   */
  public String getDataHolding(String userid, String communityid, String fullFileName) throws Exception {
    return
        "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
        "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
        " <Selection>\n" +
        "    <AllSelectionItem />\n" +
        "  </Selection>\n" +
        "  <TableClause>\n" +
        "    <FromClause>\n" +
        "      <TableReference>\n" +
        "        <Table>\n" +
        "          <Name>MySpaceDummy</Name>\n" +
        "          <AliasName>m</AliasName>\n" +
        "        </Table>\n" +
        "      </TableReference>\n" +
        "    </FromClause>\n" +
        "  </TableClause>\n" +
        "</Select>";
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#listDataHolding(java.lang.String, java.lang.String, java.lang.String)
   */
  public String listDataHolding(String userid, String communityid, String serverFileName) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#listDataHoldings(java.lang.String, java.lang.String, java.lang.String)
   */
  public Vector listDataHoldings(String userid, String communityid, String criteria) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#publish(java.lang.String)
   */
  public String publish(String jobDetails) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#renameDataHolding(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public String renameDataHolding(String userid, String communityid, String serverFileName, String newDataItemName) throws Exception {
    return null;
  }

  /**
   * @see org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate#saveDataHolding(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
   */
  public boolean saveDataHolding(String userid, String communityid, String fileName, String fileContent, String category, String action) throws Exception {
    return true;
  }

}
