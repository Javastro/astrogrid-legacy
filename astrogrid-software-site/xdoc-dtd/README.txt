DTD and editing stylesheets for XDOC.
---------------------------------------------------

this directory contains the XHTML DTD, plus the XDOC DTD which 
uses a subset of XHTML.

In your editor, set the default DTD to be 'xdoc.dtd'

when creating new documents, the root element should be 'document'

this dtd defines the mandatory structural tags for an XDOC document,
plus a simple subset of XHTML for writing content in. If you need a
defintion of the XHTML tags, see 
a  tutorial such as 
http://www.w3schools.com/xhtml/xhtml_html.asp
and a tag reference such as
http://www.w3schools.com/xhtml/xhtml_reference.asp

Other XHTML
tags can be validly used in an XDOC document, but I've kept the set as
short as possible for simplicity, and for predictable generation of 
html / PDF / javahelp sites.


-----

The Morphon XML editor (java, free, webstartable here - 
http://www.morphon.com/webstart/index.shtml ) 
is a validating editor that provides WYSIWYG using a CSS stylesheet
to render the document you're working on.

the xdoc.css stylesheet in this direcotry can be used for this purpose.
It's not pretty, but better than looking at tags, and works quite nicely.
It may work with other XML editors too - not tried it in oXygen.


---

myself, I'd just use eclipse and the XmlBuddy, but hopefully this DTD will let every 
feel that they can manage to write documentation in glorified HTML :)