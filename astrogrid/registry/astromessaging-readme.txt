README: Release Notes
=====================

This file describes the contents of the astromessaging.war file.

The Web Application Archive (WAR) contains the first Iteration 02 release the AstroGrid messaging components.  These componenets are:

- AstroGrid Logging Web Service (AstroLog)

- AstroGrid Messaging Web Service (AstroMQ)

The WAR is a modified version of the Apache Axis release 1.1rc2 those modifications being the inclusion of the above web services.  The following requirements must be in place before use:

- PostgreSQL (tested with version 7.3.2)

- OpenJMS (tested with version 0.7.5)

- XMLBlaster (tested with version 0.845)

The database used has not yet been migrated to DB2.  This migration will take place as soon as possible.  No code will have to be changed.  The migration path will involve the installation of DB2 and the updating of some OpenJMS and XMLBlaster configuration parameters.  For help on installation and configuration of OpenJMS and XMLBlaster for the purposes of AstroGrid messaging, please see install.txt.

To invoke the web services, a SOAP message must be sent to the web application on the URL http://<WEB_SERVER>:<PORT>/<WEB_APP>/services/MessagingService with the following substitutions:

- <WEB_SERVER>: the IP address or DNS name of the web server

- <PORT>: the port number of the web server; typically 8080

- <WEB_APP>: the web application name; typically astromessaging

The CVS tree contains example SOAP message bodies for the different messages that may be sent in the following files:

- log-example.xml: log a message
- query-log-example.xml: query the message log
- enqueue-example.xml: enqueue a message
- dequeue-example.xml: dequeue a message
- queue-callback-start-example.xml: start receiving asynchronous callbacks
- queue-callback-stop-example.xml: stop receiving asynchronous callbacks

Logging a message and querying the message log is straightforward as successful logging will result in the logged message being persisted.  Enqueueing a message will place a message on the queue when successful.  When a message is dequeued, that message is no longer available to any other subscriber to the queue.  Asynchronous callbacks can be used safely as the message is only removed from the queue if the web service which receives the callback actually exists and accepts the message without throwing an error.

An example client exists within CVS for sending the SOAP message bodies in the correct way:

- org.astrogrid.registry.util.SoapMessagingClient

This example client can send a given file/SOAP message (-f) a specified number of times (-n) to the given URL (-u).  This class requires all of the Axis libraries to be on its classpath.
