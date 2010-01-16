#!/bin/bash

PREF=/tmp/testvoc_eo-en

 lt-expand tmp1_apertium-eo-en.eo.dix |grep -v REGEXP | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f1 -d'%' > ${PREF}0.txt &

 lt-expand tmp1_apertium-eo-en.eo.dix |grep -v REGEXP | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee ${PREF}1.txt |
        apertium-pretransfer|
        apertium-transfer apertium-eo-en.eo-en.t1x  eo-en.t1x.bin  eo-en.autobil.bin |
        apertium-interchunk apertium-eo-en.eo-en.t2x  eo-en.t2x.bin |
        apertium-postchunk apertium-eo-en.eo-en.t3x  eo-en.t3x.bin  | tee ${PREF}2.txt |
        lt-proc -d eo-en.autogen.bin > ${PREF}3.txt

paste -d _  ${PREF}0.txt ${PREF}1.txt ${PREF}2.txt ${PREF}3.txt | sed 's/\^;<sent>\$//g' | sed 's/ \.//g' | sed 's/_/   -----> /g' > testvoc_eo-en.txt

grep '@' ${PREF}2.txt
grep '#' ${PREF}3.txt 
