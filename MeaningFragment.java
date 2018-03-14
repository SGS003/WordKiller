package com.example.wordkiller;

import java.io.IOException;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MeaningFragment extends Fragment {
	private Button known;
	private Button unknown;
	int index;
	int wordIndex;
	Word w = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.meaning_fragment, container,
				false);
		known = (Button) view.findViewById(R.id.known);
		unknown = (Button) view.findViewById(R.id.unknown);
		Log.v("wordIndex in meaning fragment", String.valueOf(wordIndex));
		known.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					modifyDB();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TestFragment test = new TestFragment().newInstance(index);
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, test).commit();
			}

		});
		unknown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TestFragment test = new TestFragment().newInstance(index);
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment_container, test).commit();
			}
		});
		// get paras from test fragment
		if (getArguments() != null) {
			index = (int) getArguments().getLong("index");
			Log.v("index", String.valueOf(index));
			wordIndex = (int) getArguments().getLong("wordIndex");
			Log.v("wordindex", String.valueOf(wordIndex));
		}
		try {
			getWord();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView word = (TextView) view.findViewById(R.id.word);
		word.setText(w.getWord());
		TextView chinese = (TextView) view.findViewById(R.id.chinese);
		chinese.setText(w.getChinese());
		TextView english = (TextView) view.findViewById(R.id.english);
		english.setText(w.getEnglish());
		return view;
	}

	private Word getWord() throws IOException {
		// TODO Auto-generated method stub

		DBHelper dbhelper = new DBHelper(this.getActivity());
		dbhelper.createDataBase();
		dbhelper.openDataBase();

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);
		Cursor cursor = db.rawQuery("SELECT * FROM List" + index + " WHERE id="
				+ wordIndex, null);
		Log.v("number", String.valueOf(cursor.getCount()));

		while (cursor.moveToNext()) {
			String word = cursor.getString(cursor.getColumnIndex("word"));
			String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
			String english = cursor.getString(cursor.getColumnIndex("english"));
			int rightTimes = cursor.getInt(cursor.getColumnIndex("rightTimes"));
			int wrongTimes = cursor.getInt(cursor.getColumnIndex("wrongTimes"));
			int mastered = cursor.getInt(cursor.getColumnIndex("mastered"));
			w = new Word(word, chinese, english, mastered, rightTimes,
					wrongTimes);
			Log.e("Master IN MEANING!!!!", String.valueOf(w.mastered));
		}
		cursor.close();

		return w;
	}

	private void modifyDB() throws IOException {
		Log.v("modifydb", "modifydb");
		/*
		 * user choose "i know this word" update the rightTimes property Of a
		 * word, if the rightTimes has reached 6, the mastered property need to
		 * be changed to true if it is right again
		 */
		DBHelper dbhelper = new DBHelper(this.getActivity());
		dbhelper.createDataBase();
		dbhelper.openDataBase();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				DBHelper.DB_PATH + "/" + DBHelper.DB_NAME, null);

		Cursor cursor = db.rawQuery("SELECT * FROM List" + index + " WHERE id="
				+ wordIndex + " ", null);
		int rightTimes = 0;
		while (cursor.moveToNext()) {
			
			rightTimes = cursor.getInt(cursor.getColumnIndex("rightTimes"));
			Log.v("modifydb", rightTimes+"get the current right times");
		}
		rightTimes++;
		Log.v("modifydb", String.valueOf(rightTimes));
		db.execSQL("UPDATE List" + index + " SET rightTimes=" + rightTimes
				+ " WHERE id=" + wordIndex);

		if (rightTimes == 7) {
			Log.v("modifydb", rightTimes+"update mastered to 1");
			db.execSQL("UPDATE List" + index + " SET mastered=1 WHERE id="
					+ wordIndex);
			/*
			 * change column mastered value in list table add one to the the
			 * number of mastered word of the current list
			 */
			cursor = db.rawQuery("SELECT mastered from list WHERE id=" + index,
					null);
			int mastered = 0;
			while (cursor.moveToNext()) {
				
				mastered = cursor.getInt(cursor.getColumnIndex("mastered"));
				Log.v("modifydb", mastered+"get the number of mastered words in this list");
			}
			mastered++;
			Log.v("modifydb", mastered+"update mastered to mastered+1");
			db.execSQL("UPDATE List SET mastered=" + mastered + " WHERE id="
					+ index);
		}
	}

	public static MeaningFragment newInstance(int index, int wordIndex) {
		MeaningFragment fragment = new MeaningFragment();
		Bundle args = new Bundle();
		args.putLong("index", index);
		args.putLong("wordIndex", wordIndex);
		fragment.setArguments(args);
		return fragment;
	}
	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}

}
