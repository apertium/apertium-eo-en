#!/bin/bash

TESTNAME=Regression_tests

NODOWNLOAD=$1
#TEMPDIR=/tmp/testcache # `mktemp -d`;
TEMPDIR=testcache
DATADIR=`pwd`;

cp *.mode modes/

#[ $NODOWNLOAD =="" ] || echo "Downloading test set $TESTNAME"
#[ $NODOWNLOAD =="" ] && echo "NOT Downloading test set $TESTNAME"

unset LC_ALL
mkdir -p $TEMPDIR
cd $TEMPDIR;

REGEN=0

if [ "$NODOWNLOAD" == "" ]; then
	REGEN=1
	LANG=en wget -N http://wiki.apertium.org/wiki/English_and_Esperanto/$TESTNAME 2>&1 | cat > wgetlog.txt
        grep "Saving to:" wgetlog.txt || REGEN=0 && echo $TESTNAME not changed on webserver
else
	echo "NOT Downloading test set $TESTNAME"
fi


DIRS=`grep '<li>' $TESTNAME | cut -f2 -d'(' | cut -f1 -d')' | uniq`

if [ "$REGEN" == "1" ]; then
	rm -f test_*
	echo "Preparing (shouldn't be necessary if $TESTNAME not changed on webserver)"

LIST=`grep '<li>' $TESTNAME | sed 's/<.*li>//g' | sed 's/ /_/g'`;

for LINE in $LIST; do
	dir=`echo $LINE | cut -f2 -d'(' | cut -f1 -d')'`;

#	echo $LINE;
	SL=`echo $LINE | cut -f2 -d')' | sed 's/<i>//g' | sed 's/<\/i>//g' | sed 's/→/@/g' | cut -f1 -d'@' | sed 's/(note:/@/g' | sed 's/_/ /g'`;
	TL=`echo $LINE | sed 's/(\w\w)//g' | sed 's/<i>//g' | sed 's/<\/i>_→/@/g' | cut -f2 -d'@' | sed 's/_/ /g'`;

	echo $SL. >> test_SL_$dir
	echo $TL. >> test_TL_$dir
done
fi


echo "Translating using Apertium:" $DIRS

for dir in $DIRS; do
	if [ $dir = "en" ]; then
		mode="en-eo";
	else 
		mode="eo-en";
	fi
	echo $mode
 	cat test_SL_$dir | nl -s : | apertium -d $DATADIR $mode > testres_TL_$dir
	cat test_TL_$dir | nl -s : | diff -Naurwi - testres_TL_$dir


	cat test_TL_$dir | nl -s : | diff - testres_TL_$dir | grep -r '[<>]' > test_diff_$dir
	for i in `cut -f2 -d' ' test_diff_$dir | cut -f1 | uniq`; do echo  --- $i ---; cat test_SL_$dir | nl -s : | grep -r "$i :" ; grep -r "^. $i\W" test_diff_$dir; done

done


