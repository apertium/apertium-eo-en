#!/bin/sh
# http://wiki.apertium.org/wiki/Asturian#Calculating_coverage


# Example use:
# zcat corpa/en.crp.txt.gz | sh corpus-stat.sh


#CMD="cut -c9- en.crp.txt"
#CMD="cat"
bunzip2 -c eowiki.crp.txt.bz2_granda > /tmp/xxeo; CMD="cut -c9- /tmp/xxeo"

F=/tmp/corpus-stat-res-eo.txt
F2=/tmp/corpus-stat-res-eo2.txt

# Calculate the number of tokenised words in the corpus:
# for some reason putting the newline in directly doesn't work, so two seds

# Sen kunmetitaj vortoj
$CMD | apertium-destxt | lt-proc-j ../eo-en.automorf.bin |apertium-retxt | sed 's/\$[^^]*\^/$^/g' | sed 's/\$\^/$\
^/g' > $F

# Kun kunmetitaj vortoj:
$CMD | apertium-destxt | lt-proc-j -e ../eo-en.automorf.bin |apertium-retxt | sed 's/\$[^^]*\^/$^/g' | sed 's/\$\^/$\
^/g' > $F2

NUMWORDS=`cat $F | wc -l`
NUMWORDS2=`cat $F2 | wc -l`
echo "Number of tokenised words in the corpus: $NUMWORDS ($NUMWORDS2 kun kunmetaĵoj)"



# Calculate the number of words that are not unknown

NUMKNOWNWORDS=`cat $F | grep -v '\*' | wc -l`
NUMKNOWNWORDS2=`cat $F2 | grep -v '\*' | wc -l`
echo "Number of known words in the corpus: $NUMKNOWNWORDS  ($NUMKNOWNWORDS2 kun kunmetaĵoj)"


# Calculate the coverage

COVERAGE=`calc "round($NUMKNOWNWORDS/$NUMWORDS*1000)/10"`
COVERAGE2=`calc "round($NUMKNOWNWORDS2/$NUMWORDS2*1000)/10"`
echo "Coverage: $COVERAGE ($COVERAGE2 kun kunmetaĵoj)"


# Show the top-ten unknown words

cat $F | grep '\*' | sort -f | uniq -c | sort -gr > ${F}nekon
cat $F2 | grep '\*' | sort -f | uniq -c | sort -gr > ${F2}nekon

echo "Top unknown words in the corpus:"
diff --side-by-side ${F}nekon ${F2}nekon | head -50


echo "Vortoj konata pro kunmetaĵa modulo"
cat $F | grep '\*' | cut -d ^ -f 2 | cut -d / -f 1   | apertium-destxt| lt-proc-j -e ../eo-en.automorf.bin | grep -v '*' | apertium-retxt > ${F}rekon
cat ${F}rekon | cut -d ^ -f 2 | cut -d / -f 1  | sed -e 's/$/./g'  | apertium -d .. eo-en-compounds > ${F}rekon2
paste ${F}rekon2 ${F}rekon | sort -f | uniq -c | sort -gr > ${F}_rekonitaj_kunmetaĵoj.txt
echo "Rigardu: ${F}_rekonitaj_kunmetaĵoj.txt"

echo rm -f $F $F2 ${F}nekon ${F2}rekon ${F}rekon2
