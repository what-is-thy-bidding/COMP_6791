package Inverted_index;

import java.util.Iterator;
import java.util.LinkedList;

//--------------------------------Data Structure----------------------------------


class data_structure{
	private  String term_id;
	private  LinkedList<Integer> posting_list = new LinkedList<Integer>();
	
	public data_structure(String term){
		this.term_id=term;
	}
	public data_structure(String term, int doc){
		this.term_id=term;
		this.posting_list.add(doc);
	}
	public void set(String term,int doc){
		this.term_id= term;
		this.posting_list.add(doc);
	}
	
	public String get_term(){
		return term_id;
	}
	public void add_doc_id(int doc_id){
		posting_list.add(doc_id);
	}
	
	public LinkedList<Integer> get_posting_list(){
		return posting_list;
	}
	public void print(){
		System.out.print("TERM ID = "+ term_id + "  FREQUENCY = "+ posting_list.size()/*frequency*/+ "  ---> ");
		
		Iterator<Integer> itr = posting_list.iterator();
		System.out.print("POSTING LIST :- || ");
		
		while(itr.hasNext()){
			System.out.print(itr.next()+" || " );
		}
		System.out.println();
		
	}

}


