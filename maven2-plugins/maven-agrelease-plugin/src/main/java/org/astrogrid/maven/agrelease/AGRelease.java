package org.astrogrid.maven.agrelease;

/*
 * $Id: AGRelease.java,v 1.3 2009/06/04 15:13:09 pah Exp $
 * 
 * Created on 14 May 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.manager.WagonConfigurationException;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Site;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.apache.maven.wagon.CommandExecutionException;
import org.apache.maven.wagon.CommandExecutor;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.UnsupportedProtocolException;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.observers.Debug;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.repository.Repository;
import org.codehaus.plexus.PlexusContainer;

/**
 * Goal which creates appropriate site links in AG documentation.
 *
 * @description creates appropriate site links in AG documentation.
 * 
 * @goal release
 * 
 * @phase site-deploy
 * 
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 May 2009
 */
public class AGRelease
    extends AbstractMojo
{
    
    static final String siteRegexp = "://[^/]+/+(.+)/+p/+([^/]+)/";
    static final Pattern sitePattern = Pattern.compile(siteRegexp);

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     */
    private WagonManager wagonManager;

    /**
     * The current user system settings for use in Maven.
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings settings;

    private PlexusContainer container;

    public void execute()
        throws MojoExecutionException
    {
        
        //TODO should not do this for SNAPSHOTS...
        DistributionManagement distributionManagement = project.getDistributionManagement();
        
        if(project.getVersion().endsWith("SNAPSHOT"))
        {
            getLog().info("Links not made for shapshot versions");
            return;
        }

        if ( distributionManagement == null )
        {
            throw new MojoExecutionException( "Missing distribution management information in the project" );
        }

        Site site = distributionManagement.getSite();

        if ( site == null )
        {
            throw new MojoExecutionException(
                "Missing site information in the distribution management element in the project.." );
        }

        String url = site.getUrl();

        String id = site.getId();

        if ( url == null )
        {
            throw new MojoExecutionException( "The URL to the site is missing in the project descriptor." );
        }
        getLog().debug( "The site links are being made at  '" + url + "'");

        Repository repository = new Repository( id, url );

        Wagon wagon;

        try
        {
            wagon = wagonManager.getWagon( repository );
 //           configureWagon( wagon, repository.getId(), settings, container, getLog() );
        }
        catch ( UnsupportedProtocolException e )
        {
            throw new MojoExecutionException( "Unsupported protocol: '" + repository.getProtocol() + "'", e );
        }
        catch ( WagonConfigurationException e )
        {
            throw new MojoExecutionException( "Unable to configure Wagon: '" + repository.getProtocol() + "'", e );
        }

        if ( !wagon.supportsDirectoryCopy() )
        {
            throw new MojoExecutionException(
                "Wagon protocol '" + repository.getProtocol() + "' doesn't support directory copying" );
        }

        try
        {
            Debug debug = new Debug();

            wagon.addSessionListener( debug );

            wagon.addTransferListener( debug );

            ProxyInfo proxyInfo = getProxyInfo( repository, wagonManager );
            if ( proxyInfo != null )
            {
                wagon.connect( repository, wagonManager.getAuthenticationInfo( id ), proxyInfo );
            }
            else
            {
                wagon.connect( repository, wagonManager.getAuthenticationInfo( id ) );
            }

            
           Matcher matcher = sitePattern.matcher(url);
           if(matcher.find()){
               String start = matcher.group(1);
               String name = matcher.group(2);
               getLog().debug("start="+start + ", name="+name);
         
           
         
           if ( wagon instanceof CommandExecutor )
            {
                CommandExecutor exec = (CommandExecutor) wagon;
                
                //TODO need to work out exactly what links are needed;
                //make a link to the "current" version
                
                //link does not work - url redirects needed because of diffent levels and relative links
//                exec.executeCommand( "rm -f /" + start + "latest/" + name +
//                        "; cd /"+ start + "latest/; ln -s ../p/"+name+"/"+project.getVersion()+" "+ name);
                final String dirname = "/"+ start + "latest/" + name + "/";
                exec.executeCommand( "if [ ! -d "+dirname+" ]; then  mkdir "+dirname+ 
                        ";fi;echo  'Redirect 301 /doc/latest/"+name+"/index.html http://software.astrogrid.org/doc/p/"+name+"/"+
                        project.getVersion()+"/index.html' >" + dirname +
                        ".htaccess;");

                final String artifactname = project.getArtifactId()+"-"+project.getVersion()+"."+project.getPackaging();
                //IMPL the quoting is rather bash dependent - perhaps better to send the command as a file - or use xsltproc...
                exec.executeCommand(
                        "perl -p -i -e '\"'\"'if(/"+project.getArtifactId()+
                        "/){s@<a class=\"download\".+</a>@<a class=\"download\" href=\"http://www.astrogrid.org/maven2/org/astrogrid/"+
                        project.getArtifactId()+"/"+project.getVersion()+"/"+artifactname+"\">"+artifactname+"</a>@};'\"'\"' /"+
                        start + "/versions.html"
                        );
            }
           }
           else {
               getLog().error("site layout does not match Astrogrid Standard pattern");
           }
        }
        catch ( ConnectionException e )
        {
            throw new MojoExecutionException( "Error configuring site", e );
        }
        catch ( AuthenticationException e )
        {
            throw new MojoExecutionException( "Error configuring site", e );
        }
        catch ( CommandExecutionException e )
        {
            throw new MojoExecutionException( "Error configuring site", e );
        }
        finally
        {
            try
            {
                wagon.disconnect();
            }
            catch ( ConnectionException e )
            {
                getLog().error( "Error disconnecting wagon - ignored", e );
            }
        }
        }
    
    /**
     *
     * <p>
     * Get the <code>ProxyInfo</code> of the proxy associated with the <code>host</code>
     * and the <code>protocol</code> of the given <code>repository</code>.
     * </p>
     * <p>
     * Extract from <a href="http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html">
     * J2SE Doc : Networking Properties - nonProxyHosts</a> : "The value can be a list of hosts,
     * each separated by a |, and in addition a wildcard character (*) can be used for matching"
     * </p>
     * <p>
     * Defensively support for comma (",") and semi colon (";") in addition to pipe ("|") as separator.
     * </p>
     * 
     * @return a ProxyInfo object instantiated or <code>null</code> if no matching proxy is found
     * @TODO copied from org.apache.maven.plugins.site.SiteDeployMojo - is this really necessary
     */
    public static ProxyInfo getProxyInfo( Repository repository, WagonManager wagonManager )
    {
        ProxyInfo proxyInfo = wagonManager.getProxy( repository.getProtocol() );

        if ( proxyInfo == null )
        {
            return null;
        }

        String host = repository.getHost();
        String nonProxyHostsAsString = proxyInfo.getNonProxyHosts();
        String[] nonProxyHosts = StringUtils.split( nonProxyHostsAsString, ",;|" );
        for ( int i = 0; i < nonProxyHosts.length; i++ )
        {
            String nonProxyHost = nonProxyHosts[i];
            if ( StringUtils.contains( nonProxyHost, "*" ) )
            {
                // Handle wildcard at the end, beginning or middle of the nonProxyHost
                String nonProxyHostPrefix = StringUtils.substringBefore( nonProxyHost, "*" );
                String nonProxyHostSuffix = StringUtils.substringAfter( nonProxyHost, "*" );
                // prefix*
                if ( StringUtils.isNotEmpty( nonProxyHostPrefix ) && host.startsWith( nonProxyHostPrefix )
                    && StringUtils.isEmpty( nonProxyHostSuffix ) )
                {
                    return null;
                }
                // *suffix
                if ( StringUtils.isEmpty( nonProxyHostPrefix )
                    && StringUtils.isNotEmpty( nonProxyHostSuffix ) && host.endsWith( nonProxyHostSuffix ) )
                {
                    return null;
                }
                // prefix*suffix
                if ( StringUtils.isNotEmpty( nonProxyHostPrefix ) && host.startsWith( nonProxyHostPrefix )
                    && StringUtils.isNotEmpty( nonProxyHostSuffix ) && host.endsWith( nonProxyHostSuffix ) )
                {
                    return null;
                }
            }
            else if ( host.equals( nonProxyHost ) )
            {
                return null;
            }
        }
        return proxyInfo;
    }

}
