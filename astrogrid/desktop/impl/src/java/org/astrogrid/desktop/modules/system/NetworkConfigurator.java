package org.astrogrid.desktop.modules.system;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.pref.Preference;

/**
 * Performs network configuration according to system preferences.
 *
 * <p><b>Note:</b> that the policy for setting these proxy settings 
 * would need some revision to work well with Java WebStart (i.e. to
 * pick up proxy settings from the WebStart environment).
 *
 * @author   Mark Taylor
 * @since    12 Feb 2008
 */
public class NetworkConfigurator implements Runnable {

    private String proxyHost;
    private int proxyPort;
    private boolean preferIPv4Stack;
    private boolean preferIPv6Addresses;

    private static final Log logger = LogFactory.getLog(NetworkConfigurator.class);

    public NetworkConfigurator(String proxyHost, String proxyPort, Preference preferIPv4Stack, Preference preferIPv6Addresses) {
        this.proxyHost = proxyHost;
        int portNum;
        try {
            portNum = Integer.parseInt(proxyPort);
        }
        catch (NumberFormatException e) {
            int dfltPort = 80;
            logger.warn("Bad port number string \"" + proxyPort +"\" - use " + dfltPort);
            portNum = dfltPort;
        }
        this.proxyPort = portNum;
        this.preferIPv4Stack = preferIPv4Stack.asBoolean();
        this.preferIPv6Addresses = preferIPv6Addresses.asBoolean();
    }

    /**
     * Invoked by hivemind to set properties.
     */
    public void run() {
        Properties props = System.getProperties();
        if (proxyHost != null && proxyHost.trim().length() > 0) {
            setProxy(props, this.proxyHost, this.proxyPort);
        }
        props.setProperty("java.net.preferIPv4Stack", Boolean.toString(this.preferIPv4Stack));
        props.setProperty("java.net.preferIPv6Addresses", Boolean.toString(this.preferIPv6Addresses));
    }

    /**
     * Sets required system properties to indicate use of an HTTP proxy server.
     *
     * @param proxyHost  proxy host
     * @param proxyPort  proxy port
     */
    private void setProxy(Properties props, String proxyHost, int proxyPort) {
        String host = this.proxyHost;
        String port = Integer.toString(this.proxyPort);

        // Belt and braces.  These property names apparently changed between
        // releases.  Note there are also deployment.proxy.http.{host,port},
        // but these only apply to WebStart.  Coping with a WebStart environment
        // would be more complicated in any case, since one probably ought to
        // read the defaults from the values set by WebStart rather than from
        // the user-set preferences.
        props.setProperty("http.proxyHost", host);
        props.setProperty("http.proxyPort", port);
        props.setProperty("proxyHost", host);
        props.setProperty("proxyPort", port);
    }

    @Override
    public String toString() {
        return new StringBuffer()
              .append("proxyHost: ")
              .append(proxyHost)
              .append('\n')
              .append("proxyPort: ")
              .append(proxyPort)
              .append('\n')
              .append("preferIPv4Stack: ")
              .append(preferIPv4Stack)
              .append('\n')
              .append("preferIPv6Addresses: ")
              .append(preferIPv6Addresses)
              .toString();
    }
}
