package  org.astrogrid.datacenter.parser.test;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Data Centre Group
 *
 */

public class SortString {

  public static String[] keyWords = {
      "select", "from", "where"};

  /**
   *
   * @param query String
   * @return String
   */
  public static String classification(String query) {
    String subQuery = new String();
    return (subQuery);
  }

  /**
   *
   * @param query String
   * @param lenght int
   * @return String[][]
   */

  public static String[][] select(String subQuery, int lenght) {

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
  public static String[][] from(String subQuery, int lenght) {
    String[][] collumnList = new String[lenght][2];
    return collumnList;
  }

  /**
   *
   * @param query String
   * @param lenght int
   * @return String[][]
   */
  public static String[][] where(String subQuery, int lenght) {
    String[][] collumnList = new String[lenght][2];
    return collumnList;
  }

  public static void main(String[] args) {

  }

}
