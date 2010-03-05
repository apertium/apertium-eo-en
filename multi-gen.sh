#!/bin/bash

if [ $# -lt 1 ]; then
	echo 'Usage: sh multi-gen.sh <file>';
	exit;
fi

if [ ! -f $1 ]; then
	exit;	
fi

echo '  <section id="multiwords" type="standard">';
for word in `cat $1 | sed 's/ /<b\/>/g'`; do 
	echo '    <!-- '`echo $word | sed 's/<b\/>/ /g'`' -->';
	# Singular, nominative
	echo '    <e>';
	echo '      <p>';
	echo '        <l>'$word'</l>';
	echo '        <r>'$word'<s n="n"/><s n="sg"/><s n="nom"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Singular, accusative
	echo '    <e>';
	echo '      <p>';
	echo '        <l>'`echo $word | sed 's/<b\/>/n<b\/>/g' | sed 's/$/n/g'`'</l>';
	echo '        <r>'$word'<s n="n"/><s n="sg"/><s n="acc"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Plural, nominative
	echo '    <e>';
	echo '      <p>';
	echo '        <l>'`echo $word | sed 's/<b\/>/j<b\/>/g' | sed 's/$/j/g'`'</l>';
	echo '        <r>'$word'<s n="n"/><s n="pl"/><s n="nom"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Plural, accusative
	echo '    <e>';
	echo '      <p>';
	echo '        <l>'`echo $word | sed 's/<b\/>/jn<b\/>/g' | sed 's/$/jn/g'`'</l>';
	echo '        <r>'$word'<s n="n"/><s n="pl"/><s n="acc"/></r>';
	echo '      </p>';
	echo '    </e>';





	echo '    <!-- adjektiva formo de '`echo $word | sed 's/<b\/>/ /g'`' -->';
	word2=`echo $word | sed 's/a<b\/>/-/' | sed 's/o$/a/'` ;
	# Singular, nominative
	echo '    <e r="RL">';
	echo '      <p>';
	echo '        <l>'$word2'</l>';
	echo '        <r>'$word'<s n="adj"/><s n="sg"/><s n="nom"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Singular, accusative
	echo '    <e r="RL">';
	echo '      <p>';
	echo '        <l>'`echo $word2 | sed 's/<b\/>/n<b\/>/g' | sed 's/$/n/g'`'</l>';
	echo '        <r>'$word'<s n="adj"/><s n="sg"/><s n="acc"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Plural, nominative
	echo '    <e r="RL">';
	echo '      <p>';
	echo '        <l>'`echo $word2 | sed 's/<b\/>/j<b\/>/g' | sed 's/$/j/g'`'</l>';
	echo '        <r>'$word'<s n="adj"/><s n="pl"/><s n="nom"/></r>';
	echo '      </p>';
	echo '    </e>';

	# Plural, accusative
	echo '    <e r="RL">';
	echo '      <p>';
	echo '        <l>'`echo $word2 | sed 's/<b\/>/jn<b\/>/g' | sed 's/$/jn/g'`'</l>';
	echo '        <r>'$word'<s n="adj"/><s n="pl"/><s n="acc"/></r>';
	echo '      </p>';
	echo '    </e>';

done
echo '  </section>';
