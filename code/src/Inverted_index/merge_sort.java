package Inverted_index;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;


public class merge_sort {
	private static LinkedList<data_structure> merged_list=new LinkedList<data_structure>();	
	//private static int postings_count = 0;
	//private static int term_count=0;
	public static void merge(LinkedList<data_structure> block_list){
		if(merged_list.isEmpty()){
			Iterator<data_structure>it = block_list.iterator();
			while(it.hasNext()){
				data_structure node = it.next();
				merged_list.add(node);
			}
		}else{
			
			int block_size = block_list.size()-1;
			int i=0, j=0;
			data_structure block_node = block_list.get(i);
			data_structure merge_node = merged_list.get(j);
			while(i<=block_size){
				if(j>=merged_list.size()){					
					merged_list.add(block_node);
					i++;
					if(i>block_size){
						break;
					}
					block_node=block_list.get(i);
				}
				else if(block_node.get_term().compareTo(merge_node.get_term())<0){
					merged_list.add(j,block_node);
					i++;
					if(i>block_size){
						break;
					}
					if(j<merged_list.size()){
						j++;
					}
					block_node=block_list.get(i);

					
				}else if(block_node.get_term().compareTo(merge_node.get_term())>0){
					if(j<merged_list.size()){
						j++;
					}
					if(j<merged_list.size()){
						merge_node=merged_list.get(j);
					}
				}else if(block_node.get_term().compareTo(merge_node.get_term())==0){
					merged_list.get(j).get_posting_list().addAll(block_node.get_posting_list());
					i++;
					if(i>block_size){
						break;
					}
					if(j<merged_list.size()){
						j++;
					}
					block_node=block_list.get(i);
					if(j<merged_list.size()){
						merge_node=merged_list.get(j);
					}
						

					
				}
			}

		}


	}
	
	public static void data_from_file(String file_name) throws Exception{
		File file = new File(file_name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st="";
		LinkedList<data_structure> block_list = new LinkedList<data_structure>();
		while((st = br.readLine())!=null){
			if(st.contains("TERM = ")){
				String term = st.replaceAll("TERM = ","");
				st=st.replaceAll(" ","");
				data_structure node = new data_structure(term);
				st=br.readLine();//----------->usually reaches the POSTING LIST
				String list=st.replaceAll("Posting List =", "");
				if(!list.contains("END")){
					st=br.readLine();
					while(!st.contains("END")){
						list=list+st;
						st=br.readLine();
					}
					list=list+st;
				}if(list.contains("END")){
					String []array = list.split(",");
					for(int i=0;!array[i].contains("END");i++){
						array[i]=array[i].replaceAll(" ","");
							int doc_id=Integer.parseInt(array[i]);
							node.add_doc_id(doc_id);
					}
				}
				
				block_list.add(node);
			}
			
		}
		br.close();
		merge(block_list);
	}
	public static void block_files() throws Exception{
		
		for(int i = 0; i<44; i++){
			System.out.println("FILE NUMBER = "+i );
			String file_name= "/home/a/a_isht/Desktop/COMP_6791_P1/block_inverted_indexes/A"+i+".txt";
			data_from_file(file_name);

		}
	}
	
	public static void write_merged_index() throws IOException{
		System.out.println("Writing the final merged file ");
		Iterator<data_structure> it = merged_list.iterator();
		BufferedWriter output = null;
		output = new BufferedWriter(new FileWriter("/home/a/a_isht/Desktop/COMP_6791_P1/merge_sort_index/merged_index.txt"));
		while(it.hasNext()){
			data_structure node = it.next();
			output.write("TERM = "+node.get_term());
			//term_count++;
			output.newLine();
			output.write("Posting List = ");
			Iterator<Integer> itInt = node.get_posting_list().iterator();
				while(itInt.hasNext()){
					//postings_count++;
					output.write(Integer.toString(itInt.next())+ ",");
				}
			output.write("END"); // Marker to mark the end of the posting list
			output.newLine();
		}
		output.flush();
		output.close();
		merged_list.clear();
	}
	
	public static void main(String []Args) throws Exception{
		//System.out.println("The Merging begins");
		block_files();// reading all the block files and then bringing them to main memory
		write_merged_index();// writing final merged index back into the disk
		


		
	}
	

}
