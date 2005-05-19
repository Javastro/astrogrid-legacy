package org.altara.mars.probe.jdbc;

import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;

public class JdbcProbeFactory extends ProbeFactory {

	private String[] paramNames = 
    {"proto", "driver", "db", "user", "pass"};

	private String[] paramLabels = 
    {"Protocol", "Driver", "Database", "Username", "Password"};
	
    private JdbcProbe probe;

public JdbcProbeFactory() {

	this("jdbc");
}

public JdbcProbeFactory(String name) {
	super(name);
}

public int getDefaultPort() {
	return 0;
}

public String[] getServiceParamNames() {
	return paramNames;
}

public String[] getServiceParamLabels() {
	return paramLabels;
}

public String getSerivceParamDefault(Service service, String name) {
    // none of them really have a default, so we can cheat
	return "";
}

public Probe createProbe(Service service) {
	return new JdbcProbe(service);
}
}
