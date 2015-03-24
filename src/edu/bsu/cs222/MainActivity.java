package edu.bsu.cs222;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.word.R;

import edu.bsu.cs222.dao.WordDao;
import edu.bsu.cs222.domain.Word;

public class MainActivity extends Activity {

	private List<Word> words;
	private WordDao wordDao;
	private EditText etWord;
	private ListView lvWord;
	private BaseAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		etWord = (EditText) findViewById(R.id.et_word);
		lvWord = (ListView) findViewById(R.id.lv_word);
		wordDao=new WordDao(this);
		words=wordDao.list();
		initListView();
	}

	private void initListView() {
		adapter=new BaseAdapter(){

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public int getCount() {
				if(words==null||words.size()==0){
					return 0;
				}
				return words.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=getLayoutInflater().inflate(R.layout.list_item, parent,false);
				}
				TextView tvWord=(TextView) convertView.findViewById(R.id.tv_word);
				TextView tvBook=(TextView) convertView.findViewById(R.id.tv_book);
				TextView tvComment=(TextView) convertView.findViewById(R.id.tv_comment);
				ImageView ivIcon=(ImageView) convertView.findViewById(R.id.iv_icon);
				final Word word=words.get(position);
				tvWord.setText(word.getWord());
				if(word.getFlag()==0){
					ivIcon.setImageResource(R.drawable.star_empty);
				}else{
					ivIcon.setImageResource(R.drawable.star);
				}
				if(TextUtils.isEmpty(word.getBook())){
					tvBook.setVisibility(View.GONE);
					tvBook.setText("");
				}else{
					tvBook.setVisibility(View.VISIBLE);
					tvBook.setText("Book: "+word.getBook());
				}
				if(TextUtils.isEmpty(word.getComment())){
					tvComment.setVisibility(View.GONE);
					tvComment.setText("");
				}else{
					tvComment.setVisibility(View.VISIBLE);
					tvComment.setText("Comment: "+word.getComment());
				}
				tvWord.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						Intent intent=new Intent(MainActivity.this,EditWordActivity.class);
						intent.putExtra("word", word);
						startActivityForResult(intent, 0);
					}
					
				});
				ivIcon.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						word.setFlag(1-word.getFlag());
						wordDao.edit(word);
						Collections.sort(words);
						adapter.notifyDataSetChanged();
						lvWord.setSelection(words.indexOf(word));
					}
					
				});
				return convertView;
			}
			
		};
		lvWord.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		wordDao.closeDB();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_search:
			search();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void add_click(View v) {
		String wordStr=etWord.getText().toString().toLowerCase(Locale.getDefault());
		if(TextUtils.isEmpty(wordStr)){
			Toast.makeText(this, "pleast input word!", Toast.LENGTH_SHORT).show();
			return;
		}
		Word word=wordDao.get(wordStr);
		if(word!=null){
			Toast.makeText(this, wordStr+" exists!", Toast.LENGTH_SHORT).show();
			return;
		}
		word=new Word();
		word.setWord(wordStr);
		wordDao.add(word);
		words.add(word);
		Collections.sort(words);
		adapter.notifyDataSetChanged();
		etWord.setText("");
		etWord.requestFocus();
	}

	private void search() {
		String wordStr=etWord.getText().toString().toLowerCase(Locale.getDefault());
		if(TextUtils.isEmpty(wordStr)){
			Toast.makeText(this, "pleast input word!", Toast.LENGTH_SHORT).show();
			
		}
		words=wordDao.search(wordStr);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK&&data!=null){
			String operate=data.getStringExtra("operate");
			Word word=(Word) data.getSerializableExtra("word");
			int index=words.indexOf(word);
			if(operate.equals("delete")){
				words.remove(index);
			}else{
				words.set(index, word);
			}
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
