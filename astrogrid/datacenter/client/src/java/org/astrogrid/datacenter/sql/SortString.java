package  org.astrogrid.datacenter.sql;

/**
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */

public class SortString {

  public static String [] keyWords = {"select","from","where"};

  /**
   *
   * @param query String
   * @return String
   */
  public static String classification(String query){
  String subQuery = new String();
  return(subQuery);
  }


  /**
   *
   * @param query String
   * @param lenght int
   * @return String[][]
   */

  public static String[][] select(String subQuery, int lenght){

    //array declaration with of two collumns, the first one identify
    //Table Alias, the other Collumn name
    String[][] collumnList = new String[lenght][2];
    return collumnList;
  }

  /**
   *
   * @param query String
   * @param lenght int
   * @return String[][]
   */
  public static String[][] from(String subQuery, int lenght){
    String[][] collumnList = new String[lenght][2];
    return collumnList;
  }

  /**
   *
   * @param query String
   * @param lenght int
   * @return String[][]
   */
  public static String[][] where(String subQuery, int lenght){
    String[][] collumnList = new String[lenght][2];
    return collumnList;
  }


  public static void main(String[] args) {


  }

}
