/* Generated by Together */

public class PermissionDataObject implements IDbObject {
    public ResourceDataObject getResource(){ return resource; }

    public void setResource(ResourceDataObject resource){ this.resource = resource; }

    public GroupDataObject getGroup(){ return group; }

    public void setGroup(GroupDataObject group){ this.group = group; }

    public String getAction(){ return action; }

    public void setAction(String action){ this.action = action; }

      public boolean insert(Connection conn) {

        return true;
    }

    public boolean remove(Connection conn) {

        return true;
    }

    public boolean update(Connection conn) {

        return true;
    }

    public IDbObject getDataObject(Connection conn) {

    }

    public ArrayList getDataObjects(Connection conn) {

    }



    private ResourceDataObject resource;
    private GroupDataObject group;
    private String action;
}
