package com.example.wordkiller;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TestActivity extends Activity {
	int index;
	TextView tv1;
	TextView tv2;
	Word[] words = new Word[100];
	int wordIndex;
	private ProgressBar pb;
	// final Intent intent = new Intent();
	private TestFragment test;
	private MeaningFragment meaning;
	FragmentManager fm;
	Boolean mode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// pb = (ProgressBar) findViewById(R.id.progress);
		Bundle bd = this.getIntent().getExtras();
		index = bd.getInt("index");
		mode = bd.getBoolean("mode");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		setDefaultFragment();
		tv2 = (TextView) findViewById(R.id.textView2);

	}

	private void setDefaultFragment() {

		/* set test Fragment as default fragment */

		TestFragment test = new TestFragment();
		test.setIndex(index);
		getFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, test).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.back:
			if (!mode) {
				Intent intent = new Intent(TestActivity.this,
						ListActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(TestActivity.this,
						TaskListActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.search:
			Uri uri = Uri.parse("http://www.dictionary.com");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void share() {

	}
}
