outputFolder="/home/mustafa/workspace/MLHW3/src/Output/enron4/"
iteration=(50 100 150 200 250 300 350 400 450 500 550 600 650 700 750 800 850 900 950 1000)

for i in ${iteration[@]}
do
     outputFile=$outputFolder$i".txt"
     java Perceptron enron4train/train enron4test/test $i 0.01 0 > $outputFile
done

