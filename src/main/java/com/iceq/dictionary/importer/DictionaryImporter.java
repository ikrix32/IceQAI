package com.iceq.dictionary.importer;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.iceq.dictionary.Adjective;
import com.iceq.dictionary.Substantive;
import com.iceq.dictionary.Verb;
import com.iceq.dictionary.importer.web.AdjectiveDeclinationReader;
import com.iceq.dictionary.importer.web.ItemPage;
import com.iceq.dictionary.importer.web.SubstantiveDeclinationReader;
import com.iceq.dictionary.importer.web.VerbConjugationReaderConjugareRo;
import com.iceq.dictionary.importer.web.WikitionaryListReader;

public class DictionaryImporter
{
	DictionaryDatabaseDriver			dbDriver		= new DictionaryDatabaseDriver();
	WikitionaryListReader				categoryReader	= new WikitionaryListReader();

	AdjectiveDeclinationReader			adjReader		= new AdjectiveDeclinationReader();
	SubstantiveDeclinationReader		substReader		= new SubstantiveDeclinationReader();
	VerbConjugationReaderConjugareRo	verbReader		= new VerbConjugationReaderConjugareRo();

	
	public void importNumerals( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					String str = page.get(i);
					if (!str.contains(" "))
					{
						System.out.println("\n - " + str);
						dbDriver.insertNumeral(str);
					}else{
						System.out.println("Skip wrong shit:"+str);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
			} else
				break;
		}
		dbDriver.databaseClose();
	}
	
	public void importConjunctions( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					String str = page.get(i);
					if (!str.contains(" "))
					{
						System.out.println("\n - " + str);
						dbDriver.insertConjunction(str);
					}else{
						System.out.println("Skip wrong shit:"+str);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
			} else
				break;
		}
		dbDriver.databaseClose();
	}
	
	public void importPronouns( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					String str = page.get(i);
					if (!str.contains(" "))
					{
						System.out.println("\n - " + str);
						dbDriver.insertPronoun(str);
					}else{
						System.out.println("Skip wrong shit:"+str);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
				//Thread.sleep(100);
			} else
				break;
		}
		dbDriver.databaseClose();
	}
	
	public void importAdverbs( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					String str = page.get(i);
					if (!str.contains(" "))
					{
						System.out.println("\n - " + str);
						dbDriver.insertAdverb(str);
					}else{
						System.out.println("Skip wrong shit:"+str);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
				//Thread.sleep(100);
			} else
				break;
		}
		dbDriver.databaseClose();
	}
	
	public void importAdjectives( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					String adjStr = page.get(i);
					if (!adjStr.contains(" "))
					{
						System.out.println("\n - " + adjStr);
						Adjective adj = adjReader.getAdjective(adjStr);
						if (adj != null)
						{
							dbDriver.insertAdjective(adj);
						} else
						{
							System.out.println("Cannot find ajective:" + page.get(i));
						}
					}else{
						System.out.println("Skip wrong shit:"+adjStr);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
				//Thread.sleep(100);
			} else
				break;
		}
		dbDriver.databaseClose();
	}
	
	public void importVerbs( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();

		String nextPage = startPage;
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					System.out.println("\n - " + page.get(i));
					Verb v = verbReader.getVerbConjugation(page.get(i));
					if (v != null)
					{
						System.out.println(v.toString());
						dbDriver.insertVerb(v);
					} else
					{
						System.out.println("Cannot find verb:" + page.get(i));
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
				Thread.sleep(1000);
			} else
				break;
		}
		dbDriver.databaseClose();
	}

	public void importSubstantives( String serverURL, String startPage, String startTag, String endTag, String nexPageTag) throws Exception
	{
		categoryReader.setTags(startTag, endTag, nexPageTag);

		dbDriver.databaseOpen();
		String nextPage = startPage;
		WatchDog wg = new WatchDog();
		wg.touch(nextPage, null);
		Thread t = new Thread(wg);
		t.start();
		while (nextPage != null)
		{
			ItemPage page = categoryReader.getPage(serverURL + nextPage);

			if (page != null)
			{
				for (int i = 0; i < page.itemCount(); i++)
				{
					try
					{
						String subst = page.get(i);
						if (!subst.contains(" "))
						{
							Substantive su = substReader.getSubstantive(subst);

							if (su != null)
							{
								//dbDriver.insertSubstantive(su);
								dbDriver.updateSubstantive(su);
								// System.out.println("\n - " + su.toString());
							} else
							{
								System.out.println("Cannot find substantive: " + page.get(i));
							}
						}
						wg.touch(nextPage, subst);
					} catch (SocketException ex)
					{
						System.out.println("Failed to download page for: " + page.get(i));
						i--;
						Thread.sleep(10000);
					} catch (SocketTimeoutException e)
					{
						System.out.println("Failed to download page for: " + page.get(i));
						i--;
						Thread.sleep(10000);
					}
				}
				nextPage = page.nextPage;
				dbDriver.databaseCommit();
			} else
				break;
		}
		wg.stop();
		dbDriver.databaseClose();
	}

	public static void main( String[] args)
	{
		String SERVER_URL = "https://ro.wiktionary.org";
		//String VERBS_LIST_PAGE = "/wiki/Categorie:Verbe_în_română";
		String SUBSTANTIVES_LIST_PAGE = "/w/index.php?title=Categorie:Substantive_în_română&pagefrom=telemea#mw-pages";// "/wiki/Categorie:Substantive_în_română"; 
		String ADJECTIVES_LIST_PAGE = "/wiki/Categorie:Adjective_în_română";
		
		String ADVERBS_LIST_PAGE 		= "/wiki/Categorie:Adverbe_în_română";
		String NUMERALS_LIST_PAGE 		= "/wiki/Categorie:Numerale_în_română";
		String CONJUNCTIONS_LIST_PAGE 	= "/wiki/Categorie:Conjuncții_în_română";
		String PRONOUNS_LIST_PAGE 		= "/wiki/Categorie:Pronume_în_română";
		
		DictionaryImporter importer = new DictionaryImporter();
		try
		{
//			importer.importVerbs(SERVER_URL,VERBS_LIST_PAGE,"pagina următoare", "pagina anterioară", "pagina următoare");
			importer.importSubstantives(SERVER_URL, SUBSTANTIVES_LIST_PAGE, "pagina următoare", "pagina anterioară", "pagina următoare");
//			importer.importPronouns(SERVER_URL, PRONOUNS_LIST_PAGE, "Următoarele", "Adus de la", "pagina următoare");
//			importer.importConjunctions(SERVER_URL, CONJUNCTIONS_LIST_PAGE, "Următoarele", "Adus de la", "pagina următoare");
//			importer.importNumerals(SERVER_URL, NUMERALS_LIST_PAGE, "Următoarele", "Adus de la", "pagina următoare");
//			importer.importAdverbs(SERVER_URL, ADVERBS_LIST_PAGE, "pagina următoare", "pagina anterioară", "pagina următoare");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
