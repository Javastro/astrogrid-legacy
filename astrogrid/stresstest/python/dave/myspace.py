#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Wrapper class for MySpace access.
#     </meta:title>
#     <meta:description>
#         Wrapper class for MySpace access.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 461 $
#         $LastChangedDate: 2008-01-07 09:33:11 +0000 (Mon, 07 Jan 2008) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>
#

import os.path
import logging

class MySpace:

    def __init__(self, ar, username, password, community):
        self.username  = username
        self.password  = password
        self.community = community

    #
    # Login to our myspace.
    def login(self, ar):
        if not ar.astrogrid.community.isLoggedIn():
            logging.debug(
                "Login as [%s] at [%s]",
                self.username,
                self.community
                )
            ar.astrogrid.community.login(
                self.username,
                self.password,
                self.community
                )

    #
    # Convert a myspace path to an ivorn.
    def ivorn(self, path):
        if not path.startswith('ivo://'):
            return 'ivo://' + self.community + '/' + self.username + '#' + path
        else:
            return path

    #
    # Create a folder.
    def createFolder(self, ar, path):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Creating myspace folder [%s]", ivorn)
        ar.astrogrid.myspace.createFolder(
            ivorn
            )

    #
    # Delete a file.
    def deleteFile(self, ar, path):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Deleting myspace file [%s]", ivorn)
        ar.astrogrid.myspace.delete(ivorn)

    #
    # Create a file.
    def createFile(self, ar, path):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Creating myspace file [%s]", ivorn)
        ar.astrogrid.myspace.createFile(
            ivorn
            )

    #
    # Rename a file.
    def renameFile(self, ar, path, name):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Renaming myspace file [%s][%s]", ivorn, name)
        ar.astrogrid.myspace.rename(
            ivorn,
            name
            )

    #
    # Move a file.
    def moveFile(self, ar, path, dest):
        self.login(ar)
        ivorn = self.ivorn(path)
        a,b = os.path.split(dest)
        c = self.ivorn(a)
        logging.debug("Moving myspace file [%s][%s][%s]", ivorn, c, b)
        ar.astrogrid.myspace.move(
            ivorn,
            c,
            b
            )

    #
    # Copy a file
    def copyFile(self, ar, path, dest):
        self.login(ar)
        ivorn = self.ivorn(path)
        a,b = os.path.split(dest)
        c = self.ivorn(a)
        logging.debug("Copying myspace file [%s][%s][%s]", ivorn, c, b)
        ar.astrogrid.myspace.copy(
            ivorn,
            c,
            b
            )

    #
    # Get a URL to send data to.
    def getWriteContentURL(self, ar, path):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Requesting URL to send data to [%s]", ivorn)
        return ar.astrogrid.myspace.getWriteContentURL(
            ivorn
            )

    #
    # Get a URL to read data from.
    def getReadContentURL(self, ar, path):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Requesting URL to read data from [%s]", ivorn)
        return ar.astrogrid.myspace.getReadContentURL(
            ivorn
            )

    #
    # Read data from a URL.
    def copyURLToContent(self, ar, path, url):
        self.login(ar)
        ivorn = self.ivorn(path)
        logging.debug("Reading data from URL [%s][%s]", ivorn, url)
        return ar.astrogrid.myspace.copyURLToContent(
            url,
            ivorn
            )

    #
    # Logout
    def logout(self, ar):
        if ar.astrogrid.community.isLoggedIn():
            logging.debug(
                "Logging out as [%s] at [%s]",
                 )
            ar.astrogrid.community.logout()
