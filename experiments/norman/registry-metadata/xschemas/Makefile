#LX=$(CT)/code/lx/build/classes
LX=$(CT)/code/lx/lx.jar
CWM=PYTHONPATH=/Volumes/Lagrange/Data/tools/cwm-cvs/lib/python2.3/site-packages /Volumes/Lagrange/Data/tools/cwm-cvs/bin/cwm
JENA=$T/Jena-2.3

# DISTDIR is where 'install' puts things -- should be overridden by parent makefile
DISTDIR=XXX

# List of schemas which have been verified to convert sanely with
# xschema2rdfs.lx.
AUTOCONVERTIBLE=SIA-v1.0 VODataService-v1.0 \
	VORegistry-v1.0 \
	TabularDB-v0.3 ConeSearch-v1.0 stc-v1.30 SSA-v0.4 \
	CEA-v1.0 CEABase-v1.1 CEAImplementation-v2.0 CEATypes-v1.1 \
	VOApplication-v1.0rc1 
# The XSchemas SIA-0.7, VODataService-v0.5 and VORegistry-v0.3 also work, but
# I'm no longer working with those, so they're unverified, and untweaked.
# TabularDB-v0.3 appears to be the most recent version of that series --
# there's no v1.0 that I can find.

all: $(AUTOCONVERTIBLE:=.rdfs)

%.rdfs: %.xsd xschema2rdfs.lx
	java -cp $(LX) uk.me.nxg.lx.helpers.Transformer \
	  $< xschema2rdfs.lx $@
# %.rdfs: %.xsd xschema2rdfs.xslt
# 	rm -f $@
# 	xsltproc xschema2rdfs.xslt $< >$@.tmp && mv $@.tmp $@

%.xslt: %.lx
	rm -f $@
	java -cp $(LX) uk.me.nxg.lx.helpers.Transformer \
	  http://ns.nxg.me.uk/lx/generated-prefix=x --indent \
	  xschema2rdfs.lx >$@.tmp && mv $@.tmp $@

%.n3: %.rdfs
	rm -f $@
	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` \
	  java jena.rdfcopy $< RDF/XML N3 >$@
#	$(CWM) -pipe -rdf $< -n3 >$@.tmp && mv $@.tmp $@

# Following is better than .rdfs -> .n3, but requires rapper
%.ttl: %.rdfs
	rm -f $@ $@.tmp
	rapper -o turtle $< >$@.tmp && mv $@.tmp $@

%.nt: %.rdfs
	rm -f $@
	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` \
	  java jena.rdfcopy $< RDF/XML N-TRIPLE >$@

# check using Jena eyeball
%.eyeballed: %.rdfs
	rm -f $@
	../code/eyeball.sh \
	  -assume $(T)/eyeball-2.0/mirror/owl.rdf \
	    ../rdfschemas/VOResource-1.0.owl \
	    VODataService-v1.0.rdfs \
	  -check $< \
	  >$@.tmp 2>&1 && mv $@.tmp $@
#%.eyeballed: %.rdfs
#	rm -f $@
#	../code/eyeball.sh \
#	  -assume owl \
#	  -assume ../rdfschemas/VOResource-1.0.owl \
#	  -assume VODataService-v1.0.rdfs \
#	  -check $< \
#	  >$@.tmp 2>&1 && mv $@.tmp $@

all-schemas.rdfs: $(AUTOCONVERTIBLE:=.rdfs)
	rm -f $@
	$(CWM) -pipe -rdf $(AUTOCONVERTIBLE:=.rdfs) >$@.tmp && mv $@.tmp $@

# Produce a normalised test.nt from ../test/test.xsd.
#test.xsd:
#	ln -s ../test/test.xsd

# Create normalised N3 from RDFS (in alpha order, and without comments).
# Actually, only the first couple of elements in this pipeline are strictly
# necessary, but I got a bit carried away converting it to N3....
# It doesn't work in the presence of blank nodes.
test-schema.n3: test-schema.rdfs
	rm -f $@ $@.temp
	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` \
	  java jena.rdfcopy $< RDF/XML N-TRIPLE | \
	  grep -v 'rdf-schema#comment' | \
	  sed 's/  */	/g; s,<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>,a,; s,<http://www.w3.org/2000/01/rdf-schema#\([A-Za-z]*\)>,rdfs:\1,; s,<http://www.w3.org/1999/02/22-rdf-syntax-ns#\([A-Za-z]*\)>,rdf:\1,'| \
	  sort | \
	  awk 'BEGIN {printf "# Converted from $<"} {if ($$1 == PREV) {printf " ;\n\t%s\t%s", $$2, $$3;} else { printf " .\n\n%s\n\t%s\t%s", $$1, $$2, $$3; PREV=$$1; }} END {print " ."}' \
	  >$@.temp && mv $@.temp $@

install: all
	test -d $(DESTDIR)
	cp $(AUTOCONVERTIBLE:=.rdfs) $(DISTDIR)

check: test-schema.n3
	diff test-schema.n3-correct test-schema.n3

clean:
	rm -f $(AUTOCONVERTIBLE:=.rdfs) all-schemas.*
