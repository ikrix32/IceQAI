package com.iceq.dictionary.importer;

import com.iceq.dictionary.Substantive;
import com.iceq.dictionary.importer.web.SubstantiveDeclinationReader;

interface SubstantiveResultHandler{
	void done(String substantive,Substantive s);
}

public class ThreadReader implements Runnable
{
	
	String subst;
	SubstantiveResultHandler handler;
	
	public ThreadReader(String subtantive,SubstantiveResultHandler resultHandler){
		subst = subtantive;
		handler = resultHandler;
	}
	
	SubstantiveDeclinationReader reader = new SubstantiveDeclinationReader();
	
	public void run(){
		Substantive su = null;
		System.out.println("Reading "+subst);
		try
		{
			su = reader.getSubstantive(subst);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.done(subst,su);
	}
}
