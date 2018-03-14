package com.example.wordkiller;

import java.io.IOException;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestFragment extends Fragment {
	int index = 0;
	int wordIndex=0;
	FragmentManager fm;
	int masteredNum = 0;
	private Button tap;
	private TextView tv;
	String word ="";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/* get paras from meaning fragment */
		if (getArguments() != null) {
			index = (int) getArguments().getLong("index");
		}

		View view = inflater.inflate(R.layout.test_fragment, container, false);
		tap = (Button) view.findViewById(R.id.tap);
		tv = (TextView) view.findViewById(R.id.word);
		try {
			tv.setText(getWord());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MeaningFragment meaning = MeaningFragment.newInstance(index,
						wordIndex);
				meaning.setWordIndex(wordIndex);
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, meaning).commit();

			}
		});

		/* master check */
		Boolean masteredAll = false;
		try {
			masteredAll = masteredCheck();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.v("not going into masterCheck", "eeroro");
		}
		if (masteredAll) {
			dialog();
		}

		Button b = (Button) view.findViewById(R.id.progress);
		TextView state = (TextView) view.findViewById(R.id.state);
		Log.v("num", String.valueOf(masteredNum));
		// or use level list
		if (masteredNum >= 20)
			b.setBackground(getResources().getDrawable(R.drawable.item_20));
		if (masteredNum >= 40)
			b.setBackground(getResources().getDrawable(R.drawable.item_40));
		if (masteredNum >= 60)
			b.setBackground(getResources().getDrawable(R.drawable.item_60));
		if (masteredNum >= 80)
			b.setBackground(getResources().getDrawable(R.drawable.item_80));
		if (masteredNum == 100)
			b.setBackground(getResources().getDrawable(R.drawable.item_100));
		state.setText(masteredNum + " out of 100 done");
		return view;
	}

	private void dialog() {
		// TODO Auto-generated method stub
		final AlertDialog.Builder normalDialog = new AlertDialog.Builder(
				this.getActivity());
		normalDialog.setTitle("Congratulations");
		normalDialog.setMessage("You have mastered all the words in List"
				+ index + "!");
		normalDialog.show();
	}

	public boolean masteredCheck() throws IOException {
		/*
		 * whether words in the list are all mastered or not i is to count the
		 * number of mastered words, if the number reaches the size of the
		 * list,it means all the words have been mastered change the mastered
		 * property of list to track the number of mastered words
		 */
		Log.v("now your here", "eeroro");
		DBHelper dbhelper = new DBHelper(this.getActivity());
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor = db.rawQuery("SELECT mastered FROM List" + index, null);
		int mastered = 0;
		while (cursor.moveToNext()) {
			mastered = cursor.getInt(cursor.getColumnIndex("mastered"));
			if (mastered == 1)
				masteredNum++;
		}
		if (masteredNum > 1) {
			Log.v("masteredNum", String.valueOf(masteredNum));
		}
		if (masteredNum == 100) {
			return true;
		} else
			return false;
	}

	private CharSequence getWord() throws IOException {
		// TODO Auto-generated method stub
		DBHelper dbhelper = new DBHelper(this.getActivity());
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		wordIndex = new Random().nextInt(100) + 1;

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor = db.rawQuery("SELECT * FROM List" + index + " WHERE id="
				+ wordIndex, null);
		Log.v("wordIndex in test fragment", String.valueOf(wordIndex)+word);
		int rightTimes=0;
		if (cursor.moveToNext()) {//while
			/* There is no need to have mastered word come up again */
			rightTimes = cursor.getInt(cursor.getColumnIndex("rightTimes"));
			Log.v("right  in test fragment",String.valueOf(rightTimes));
//			if (rightTimes == 7){
//				Log.v("reset index", "7 right so wordindex reset");
//				getWord();
//			}
				
			word = cursor.getString(cursor.getColumnIndex("word"));
			Log.v("word in test fragment",word+wordIndex);
		}
		cursor.close();
		return word;
	}

	public static TestFragment newInstance(int index) {
		TestFragment fragment = new TestFragment();
		Bundle args = new Bundle();
		args.putLong("index", index);
		fragment.setArguments(args);
		return fragment;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
