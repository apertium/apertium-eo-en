#!/bin/sh
# http://wiki.apertium.org/wiki/Asturian#Calculating_coverage


# Example use:
# zcat corpa/en.crp.txt.gz | sh corpus-stat.sh


#CMD="cut -c9- en.crp.txt"
#CMD="cat"
bunzip2 -c eowiki.crp.txt.bz2 > /tmp/xxeo; CMD="cut -c9- /tmp/xxeo"

F=/tmp/corpus-stat-res-eo.txt

# Calculate the number of tokenised words in the corpus:
# for some reason putting the newline in directly doesn't work, so two seds

# Sen kunmetitaj vortoj
$CMD | apertium-destxt | lt-proc ../eo-en.automorf.bin |apertium-retxt | sed 's/\$[^^]*\^/$^/g' | sed 's/\$\^/$\
^/g' > $F
# Kun kunmetitaj vortoj:
# $CMD | apertium-destxt | lt-proc-j -e ../eo-en.automorf.bin |apertium-retxt | sed 's/\$[^^]*\^/$^/g' | sed 's/\$\^/$\
#^/g' > $F

NUMWORDS=`cat $F | wc -l`
echo "Number of tokenised words in the corpus: $NUMWORDS"



# Calculate the number of words that are not unknown

NUMKNOWNWORDS=`cat $F | grep -v '\*' | wc -l`
echo "Number of known words in the corpus: $NUMKNOWNWORDS"


# Calculate the coverage

COVERAGE=`calc "round($NUMKNOWNWORDS/$NUMWORDS*1000)/10"`
echo "Coverage: $COVERAGE %"


# Show the top-ten unknown words.

echo "Top unknown words in the corpus:"
cat $F | grep '\*' | sort -f | uniq -c | sort -gr | head -50

rm -f $F
