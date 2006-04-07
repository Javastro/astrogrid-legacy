CVS_TOP=../..
include $(CVS_TOP)/text/webpages/misc/Makefile.webpages

PNG=asserted-hierarchy.png inferred-hierarchy.png

access-control.html: access-control.xml access-control.bbl

install: $(WEB)/note/2006/access-control/access-control.html

$(WEB)/note/2006/access-control/access-control.html: access-control.html $(PNG)
	$(GENERAL_INSTALL_WEBPAGE)
	D=`dirname $@`; \
	  for png in $(PNG); do \
	    rm -f $$D/$$png; \
	    cp $$png $$D; \
	  done

