#!/bin/bash

TESTNAME=Regression_tests

cp *.mode modes/

echo "Downloading test set $TESTNAME"

unset LC_ALL
mkdir .testcache > /dev/null 2>&1
cd .testcache
rm -f test_*
wget  -N http://wiki.apertium.org/wiki/English_and_Esperanto/$TESTNAME 2>&1 | grep "Remote file is newer" > /dev/null || echo $TESTNAME not changed on webserver

#echo "Preparing (shoulnt be necesaty if $TESTNAME not changed on webserver)"

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
	SL=`echo $LINE | cut -f2 -d')' | sed 's/<i>//g' | sed 's/<\/i>//g' | cut -f2 -d'*' | sed 's/→/@/g' | cut -f1 -d'@' | sed 's/(note:/@/g' | sed 's/_/ /g'`;
	TL=`echo $LINE | sed 's/(\w\w)//g' | sed 's/<i>//g' | cut -f2 -d'*' | sed 's/<\/i>_→/@/g' | cut -f2 -d'@' | sed 's/_/ /g'`;

	echo $SL. >> test_SL_$mode
	echo $TL. >> test_TL_$mode

	#TR=`echo $SL | apertium -d .. $mode`;
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
 	cat test_SL_$mode | nl -s : | apertium -d .. $mode > test_TR_$mode
	cat test_TL_$mode | nl -s : | diff -wi - test_TR_$mode
done


