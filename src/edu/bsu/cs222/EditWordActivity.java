package edu.bsu.cs222;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.study.word.R;

import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Word;

public class EditWordActivity extends Activity {

	private Word word;
	private TextView tvWord;
	private EditText etBook;
	private EditText etComment;
	private WordDao wordDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_word);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tvWord = (TextView) findViewById(R.id.tv_word);
		etBook = (EditText) findViewById(R.id.et_book);
		etComment = (EditText) findViewById(R.id.et_comment);
		
		Intent intent=getIntent();
		word=(Word) intent.getSerializableExtra("word");
		tvWord.setText("Word: "+word.getWord());
		etBook.setText(word.getBook());
		etComment.setText(word.getComment());
		
		wordDao=new WordDao(this);
	}

	@Override
	protected void onDestroy() {
		wordDao.closeDB();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.edit_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_save:
			save();
			return true;
		case R.id.action_delete:
			delete();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void save() {
		word.setBook(etBook.getText().toString());
		word.setComment(etComment.getText().toString());
		wordDao.edit(word);
		Intent data=new Intent();
		data.putExtra("operate", "edit");
		data.putExtra("word", word);
		setResult(RESULT_OK, data);
		finish();		
	}

	private void delete() {
		wordDao.delete(word.getWord());
		Intent data=new Intent();
		data.putExtra("operate", "delete");
		data.putExtra("word", word);
		setResult(RESULT_OK, data);
		finish();
	}
}
