# prefix must be set on command-line (more likely by parent Makefile)
prefix=/no/such/directory
bindir=$(prefix)/bin
# version string should be overridden by parent
VERSION_STRING=$Id: Makefile,v 1.4 2007/03/30 15:32:56 norman Exp $

INST_FILES=upload.sh dsa-create.mysql ssw-for-rhessi rhessi2dbase.pro

edit=sed -e 's,@BASEDIR\@,$(prefix),g' \
	 -e 's/@GENERATED_FILE\@/Generated file, DO NOT EDIT!/' \
	 -e 's/@VERSION_STRING\@/$(VERSION_STRING)/'

all:
	@echo "Nothing to do"

install: all
	test -d $(prefix) || { echo "No directory $(prefix)"; false; }
	test -d $(bindir) || mkdir $(bindir)
	for f in $(INST_FILES); do \
	  rm -f $(bindir)/$$f; \
	  $(edit) $$f >$(bindir)/$$f; \
	  chmod +x $(bindir)/$$f; \
	done

clean:
	@echo "Nothing to clean in scripts/"
