README
======

Contents
--------

1 Developer's Guide

1 Developer's Guide
-------------------

The subproject-template/ directory in /astrogrid/portal is designed as a base for developers of Portal sub components.  Here is what you must do with this directory to turn it into your component:

- copy the directory to a director of your own name

- remove all CVS directories recursively
  - failure to do this will result in your commits overriding this template

- edit project.xml
  - change the project id and name
  - add your own dependencies

- edit maven.xml
  - change 'subproject' to your subproject name

- place your source code in src/java/

- place your test code in test/java/

- place your web content under site/
  - sitemap.xmap must live directly under site/
  - web content can be in any directory structure you like as long as your sitemap.xmap reflects this

- add your menu to WEB-INF/menu in a file subproject-name.xml
  - this file should conform to WEB-INF/menu/menu-schema.xsd

- menu links should be of the form:
  /astrogrid-portal/main/mount/subproject/your-url

- your subsitemap will receive your-url
  - your pipeline matcher should return an XHTML <div> tag with your content enclosed

- install your subproject
  - run maven war:war to produce target/astrogrid-portal-subproject.war
  - cd to the $CATALINA_HOME/webapps/astrogrid-portal directory
  - extract your war here
