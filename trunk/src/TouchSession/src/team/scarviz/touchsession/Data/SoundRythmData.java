package team.scarviz.touchsession.Data;

import java.util.ArrayList;
import java.util.List;


public class SoundRythmData {

	private int filterColor;
	private int soundId = -1;


	public int getFilterColor() {
		return filterColor;
	}

	public void setFilterColor(int filterColor) {
		this.filterColor = filterColor;
	}

	public int getSoundId() {
		return soundId;
	}

	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}

	public SoundRythmData(int filterColor, int soundId) {
		super();
		this.filterColor = filterColor;
		this.soundId = soundId;
	}

	public SoundRythmData() {
		super();
	}

	public static List<SoundRythmData> createSoundGridViewData(){

		List<SoundRythmData> retList = new ArrayList<SoundRythmData>(0);
		for(int i = 0;i<16;i++){
			retList.add(new SoundRythmData());
		}

		return retList;

	}

}
