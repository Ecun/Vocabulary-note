package edu.bsu.cs222.domain;

import java.io.Serializable;

public class Word implements Comparable<Word>,Serializable {
	private static final long serialVersionUID = -578702508165777658L;
	private String word;
	private String book;
	private String comment;
	private int flag;

	public Word(){
		
	}
	
	public Word(String word){
		this.word=word;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Word)){
			return false;
		}
		Word other=(Word) o;
		if(!word.equals(other.word)){
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(Word another) {
		int t = another.flag-flag;
		if (t == 0) {
			t = word.compareTo(another.word);
		}
		return t;
	}
}
