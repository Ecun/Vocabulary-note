package edu.bsu.cs222.test;

import java.util.List;

import android.test.AndroidTestCase;
import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Word;

public class TestWordDao extends AndroidTestCase {
	
	private WordDao wordDao;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wordDao=new WordDao(getContext());
		wordDao.clear();
	}
	
	@Override
	protected void tearDown() throws Exception {
		wordDao.closeDB();
		super.tearDown();
	}

	public void testClear() throws Exception{
		wordDao.clear();
		assertEquals(0, wordDao.list().size());
	}

	public void testAdd() throws Exception{
		wordDao.add(new Word("dog"));
		wordDao.add(new Word("cat"));
		List<Word> words=wordDao.list();
		assertEquals(2, words.size());
	}
	
	public void testDelete(){
		wordDao.add(new Word("dog"));
		wordDao.add(new Word("cat"));
		wordDao.delete("dog");
		assertEquals(1, wordDao.list().size());
	}

	public void testEdit(){
		Word word=new Word("dog");
		wordDao.add(word);
		word.setBook("book");
		word.setComment("comment");
		word.setFlag(1);
		wordDao.edit(word);
		word=wordDao.get("dog");
		assertEquals("dog",word.getWord());
		assertEquals("book",word.getBook());
		assertEquals("comment",word.getComment());
		assertEquals(1,word.getFlag());
	}

	public void testGet() throws Exception{
		wordDao.add(new Word("dog"));
		Word word=wordDao.get("dog");
		assertEquals("dog", word.getWord());
	}
	
	public void testList() throws Exception{
		wordDao.add(new Word("dog"));
		wordDao.add(new Word("cat"));
		List<Word> words=wordDao.list();
		assertEquals(2, words.size());
		assertEquals("cat", words.get(0).getWord());
		assertEquals("dog", words.get(1).getWord());
	}
	
	public void testSearch() throws Exception{
		wordDao.add(new Word("dog"));
		wordDao.add(new Word("cat"));
		wordDao.add(new Word("book"));
		List<Word> words=wordDao.search("o");
		assertEquals(2, words.size());
		assertEquals("book", words.get(0).getWord());
		assertEquals("dog", words.get(1).getWord());
	}

}
