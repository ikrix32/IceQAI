package com.iceq.dictionary.importer;

public class WatchDog implements Runnable
{
	String crtPage;
	String crtWord;
	long m_lastResponsetime = System.currentTimeMillis();
	
	boolean m_running;
	
	public void touch(String crtpage,String crtWord){
		m_lastResponsetime = System.currentTimeMillis();
		this.crtPage = crtpage;
		this.crtWord = crtWord;
	}
	
	public void run()
	{
		m_running = true;
		while(m_running){
			if(System.currentTimeMillis() - m_lastResponsetime > 10000){
				System.out.println("Warning!!! LastPage: "+crtPage+" crt word:"+crtWord);
			}
			
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		m_running = false;
	}
}
