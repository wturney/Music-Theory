package com.mt;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.theory.Score;

public abstract class QuizActivity extends Activity implements OnClickListener {

	private ImageView correctImage;
	private Score currentQuestion;

	private ImageView incorrectImage;
	private MediaPlayer mp;
	private CountDownTimer nextQuestionViewTimer = new CountDownTimer(1000, 500) {

		@Override
		public void onFinish() {
			displayQuestion();
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	};

	private int questionNumber;

	public abstract void displayQuestion();

	public void displayResult(ImageView result) {
		FrameLayout frame = (FrameLayout) findViewById(R.id.question_frame);
		frame.removeAllViews();
		frame.addView(result);
	}

	public abstract boolean evaluateAnswer(int clickedResourceId);

	public abstract Score generateQuestion();

	public Score getCurrentQuestion() {
		return currentQuestion;
	}

	public MediaPlayer getMediaPlayer() {
		return mp;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	@Override
	public void onClick(View v) {
		if (evaluateAnswer(v.getId())) {
			onCorrectAnswer();
		} else {
			onIncorrectAnswer();
		}

		nextQuestionViewTimer.start();
	}

	public void onCorrectAnswer() {
		currentQuestion = generateQuestion();
		questionNumber++;
		displayResult(correctImage);
	}

	public void onIncorrectAnswer() {
		displayResult(incorrectImage);
	}

	public boolean questionReady() {
		return currentQuestion != null;
	}

	public void setCurrentQuestion(Score currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mp = new MediaPlayer();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		currentQuestion = savedInstanceState.getParcelable("score");
		questionNumber = savedInstanceState.getInt("qnum");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (correctImage == null) {
			correctImage = new ImageView(getApplicationContext());
			correctImage.setScaleType(ScaleType.CENTER);
			correctImage.setImageResource(R.drawable.correct);

			incorrectImage = new ImageView(getApplicationContext());
			incorrectImage.setScaleType(ScaleType.CENTER);
			incorrectImage.setImageResource(R.drawable.incorrect);
		}

		if (!questionReady()) {
			currentQuestion = generateQuestion();
		}

		displayQuestion();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("score", currentQuestion);
		outState.putInt("qnum", questionNumber);
		super.onSaveInstanceState(outState);
	}
}
