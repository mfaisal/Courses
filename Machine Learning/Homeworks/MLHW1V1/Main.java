import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author mustafa
 *
 */
public class Main {
	
	public static ArrayList<String> featureNames = new ArrayList<String>();
	public static Node dTree=null;
	static Map<Integer,ArrayList<Integer>> t_data_list = new HashMap<Integer,ArrayList<Integer>>();
	
	public  static void main(String[] args){
		
		if(args.length!=6){
			System.out.println("Invalid Input!!");
		}
		else
		{
			int L = Integer.parseInt(args[0].trim()); 
			int K = Integer.parseInt(args[1].trim());
			
			String t_d_set=args[2].trim(); // csv for training data set.
			String val_d_set=args[3].trim();  // csv for validation data set.
			String test_d_set = args[4].trim(); // csv for test data set.
			
			String printTree = args[5].trim(); //yes or other
			
			try {
				readFile(t_d_set);
				ArrayList<Integer> exampleIndices = new ArrayList<Integer>();
				
				for(int index:t_data_list.keySet())
				{
					exampleIndices.add(index);
				}
				
				ArrayList<String> candidateList = new ArrayList<String>();
				for(String nName: featureNames){
					candidateList.add(nName);
				}
				candidateList.remove("Class");
				
				Node InfoGainDt = new Node();
				InfoGainDt.setNodeName("root");
				
				createDecisionTree(candidateList,InfoGainDt,exampleIndices,true,0,1);
				
				
				ArrayList<String> candidateList1 = new ArrayList<String>();
				for(String nName: featureNames){
					candidateList1.add(nName);
				}
				candidateList1.remove("Class");
				
				Node varianceGainDt = new Node();
				varianceGainDt.setNodeName("root");
				createDecisionTree(candidateList1,varianceGainDt,exampleIndices,true,0,0);
				
				
				t_data_list = new HashMap<Integer,ArrayList<Integer>>();
				readFile(val_d_set);
				
				Node infoPrunNode = new Node();
				Node infoPNdCpy = new Node();
				infoPNdCpy = InfoGainDt.copy();
				infoPrunNode = postPruning(infoPNdCpy, L, K);
				
				
				Node variancePruneNode = new Node();
				Node varPNdCpy = new Node();
				varPNdCpy = varianceGainDt.copy();
				variancePruneNode = postPruning(varPNdCpy, L, K);
				
				
				t_data_list = new HashMap<Integer,ArrayList<Integer>>();
				readFile(test_d_set);
				
				System.out.println("============Accuracy Information=====================");
				System.out.println("Accuracy for Information Gain: " + calculateAccuracy(InfoGainDt));
				System.out.println("Accuracy for Variance Impurity: " + calculateAccuracy(varianceGainDt));
				System.out.println("Accuracy for Information Gain with pruned tree: " + calculateAccuracy(infoPrunNode));
				System.out.println("Accuracy for Variance Impurity with pruned tree: " + calculateAccuracy(variancePruneNode));
				
				System.out.println("============Print Tree==============================================================================");
				if(printTree.equals("yes")){
					System.out.println("**********************Decision Tree for Information Gain***************");
					printDTree(InfoGainDt);
					System.out.println(String.format("**********************Pruned Decision Tree for Information Gain(%d,%d)***************",L,K));
					printDTree(infoPrunNode);
					System.out.println("**********************Decision Tree for Variance Impurity***************");
					printDTree(varianceGainDt);
					System.out.println(String.format("**********************Pruned Decision Tree for Variance Impurity(%d,%d)***************",L,K));
					printDTree(variancePruneNode);
				}
				
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(t_d_set);
		}
	}
	
	/**
	 * Filled the array list with the non-leaf node indices.
	 * @param node
	 * @param indices
	 */
	static void getNodeIndices(Node node, ArrayList<Integer> indices){
		if(node!=null){
			indices.add(node.getNodeIndex());
			
			if(node.getlChild()!=null){
				getNodeIndices(node.getlChild(), indices);
			}
			if(node.getrChild()!=null){
				getNodeIndices(node.getrChild(), indices);
			}
		}
	}
	/**
	 * This method run pruning algorithm over given decision tree, nd
	 * @param nd
	 * @param L
	 * @param K
	 * @return Pruned decision tree.
	 */
	static Node postPruning(Node nd, int L, int K){
		ArrayList<Integer> nodesIndices =null;
		
		
		Node best = new Node(); 
		best = nd.copy(); // copy D to D_best
		
		
		
		double accbest = -1; //initializing the accuracy
		
		Random rnd = new Random();
		
		
		
		for(int i=1;i<=L;i++){
			Node cpNd = new Node(); //
			cpNd = nd.copy(); //copy D to a new D'
			
			int M = rnd.nextInt(K)+1;
			
			for(int j=1;j<=M;j++){
				if(cpNd!=null){
					nodesIndices = new ArrayList<Integer>(); //contain non-leaf nodes' indices
					getNodeIndices(cpNd,nodesIndices);
					int P = rnd.nextInt((nodesIndices.size()));
					removeNode(cpNd, nodesIndices.get(P));
				}
			}
			
			double accCurrent = calculateAccuracy(cpNd);
			if(accCurrent>accbest){
				best = new Node();
				best = cpNd.copy(); // updating D_best
				accbest = accCurrent; // updating accuracy
			}
			
		}
		
		return best;
	}
	
	/**
	 * This method remove or delete a non-leaf node, index is given, from decision tree, dt 
	 * @param dt
	 * @param index
	 */
	private static void removeNode(Node dt, int index){
		if(dt!=null){
			if(dt.getNodeIndex()==index){
				
				if(dt.getpNode()==null)
					return;
				
				if(dt.getpNode().getlChild()!=null){
					if(dt.getpNode().getlChild().getNodeIndex()==index){
						dt.getpNode().setlChild(null);
						
						if(dt.getpExZero()>=dt.getpExOne()){
							dt.getpNode().setlClass(0);
						}else{
							dt.getpNode().setlClass(1);
						}
					}
				}
				if(dt.getpNode().getrChild()!=null){
					if(dt.getpNode().getrChild().getNodeIndex()==index){
					
						dt.getpNode().setrChild(null);
						if(dt.getpExZero()>=dt.getpExOne()){
							dt.getpNode().setrClass(0);
						}else{
							dt.getpNode().setrClass(1);
						}
					}
				}
			}
			
			if(dt.getlChild()!=null){
				removeNode(dt.getlChild(),index);
			}
			if(dt.getrChild()!=null){
				removeNode(dt.getrChild(), index);
			}
		}
	}
	/**
	 * 
	 * @param dt
	 * @return
	 */
	private static double calculateAccuracy(Node dt){
		ArrayList<Integer> predictions = new ArrayList<Integer>();
		for(ArrayList<Integer> instance:t_data_list.values()){
			predictions.add(predict(instance,dt));
		}
		int counter=1;
		double correct=1;
		for(int pre:predictions){
			if(pre==t_data_list.get(counter).get(featureNames.size()-1)){
				correct++;
			}
			counter++;
		}
		
		double totalInstances = (double)t_data_list.size();
		double accuracy = correct/totalInstances;
		
		return accuracy*100;
	}
	/**
	 * This method classifies each instance or example with decision tree, dt
	 * @param instance
	 * @param dt
	 * @return predicted class 1 or 0.
	 */
	private static int predict(ArrayList<Integer> instance, Node dt){
		
		if(instance.get(dt.getNodeIndex())==dt.getlClass()){
			return 0;
		}
		else if(instance.get(dt.getNodeIndex())==dt.getrClass()){
			return 1;
		}else if(dt.getlChild()!=null){
			return predict(instance,dt.getlChild());	
		}else if(dt.getrChild()!=null){
			return predict(instance,dt.getrChild());
		}
		
		return -1;
	}
	
/**
 * Read the CSV file with given name
 * @param t_d_set
 * @throws FileNotFoundException
 * @throws IOException
 */
	private static void readFile(String t_d_set) throws FileNotFoundException,
			IOException {
		BufferedReader bReader = new BufferedReader(new FileReader(t_d_set));
		String line = "";
		int l_counter = 1;
		
		while((line=bReader.readLine())!=null){
			
			String[] fElements = line.split(",");
			
			if(l_counter==1 && featureNames.size()==0){  // storing features' or attributes' name
				
				for(String fN:fElements){
					featureNames.add(fN);
				}
			}
			else if(l_counter>1){
				ArrayList<Integer> l_data = new ArrayList<Integer>();
				for(String fN:fElements){
					int val = Integer.parseInt(fN);
					l_data.add(val);
				}
				t_data_list.put((l_counter-1), l_data);
			}
			
			l_counter++; //line counter
		}
		
		bReader.close();
	}
	/**
	 * Print the decision tree
	 * @param root
	 */
	static void printDTree(Node root){
		
		for(int i=1;i<=root.getLevel();i++){
			System.out.print("|  ");
		}
		
		if(root.getlChild()!=null){
			System.out.println(root.getNodeName()+" = 0 : \n" );
			printDTree(root.getlChild());
		}else{
			System.out.println(root.getNodeName() + " = 0: " + root.getlClass() + "\n");
		}
		
		for(int i=1;i<=root.getLevel();i++){
			System.out.print("|  ");
		}
		
		if(root.getrChild()!=null){
			System.out.println(root.getNodeName()+" = 1 : \n" );
			printDTree(root.getrChild());
		}else{
			System.out.println(root.getNodeName() + " = 1: " + root.getrClass() + "\n");
		}
		
	}
	
	
	
	/**
	 * Calculate entropy for given probabilities.
	 * @param probs
	 * @return entropy
	 */
	static double entropyCalc(double[] probs){
		double entropy =0;
		for(double prb:probs){
			if(prb!=0){
				entropy += prb*log(1/prb);
			}else{
				entropy +=0;
			}
				
		}
		return entropy;
	}
	/**
	 * Convert entropy to base 2 logarithm.
	 * @param x
	 * @return double, base 2 logarithm.
	 */
	static double log(double x)
	{
	    return (Math.log(x) / Math.log(2));
	}
	
	/**
	 * Calculate variance for given probabilities.
	 * @param probs
	 * @return double, variance.
	 */
	static double getVariance(double[] probs){
		double mul=1;
		for(double prb:probs){
			mul*=prb;
		}
		return mul;
	}
	
	/**
	 * Calculate probabilities of binary feature values for specific feature from given examples. 
	 * @param featureIndex
	 * @param exampleIndices
	 * @return array of probabilities for various feature values.
	 */
	static double[] probabilitiesCalc(int featureIndex,ArrayList<Integer>exampleIndices){
		double[] counter = new double[2];
		
		for(int exIndex:exampleIndices){
			if(t_data_list.get(exIndex).get(featureIndex)==1)
				counter[1]++;
			else
				counter[0]++;
		}
		counter[1] = counter[1]/exampleIndices.size();
		counter[0] = counter[0]/exampleIndices.size();
		
		return counter;
	}
	/**
	 * Provide array list of indices for specific feature value of given feature index.
	 * @param featureIndex
	 * @param featureVal
	 * @return array list of indices.
	 */
	static ArrayList<Integer> getIndices(int featureIndex, int featureVal){
		ArrayList<Integer> fValIndices = new ArrayList<Integer>();
		
		for(int ind:t_data_list.keySet()){
			if(t_data_list.get(ind).get(featureIndex)==featureVal){
				fValIndices.add(ind);
			}
		}
		
		return fValIndices;
	}
	/**
	 * Generate an array list for given feature index with specific feature value for example indices.
	 * @param featureIndex
	 * @param featureVal
	 * @param exampleIndices
	 * @return array list of feature value indices.
	 */
	static ArrayList<Integer> getFeatureValIndices(int featureIndex, int featureVal, ArrayList<Integer> exampleIndices){
		ArrayList<Integer> fValIndices = new ArrayList<Integer>();
		
		for(int ind:exampleIndices){
			if(t_data_list.get(ind).get(featureIndex)==featureVal){
				fValIndices.add(ind);
			}
		}
		
		return fValIndices;
	}
	
	
	/**
	 * Calculate variance impurity for given examples and feature index.
	 * @param classVariance
	 * @param exampleIndices
	 * @param featureIndex
	 * @return variance impurity.
	 */
	static double getVarianceGain(double classVariance, ArrayList<Integer> exampleIndices, int featureIndex){
		
		ArrayList<Integer> oneIndices = getFeatureValIndices(featureIndex,1,exampleIndices); //indices of examples with 1
		ArrayList<Integer> zeroIndices = getFeatureValIndices(featureIndex,0,exampleIndices); //indices of examples with 0
		
		double totalExamples = exampleIndices.size(); // total number of examples
		
		double oneVarianceFac = 0;
		if(oneIndices.size()!=0){
			double[] probsOne=probabilitiesCalc(featureNames.size()-1,oneIndices); //
			double varrianceOne=getVariance(probsOne);
			oneVarianceFac = ((double)oneIndices.size()/totalExamples)*varrianceOne;
		}
		
		double zeroVarianceFac =0;
		if(zeroIndices.size()!=0){
			double[] probsZero=probabilitiesCalc(featureNames.size()-1,zeroIndices);
			double varrianceZero=getVariance(probsZero);
			zeroVarianceFac = ((double)zeroIndices.size()/totalExamples)*varrianceZero;
		}
		
		double varianceGain = classVariance-(oneVarianceFac+zeroVarianceFac);
		
		return varianceGain;
	}
	
	
	/**
	 * Calculate information gain for given examples and feature index.
	 * @param classEntropy
	 * @param exampleIndices
	 * @param featureIndex
	 * @return double, information gain.
	 */
	static double getInformationGain(double classEntropy, ArrayList<Integer> exampleIndices, int featureIndex){
		
		ArrayList<Integer> oneIndices = getFeatureValIndices(featureIndex,1,exampleIndices); //indices of examples with 1
		ArrayList<Integer> zeroIndices = getFeatureValIndices(featureIndex,0,exampleIndices); //indices of examples with 0
		
		double totalExamples = exampleIndices.size(); // total number of examples
		
		double oneEntropFac = 0;
		if(oneIndices.size()!=0){
			double[] probsOne=probabilitiesCalc(featureNames.size()-1,oneIndices); //
			double entropyOne=entropyCalc(probsOne);
			oneEntropFac = ((double)oneIndices.size()/totalExamples)*entropyOne;
		}
		
		double zeroEntropFac =0;
		if(zeroIndices.size()!=0){
			double[] probsZero=probabilitiesCalc(featureNames.size()-1,zeroIndices);
			double entropyZero=entropyCalc(probsZero);
			zeroEntropFac = ((double)zeroIndices.size()/totalExamples)*entropyZero;
		}
		
		double infoGain = classEntropy-(oneEntropFac+zeroEntropFac);
		
		return infoGain;
	}
/**
 * Generate a decision tree using specified heuristic.
 * @param candiateList
 * @param targetNode
 * @param exampleIndices
 * @param isleft
 */
	static void createDecisionTree(ArrayList<String> candiateList,Node targetNode, ArrayList<Integer> exampleIndices,boolean isleft,int level, int gainMetric){
		
		if(candiateList.size()==0||exampleIndices.size()==0)
			return;
		double[] probs = probabilitiesCalc(featureNames.size()-1,exampleIndices);
		

		
		if(probs[1]==1){
			targetNode.setrClass(1);
			targetNode.setrChild(null);
			targetNode.setpExZero(0);
			targetNode.setpExOne(0);
			return;
		}
		else if(probs[0]==1){
			targetNode.setlClass(0);
			targetNode.setlChild(null);
			targetNode.setpExZero(0);
			targetNode.setpExOne(0);
			return;
		}
		else{
			 double classMetric=0;
			 if(gainMetric==1){
				 classMetric = entropyCalc(probabilitiesCalc(featureNames.size()-1,exampleIndices));
			 }else{
				 classMetric = getVariance(probabilitiesCalc(featureNames.size()-1,exampleIndices));
			 }
				 
			 double maxGain = -1;
			 String rNode="";
			
			 for(String nName:candiateList){
				 int i = featureNames.indexOf(nName);
				 double gain =0;
				 if(gainMetric==1){
					 gain = getInformationGain(classMetric,exampleIndices,i);
				 }else{
					 gain = getVarianceGain(classMetric,exampleIndices,i);
				 }
				 
				 if(maxGain<gain){
					 maxGain=gain;
					 rNode=nName;
				 }
			}
			
			
			int featureIndex = featureNames.indexOf(rNode);
			
			
			ArrayList<Integer> oneIndices = getFeatureValIndices(featureIndex,1,exampleIndices);
			ArrayList<Integer> zeroIndices = getFeatureValIndices(featureIndex,0,exampleIndices); 
			
			candiateList.remove(rNode);
			
			if(targetNode.getNodeName().equals("root")){
				targetNode.setNodeName(rNode);
				targetNode.setNodeIndex(featureIndex);
				targetNode.setNodeName(rNode);
				targetNode.setLevel(level);
				targetNode.setpNode(null);
				targetNode.setpExZero(probs[0]);
				targetNode.setpExOne(probs[1]);
				
				if(oneIndices.size()>0){
					createDecisionTree(candiateList,targetNode,oneIndices,false,level+1,gainMetric);
				}
				if(zeroIndices.size()>0){
					createDecisionTree(candiateList,targetNode,zeroIndices,true,level+1,gainMetric);
				}
				
			}else{
				Node childNode = new Node();
				childNode.setNodeIndex(featureIndex);
				childNode.setNodeName(rNode);
				childNode.setpNode(targetNode);
				childNode.setLevel(level);
				childNode.setpExZero(probs[0]);
				childNode.setpExOne(probs[1]);
				
				if(isleft){
					targetNode.setlChild(childNode);
					targetNode.setlClass(-1);
				}else{
					targetNode.setrChild(childNode);
					targetNode.setrClass(-1);
				}
				
				if(oneIndices.size()>0){
					createDecisionTree(candiateList,childNode,oneIndices,false,level+1,gainMetric);
				}
				if(zeroIndices.size()>0){
					createDecisionTree(candiateList,childNode,zeroIndices,true,level+1,gainMetric);
				}
			}
			
			
			return;
		}
	}
}
