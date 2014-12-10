import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


public class CrossValidationApp {

	private static String filename;
	private static String filename2; 
	private int kFold;
	private int m;
	private int t;
	private int rows;
	private int cols;
	private int[][] examplesList;
	private char[][] labels;
	private String[] exampleDataMapping;
	Map<Integer,List<MyIteration>> data;

	public CrossValidationApp() {
		super();
		filename = "C:/Users/Anvesh/Google Drive/workspace/ML1/src/first_file.txt";
		filename2 = "C:/Users/Anvesh/Google Drive/workspace/ML1/src/second_file.txt";
//		filename = "C:/Users/Anvesh/workspace/ML1/src/file01.txt";
//		filename2 = "C:/Users/Anvesh/workspace/ML1/src/file02.txt";
		this.kFold = 1;
		this.m = 1;
		this.t = 1;
		this.rows = 1;
		this.cols = 1;
		this.data = new HashMap<Integer,List<MyIteration>>();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getKFold() {
		return kFold;
	}

	public void setKFold(int kFold) {
		this.kFold = kFold;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		CrossValidationApp.filename = filename;
	}

	public String getFilename2() {
		return filename2;
	}

	public void setFilename2(String filename2) {
		CrossValidationApp.filename2 = filename2;
	}
	
	
	public class MyIteration {

		List<Integer> include_list;
		List<Integer> exclude_list;
		
		public MyIteration(List<Integer> include_list, List<Integer> exclude_list) {
			super();
			this.include_list = include_list;
			this.exclude_list = exclude_list;
		}
		
		public MyIteration() {
			super();
			include_list = new ArrayList<Integer>();
			exclude_list = new ArrayList<Integer>();
		}

		public List<Integer> getInclude_list() {
			return include_list;
		}
		public void setInclude_list(List<Integer> include_list) {
			this.include_list = include_list;
		}
		public List<Integer> getExclude_list() {
			return exclude_list;
		}
		public void setExclude_list(List<Integer> exclude_list) {
			this.exclude_list = exclude_list;
		}
			
	}


	public void loadFiles(){

		Scanner sc = new Scanner(System.in);

		System.out.println("Please provide the file 1 with path : ");

		if(sc.hasNext()){
			filename = sc.nextLine();
		} else {
			System.out.println("File 1 not provided... ");
			System.exit(-1);
		}

		System.out.println("Please provide the file 2 with path : ");

		if(sc.hasNext()){
			filename2 = sc.nextLine();
		} else {
			System.out.println("File 2 not provided... ");
			System.exit(-1);
		}

		sc.close();
	}


	public void readFile1(String fname) {

		File file = new File(fname);
		Scanner sc;
		String line = null;

		try {
			sc = new Scanner(file);

			if(sc.hasNextLine()) {
				line = sc.nextLine();

				String tokens[] = line.split(" ");

				if(tokens.length == 3){
					this.kFold = Integer.parseInt(tokens[0]);
					this.m = Integer.parseInt(tokens[1]);
					this.t = Integer.parseInt(tokens[2]);
					this.examplesList = new int[this.t][this.m];
				} else {
					System.out.println("Incorrect file 1 input!");
					System.exit(-1);
				}				
			} else {
				System.out.println("No data in file 1!");
				System.exit(-1);
			}

			int y = 0;

			while (sc.hasNextLine()) {

				line = sc.nextLine();

				String tokens[] = line.split(" ");

				if(tokens.length == this.m){
					for(int z = 0;z<this.m;z++) {
						this.examplesList[y][z] = Integer.parseInt(tokens[z]);
					}	
				} else {
					System.out.println("Incorrect file 1 input!");
					System.exit(-1);
				}		

				y++;
			}

			if(y!=this.t){
				System.out.println("Incorrect file 1 input!");
				System.exit(-1);
			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void readFile2(String fname) {

		File file = new File(fname);
		Scanner sc;
		String line = null;

		try {
			sc = new Scanner(file);

			if(sc.hasNext()) {
				line = sc.nextLine();

				String tokens[] = line.split(" ");

				if(tokens.length == 2){
					this.rows = Integer.parseInt(tokens[0]);
					this.cols = Integer.parseInt(tokens[1]);
					this.labels = new char[this.rows][this.cols];
					this.exampleDataMapping = new String[this.m];
				} else {
					System.out.println("Incorrect file 2 input!");
					System.exit(-1);
				}				
			} else {
				System.out.println("No Data in file 2!");
				System.exit(-1);
			}

			int y = 0;
			int exampleNum = 0;

			while (sc.hasNextLine()) {

				line = sc.nextLine();

				String tokens[] = line.split(" ");

				if(tokens.length == this.cols){
					for(int z = 0;z<this.cols;z++) {
						this.labels[y][z] = tokens[z].charAt(0);

						if(this.labels[y][z] == '+' || this.labels[y][z] == '-'){
							this.exampleDataMapping[exampleNum] = Integer.toString(z)+ " " + Integer.toString(y);
							exampleNum++;
						}

					}	
				} else {
					System.out.println("Incorrect file 2 input!");
					System.exit(-1);
				}		

				y++;
			}

			if(y!=this.rows){
				System.out.println("Incorrect file 2 input!");
				System.exit(-1);
			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void printMap(Map<Integer,List<MyIteration>> data) {

		System.out.println();
		System.out.println();
		System.out.println();

		for(Entry<Integer, List<MyIteration>> entry : data.entrySet()){

			System.out.print(entry.getKey() + " ---> ");

			List<MyIteration> iterationList = (ArrayList<MyIteration>) entry.getValue();

			for(int index = 0; index < iterationList.size();index++) {

				System.out.print("fold - ");

				List<Integer> iList = iterationList.get(index).getExclude_list();
				List<Integer> eList = iterationList.get(index).getInclude_list();

				for(int a =0;a<iList.size();a++){
					System.out.print(iList.get(a) + " ");
				}

				System.out.print(" || training set - ");

				for(int a = 0;a<eList.size();a++){
					System.out.print(eList.get(a) + " ");
				}

				System.out.println();
			}
		}
	}


	public void splitAndSaveData(int kFold){

		int isEven = this.m%kFold==0 ? 1 : 0; 
		int sizeOfEachFold = this.m/kFold;
		int fold_start = 0;
		int fold_end = 0;

		for(int y = 0; y < this.t; y++){

			List<MyIteration> iterationList = new ArrayList<MyIteration>();

			for(int fold = 0; fold < kFold; fold++){

				fold_start = (fold*sizeOfEachFold);

				if(isEven == 0 && fold == kFold-1){
					fold_end = ((fold+1)*sizeOfEachFold)+1;
				} else {
					fold_end = (fold+1)*sizeOfEachFold;
				}


				MyIteration iteration = new MyIteration();


				for(int z = fold_start;z < fold_end;z++){
					iteration.exclude_list.add(this.examplesList[y][z]);
				}


				for(int z = 0; z < fold_start; z++){
					iteration.include_list.add(this.examplesList[y][z]);
				}

				for(int z = fold_end; z < this.m; z++){
					iteration.include_list.add(this.examplesList[y][z]);
				}


				iterationList.add(iteration);
			}			

			this.data.put(y, iterationList);
		}	

		// printMap(this.data);
	}



	private HashMap<Integer, Integer> sortByValues(Map<Integer, Integer> map) { 
		List<Object> list = new LinkedList<Object>(map.entrySet());

		//  Custom Comparator 
		Collections.sort(list, new Comparator<Object>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public int compare(final Object o1, final Object o2) {
				return ((Comparable) (((Entry<Integer, Integer>) o1)).getValue()).compareTo((Comparable)((Entry<Integer,Double>) (o2)).getValue());
			}
		});

		// Copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap<Integer, Integer> sortedHashMap = new LinkedHashMap<Integer, Integer>();
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Entry<Integer, Integer> entry = (Entry<Integer, Integer>) it.next();
			sortedHashMap.put((Integer)entry.getKey(), (Integer)entry.getValue());
		} 
		return sortedHashMap;
	}


	public List<Integer> nearest_neighbors(Integer example, List<Integer> iList, int kValue){

		int x1 = 0;
		int x2 = 0;
		int z1 = 0;
		int z2 = 0;
		int distance = 0;

		List<Integer> nearestNeighbors = new ArrayList<Integer>();

		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		Map<Integer,Integer> sortedMap = new HashMap<Integer,Integer>();

		String tokens[] = this.exampleDataMapping[example.intValue()].split(" ");
		x1 = Integer.parseInt(tokens[1]);
		x2 = Integer.parseInt(tokens[0]);
		
//		x2 = Integer.parseInt(this.exampleDataMapping[example.intValue()].substring(0,1));
//		x1 = Integer.parseInt(this.exampleDataMapping[example.intValue()].substring(1,2));

		//		System.out.println("x1 = " + x2 + "; x2 = " + x1 + ";");

		for(int index = 0;index<iList.size();index++){

			String tok[] = this.exampleDataMapping[iList.get(index).intValue()].split(" ");
			z1 = Integer.parseInt(tok[1]);
			z2 = Integer.parseInt(tok[0]);
			
//			z2 = Integer.parseInt(this.exampleDataMapping[iList.get(index).intValue()].substring(0,1));
//			z1 = Integer.parseInt(this.exampleDataMapping[iList.get(index).intValue()].substring(1,2));

			distance = (Math.abs((z1-x1)*(z1-x1)) + Math.abs((z2-x2)*(z2-x2)));

			map.put(iList.get(index), distance);
		}

		sortedMap = sortByValues(map);

		List<Integer> list = new ArrayList<Integer>(sortedMap.keySet());

		for(int index = 0; index<list.size();index++){
			//			System.out.println("Euclidean distance from example - " + list.get(index) + " : " + sortedMap.get(list.get(index)));
		}

		for(int index = 0;index<kValue;index++){
			//			System.out.println("Nearest Neighbors - " + list.get(index) + " : " + sortedMap.get(list.get(index)));
			nearestNeighbors.add(list.get(index));
		}

		return nearestNeighbors;
	}



	public double find_k_NN_error_per_element(Integer example, List<Integer> iList, int kValue) {

		double error = 0.0;
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		int num_of_positives = 0;
		int num_of_negatives = 0;
		char actualResult;
		char expectedResult;

		List<Integer> nearestNeighbors = new ArrayList<Integer>();

		String tokens[] = this.exampleDataMapping[example.intValue()].split(" ");
		x1 = Integer.parseInt(tokens[1]);
		x2 = Integer.parseInt(tokens[0]);
		
//		x2 = Integer.parseInt(this.exampleDataMapping[example.intValue()].substring(0,1));
//		x1 = Integer.parseInt(this.exampleDataMapping[example.intValue()].substring(1,2));

		actualResult = labels[x1][x2];

		nearestNeighbors = nearest_neighbors(example,iList,kValue);

		for(int index = 0;index<nearestNeighbors.size();index++){

			String tok[] = this.exampleDataMapping[nearestNeighbors.get(index).intValue()].split(" ");
			y1 = Integer.parseInt(tok[1]);
			y2 = Integer.parseInt(tok[0]);
			
//			y2 = Integer.parseInt(this.exampleDataMapping[nearestNeighbors.get(index).intValue()].substring(0,1));				
//			y1 = Integer.parseInt(this.exampleDataMapping[nearestNeighbors.get(index).intValue()].substring(1,2));

			expectedResult = labels[y1][y2];

			if(expectedResult == '+'){
				num_of_positives++;
			} else if(expectedResult == '-'){
				num_of_negatives++;
			}
		}

		if(num_of_positives>num_of_negatives){
			expectedResult = '+';
		} else if(num_of_positives<num_of_negatives){
			expectedResult = '-';
		} else {
			expectedResult = '-';
		}

		//		System.out.println("actual result : " + actualResult);
		//		System.out.println("expected result : " + expectedResult);

		if(actualResult != expectedResult){
			error = error + 1;
		}

		return error;		
	}


	public void label_grid_k_NN(int kValue) {

		int distance = 0;
		int num_of_positives = 0;
		int num_of_negatives = 0;
		char expectedResult;
		char[][] updated_labels = new char[this.rows][this.cols];

		for(int y = 0;y<this.rows;y++) {
			for(int z = 0;z<this.cols;z++) {
				updated_labels[y][z] = this.labels[y][z];
			}
		}

		for(int y = 0;y<this.rows;y++) {
			for(int z = 0;z<this.cols;z++) {

				if(this.labels[y][z] == '.'){

					num_of_positives = 0;
					num_of_negatives = 0;

//										System.out.println("row - " + y + "column - " + z);

					Map<Integer,Integer> map = new HashMap<Integer,Integer>();
					Map<Integer,Integer> sortedMap = new HashMap<Integer,Integer>();

					List<Integer> nearestNeighbors = new ArrayList<Integer>();


					for(int index = 0;index<this.exampleDataMapping.length;index++){
						
						String tokens[] = this.exampleDataMapping[index].split(" ");
						int y1 = Integer.parseInt(tokens[1]);
						int z1 = Integer.parseInt(tokens[0]);
						
//						int y1 = Integer.parseInt(this.exampleDataMapping[index].substring(1,2));
//						int z1 = Integer.parseInt(this.exampleDataMapping[index].substring(0,1));

						distance = (Math.abs((z1-z)*(z1-z)) + Math.abs((y1-y)*(y1-y)));

//						System.out.println("index - " + index + " : (x1 - " + y + ",y1 - " + z + ")  (x2 - " + y1 + ",y2 - " + z1 + ")   Distance - " + distance);
						
						map.put(index, distance);

					}

					sortedMap = sortByValues(map);

					List<Integer> list = new ArrayList<Integer>(sortedMap.keySet());

//										for(int index = 0; index<list.size();index++){
//											System.out.println("Euclidean distance from example - " + list.get(index) + " (" + Integer.parseInt(this.exampleDataMapping[list.get(index)].substring(1,2)) + "," + Integer.parseInt(this.exampleDataMapping[list.get(index)].substring(0,1)) + ") : " + sortedMap.get(list.get(index)));
//										}

					for(int index = 0;index<kValue;index++){
//												System.out.println("Nearest Neighbors - " + list.get(index) + " : " + sortedMap.get(list.get(index)));
						nearestNeighbors.add(list.get(index));
					}

					for(int index = 0;index<nearestNeighbors.size();index++){
						
						String tokens[] = this.exampleDataMapping[nearestNeighbors.get(index).intValue()].split(" ");
						int y2 = Integer.parseInt(tokens[1]);
						int z2 = Integer.parseInt(tokens[0]);

//						int z2 = Integer.parseInt(this.exampleDataMapping[nearestNeighbors.get(index).intValue()].substring(0,1));				
//						int y2 = Integer.parseInt(this.exampleDataMapping[nearestNeighbors.get(index).intValue()].substring(1,2));

						expectedResult = labels[y2][z2];

						if(expectedResult == '+'){
							num_of_positives++;
						} else if(expectedResult == '-'){
							num_of_negatives++;
						}
					}

					if(num_of_positives>num_of_negatives){
						updated_labels[y][z] = '+';
					} else if(num_of_positives<num_of_negatives){
						updated_labels[y][z] = '-';
					} else {
						updated_labels[y][z] = '-';
					}
				}
//				else {
//					System.out.println("row - " + y + "column - " + z);
//					System.out.println(this.labels[y][z]);
//				}
			}			
		}


		System.out.println("Updated grid with " + kValue + "-NN : ");
//		System.out.println();
		for(int y = 0;y<this.rows;y++) {
			for(int z = 0;z<this.cols;z++) {
				System.out.print(updated_labels[y][z] + " ");
			}
			System.out.println();
		}
//		System.out.println();
	}

	public void evaluation(Map<Integer,List<MyIteration>> data, int kValue){

		int a = 0;
		double error = 0.0;
		double localerror = 0.0;
		double errorList[] = new double[this.t];
		double variance = 0.0;
		double sigma = 0.0;
		
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
		//		System.out.println("For k = " + kValue + " : ");
//		System.out.println("******************************************************************************************************");

		if(kValue > this.m/this.kFold) {
			System.out.println("k = " + kValue);
//			System.out.println("For k = " + kValue + " : ");
			System.out.println("Size of the training/testing set is less than given K value : " + kValue + ". Hence, stop! ");
			return;
		}

		for(Entry<Integer, List<MyIteration>> entry : data.entrySet()){


			List<MyIteration> iterationList = (ArrayList<MyIteration>) entry.getValue();

			for(int in = 0; in < iterationList.size();in++) {

				List<Integer> iList = iterationList.get(in).getExclude_list();
				List<Integer> eList = iterationList.get(in).getInclude_list();

					//					System.out.println();
					//					System.out.print("Fold - ");
					//
					//					for(int index=0;index<eList.size();index++){
					//						System.out.print(eList.get(index) + " ");
					//					}
					//
					//					System.out.print(" ||  Training Set - ");
					//
					//					for(int index=0;index<iList.size();index++){
					//						System.out.print(iList.get(index) + " ");
					//					}
					//
					//					System.out.println();
					//					System.out.println();

					for(int index = 0;index<eList.size();index++){

						//						System.out.println("Testing example (" + eList.get(index) + ") on the above training set with " + kValue + " - NN .....");

						localerror = find_k_NN_error_per_element(eList.get(index),iList,kValue);

						//						System.out.println("Error for example (" + eList.get(index) + ") is = " + localerror);

						errorList[a] = errorList[a] + localerror;

						//						System.out.println();
					}	


			}

			errorList[a] = errorList[a]/this.m;

			//			System.out.println();
			//			System.out.println("Error for permutation " + entry.getKey() + " is = " + errorList[a]);

			error = error + errorList[a];

			a++;

			//			System.out.println("-------------------------------------------------------------------------------------------------");
		}

		error = error/this.t;
		error = Math.round(error*100.00)/100.00;

		for(int b=0;b<errorList.length;b++){
			variance = variance + ((errorList[b]-error)*(errorList[b]-error));
		}

		sigma = Math.sqrt(variance/((this.t)-1));
		sigma = Math.round(sigma*100.00)/100.00;

//		System.out.println();

		System.out.println("k = " + kValue + " e = " + error + " sigma = " + sigma);
//		System.out.println("For k = " + kValue + " : error = " + error);
//		System.out.println("            variance = " + variance);
//		System.out.println("            sigma = " + sigma);

		label_grid_k_NN(kValue);

		System.out.println();
//		System.out.println("******************************************************************************************************");

	}

	
	public void test() {
		
		System.out.println();
		
	}
	

	public static void main(String[] args) {


		Scanner sc = new Scanner(System.in);
		
		CrossValidationApp cvapp = new CrossValidationApp();

//		System.out.println("Enter the full-file path for Cross-Validation information file : ");
		
//		cvapp.readFile1(sc.nextLine().toString());
		
//		System.out.println("Enter the full-file path for Grid file : ");

//		cvapp.readFile2(sc.nextLine().toString());

		cvapp.readFile1(filename);
		cvapp.readFile2(filename2);
		
		System.out.println();
		System.out.println("Cross-Validation information : ");

		for(int y = 0;y<cvapp.t;y++) {
			for(int z = 0;z<cvapp.m;z++) {
				System.out.print(cvapp.examplesList[y][z] + " ");
			}
			System.out.println();
		}

		System.out.println();
		System.out.println("Initial Grid : ");

		for(int y = 0;y<cvapp.rows;y++) {
			for(int z = 0;z<cvapp.cols;z++) {
				System.out.print(cvapp.labels[y][z] + " ");
			}
			System.out.println();
		}

		cvapp.splitAndSaveData(cvapp.kFold);

		cvapp.evaluation(cvapp.data, 1);
		cvapp.evaluation(cvapp.data, 2);
		cvapp.evaluation(cvapp.data, 3);
		cvapp.evaluation(cvapp.data, 4);
		cvapp.evaluation(cvapp.data, 5);
		
		
		sc.close();
		
	}

}
