import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/*
 * Name: Mustafa Amir Faisal
 * Net Id: maf120030
 */


public class EMGMM {
	
	static ArrayList<Double > dPoints = new ArrayList<Double>();
	static String dataFileName = "em_data.txt";
	static int numClusters = 3;
	static double[] mixture_weight;
	static double[] mixture_mean;
	static double[] mixture_standardDeviation;
	static Map<Double, ArrayList<Double>> estepData = new HashMap<Double, ArrayList<Double>>(); 
	static double prevLogLiklihood = 0.0;
	static double knwonVariance = 1.0;
	static boolean isKnowVariance = false;
	static int scalingfactor = 10;
	public static void main(String[] args){
		
		
		
		if(args.length>=2){
			dataFileName = args[0].trim();
			numClusters = Integer.parseInt(args[1].trim());
			//scalingfactor = Integer.parseInt(args[2].trim()); //variance scaling factor
		}
		
		if(args.length>=3){
			dataFileName = args[0].trim();
			numClusters = Integer.parseInt(args[1].trim());
			knwonVariance = Double.parseDouble(args[2].trim());
			isKnowVariance=true;
			//scalingfactor = Integer.parseInt(args[3].trim()); //variance scaling factor
		}
		
		
		
		gatherData(new File(dataFileName));
		
		
		double maxValue =-99999999999.0;
		int maxIteration = 0;
		
		for(int iter=1;iter<=100;iter++){
			
			initialization();
			System.out.println("====================Iteration : " + iter+"===================");
			System.out.println(" Parameter values after initialization: ");
			printParameters();
			
			double currentlikelihood=0.0;
			//maxValue =-99999999999.0;
			
			
			do{
				prevLogLiklihood = calculateLogLikelihood();
				//System.out.println("previous loglikelihood: " + prevLogLiklihood);
				
				calculateEstep();
				calculateMstep();
				currentlikelihood = calculateLogLikelihood();
				
				//System.out.println("current loglikelihood: " + currentlikelihood);
				
			}while(prevLogLiklihood!=currentlikelihood);
			
			
			
			if(prevLogLiklihood>maxValue){
				
				//System.out.println(" Parameter values after convergence: ");
				//printParameters();
				maxValue = prevLogLiklihood;
				maxIteration = iter;
				//System.out.println("Current loglikelihood: " + maxValue);
			}
			
			System.out.println(" Parameter values after convergence: ");
			printParameters();
			System.out.println("Current loglikelihood: " + maxValue);
		}
		System.out.println("Maxmum log liklihood :" + maxValue + " for iteration number: " + maxIteration);
	}
	
	static void printParameters(){
		
		System.out.println("Mixture Weights: ");
		
		for(int i=0;i<numClusters;i++){
			System.out.println("\t Weight for cluster " + i + " : " + mixture_weight[i] );
		}
		
		System.out.println("Mixture mean: ");
		
		for(int i=0;i<numClusters;i++){
			System.out.println("\t Mean for cluster " + i + " : " + mixture_mean[i] );
		}
		
		System.out.println("Mixture variance: ");
		
		for(int i=0;i<numClusters;i++){
			System.out.println("\t variance for cluster " + i + " : " + Math.pow(mixture_standardDeviation[i],2) );
		}
		
		
	}
	static void initialization(){
		
		//mixture initialization
		mixture_weight = new double[numClusters];
		
		
		
		for(int i=0;i<mixture_weight.length;i++){
			mixture_weight[i] = (double)(1/(double)mixture_weight.length);
		}
		
		
		//mean initialization
		ArrayList<Integer> prevSelected = new ArrayList<Integer>();
		
		mixture_mean = new double[numClusters];
		
		Random rmd = new Random();
		
		
		for(int i=0;i<mixture_mean.length;i++){
			
			int nextInt = rmd.nextInt(dPoints.size());
			
			while(prevSelected.contains(nextInt)){
				nextInt = rmd.nextInt(dPoints.size());
			}
			
			prevSelected.add(nextInt);
			mixture_mean[i] = dPoints.get(nextInt);
			
		}
		
		//variance initialization
		mixture_standardDeviation= new double[numClusters];
		if(isKnowVariance){ // For known variance
			for(int i=0;i<mixture_standardDeviation.length;i++){
				mixture_standardDeviation[i] = Math.sqrt(knwonVariance);
			}
		}
		else{
			double std = getStandardDevision(dPoints);
			
			for(int i=0;i<mixture_standardDeviation.length;i++){
				mixture_standardDeviation[i] = std*rmd.nextInt(scalingfactor);
			}
		}
	}
	
	static void calculateEstep(){
		
		estepData.clear();
		
		for(double dp:dPoints){
			ArrayList<Double> xGk = new ArrayList<Double>();
			
			double sum = 0.0;
			double val = 0.0;
			
			double[] lmw = new double[numClusters];
			
			for(int i=0;i<numClusters;i++){
				val = calculateGaussionDensity(mixture_mean[i], mixture_standardDeviation[i],dp)*mixture_weight[i];
				lmw[i] = val;
				
				sum+=val;
			}
			
			for(int i=0;i<numClusters;i++){
				if(sum!=0.0)
					val = lmw[i]/sum;
				else
					val = 0.0;
				
				xGk.add(i,val);
			}
			
			estepData.put(dp, xGk);
		}
	}
	
	static void calculateMstep(){
		double[] weights = new double[numClusters];
		double[] temSum = new double[numClusters];
		
		for(int i=0;i<numClusters;i++){
			weights[i] = 0.0;
			temSum[i] = 0.0;
		}
		
		for(double ks:estepData.keySet()){
			for(int i=0;i<numClusters;i++){
				weights[i] += estepData.get(ks).get(i);
				temSum[i] += estepData.get(ks).get(i)*ks;
			}
		}
		
		
		for(int i=0;i<numClusters;i++){  
			mixture_weight[i] = weights[i]/(double)estepData.size(); // updating mixture weights
			if(weights[i]!=0)
				mixture_mean[i] = temSum[i]/weights[i]; // updating mean
			else
				mixture_mean[i] = 0.0;
		}
		
		if(isKnowVariance){
			for(int i=0;i<numClusters;i++){
				mixture_standardDeviation[i] = Math.sqrt(knwonVariance);
			}
		}else{
		
			for(int i=0;i<numClusters;i++){
				temSum[i] = 0.0;
			}
			
			for(double ks:estepData.keySet()){
				for(int i=0;i<numClusters;i++){
					temSum[i] += Math.pow((ks-mixture_mean[i]), 2)*estepData.get(ks).get(i);
				}
			}
			
			for(int i=0;i<numClusters;i++){
				mixture_standardDeviation[i] = Math.sqrt(temSum[i]);
			}
		}
	}
	
	static double calculateLogLikelihood(){
		
		double sum =0.0;
		double tempSum = 0.0;
		
		for(double ks:estepData.keySet()){
			tempSum = 0.0;
			for(int i=0;i<numClusters;i++){
				tempSum += calculateGaussionDensity(mixture_mean[i], mixture_standardDeviation[i], ks)*mixture_weight[i];
			}
			
			sum+=Math.log(tempSum);
		}
		
		return sum;
	}
	
	static double calculateGaussionDensity(double mean, double std, double dpoint){
		
		double term1 = Math.sqrt(2*Math.PI)*std;
		if(term1!=0.0)
			term1 = 1/term1;
		else
			term1 = 0.0;
		
		double term2=0.0;
		if(std!=0.0){
			term2 = Math.exp(-0.5*Math.pow((mean-dpoint), 2)*Math.pow(std*std, -2));
		}
		
			//if(Double.isNaN(term1*term2))
			//	System.out.println("term1 " + term1 + " term2 " + term2);
		return term1*term2;
	}
	
	static void gatherData(File dataFile){
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(dataFile.getAbsolutePath());
			br = new BufferedReader(fr);
			
			String line;
			
			while((line=br.readLine())!=null){
				line = line.trim();
				dPoints.add(Double.parseDouble(line));	
			}
			
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static double getSum(ArrayList<Double> data){
		double sum = 0.0;
		
		for(double item:data){
			sum+=item;
		}
		
		return sum;
	}
	
	static double getMean(ArrayList<Double> data){
		double sum = getSum(data);
		return sum/(double)data.size();
	}
	
	static double getVariance(ArrayList<Double> data){
		
		double mean = getMean(data);
		double sqrSum = 0.0;
		
		for(double item:data){
			sqrSum += (item-mean)*(item-mean);
		}
		
		return sqrSum/(double)data.size();
	}
	
	static double getStandardDevision(ArrayList<Double> data){
		double variance = getVariance(data);
		
		return Math.sqrt(variance);
	}
	
}
