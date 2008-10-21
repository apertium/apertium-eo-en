grep 'o</i><par n="nommf__n"/>' apertium-eo-en.eo.dix.xml | grep -v 'oo"' && echo Ne metu o post radiko && echo
grep 'a</i><par n="adj__adj"/>' apertium-eo-en.eo.dix.xml && echo Ne metu a post radiko && echo
grep 'i</i><par n="verb__vblex"/>' apertium-eo-en.eo.dix.xml | grep -v 'ii"' && echo Ne metu i post radiko && echo
grep '<par n="nom__n"/>' apertium-eo-en.eo.dix.xml | grep -v 'o</i><par ' && echo Metu o post radiko && echo
grep '<b/>' apertium-eo-en.eo.dix.xml | grep nom__n && echo Movu al apertium-eo-en.eo-multiwords.txt

grep '<s n="ant"/>' apertium-eo-en.eo-en.dix | grep '<s n="top"/>' && echo '<s n="top"/> ne sxangxu al <s n="ant"/> aux inverse'

#grep '<par n="Barcelona__np"/>' apertium-eo-en.eo.dix.xml | grep -v 'o</i><par ' && echo Metu o post radiko && echo
#grep '<par n="komence__adv"/>' apertium-eo-en.eo.dix.xml | grep -v 'e</i><par ' && echo Metu e post radiko?? && echo


cat apertium-eo-en.eo.dix.xml | grep '<par n="nommf__n"/>' | cut -f2 -d'"'  | sort -u  > /tmp/esperanto_nouns_with_gender_eodix.txt
diff /tmp/esperanto_nouns_with_gender_eodix.txt res/esperanto_nouns_with_gender.txt || echo Problemo de genro en eo.dix

cat apertium-eo-en.eo-en.dix | grep 'n="MF_GD"' | cut -f4 -d'>' | cut -f1 -d'<'  | sort -u  > /tmp/esperanto_nouns_with_gender_bidix.txt
#diff /tmp/esperanto_nouns_with_gender_bidix.txt res/esperanto_nouns_with_gender.txt || echo Problemo de genro en eo-en.dix
