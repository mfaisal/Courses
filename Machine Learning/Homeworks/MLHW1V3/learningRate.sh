outputFolder="/home/mustafa/workspace/MLHW3/src/Output/enron4/learningRate/"
iteration=(0 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0 )

for i in ${iteration[@]}
do
     outputFile=$outputFolder$i".txt"
     java Perceptron enron4train/train enron4test/test 200 $i 0 > $outputFile
done
