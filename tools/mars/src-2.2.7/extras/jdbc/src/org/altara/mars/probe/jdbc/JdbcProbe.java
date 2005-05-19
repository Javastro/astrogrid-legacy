package org.altara.mars.probe.jdbc;

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.sql.*;

public class JdbcProbe extends Probe {

	private String host;
	private String db;
	private String uname;
	private String pword;
	private String driver;
	private String protocol;
	private String url;

    public JdbcProbe(Service service) {

        // snag the service
        super.setService(service);
	
        // get parameters
        this.host = service.getHost().getAddress().getHostName();
        this.db = service.getParameter("db");
        this.uname = service.getParameter("user");
        this.pword = service.getParameter("pass");
        this.driver = service.getParameter("driver");
        this.protocol = service.getParameter("proto");
        
        // construct URL
        url = "jdbc:"+protocol+"://"+host+"/"+db;

    }

    protected Status doProbe() throws ClassNotFoundException {
        ClientDebugger debug = Debug.getCurrent().newDebugger("JDBC "+url);

        // ensure driver load
        Class.forName(driver);
        debug.message("driver OK");

        Status status;
        try {   
            Connection conn = DriverManager.getConnection(url, uname, pword);
            debug.message("connection opened");
            conn.close();
            debug.message("connection closed");
            status = new Status(Status.UP);
        } catch (SQLException sqe) {
            status = new Status(Status.DOWN);
            status.setProperty("received", sqe.toString());
        } finally {
            debug.close();
        }

        return status;
    }
}
