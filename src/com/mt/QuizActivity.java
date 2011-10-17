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

	/**
	 * Implementing classs should use this method to do any necessary view
	 * manipulation for displaying the question
	 */
	public abstract void displayQuestion();

	public void displayResult(ImageView result) {
		FrameLayout frame = (FrameLayout) findViewById(R.id.question_frame);
		frame.removeAllViews();
		frame.addView(result);
	}

	/**
	 * This method is used to determine if the resource selected by the user was
	 * in fact the correct answer to the question.
	 * 
	 * @param clickedResourceId
	 *            The resource selected by the player as their answer to the
	 *            current question
	 * @return True only if the provided resource id matches the resource
	 *         associated with the correct answer.
	 */
	public abstract boolean evaluateAnswer(int clickedResourceId);

	/**
	 * Generates whatever musical notation is necessary to represent this
	 * question. E.g. Note quiz randomly selects a Clef, Octave and Tone to be
	 * displayed to a user on the staff.
	 * 
	 * @return A Score object representing the musical notation to be displayed
	 *         for this question.
	 */
	public abstract Score generateQuestion();

	/**
	 * Returns the current question displayed to the user
	 * 
	 * @return
	 */
	public Score getCurrentQuestion() {
		return currentQuestion;
	}

	/**
	 * The media player associated with this quiz. Implementing classes should
	 * reset and reuse this rather than creating multiple instances.
	 * 
	 * @return
	 */
	public MediaPlayer getMediaPlayer() {
		return mp;
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

	/**
	 * Called after a user has selected the correct answer, and before the next
	 * question has been generated.
	 */
	public void onCorrectAnswer() {
		currentQuestion = generateQuestion();
		displayResult(correctImage);
	}

	/**
	 * Called after the user has selected an incorrect answer, and before the
	 * question is redisplayed
	 */
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
