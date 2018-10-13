package Inverted_index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
/***
 * This is a basic level spimi implementation on the Rueters-21578.
 * @author AKSHAT BISHT
 *
 */
public class Inverted_index {
	static LinkedList <data_structure> list = new LinkedList<data_structure>();//Contains the Block Inverted Index
	static int file_number=0;//Which file number each block is allocated to

	public static void save_block_index() throws IOException{// Function to write a block to Disk
		Iterator<data_structure> it = list.iterator();
		BufferedWriter output = null;
		output = new BufferedWriter(new FileWriter("/home/a/a_isht/Desktop/COMP_6791_P1/block_inverted_indexes/"+"A"+file_number+".txt"));
		while(it.hasNext()){
			data_structure node = it.next();
			output.write("TERM = "+node.get_term());
			output.newLine();
			output.write("Posting List = ");
			Iterator<Integer> itInt = node.get_posting_list().iterator();
				while(itInt.hasNext()){
					output.write(Integer.toString(itInt.next())+ ",");
				}
			output.write("END"); // Marker to mark the end of the posting list
			output.newLine();
		}
		output.flush();
		output.close();
		file_number++;
		list.clear();
		
	}
	
	public static void check_duplicates_postings(data_structure node, int docId){// This function checks if the posting already exists in the Index
		Iterator<Integer> it = node.get_posting_list().iterator();
		int check=0;
			while(it.hasNext()){
				if(it.next()==docId){// If the docId already exists in the postin list then return then don't add it to the list
					check=1;
					break;
				}
			}
			if(check==0){// this code is activated if no duplicate of the docId is found in the posting list
				node.get_posting_list().add(docId); //the unique docId is added to the Posting list.
			}
		
	}
	
	public static void token_search(String token, int docId){
		
			if(list.isEmpty()){
				list.add(0,new data_structure(token,docId));
			}
		
			else{
				Iterator<data_structure> it = list.iterator();
				boolean insert = false;
				int pos=0;
				while(it.hasNext()){	
					data_structure nodeFwd =  it.next();
					if(token.equals(nodeFwd.get_term())){ //check if duplicates of termIds already exists
						check_duplicates_postings(nodeFwd, docId);// check if duplicates of this docIds already exists
						insert=true;
						break;
						
					}else if(token.compareTo(nodeFwd.get_term())<0){//termId doesn't exist add it to the middle of the list
						list.add(pos,new data_structure(token, docId));
						insert=true;
						break;
					}else{
						pos++;
					}
				}
				if(insert==false){
					list.add(new data_structure(token,docId));//termId doesn't exist and add it to the end of the list
				}
			
			}
		
		
	}
	private static String stop_words(String sentence){
		//List of Stop words
		String[] stopwords = { "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any",
				"are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both",
				"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing",
				"don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
				"have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself",
				"him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
				"isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no",
				"nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
				"out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so",
				"some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then",
				"there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those",
				"through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're",
				"we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while",
				"who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll",
				"you're", "you've", "your", "yours", "yourself", "yourselves" };
		for(int i=0;i<stopwords.length; i++){
			stopwords[i]=" "+stopwords[i]+" ";
			sentence= sentence.replaceAll(stopwords[i], " ");
		}
		
		return sentence;
	}

	
	public static String clean_sentence(String sentence){
		sentence=sentence.toLowerCase();//case folding
		sentence=stop_words(sentence);//stop words
		sentence = sentence.replaceAll("\\d"," ");  //removing numbers
		sentence=sentence.replaceAll("[<>/,()*-;:&#]" , " ");//cleaning of html tags and other data
		sentence=sentence.replaceAll("'s "," ");// cleaning all the 's at the end of the terms
		sentence=sentence.replaceAll("\"","");// Cleaning all the " in the string
		
		return sentence;
	}
	
	


	public static void tokenizer(String sentence, int docId){
		sentence=clean_sentence(sentence);
		StringTokenizer st1 = new StringTokenizer(sentence);
		String token="";
		while(st1.hasMoreTokens()){
		    	  token=st1.nextToken();
		    	  token_search(token, docId);
		      }

	}
	
	
	public static void block(String text, int docId){
		tokenizer(text, docId);
	}
	
	public static void break_file(File file, int line, int docId) throws Exception {
		
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String text="";
			for(int i=0;i<line;i++){// read till the buffer reader pointer has reached the line number reached the string containing "NewId"
				text=br.readLine();
				
			}
			

			while(!text.contains("<TEXT")){ // move the pointer till the buffer pointer has reached the string containing "<TEXT>"
				text=br.readLine();
				line++;
			}		
			System.out.println("------------NEWID : "+docId );
			while(!text.contains("</TEXT>")){ //Keep reading all the text till we reach "</TEXT>"
				text=br.readLine();//Read the line and store it into a string
				block(text, docId );// Send the line for Processing with the DocId("NewId")
		
			}
			br.close();
	}

	
	public static int docId(String st){// docId
		int id=0;
		String word="NEWID=";
		int wordIndex = st.indexOf(word);
		wordIndex= wordIndex+7;
		st= st.substring(wordIndex);
		word="\"";
		wordIndex=st.indexOf(word);
		st=st.substring(0, wordIndex);
		id= Integer.parseInt(st);
		return id;
		
	}
	

	

	
	public static void analyse_files()throws Exception{
		//read the file
		File folder = new File("/home/a/a_isht/Desktop/COMP_6791_P1/reuters_files");
		File[] listOfFiles = folder.listFiles();
		
		for(File file : listOfFiles){
			
			if(file.isFile()){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String st;
				int line=0;
				int count =0;

				while ((st = br.readLine())!= null){
					line++;

						if(st.contains("NEWID=")){ //if the text contains NewId and the block is less than that of 500 NewIds
							int docId=docId(st);
							break_file(file,line,docId);
							count++;
						}
						else if(count==500) { //if 500 has been reached then reset it to 0
							save_block_index();
							System.out.println("-------------------THIS IS ONE BLOCK---------------------");
							count=0;//update the count back to zero	
						}
					
					
				}
				br.close();
			}
			else{
				break;
			}
			
		}
		save_block_index();// for all the data after 21500
	
	}

	
	
	public static void main(String Args[])throws Exception{
		analyse_files();
	}


	
	
	
}


