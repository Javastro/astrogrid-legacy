import java.net.InetAddress;

/**
 * @author peter.shillan
 */
public class InetLookup {

  public static void main(String[] args) {
    try {
      InetAddress byName = InetAddress.getByName("localhost");
      InetAddress localHost = InetAddress.getLocalHost();
      InetAddress[] all = InetAddress.getAllByName("localhost");
      
      System.out.println("byName: " + byName);
      System.out.println("localHost: " + localHost);
      System.out.println("localHost full: " + localHost.getCanonicalHostName());
      System.out.println("all: " + all);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
