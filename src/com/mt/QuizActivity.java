package com.mt;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mt.theory.Score;

public abstract class QuizActivity extends Activity implements OnClickListener {

	private ImageView correctImage;
	private ImageView incorrectImage;

	private Score currentQuestion;

	private CountDownTimer nextQuestionViewTimer = new CountDownTimer(1000, 500) {

		@Override
		public void onFinish() {
			displayQuestion();
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	};

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

	@Override
	public void onClick(View v) {
		if (evaluateAnswer(v.getId())) {
			currentQuestion = generateQuestion();
			displayResult(correctImage);
		} else {
			displayResult(incorrectImage);
		}

		nextQuestionViewTimer.start();
	}

	public boolean questionReady() {
		return currentQuestion != null;
	}

	public void setCurrentQuestion(Score currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		currentQuestion = savedInstanceState.getParcelable("score");

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

		super.onSaveInstanceState(outState);
	}
}
