package com.mt.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class MidiTrack {

	private static final int[] FOOTER = new int[] {
		0x01, 0xFF, 0x2F, 0x00 };

	private static final int[] HEADER = new int[] {
		0x4d, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x01, 0x00, 0x10, 0x4d, 0x54, 0x72, 0x6B };

	private static final int[] KEY_SIGNATURE = new int[] {
		0x00, 0xFF, 0x59, 0x02, 0x00, 0x00 };
	private static final int MICROSECONDS_PER_MINUTE = 60000000;

	private static final int[] TIME_SIGNATURE = new int[] {
		0x00, 0xFF, 0x58, 0x04, 0x04, 0x02, 0x30, 0x08 };

	protected static byte[] intArrayToBytes(int[] data) {
		byte[] out = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			out[i] = (byte) data[i];
		}
		return out;
	}

	protected Vector<int[]> playEvents;

	public MidiTrack() {
		playEvents = new Vector<int[]>();
	}

	public void noteOff(int delta, int pitch) {
		playEvents.add(new int[] {delta, 0x81, pitch, 0x0});
	}

	public void noteOn(int delta, int pitch, int velocity) {
		playEvents.add(new int[] {delta, 0x91, pitch, velocity});
	}

	public void addNote(int duration, int pitch, int velocity) {
		noteOn(0x0, pitch, velocity);
		noteOff(duration, pitch);
	}

	public void progChange(int program) {
		playEvents.add(new int[] {0x0, 0xC0, program});
	}

	public void setBPM(int bpm) {
		int mpqn = MICROSECONDS_PER_MINUTE / bpm;
		if (mpqn > 8355711) {
			mpqn = 8355711;
		} else if (mpqn < 100000) {
			mpqn = 100000;
		}

		int tempoEvent[] = new int[7];
		tempoEvent[0] = 0x00;
		tempoEvent[1] = 0xFF;
		tempoEvent[2] = 0x51;
		tempoEvent[3] = 0x03;
		for (int i = 0; i < 3; i++) {
			int offset = (2 - i) * 8;
			tempoEvent[i + 4] = (byte) ((mpqn >>> offset) & 0xFF);
		}

		playEvents.add(tempoEvent);
	}

	public void writeToFile(String filename, File directory) throws IOException {
		File file = new File(directory, filename);

		FileOutputStream out = new FileOutputStream(file, false);

		out.write(intArrayToBytes(HEADER));

		int size = KEY_SIGNATURE.length + TIME_SIGNATURE.length + FOOTER.length;

		for (int i = 0; i < playEvents.size(); i++)
			size += playEvents.elementAt(i).length;

		int high = size / 256;
		int low = size - (high * 256);
		out.write((byte) 0);
		out.write((byte) 0);
		out.write((byte) high);
		out.write((byte) low);

		out.write(intArrayToBytes(KEY_SIGNATURE));
		out.write(intArrayToBytes(TIME_SIGNATURE));

		for (int i = 0; i < playEvents.size(); i++) {
			out.write(intArrayToBytes(playEvents.elementAt(i)));
		}

		out.write(intArrayToBytes(FOOTER));
		out.close();
	}
}
