#!/bin/bash

 lt-expand apertium-eo-en.eo.dixtmp1 | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f1 -d'%' > tmp_testvoc0.txt &

 lt-expand apertium-eo-en.eo.dixtmp1 | grep -e ':>:' -e '\w:\w' | sed 's/:>:/%/g' | sed 's/:/%/g' | cut -f2 -d'%' |  sed 's/^/^/g' | sed 's/$/$ ^;<sent>$/g' | tee tmp_testvoc1.txt |
        apertium-pretransfer|
        apertium-transfer apertium-eo-en.eo-en.t1x  eo-en.t1x.bin  eo-en.autobil.bin |
        apertium-interchunk apertium-eo-en.eo-en.t2x  eo-en.t2x.bin |
        apertium-postchunk apertium-eo-en.eo-en.t3x  eo-en.t3x.bin  | tee tmp_testvoc2.txt |
        lt-proc -d eo-en.autogen.bin > tmp_testvoc3.txt

paste -d _ tmp_testvoc0.txt tmp_testvoc1.txt tmp_testvoc2.txt tmp_testvoc3.txt | sed 's/\^;<sent>\$//g' | sed 's/ \.//g' | sed 's/_/   -----> /g'

