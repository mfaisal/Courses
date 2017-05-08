outputFolder="/home/mustafa/workspace/MLHW3/src/Output/enron4/MLP/Iteration/"

trainingfile="/home/mustafa/workspace/MLHW3/Arff/enron4/train.arff"
testfile="/home/mustafa/workspace/MLHW3/Arff/enron4/test.arff"

iteration=(600 700 800 900 1000)

for i in ${iteration[@]}
do
     outputFile=$outputFolder$i".txt"
     java -cp weka.jar weka.classifiers.functions.MultilayerPerceptron -L 0.01 -M 0.01 -N $i -V 0 -S 0 -E 20 -H 2 -t $trainingfile -T $testfile > $outputFile
done

