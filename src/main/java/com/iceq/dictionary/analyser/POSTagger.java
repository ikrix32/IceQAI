package com.iceq.dictionary.analyser;

import java.util.List;

import com.iceq.dictionary.PartOfSpeech;

public class POSTagger 
{
	private enum Tag{
		Ar,//Articol
		Su,//Substantiv
		Vb,//Verb
		Av,//Adverb
		Aj,//Adjectiv
	}
	
	final static String[] rules = new String[]{
		"ArSuAj->ASA",
		"ArSu->AS",
		"SuAj->SA",
		"ArSu->AS",
		"VbASA->VP",
		"",
		"ASAVb->SS",
		
	};
	
	public List<PartOfSpeech> tag(List<Word> words){
		return null;
	}
}
