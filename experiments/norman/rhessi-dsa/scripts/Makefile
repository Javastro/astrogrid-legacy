# prefix must be set on command-line
prefix=/no/such/directory
bindir=$(prefix)/bin

INST_FILES=dsa-create.mysql ssw-for-rhessi rhessi2dbase.pro
EDIT_FILES=upload.sh

edit=sed -e 's,@BASEDIR\@,$(prefix),g' \
	 -e 's/@GENERATED_FILE\@/Generated file, DO NOT EDIT!/'

all:
	@echo "Nothing to do"

install: all
	test -d $(prefix) || { echo "No directory $(prefix)"; false; }
	test -d $(bindir) || mkdir $(bindir)
	cp $(INST_FILES) $(bindir)
	for f in $(EDIT_FILES); do \
	  rm -f $(bindir)/$$f; \
	  $(edit) $$f >$(bindir)/$$f; \
	done

clean:
	@echo "Nothing to clean in scripts/"
