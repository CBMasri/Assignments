for i in {1..4} ; do
	echo "---------- ERROR TEST $i ----------"
	cat ./error$i.txt | ./sengfmt3
done

echo "---------- ERROR TEST 5 ----------"
./sengfmt3 banana.pickle

echo "---------- ERROR TEST 6 ----------"
./sengfmt3
