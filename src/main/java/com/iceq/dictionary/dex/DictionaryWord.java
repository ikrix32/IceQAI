package com.iceq.dictionary.dex;

import java.util.ArrayList;
import java.util.List;

import com.iceq.dictionary.dex.Word.Type;

public class DictionaryWord
{
	public String text;
	public String lexem;
	
	public List<Type> types = new ArrayList<Type>();
	
	public void addType(Type t){
		if(!types.contains(t))
			types.add(t);
	}
	
	public String toString(){
		return "["+text+"]["+lexem+"]["+types.get(0)+"]";
	}
}
