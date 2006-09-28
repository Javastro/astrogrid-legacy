package uk.ac.roe.astrogrid.tests.agatroe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestUser {

    static {
        Properties props = new Properties();
        File file = new File("test.properties");
        try {
            props.load(new FileInputStream(file));
            PASS = props.getProperty("test.pass","");
            USER = props.getProperty("test.user","test");
            COMMUNITY = props.getProperty("test.community","roe.ac.uk");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    /*
    public static final String USER = "test";
    public static final String PASS = "test";
    public static final String COMMUNITY = "wfau.roe.ac.uk";
    */
    public static  String USER;
    public static  String PASS;
    public static  String COMMUNITY;

}
