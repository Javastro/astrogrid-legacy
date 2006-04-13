# This directory handles generating the files listed in $(PRODUCTS).
# These products are also checked in (currently), so that the whole thing
# can be built on a platform which doesn't have make.

PRODUCTS=base.css

all: $(PRODUCTS)

# base.css should be copied to ../base.css
base.css: base.css.in base.css.settings
	rm -f $@ $@-tmp
	m4 base.css.settings base.css.in >$@-tmp && mv $@-tmp $@

install: $(PRODUCTS)
	cp base.css ..

clean:
	rm -f $(PRODUCTS)
