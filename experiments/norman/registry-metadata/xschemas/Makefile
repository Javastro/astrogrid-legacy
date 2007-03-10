LX=../../../code/lx/build/classes
CWM=PYTHONPATH=/Volumes/Lagrange/Data/tools/cwm-cvs/lib/python2.3/site-packages /Volumes/Lagrange/Data/tools/cwm-cvs/bin/cwm
JENA=$T/Jena-2.3

# List of schemas which have been verified to convert sanely with
# xschema2rdfs.lx.  Well..., reasonably sanely, modulo a few glitches.
AUTOCONVERTIBLE=SIA-v1.0 VODataService-v1.0 VORegistry-v1.0 \
	TabularDB-v0.3
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

%.nt: %.rdfs
	rm -f $@
	CLASSPATH=`ls $(JENA)/lib/*.jar|sed -n '1,$$H;$${g;s/\n/:/gp;}'` \
	  java jena.rdfcopy $< RDF/XML N-TRIPLE >$@

# check using Jena eyeball
%.eyeballed: %.rdfs
	rm -f $@
	../code/eyeball.sh \
	  -assume owl ../rdfschemas/VOResource-1.0.owl VODataService-v1.0.rdfs \
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

clean:
	rm -f $(AUTOCONVERTIBLE:=.rdfs) all-schemas.*
