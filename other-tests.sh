#!/bin/bash

if [ "$1" == "" ]; then
echo "Checking outstanding tests..."
LIST=`
wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Outstanding_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed -r 's/[ \t]+/_/g'
`;
else
echo "Checking ALL tests..."
LIST=`
wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Outstanding_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed -r 's/[ \t]+/_/g'
wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Proposed_future_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed -r 's/[ \t]+/_/g'
wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Rejected_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed -r 's/[ \t]+/_/g'
wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Regression_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed -r 's/[ \t]+/_/g'
`;
fi


#LIST=`wget -O - -q http://wiki.apertium.org/wiki/English_and_Esperanto/Outstanding_tests | grep -E '((<li>.*→)|<h2>)' | sed 's/<.*li>//g' | sed 's/ /_/g'`;

cp *.mode modes/

for LINE in $LIST; do
	dir=`echo $LINE | cut -f2 -d'(' | cut -f1 -d')'`;

	if [ $dir = "en" ]; then
		mode="en-eo";
	elif [ $dir = "eo" ]; then
		mode="eo-en";
	else 
		echo ==`echo $LINE | sed 's/^.*_<span_class="mw-headline">//g' | sed 's/<\/span>.*//g' | sed 's/_/ /g'`==
		continue;
	fi

#	echo $LINE;
	SL=`echo $LINE | cut -f2 -d')' | sed 's/<i>//g' | sed 's/<\/i>//g' | cut -f2 -d'*' | sed 's/→/@/g' | cut -f1 -d'@' | sed 's/(note:/@/g' | sed 's/_/ /g'`;
	TL=`echo $LINE | sed 's/(\w\w)//g' | sed 's/<i>//g' | cut -f2 -d'*' | sed 's/<\/i>_→/@/g' | cut -f2 -d'@' | sed 's/_/ /g'`;

	TR=`echo $SL | apertium -d . $mode`;

	if [[ `echo $TR | tr '[A-Z]' '[a-z]'` != `echo $TL | tr '[A-Z]' '[a-z]'` ]]; then 
		echo -e $mode"\t "$SL"\n\t-$TL\n\t+ "$TR"\n";
	else
		echo -e $mode"\t "$SL"\nWORKS!\t $TL\n";
	fi

done
