package team.scarviz.touchsession.Data;

import java.util.ArrayList;
import java.util.List;


public class SoundRhythmData {

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

	public SoundRhythmData(int filterColor, int soundId) {
		super();
		this.filterColor = filterColor;
		this.soundId = soundId;
	}

	public SoundRhythmData() {
		super();
	}

	public static List<SoundRhythmData> createSoundGridViewData(){

		List<SoundRhythmData> retList = new ArrayList<SoundRhythmData>(0);
		for(int i = 0;i<16;i++){
			retList.add(new SoundRhythmData());
		}

		return retList;

	}

	public static List<Integer> getUniqueSoundId(List<SoundRhythmData> datas){

		//入っていないか、もしくは同じデータは入れない
		List<Integer> retList = new ArrayList<Integer>();
		for(SoundRhythmData sound : datas){
			if(retList.contains(sound.soundId) || sound.soundId < 0) continue;
				retList.add(sound.soundId);
		}
		return retList;
	}

}
