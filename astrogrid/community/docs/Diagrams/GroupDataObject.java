/* Generated by Together */

public class GroupDataObject implements IDbObject {
    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public boolean isMultiType(){ return multiType; }

    public void setMultiType(boolean multiType){ this.multiType = multiType; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }
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

    public GroupDataObject getGroupOwner(){ return groupOwner; }

    public void setGroupOwner(GroupDataObject groupOwner){ this.groupOwner = groupOwner; }

    private String name;
    private boolean multiType;
    private String description;
    private GroupDataObject groupOwner;
}
