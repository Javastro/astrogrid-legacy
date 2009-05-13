/*
 * $Id: Fingerprint.java,v 1.1 2009/05/13 13:20:56 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Date;
//import javax.servlet.jsp.JspWriter;
import java.io.PrintWriter;

/**
 * Methods used by JSPs to show details of the sites configuration
 * <p>
 * @author M Hill, from N Winstanley's fingerprint.jsp page from Brian Ewis
 */

public class Fingerprint
{

    /**
     * Returns an HTML details on theIdentify the version of a jar file. This uses a properties file
     * containing known names and sizes in the format
     * 'name(size)=version'. Version strings should be like 'xerces-1.4'
     * ie they should include the name of the library.
     */
    public String getFileVersion(File file) throws IOException {
        String key="<td>"+file.getName()+"</td>";
        key+= "<td>"+file.length()+"</td>";
        Date timestamp=new Date(file.lastModified());
        key+= "<td>"+timestamp.toString()+"</td>";
        return key;

        /* TODO: implement
        String value=versionProps.getProperty(key);
        if (value==null) {
            // make it possible to have jars without version nos
            value=versionProps.getProperty(file.getName());
        }
        if (value==null) {
            // fall back on something obvious
            value=key;
            Date timestamp=new Date(file.lastModified());
            value+=" / "+timestamp.toString();
        }
        return value;
        */
    }

    /**
     * Split up a classpath-like variable. Returns a list of files.
     * TODO: this can't cope with relative paths. I think theres code in BCEL that
     * can be used for this?
     */
    private File[] splitClasspath(String path) throws IOException {
        java.util.StringTokenizer st=
            new java.util.StringTokenizer(path,
                            System.getProperty("path.separator"));
        int toks=st.countTokens();
        File[] files=new File[toks];
        for(int i=0;i<toks;i++) {
            files[i]=new File(st.nextToken());
        }
        return files;
    }

    /** given a list of files, return a list of jars which actually exist */
    File[] scanFiles(File[] files) throws IOException {
        File[] jars=new File[files.length];
        int found=0;
        for (int i=0; i<files.length; i++) {
            if (files[i].getName().toLowerCase().endsWith(".jar")
                    && files[i].exists()) {
                jars[found]=files[i];
                found++;
            }
        }
        if (found<files.length) {
            File[] temp=new File[found];
            System.arraycopy(jars,0,temp,0,found);
            jars=temp;
        }
        return jars;
    }

    private static final File[] NO_FILES=new File[0];

    /** scan a directory for jars */
    public File[] scanDir(String dir) throws IOException
        {
        return scanDir(new File(dir));
        }
        
    public File[] scanDir(File dir) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            return NO_FILES;
        }
        return scanFiles(dir.listFiles());
    }

    /** scan a classpath for jars */
    public File[] scanClasspath(String path) throws IOException {
        if (path==null) {
            return NO_FILES;
        }
        return scanFiles(splitClasspath(path));
    }

    /**
     * scan a 'dirpath' (like the java.ext.dirs system property) for jars
     */
    public File[] scanDirpath(String path) throws IOException {
        if (path==null) {
            return NO_FILES;
        }
        File[] current=new File[0];
        File[] dirs=splitClasspath(path);
        for(int i=0; i<dirs.length; i++) {
            File[] jars=scanDir(dirs[i]);
            File[] temp=new File[current.length+jars.length];
            System.arraycopy(current,0,temp,0,current.length);
            System.arraycopy(jars,0,temp,current.length,jars.length);
            current=temp;
        }
        return scanFiles(current);
    }

    /** print out the jar versions for a directory */
    public void listDirectory(String title, PrintWriter out,String dir, String comment) throws IOException {
        listVersions(title, out,scanDir(dir), comment);
    }

    /** print out the jar versions for a directory-like system property */
    public void listDirProperty(String title, PrintWriter out,String key, String comment) throws IOException {
        listVersions(title, out,scanDir(System.getProperty(key)), comment);
    }

    /** print out the jar versions for a classpath-like system property */
    public void listClasspathProperty(String title, PrintWriter out,String key, String comment) throws IOException {
        listVersions(title, out,scanClasspath(System.getProperty(key)), comment);
    }

    /** print out the jar versions for a 'java.ext.dirs'-like system property */
    public void listDirpathProperty(String title, PrintWriter out,String key, String comment) throws IOException {
        listVersions(title, out,scanDirpath(System.getProperty(key)), comment);
    }

    /** print out the jar versions for a given list of files */
    public void listVersions(String title, PrintWriter out,File[] jars, String comment) throws IOException {
        out.print("<h2>");
        out.print(title);
        out.println("</h2>");
        out.println("<table>");
        for (int i=0; i<jars.length; i++) {
            out.println("<tr>"+getFileVersion(jars[i])+"</tr>");
        }
        out.println("</table>");
        if(comment!=null && comment.length()>0) {
            out.println("<p>");
            out.println(comment);
            out.println("<p>");
        }
    }

 }
