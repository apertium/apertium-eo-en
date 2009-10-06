
echo
echo ./testvoc_en-eo.sh 
./testvoc_en-eo.sh 

echo
echo ./testvoc_eo-en.sh 
./testvoc_eo-en.sh 

if [ -e corpa/en.crp.txt ]
then
	cd corpa
	./elpaki.sh
	cd ..
fi

echo
echo ./testcorpus_en-eo.sh 
./testcorpus_en-eo.sh 

echo
echo ./testcorpus_eo-en.sh 
./testcorpus_eo-en.sh 

