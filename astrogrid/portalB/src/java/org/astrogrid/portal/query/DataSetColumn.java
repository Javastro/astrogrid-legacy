package org.astrogrid.portal.query;

/**
 * Class Name: DataSetColumn
 * Purpose:  First let me state this name should reall change and probably will very soon.  Reason why is it is to hold
 * the name and type for a Column, UCD, Function and could probably handle other things as well.  It is used by the 
 * DataSetInformation object and the CriteriaInformation object.  DataSetInformation object uses it for holding Return
 * Columns/UCD's and such. CriteriaInformation uses it for holding Columns/UCD's and Function names.  So you can see why it needs
 * probably a different name since it can hold a lot more than just columns.
 *
 * @author Kevin Benson
 *
 */
public class DataSetColumn {
	private String name = null;
	private String type = null;

	public final String OTHER_TYPE = "OTHER";
	public final String COLUMN_TYPE = "COLUMN";
	public final String UCD_TYPE = "UCD";
	public final String FUNCTION_TYPE = "FUNCTION";

	public DataSetColumn(String name,String type) {
		this.name = name;
		this.type = type;
		if(this.type == null){this.type="";}
	}

	/**
	 * Property type method for setting the name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Property type mehtod for getting the name
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Property type method for setting the type.
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Property type method for getting the type.
	 * @return
	 */
	public String getType() {
		return this.type;
	}


	/**
	 * A equals method that is used for the indexOf on ArrayList to see if this object matches another
	 * DataSetColumn object.
	 */
	public boolean equals(Object dsColumnObj) {
		DataSetColumn dsColumn = (DataSetColumn)dsColumnObj;
		if( (this.name != null && this.name.equals(dsColumn.getName()))  ) {
			return true;
		}
		return false;
	}

}