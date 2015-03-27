for i in src/test/resources/proto/*.txt
do
echo "Running on $i"
./kbuild.sh $i
done
