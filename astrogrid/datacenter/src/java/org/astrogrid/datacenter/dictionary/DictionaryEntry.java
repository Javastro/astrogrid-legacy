package org.astrogrid.datacenter.dictionary;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class DictionaryEntry {
  private String ucd;
  private String table;
  private String column;
  
  public DictionaryEntry(String ucd, String table, String column) {
    this.ucd = ucd;
    this.table = table;
    this.column = column;
  }
  
  /**
   * @return
   */
  public String getUcd() {
    return ucd;
  }

  /**
   * @param ucd
   */
  public void setUcd(String ucd) {
    this.ucd = ucd;
  }

  /**
   * @return
   */
  public String getTable() {
    return table;
  }

  /**
   * @param table
   */
  public void setTable(String table) {
    this.table = table;
  }

  /**
   * @return
   */
  public String getColumn() {
    return column;
  }

  /**
   * @param column
   */
  public void setColumn(String column) {
    this.column = column;
  }
  
  public void verify() throws DictionaryEntryException {
    StringBuffer buffer = new StringBuffer();
    
    if(ucd == null || ucd.length() == 0) {
      if(buffer.length() > 0) {
        buffer.append(", ");
      }
      buffer.append("invalid ucd: <" + ucd + ">");
    }
    if(table == null || table.length() == 0) {
      if(buffer.length() > 0) {
        buffer.append(", ");
      }
      buffer.append("invalid table: <" + table + ">");
    }
    if(column == null || column.length() == 0) {
      if(buffer.length() > 0) {
        buffer.append(", ");
      }
      buffer.append("invalid column: <" + column + ">");
    }
    
    if(buffer.length() > 0) {
      throw new DictionaryEntryException(buffer.toString());
    } 
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    
    if(buffer.length() > 0) {
      buffer.append(", ");
    }
    buffer.append("ucd: <" + ucd + ">");

    if(buffer.length() > 0) {
      buffer.append(", ");
    }
    buffer.append("table: <" + table + ">");

    if(buffer.length() > 0) {
      buffer.append(", ");
    }
    buffer.append("column: <" + column + ">");

    return buffer.toString();    
  }
  
  public class DictionaryEntryException extends Exception {
    public DictionaryEntryException(String message) {
      super(message);
    }
    
    public DictionaryEntryException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
