grep 'o</i><par n="nommf__n"/>' apertium-eo-en.eo.dix.xml | grep -v 'oo"' && echo Ne metu o post radiko && echo
grep 'a</i><par n="adj__adj"/>' apertium-eo-en.eo.dix.xml && echo Ne metu a post radiko && echo
grep 'i</i><par n="verb__vblex"/>' apertium-eo-en.eo.dix.xml | grep -v 'ii"' && echo Ne metu i post radiko && echo
grep '<par n="komence__adv"/>' apertium-eo-en.eo.dix.xml | grep -v 'e</i><par ' && echo Metu e post radiko?? && echo
grep '<par n="nom__n"/>' apertium-eo-en.eo.dix.xml | grep -v 'o</i><par ' && echo Metu o post radiko && echo
