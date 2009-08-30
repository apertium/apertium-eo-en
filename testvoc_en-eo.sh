#!/bin/bash

#lt-expand apertium-eo-en.en.dix | grep -e ':<:' -e '\w:\w' | sed 's/:<:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee tmp_testvoc1.txt |
 lt-expand apertium-eo-en.en.dix | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee tmp_testvoc1.txt |
        apertium-pretransfer|
        apertium-transfer apertium-eo-en.en-eo.t1x  en-eo.t1x.bin  en-eo.autobil.bin |
        apertium-interchunk apertium-eo-en.en-eo.t2x  en-eo.t2x.bin |
        apertium-postchunk apertium-eo-en.en-eo.t3x  en-eo.t3x.bin  | tee tmp_testvoc2.txt |
        lt-proc -d en-eo.autogen.bin > tmp_testvoc3.txt

paste -d _ tmp_testvoc1.txt tmp_testvoc2.txt tmp_testvoc3.txt | sed 's/\^;<sent>\$//g' | sed 's/ \.//g' | sed 's/_/   ---------> /g'

