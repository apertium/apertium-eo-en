#!/bin/bash

PREF=/tmp/testvoc_en-eo

#lt-expand apertium-eo-en.en.dix | grep -e ':<:' -e '\w:\w' | sed 's/:<:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee tmp_testvoc1.txt |
 lt-expand apertium-eo-en.en.dix |grep -v REGEXP | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee ${PREF}1.txt |
        apertium-pretransfer|
        apertium-transfer apertium-eo-en.en-eo.t1x  en-eo.t1x.bin  en-eo.autobil.bin |
        apertium-interchunk apertium-eo-en.en-eo.t2x  en-eo.t2x.bin |
        apertium-postchunk apertium-eo-en.en-eo.t3x  en-eo.t3x.bin  | tee  ${PREF}2.txt |
        lt-proc -d en-eo.autogen.bin >  ${PREF}3.txt
#        lt-proc -d profiler/en-eo.autogen.bin | apertium-dixtools profilecollect dixtools-profiledata.en-eo.eo.txt > tmp_testvoc3.txt

paste -d _  ${PREF}1.txt  ${PREF}2.txt  ${PREF}3.txt | sed 's/\^;<sent>\$//g' | sed 's/ \.//g' | sed 's/_/   ---------> /g' > testvoc_en-eo.txt

grep '@' ${PREF}2.txt
grep '#' ${PREF}3.txt 
