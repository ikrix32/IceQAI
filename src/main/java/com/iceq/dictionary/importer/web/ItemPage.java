package com.iceq.dictionary.importer.web;

import java.util.ArrayList;
import java.util.List;

public class ItemPage
{
	public List<String> items = new ArrayList<String>();
	public String nextPage;
	
	public void add(String item){
		items.add(item);
	}
	
	public String get(int i){
		return items.get(i);
	}
	
	public int itemCount(){
		return items.size();
	}
	
	public void setNextPage(String nextPage){
		this.nextPage = nextPage;
	}
	
	public String getNextPage(){
		return nextPage;
	}
}
