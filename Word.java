package com.example.wordkiller;

public class Word {
	private String word;
	private String english;
	private String chinese;
	int mastered;
	int rightTimes;
	int wrongTimes;

	public Word(String word, String english, String chinese, int mastered,
			int rightTimes, int wrongTimes) {
		this.setChinese(chinese);
		this.setEnglish(english);
		this.mastered = mastered;
		this.rightTimes = rightTimes;
		this.wrongTimes = wrongTimes;
		this.setWord(word);
	}

	public Word(String word, String english, String chinese) {
		this.setChinese(chinese);
		this.setEnglish(english);
		this.setWord(word);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}
}
