
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;

/* 
requires acr-interface.jar to be on groovy classpath

*/

/*
permutations to test:
vodesktop running 
    - this script should complete normally.
vodesktop not running, but vodesktop.jar on classpath 
    - ar should be started
    -this script should complete normally.
    (FAIL: at moment it prompts user to start manually, but 
    clicking ok causes a startup).
vodesktop not running, vodesktop.jar not on classpath
    - user should be prompted to start AR
    a) if user starts, script should complete normally.
    b) if user doesn't start, script should fail.
*/

// connect
public class findertest {
    public static void main(String[] args) throws Exception {
        Finder f = new Finder();
        ACR acr = f.find();
        // try accessing a function
        Configuration conf = (Configuration)acr.getService("system.configuration");
        System.out.println(conf.getKey("system.rmi.port"));
        // closedown.
        System.exit(0)  ;      
    }
}
