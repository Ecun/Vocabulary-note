package edu.bsu.cs222.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.bsu.cs222.domain.Word;

public class WordDao extends BaseDao {

	public WordDao(Context context) {
		super(context);
	}

	public void add(Word word){
		ContentValues values=new ContentValues();
		values.put("word", word.getWord());
		values.put("book", word.getBook());
		values.put("comment", word.getComment());
		values.put("flag", word.getFlag());
		db.insert("word", null, values);
	}
	
	public void edit(Word word){
		ContentValues values=new ContentValues();
		values.put("book", word.getBook());
		values.put("comment", word.getComment());
		values.put("flag", word.getFlag());
		db.update("word", values, "word=?", new String[]{word.getWord()});		
	}
	
	public void delete(String word){
		db.delete("word", "word=?", new String[]{word});
	}
	
	public void clear(){
		db.delete("word", null, null);
	}
	
	public Word get(String word){
		String sql="select word,book,comment,flag from word where word=?";
		Cursor cursor=db.rawQuery(sql,new String[]{word});
		if (cursor.moveToNext()){
			return mapRow(cursor);
		}
		return null;
	}

	public List<Word> list(){
		List<Word> words=new ArrayList<Word>();
		String sql="select word,book,comment,flag from word";
		Cursor cursor=db.rawQuery(sql,null);
		while (cursor.moveToNext()){
			words.add(mapRow(cursor));
		}
		Collections.sort(words);
		return words;
	}

	public List<Word> search(String word){
		List<Word> words=new ArrayList<Word>();
		String sql="select word,book,comment,flag from word where word like ?";
		Cursor cursor=db.rawQuery(sql,new String[]{"%"+word+"%"});
		while (cursor.moveToNext()){
			words.add(mapRow(cursor));
		}
		Collections.sort(words);
		return words;
	}

	private Word mapRow(Cursor cursor) {
		Word word=new Word();
		word.setWord(cursor.getString(0));
		word.setBook(cursor.getString(1));
		word.setComment(cursor.getString(2));
		word.setFlag(cursor.getInt(3));
		return word;
	}
}
