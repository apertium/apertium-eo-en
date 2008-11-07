#!/bin/bash

TESTNAME=Regression_tests

NODOWNLOAD=$1
TEMPDIR=/tmp/testcache # `mktemp -d`;
DATADIR=`pwd`;

cp *.mode modes/

#[ $NODOWNLOAD =="" ] || echo "Downloading test set $TESTNAME"
#[ $NODOWNLOAD =="" ] && echo "NOT Downloading test set $TESTNAME"

unset LC_ALL
mkdir -p $TEMPDIR
cd $TEMPDIR;
rm -f test_*


if [ "$NODOWNLOAD" == "" ]; then
	wget  -N http://wiki.apertium.org/wiki/English_and_Esperanto/$TESTNAME 2>&1 | grep "Remote file is newer" > /dev/null || echo $TESTNAME not changed on webserver
else
	echo "NOT Downloading test set $TESTNAME"
fi



#echo "Preparing (shouldn't be necessary if $TESTNAME not changed on webserver)"

LIST=`cat $TESTNAME | grep '<li>' | sed 's/<.*li>//g' | sed 's/ /_/g'`;
MODES=

for LINE in $LIST; do
	dir=`echo $LINE | cut -f2 -d'(' | cut -f1 -d')'`;

	if [ $dir = "en" ]; then
		mode="en-eo";
	elif [ $dir = "eo" ]; then
		mode="eo-en";
	else 
		continue;
	fi
	MODES=`echo $MODES $mode`

#	echo $LINE;
	SL=`echo $LINE | cut -f2 -d')' | sed 's/<i>//g' | sed 's/<\/i>//g' | sed 's/→/@/g' | cut -f1 -d'@' | sed 's/(note:/@/g' | sed 's/_/ /g'`;
	TL=`echo $LINE | sed 's/(\w\w)//g' | sed 's/<i>//g' | sed 's/<\/i>_→/@/g' | cut -f2 -d'@' | sed 's/_/ /g'`;

	echo $SL. >> test_SL_$mode
	echo $TL. >> test_TL_$mode

	#TR=`echo $SL | apertium -d $DATADIR $mode`;
	#echo $TR. >> test_TRx_$mode

	#if [[ `echo $TR | tr '[A-Z]' '[a-z]'` != `echo $TL | tr '[A-Z]' '[a-z]'` ]]; then 
	#	echo -e $mode"\t "$SL"\n\t-$TL\n\t+ "$TR"\n";
	#fi

done

echo "Translating using Apertium"
#echo $MODES
MODES=`for i in $MODES; do echo $i; done | uniq`
#echo MODES = $MODES

for mode in $MODES; do
	echo $mode
 	cat test_SL_$mode | nl -s : | apertium -d $DATADIR $mode > test_TR_$mode
	cat test_TL_$mode | nl -s : | diff -Naurwi - test_TR_$mode


	cat test_TL_$mode | nl -s : | diff - test_TR_$mode | grep -r '[<>]' > test_diff_$mode
	for i in `cut -f2 -d' ' test_diff_$mode | cut -f1 | uniq`; do echo  --- $i ---; cat test_SL_$mode | nl -s : | grep -r "$i :" ; grep -r "^. $i\W" test_diff_$mode; done

done


