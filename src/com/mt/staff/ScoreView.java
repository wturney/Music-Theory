package com.mt.staff;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.mt.R;
import com.mt.theory.Accidental;
import com.mt.theory.Clef;
import com.mt.theory.KeySignature;
import com.mt.theory.Note;
import com.mt.theory.NoteGroup;
import com.mt.theory.Score;
import com.mt.theory.Tone;
import com.mt.utils.NoteUtil;

public class ScoreView extends View {

	private static final int STAFF_SPACING = 30;

	private static Drawable sharpDrawable;
	private static Drawable flatDrawable;

	private Score score;

	private Paint paint;

	public ScoreView(Context context, Score score) {
		super(context);
		this.score = score;
		this.paint = new Paint();
		paint.setColor(0xFFFFFFFF);

		if (sharpDrawable == null || flatDrawable == null) {
			sharpDrawable = getResources().getDrawable(R.drawable.sharp);
			flatDrawable = getResources().getDrawable(R.drawable.flat);
		}
	}

	private void drawKey(Canvas canvas) {
		if (score.getKeySignature() == null || score.getClef() == null) {
			return;
		}

		Drawable d;

		KeySignature key = score.getKeySignature();
		Accidental aType = key.getAccidentalType();
		Clef clef = score.getClef();
		int verticalCenter = (canvas.getClipBounds().height() / 2);
		int minKeyPosition;
		int maxKeyPosition;

		if (aType.equals(Accidental.FLAT)) {
			d = flatDrawable;
			minKeyPosition = clef.getMinFlatKeyPosition();
			maxKeyPosition = clef.getMaxFlatKeyPosition();
		} else {
			d = sharpDrawable;
			minKeyPosition = clef.getMinSharpKeyPosition();
			maxKeyPosition = clef.getMaxSharpKeyPosition();
		}

		float dRatio = (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight();
		float heightMod = aType.equals(Accidental.FLAT) ? 0.75f : 0.50f;
		int height = (int) (2.25 * STAFF_SPACING);
		int width = (int) (dRatio * height);
		int left = 150;
		int right;
		int top;
		int bottom;

		// Key signatures have a particular order and the symbols don't always
		// stay within a given octave for a particular clef. To solve the min
		// and max positions of the key signature are hard-coded for each
		// supported clef.
		for (Tone tone : key.getAccidentals()) {
			int positionValue = -1;
			int totalOffset;
			int octave = 4;
			while (positionValue < minKeyPosition || positionValue > maxKeyPosition) {
				positionValue = NoteUtil.getPositionValue(octave, tone);
				if (positionValue >= minKeyPosition && positionValue <= maxKeyPosition) {
					// Draw
					totalOffset = NoteUtil.getStepsFromStaffCenter(positionValue, clef);
					top = (int) (verticalCenter + ((totalOffset * (STAFF_SPACING / 2)) * -1) - (heightMod * height));
					bottom = top + height;
					right = left + width;

					d.setBounds(left, top, right, bottom);
					d.draw(canvas);

					left += 25;
				} else if (positionValue < minKeyPosition) {
					octave++;
				} else {
					octave--;
				}
			}
		}
	}

	private void drawNoteGroup(NoteGroup noteGroup, Canvas canvas) {
		Note note = noteGroup.getNotes().get(0);

		Rect bounds = canvas.getClipBounds();
		int verticalCenter = (bounds.height() / 2);

		int totalOffset = NoteUtil.getStepsFromStaffCenter(note, score.getClef());

		// Determine Actual Note
		int noteHeadTop = verticalCenter + ((totalOffset * (STAFF_SPACING / 2)) * -1);
		int noteHeadVerticalCenter = (int) (noteHeadTop + ((.9 * STAFF_SPACING) / 2));

		// Is this note higher / lower than the note at the staff center?
		boolean higherTone = (totalOffset >= 0);

		// Ledger Lines
		int ledgerLineCount = NoteUtil.getLedgerCount(note, score.getClef());
		boolean ledgerLines = ledgerLineCount > 0;

		if (ledgerLines) {
			int ledgerLeft = -10;
			int ledgerRight = 60;
			int offsetShift = higherTone ? STAFF_SPACING * -1 : STAFF_SPACING;
			for (int i = 0; i < ledgerLineCount; i++) {
				int ledgerTop = verticalCenter + (offsetShift * (3 + i));
				canvas.drawLine(ledgerLeft, ledgerTop, ledgerRight, ledgerTop, paint);
			}
		}

		int tempAlpha = paint.getAlpha();
		paint.setStrokeWidth(2.0f);

		// Draw Note Stem
		int stemTop = higherTone ? noteHeadVerticalCenter - 10 : noteHeadVerticalCenter - (3 * STAFF_SPACING) - 10;
		int stemBottom = higherTone ? noteHeadVerticalCenter + (3 * STAFF_SPACING) - 10 : noteHeadVerticalCenter - 10;
		int stemLeft = higherTone ? 5 : 43;

		// If the note shows ledger lines, the stem goes all the way to the
		// staff center
		stemTop = (ledgerLines && !higherTone) ? verticalCenter : stemTop;
		stemBottom = (ledgerLines && higherTone) ? verticalCenter : stemBottom;

		canvas.drawLine(stemLeft, stemTop, stemLeft, stemBottom, paint);

		paint.setStrokeWidth(1.0f);

		// Rotate & Draw Note Head
		canvas.save();
		canvas.translate(0, noteHeadTop - 5);
		canvas.rotate(-20);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		canvas.drawOval(new RectF(new Rect(0, 0, (int) (1.4 * STAFF_SPACING), (int) (0.9 * STAFF_SPACING))), paint);

		paint.setFlags(~Paint.ANTI_ALIAS_FLAG);
		paint.setAlpha(tempAlpha);
		canvas.restore();
	}

	private void drawStave(Canvas canvas, int left, int top, int right, int bottom) {
		// Draw Horizontal Staff Lines
		for (int i = 0; i < 5; i++) {
			int verticalPosition = top + (i * STAFF_SPACING);
			canvas.drawLine(left, verticalPosition, right, verticalPosition, paint);
		}

		// Draw Left Staff Boundry Line
		canvas.drawLine(left, top, left, bottom, paint);

		// Draw Thick Double Bar
		paint.setStrokeWidth(10.0f);
		canvas.drawLine(right - 5, top, right - 5, bottom, paint);
		paint.setStrokeWidth(1.0f);
		canvas.drawLine(right - 15, top, right - 15, bottom, paint);

		// Draw Clef
		Drawable d = getResources().getDrawable(score.getClef().getResourceId());
		float clefRatio = (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth();
		int clefWidth = (int) (2.7 * STAFF_SPACING);
		int clefHeight = (int) (clefRatio * clefWidth);

		int clefLeft = left + 25;
		int clefRight = clefLeft + clefWidth;

		int clefUpperPortionHeight = (int) (score.getClef().getRatioAboveMiddle() * clefHeight);
		int clefTop = bottom - ((bottom - top) / 2) - clefUpperPortionHeight;
		int clefBottom = clefTop + clefHeight;

		d.setBounds(clefLeft, clefTop, clefRight, clefBottom);
		d.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Staff Bounds
		Rect bounds = canvas.getClipBounds();
		int canvasVerticalCenter = (bounds.height() / 2);
		int canvasHorizontalCenter = (getWidth() / 2);
		int staffTop = (int) (canvasVerticalCenter - (STAFF_SPACING * 2));

		// Draw Staff
		drawStave(canvas, 20, staffTop, bounds.width() - 20, staffTop + STAFF_SPACING * 4);

		// Draw Key Signature
		drawKey(canvas);

		// Draw Notes
		canvas.save();
		canvas.translate(canvasHorizontalCenter - 15, 0);
		for (NoteGroup noteGroup : score.getNoteGroups()) {
			drawNoteGroup(noteGroup, canvas);
		}
		canvas.restore();

		LinearGradient lg = new LinearGradient(0,
			50,
			0,
			canvas.getClipBounds().height() - 50,
			0x00000000,
			0x66000000,
			Shader.TileMode.CLAMP);
		paint.setShader(lg);
		canvas.drawPaint(paint);
		paint.reset();
	}

}
