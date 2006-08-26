LX=../../../code/lx/build/classes
CWM=PYTHONPATH=/Volumes/Lagrange/Data/tools/cwm-cvs/lib/python2.3/site-packages /Volumes/Lagrange/Data/tools/cwm-cvs/bin/cwm

# List of schemas which have been verified to convert sanely with xschema2rdfs.lx
# Well..., reasonably sanely, modulo a few glitches.
AUTOCONVERTIBLE=SIA-v1.0 VODataService-v1.0 VORegistry-v1.0
# The XSchemas SIA-0.7, VODataService-v0.5 and VORegistry-v0.3 also work, but
# I'm no longer working with those, so they're unverified, and untweaked.

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
	  xschema2rdfs.lx >$@.tmp && mv $@.tmp $@

%.n3: %.rdfs
	rm -f $@
	$(CWM) -pipe -rdf $< -n3 >$@.tmp && mv $@.tmp $@

# check using Pellet eyeball
%.eyeballed: %.rdfs
	rm -f $@
	../code/eyeball.sh -check $< \
	  -assume owl -assume ../rdfschemas/VOResource-1.0.owl \
	  >$@.tmp && mv $@.tmp $@

all-schemas.rdfs: $(AUTOCONVERTIBLE:=.rdfs)
	rm -f $@
	$(CWM) -pipe -rdf $(AUTOCONVERTIBLE:=.rdfs) >$@.tmp && mv $@.tmp $@

clean:
	rm -f $(AUTOCONVERTIBLE:=.rdfs) all-schemas.*
