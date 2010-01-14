VERSION=0.9.9
LANG1=eo
LANG2=en
PREFIX1=$(LANG1)-$(LANG2)
PREFIX2=$(LANG2)-$(LANG1)

BASENAME=apertium-$(PREFIX1)

TARGETS_COMMON = $(PREFIX1) $(PREFIX2)

$(PREFIX1): $(PREFIX1).automorf.bin $(PREFIX1).autobil.bin $(PREFIX1).autogen.bin \
     $(PREFIX1).autopgen.bin $(PREFIX1).t1x.bin $(PREFIX1).t2x.bin $(PREFIX1).antaux_t2x.bin $(PREFIX1).t3x.bin \
     modes

$(PREFIX2): $(PREFIX2).automorf.bin $(PREFIX2).autobil.bin $(PREFIX2).autogen.bin \
     $(PREFIX2).autopgen.bin $(PREFIX2).t1x.bin $(PREFIX2).genitive.t1x.bin $(PREFIX2).antaux_t2x.bin $(PREFIX2).t2x.bin $(PREFIX2).t3x.bin \
     modes

tmp1_$(BASENAME).$(LANG1).dix: $(BASENAME).$(LANG1).dix.xml
	apertium-validate-dictionary $(BASENAME).$(LANG1).dix.xml
	@if test -n "$(EKSPERIMENTO)"; then \
		echo EKSPERIMENTA DOSIERO apertium-eo-en.eo.eksperimenta_parto.dix ENMETATA; \
		cat $(BASENAME).$(LANG1).dix.xml | sed -e ':a;N;$!ba;s/EBLO KOMENCO.*EBLO FINO/A ENMETO/' | sed -e '/A ENMETO/ r apertium-eo-en.eo.eksperimenta_parto.dix' | sed 's/<\/dictionary>//g' > tmp1; \
	else \
		echo Voku por eksperimenti: 'EKSPERIMENTO=jes make'; \
		cat $(BASENAME).$(LANG1).dix.xml | sed 's/<\/dictionary>//g' > tmp1; \
	fi;
	$(BASH) ./multi-gen.sh $(BASENAME).$(LANG1)-multiwords.txt >> tmp1
	echo '</dictionary>' >> tmp1
	mv tmp1 $@
     
$(PREFIX1).automorf.bin: tmp1_$(BASENAME).$(LANG1).dix
	apertium-validate-dictionary tmp1_$(BASENAME).$(LANG1).dix
	lt-comp lr tmp1_$(BASENAME).$(LANG1).dix $@ 

$(PREFIX1).autobil.bin: $(BASENAME).$(PREFIX1).dix
	apertium-validate-dictionary $(BASENAME).$(PREFIX1).dix
	lt-comp lr $(BASENAME).$(PREFIX1).dix $@

$(PREFIX1).autogen.bin: $(BASENAME).$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).$(LANG2).dix
	lt-comp rl $(BASENAME).$(LANG2).dix $@

$(PREFIX1).autopgen.bin: $(BASENAME).post-$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).post-$(LANG2).dix
	lt-comp lr $(BASENAME).post-$(LANG2).dix $@

$(PREFIX2).automorf.bin: $(BASENAME).$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).$(LANG2).dix
	lt-comp lr $(BASENAME).$(LANG2).dix $@ $(BASENAME).$(LANG2).acx

$(PREFIX2).autobil.bin: $(BASENAME).$(PREFIX1).dix
	apertium-validate-dictionary $(BASENAME).$(PREFIX1).dix
	lt-comp rl $(BASENAME).$(PREFIX1).dix $@

$(PREFIX2).autogen.bin: tmp1_$(BASENAME).$(LANG1).dix
	apertium-validate-dictionary tmp1_$(BASENAME).$(LANG1).dix
	lt-comp rl tmp1_$(BASENAME).$(LANG1).dix $@

$(PREFIX2).autopgen.bin: $(BASENAME).post-$(LANG1).dix
	apertium-validate-dictionary $(BASENAME).post-$(LANG1).dix
	lt-comp lr $(BASENAME).post-$(LANG1).dix $@

$(PREFIX1).t1x.bin: $(BASENAME).$(PREFIX1).t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX1).t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t1x $@

$(PREFIX1).t2x.bin: $(BASENAME).$(PREFIX1).t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX1).t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t2x $@

$(PREFIX1).t3x.bin: $(BASENAME).$(PREFIX1).t3x
	apertium-validate-postchunk $(BASENAME).$(PREFIX1).t3x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t3x $@

$(PREFIX2).t1x.bin: $(BASENAME).$(PREFIX2).t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX2).t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t1x $@

$(PREFIX2).genitive.t1x.bin: $(BASENAME).$(PREFIX2).genitive.t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX2).genitive.t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).genitive.t1x $@

$(PREFIX2).antaux_t2x.bin: $(BASENAME).$(PREFIX2).antaux_t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX2).antaux_t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).antaux_t2x $@

$(PREFIX1).antaux_t2x.bin: $(BASENAME).$(PREFIX1).antaux_t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX1).antaux_t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).antaux_t2x $@

$(PREFIX2).t2x.bin: $(BASENAME).$(PREFIX2).t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX2).t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t2x $@

$(PREFIX2).t3x.bin: $(BASENAME).$(PREFIX2).t3x
	apertium-validate-postchunk $(BASENAME).$(PREFIX2).t3x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t3x $@

test: $(TARGETS_COMMON)
	bash regression-tests.sh

test-nodl: $(TARGETS_COMMON)
	bash regression-tests.sh NODOWNLOAD

crptest: $(TARGETS_COMMON)
#	zcat corpa/en.crp.txt.gz |  apertium -d . en-eo > efter.txt
#	diff foer.txt efter.txt | grep -r '[<>]' > /tmp/crpdiff.txt
#	for i in `cut -f2 -d' ' /tmp/crpdiff.txt | cut -f1 | uniq`; do echo  --- $i ---; zcat corpa/en.crp.txt.gz | grep -r "^$i\W" ; grep -r "^. $i\W" /tmp/crpdiff.txt; done | less
#	diff foer.txt efter.txt | grep -r '[<>]' > /tmp/crpdiff.txt;for i in `cut -f2 -d' ' /tmp/crpdiff.txt | cut -f1 | uniq`; do echo  --- $i ---; zcat corpa/en.crp.txt.gz | grep -r "^$i\W" ; grep -r "^. $i\W" /tmp/crpdiff.txt; done | less
#	diff foer.txt efter.txt | grep -r '[<>]' > /tmp/crpdiff.txt;for i in `cut -c3-9 /tmp/crpdiff.txt | uniq`; do echo  --- $i ---; zcat corpa/en.crp.txt.gz | grep -r "^ *$i\W" ; grep -r "$i" /tmp/crpdiff.txt; done | less


EXTRA_DIST=$(BASENAME).$(LANG1).dix.xml \
           $(BASENAME).post-$(LANG1).dix $(BASENAME).post-$(LANG2).dix \
           $(BASENAME).$(LANG2).dix $(BASENAME).$(LANG1).tsx \
	   $(BASENAME).$(PREFIX1).dix \
           $(BASENAME).$(LANG2).tsx  \
           $(PREFIX1).prob $(PREFIX2).prob \
	   $(BASENAME).$(LANG2).acx \
           $(BASENAME).$(PREFIX1).t1x \
           $(BASENAME).$(PREFIX1).antaux_t2x $(BASENAME).$(PREFIX1).t2x $(BASENAME).$(PREFIX1).t3x \
           $(BASENAME).$(PREFIX2).t1x $(BASENAME).$(PREFIX2).genitive.t1x \
           $(BASENAME).$(PREFIX2).antaux_t2x $(BASENAME).$(PREFIX2).t2x $(BASENAME).$(PREFIX2).t3x \
           modes.xml multi-gen.sh

apertium_eo_endir=$(prefix)/share/apertium/apertium-$(PREFIX1)/
apertium_eo_modesdir=$(prefix)/share/apertium/modes/

modes: modes.xml
	apertium-gen-modes modes.xml
	cp *.mode modes/

$(PREFIX1).mode: modes

$(PREFIX2).mode: modes


apertium_eo_en_DATA=$(PREFIX1).automorf.bin $(PREFIX2).automorf.bin \
	$(PREFIX1).autobil.bin $(PREFIX2).autobil.bin \
	$(PREFIX1).autogen.bin $(PREFIX2).autogen.bin \
	$(PREFIX1).autopgen.bin \
	$(PREFIX2).autopgen.bin \
	$(PREFIX1).prob $(PREFIX2).prob \
	$(PREFIX1).t1x.bin $(PREFIX1).t2x.bin $(PREFIX1).antaux_t2x.bin $(PREFIX1).t3x.bin \
	$(PREFIX2).t1x.bin  $(PREFIX2).genitive.t1x.bin  $(PREFIX2).antaux_t2x.bin $(PREFIX2).t2x.bin $(PREFIX2).t3x.bin \
	$(PREFIX1).mode $(PREFIX2).mode

#	$(INSTALL_DATA) $(PREFIX1).mode $(apertium_eo_modesdir)

install-data-local:
	apertium-gen-modes modes.xml apertium-$(PREFIX1)
	$(INSTALL_DATA) $(PREFIX1).mode $(apertium_eo_modesdir)
	$(INSTALL_DATA) $(PREFIX2).mode $(apertium_eo_modesdir)
	$(INSTALL_DATA) $(BASENAME).$(PREFIX1).t1x $(BASENAME).$(PREFIX1).t2x $(apertium_eo_endir)
	$(INSTALL_DATA) $(BASENAME).$(PREFIX1).t3x $(BASENAME).$(PREFIX2).t1x $(apertium_eo_endir)
	$(INSTALL_DATA) $(BASENAME).$(PREFIX2).genitive.t1x  $(BASENAME).$(PREFIX2).antaux_t2x $(BASENAME).$(PREFIX2).t2x $(BASENAME).$(PREFIX2).t3x $(apertium_eo_endir)

CLEANFILES = -rf $(TARGETS_COMMON) tmp1_$(BASENAME).$(LANG2).dix  $(TARGETS) modes *.mode *.bin apertium-eo-en.eo-multiwords.xml
