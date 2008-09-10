/*
 * <meta:header>
 *     <meta:licence>
 *         Copyright (C) AstroGrid. All rights reserved.
 *         This software is published under the terms of the AstroGrid Software License version 1.2.
 *         See [http://software.astrogrid.org/license.html] for details. 
 *     </meta:licence>
 *     <svn:header>
 *         $LastChangedRevision: 1099 $
 *         $LastChangedDate: 2008-07-29 18:56:24 +0100 (Tue, 29 Jul 2008) $
 *         $LastChangedBy: dmorris $
 *     </svn:header>
 * </meta:header>
 *
 */
package org.astrogrid.vospace.client.delegate ;

import java.net.URI ;

import java.io.IOException ;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.util.Iterator ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.vospace.v11.client.node.Node ;
import org.astrogrid.vospace.v11.client.node.NodeTypeEnum ;

import org.astrogrid.vospace.v11.client.exception.* ;

import org.astrogrid.vospace.v02.junit.TestBase ;

/**
 * Test case for the AstroGrid VOSpace delegate.
 *
 */
public class AGVOSpaceDelegateTestCase
extends TestBase
    {
    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(AGVOSpaceDelegateTestCase.class);

    /**
     * Get the test VOSpace identifier.
     * Returns a Java URI.
     *
     */
    public URI vosrn()
      throws Exception
        {
        return new URI(
            this.getTestProperty(
                "vospace.vosrn"
                )
            );
        }

    /**
     * Generate a new VOSpace identifier.
     * Returns a Java URI.
     *
     */
    public URI vosrn(String path)
      throws Exception
        {
        return new URI(
            this.vosrn().resolve(path).toString()
            );
        }

    /**
     * Write some test data to a stream.
     *
     */
    public void write(OutputStream stream, int pages, int rows)
    throws Exception
        {
        DataOutputStream data = new DataOutputStream(
            stream
            );
        for (long i=0 ; i < pages ; i++)
            {
            log.debug("[" + i + "]");
            for (long j=0 ; j < rows ; j++)
                {
                data.writeLong(j);
                }
            }
        data.close();
        }

    /**
     * Read some test data from a stream.
     *
     */
    public void read(InputStream stream, int pages, int rows)
    throws Exception
        {
        DataInputStream data = new DataInputStream(
            stream
            );
        for (long i=0 ; i < pages ; i++)
            {
            log.debug("[" + i + "]");
            for (long j=0 ; j < rows ; j++)
                {
                data.readLong();
                }
            }
        data.close();
        }

    /**
     * Setup our test.
     *
     */
    public void setUp()
      throws Exception
        {
        }

    /**
     * Check we can import data into a new file node.
     *
     */
    public void testInportNewFileNode()
      throws Exception
        {
        //
        // Create our resolver.
        final AGVOSpaceDelegateResolver resolver = new AGVOSpaceDelegateResolverImpl(
            getTestURL(
                "registry.query.endpoint"
                )
            );
        //
        // Create our delegate.
        final AGVOSpaceDelegate delegate = resolver.resolve();
        //
        // Create our root node.
        // (using .auto to generate a new root node for each test).
        final Node root = delegate.create(
            NodeTypeEnum.TREE_NODE,
            vosrn(".auto")
            );
        log.debug("Root created");
        log.debug("  URI  [" + root.uri()  + "]");
        log.debug("  Type [" + root.type() + "]");
        log.debug("  Name [" + root.name() + "]");
        log.debug("  Path [" + root.path() + "]");

        //
        // Create a tree node, 
        // (adding 'tree' to the unique root for this test).
        final Node tree = delegate.create(
            NodeTypeEnum.TREE_NODE,
            root.vosrn().append("tree")
            );
        log.debug("Tree created");
        log.debug("  URI  [" + tree.uri()  + "]");
        log.debug("  Type [" + tree.type() + "]");
        log.debug("  Name [" + tree.name() + "]");
        log.debug("  Path [" + tree.path() + "]");

        //
        // Open a stream to write data into 'tree/file'.
        // (creating a FILE_NODE and sending data using the default application/binary format)
        OutputStream output = delegate.write(
            root.vosrn().append("tree/file")
            );
        //
        // Write some test data into the file.
        this.write(
            output,
            100,
            100
            );
        }


    /**
     * Check we can export data from a new file node.
     *
     */
    public void testExportNewFileNode()
      throws Exception
        {
        //
        // Create our resolver.
        final AGVOSpaceDelegateResolver resolver = new AGVOSpaceDelegateResolverImpl(
            getTestURL(
                "registry.query.endpoint"
                )
            );
        //
        // Create our delegate.
        final AGVOSpaceDelegate delegate = resolver.resolve();

        //
        // Create our root node.
        // (using .auto to generate a new root node for each test).
        final Node root = delegate.create(
            NodeTypeEnum.TREE_NODE,
            vosrn(".auto")
            );
        log.debug("Root created");
        log.debug("  URI  [" + root.uri()  + "]");
        log.debug("  Type [" + root.type() + "]");
        log.debug("  Name [" + root.name() + "]");
        log.debug("  Path [" + root.path() + "]");

        //
        // Create a tree node.
        // (ading 'tree' to the unique root for this test).
        final Node tree = delegate.create(
            NodeTypeEnum.TREE_NODE,
            root.vosrn().append("tree")
            );
        log.debug("Tree created");
        log.debug("  URI  [" + tree.uri()  + "]");
        log.debug("  Type [" + tree.type() + "]");
        log.debug("  Name [" + tree.name() + "]");
        log.debug("  Path [" + tree.path() + "]");

        //
        // Open a stream to write data into 'tree/file'.
        // (creating a FILE_NODE and sending data using the default application/binary format)
        OutputStream output = delegate.write(
            root.vosrn().append("tree/file")
            );
        //
        // Write some test data into the file.
        this.write(
            output,
            100,
            100
            );
        //
        // Open a stream to read data from 'tree/file'.
        // (requesting the data in whatever format it was stored in)
        InputStream input = delegate.read(
            root.vosrn().append("tree/file")
            );
        //
        // Read the data from the file.
        this.read(
            input,
            100,
            100
            );
        }
    }


