prefix=$(HOME)/rhessi-dsa

all:
	cd dsa; make
	cd scripts; make

install:
	cd dsa; make prefix=$(prefix) install
	cd scripts; make prefix=$(prefix) install
