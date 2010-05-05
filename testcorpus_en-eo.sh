#!/bin/bash

TMP=/tmp/en-eo
CRP=corpa/en.crp.txt
mkdir -p testcache
NOVA=testcache/corpus_en-eo_nova_traduko.txt
ORIGINA=testcache/corpus_en-eo_origina_traduko.txt

if [ ! -e $CRP ]
then
	echo Elpakas $CRP
	bunzip2 -c corpa/enwiki.crp.txt.bz2 > $CRP
fi

#make -s -j 3 && cat $CRP | apertium -d . en-eo > $NOVA || exit
#make -s -j 3 && cat $CRP | apertium -d . en-eo-bytecode > $NOVA || exit
cat $CRP | apertium -d . en-eo-bytecode-cg > $NOVA || exit
echo
grep '#' $NOVA && echo -e "^ Estis mankoj en la cellingva dix, montrata supre ^\n"
grep '@' $NOVA && echo -e "^ Estis mankoj en la dulingva dix, montrata supre ^\n"

if [ -e $ORIGINA ]
then
	diff -w $ORIGINA $NOVA | grep -r '[<>]' > $TMP-crpdiff.txt && for i in `cut -c3-8 $TMP-crpdiff.txt | sort -un`; do echo  --- $i ---; grep -r "^ *$i\." $CRP; grep -r "^. *$i\." $TMP-crpdiff.txt; done > testcorpus_en-eo.txt
	echo "Estis `cut -c3-8 $TMP-crpdiff.txt | sort -un | wc -l` diferenco(j) - rigardu en: testcorpus_en-eo.txt"
	echo
fi

echo "Se vi volas uzi la nunan version por kompari venontajn versiojn, voku nun:"
echo "mv $NOVA $ORIGINA"
echo

