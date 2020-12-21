###############################################################################
## Makefile for Esperanto--English
###############################################################################

LANG1=eo
LANG2=en
PREFIX1=$(LANG1)-$(LANG2)
PREFIX2=$(LANG2)-$(LANG1)

BASENAME=apertium-$(PREFIX1)

TARGETS_COMMON = $(TARGETS_PREFIX1) $(TARGETS_PREFIX2)

TARGETS_PREFIX1 =                 \
        $(PREFIX1).automorf.bin   \
        $(PREFIX1).autobil.bin    \
        $(PREFIX1).autogen.bin    \
        $(PREFIX1).autopgen.bin   \
        $(PREFIX1).t1x.bin        \
        $(PREFIX1).t2x.bin        \
        $(PREFIX1).antaux_t2x.bin \
        $(PREFIX1).t3x.bin

# 8a de feb 2011 Jacob forprenis: $(PREFIX2).rlx.bin
TARGETS_PREFIX2 =                   \
        $(PREFIX2).automorf.bin     \
        $(PREFIX2).autobil.bin      \
        $(PREFIX2).autogen.bin      \
        $(PREFIX2).autopgen.bin     \
        $(PREFIX2).t1x.bin          \
        $(PREFIX2).genitive.t1x.bin \
        $(PREFIX2).antaux_t2x.bin   \
        $(PREFIX2).antaux2_t2x.bin  \
        $(PREFIX2).t2x.bin          \
        $(PREFIX2).t3x.bin

$(PREFIX1): $(TARGETS_PREFIX1)
$(PREFIX2): $(TARGETS_PREFIX2)

# This include defines goals for install-modes, .deps/.d and .mode files:
@ap_include@


###############################################################################
## Monolingual transducers
###############################################################################


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

$(PREFIX1).autogen.bin: $(BASENAME).$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).$(LANG2).dix
	lt-comp rl $(BASENAME).$(LANG2).dix $@

$(PREFIX1).autopgen.bin: $(BASENAME).post-$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).post-$(LANG2).dix
	lt-comp lr $(BASENAME).post-$(LANG2).dix $@

$(PREFIX2).automorf.bin: $(BASENAME).$(LANG2).dix
	apertium-validate-dictionary $(BASENAME).$(LANG2).dix
	lt-comp lr $(BASENAME).$(LANG2).dix $@ $(BASENAME).$(LANG2).acx

$(PREFIX2).autogen.bin: tmp1_$(BASENAME).$(LANG1).dix
	apertium-validate-dictionary tmp1_$(BASENAME).$(LANG1).dix
	lt-comp rl tmp1_$(BASENAME).$(LANG1).dix $@

$(PREFIX2).autopgen.bin: $(BASENAME).post-$(LANG1).dix
	apertium-validate-dictionary $(BASENAME).post-$(LANG1).dix
	lt-comp lr $(BASENAME).post-$(LANG1).dix $@

###############################################################################
## Bilingual dictionaries
###############################################################################

$(PREFIX1).autobil.bin: $(BASENAME).$(PREFIX1).dix
	apertium-validate-dictionary $(BASENAME).$(PREFIX1).dix
	lt-comp lr $(BASENAME).$(PREFIX1).dix $@

$(PREFIX2).autobil.bin: $(BASENAME).$(PREFIX1).dix
	apertium-validate-dictionary $(BASENAME).$(PREFIX1).dix
	lt-comp rl $(BASENAME).$(PREFIX1).dix $@

###############################################################################
## Disambiguation rules
###############################################################################

$(PREFIX2).rlx.bin: $(BASENAME).$(PREFIX2).rlx
	cg-comp $< $@


###############################################################################
## Structural transfer rules
###############################################################################

$(PREFIX1).t1x.bin: $(BASENAME).$(PREFIX1).t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX1).t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t1x $@
	@if [ "`which apertium-preprocess-transfer-bytecode-j`" == "" ]; then echo && echo "NOTE: lttoolbox-java (used for bytecode accelerated transfer) is missing" && echo "      Therefore the following will fail (but it's OK)" && echo; fi
	-apertium-preprocess-transfer-bytecode-j $(BASENAME).$(PREFIX1).t1x $(PREFIX1).t1x.class

$(PREFIX1).t2x.bin: $(BASENAME).$(PREFIX1).t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX1).t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t2x $@

$(PREFIX1).t3x.bin: $(BASENAME).$(PREFIX1).t3x
	apertium-validate-postchunk $(BASENAME).$(PREFIX1).t3x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).t3x $@

$(PREFIX2).t1x.bin: $(BASENAME).$(PREFIX2).t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX2).t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t1x $@
	@if [ "`which apertium-preprocess-transfer-bytecode-j`" == "" ]; then echo && echo "NOTE: lttoolbox-java (used for bytecode accelerated transfer) is missing" && echo "      Therefore the following will fail (but it's OK)" && echo; fi
	-apertium-preprocess-transfer-bytecode-j $(BASENAME).$(PREFIX2).t1x $(PREFIX2).t1x.class

$(PREFIX2).genitive.t1x.bin: $(BASENAME).$(PREFIX2).genitive.t1x
	apertium-validate-transfer $(BASENAME).$(PREFIX2).genitive.t1x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).genitive.t1x $@

$(PREFIX2).antaux_t2x.bin: $(BASENAME).$(PREFIX2).antaux_t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX2).antaux_t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).antaux_t2x $@

$(PREFIX2).antaux2_t2x.bin: $(BASENAME).$(PREFIX2).antaux2_t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX2).antaux2_t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).antaux2_t2x $@

$(PREFIX1).antaux_t2x.bin: $(BASENAME).$(PREFIX1).antaux_t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX1).antaux_t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX1).antaux_t2x $@

$(PREFIX2).t2x.bin: $(BASENAME).$(PREFIX2).t2x
	apertium-validate-interchunk $(BASENAME).$(PREFIX2).t2x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t2x $@

$(PREFIX2).t3x.bin: $(BASENAME).$(PREFIX2).t3x
	apertium-validate-postchunk $(BASENAME).$(PREFIX2).t3x
	apertium-preprocess-transfer $(BASENAME).$(PREFIX2).t3x $@

###############################################################################
## Distribution
###############################################################################

EXTRA_DIST=$(BASENAME).$(LANG1).dix.xml        \
           $(BASENAME).post-$(LANG1).dix       \
           $(BASENAME).post-$(LANG2).dix       \
           $(BASENAME).$(LANG2).dix            \
           $(BASENAME).$(LANG1).tsx            \
           $(BASENAME).$(PREFIX1).dix          \
           $(BASENAME).$(LANG2).tsx            \
           $(PREFIX1).prob                     \
           $(PREFIX2).prob                     \
           $(BASENAME).$(LANG2).acx            \
           $(BASENAME).$(PREFIX1).t1x          \
           $(BASENAME).$(PREFIX1).antaux_t2x   \
           $(BASENAME).$(PREFIX1).t2x          \
           $(BASENAME).$(PREFIX1).t3x          \
           $(BASENAME).$(PREFIX2).t1x          \
           $(BASENAME).$(PREFIX2).genitive.t1x \
           $(BASENAME).$(PREFIX2).antaux_t2x   \
           $(BASENAME).$(PREFIX2).antaux2_t2x  \
           $(BASENAME).$(PREFIX2).t2x          \
           $(BASENAME).$(PREFIX2).t3x          \
           $(BASENAME).$(PREFIX2).rlx          \
           multi-gen.sh                        \
           modes.xml


###############################################################################
## Installation
###############################################################################

apertium_eo_endir=$(prefix)/share/apertium/$(BASENAME)/

# Files which should be installed to $(prefix)/share/apertium/$(BASENAME)/:
apertium_eo_en_DATA=$(TARGETS_COMMON)       \
	$(BASENAME).$(PREFIX1).t1x          \
	$(BASENAME).$(PREFIX1).antaux_t2x   \
	$(BASENAME).$(PREFIX1).t2x          \
	$(BASENAME).$(PREFIX1).t3x          \
	$(BASENAME).$(PREFIX2).t1x          \
	$(BASENAME).$(PREFIX2).genitive.t1x \
	$(BASENAME).$(PREFIX2).antaux_t2x   \
	$(BASENAME).$(PREFIX2).antaux2_t2x  \
	$(BASENAME).$(PREFIX2).t2x          \
	$(BASENAME).$(PREFIX2).t3x          \
	$(PREFIX1).prob                     \
	$(PREFIX2).prob

# don't add these, or make will fail if bytecode compilation is not supported
#	$(PREFIX1).t1x.class
#	$(PREFIX2).t1x.class

# All modes are created by the same goal, listing several will just give problems with make -j2
noinst_DATA=modes/$(PREFIX1).mode

install-data-local: install-modes


###############################################################################
## Cleanup
###############################################################################

CLEANFILES = $(TARGETS_COMMON) tmp1_$(BASENAME).$(LANG2).dix apertium_eo_en_en_eo_t1x.java apertium_eo_en_eo_en_t1x.java
clean-local:
	-rm -rf .deps modes

###############################################################################
## Test
###############################################################################

# test: all
# 	echo "hundo" | apertium -d . eo-en | tee .test-mt
# 	@echo "dog" > .test-ref
# 	@diff .test-ref .test-mt
# 	echo "dog" | apertium -d . en-eo | tee .test-mt
# 	@echo "hundo" > .test-ref
# 	@diff .test-ref .test-mt
# 	@rm .test-ref .test-mt

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
