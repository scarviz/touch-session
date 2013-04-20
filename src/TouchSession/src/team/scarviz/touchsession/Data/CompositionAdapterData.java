package team.scarviz.touchsession.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import team.scarviz.touchsession.Dto.CompositionDto;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.Listener.AudioPlayStopListener;
import team.scarviz.touchsession.Utility.JsonUtil;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.widget.Toast;


public class CompositionAdapterData extends CompositionDto {

	AudioPlayStopListener listener;
	private boolean isCheck;
	private boolean isPlay = false;
	SoundPool mSePlayer = null;
	HashMap<Integer, Integer> mSoundList;
	private static int MAX_STREAMS = 10;
	int mSelectedViewIndex = 0;
	Timer mTimer = new Timer();
	Handler mHandler = new Handler();
	int mCompletedCount = 0;

	List<SoundRhythmData> mRhythmData = null;



	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}



	public CompositionAdapterData() {
		super();
	}

	public static List<CompositionAdapterData> getAllCompositionData(Context con){
		List<CompositionAdapterData> retList = new ArrayList<CompositionAdapterData>();

		for(CompositionDto dto : getAllData(con)){
			CompositionAdapterData data = new CompositionAdapterData();
			data.setCheck(false);
			data.setComp_ms_id(dto.getComp_ms_id());
			data.setCompositionJson(dto.getCompositionJson());
			data.setEditing(dto.getEditing());
			data.setId(dto.getId());
			data.setRhythm(dto.getRhythm());
			data.setTitle(dto.getTitle());

			retList.add(data);
		}

		return retList;
	}


	public void play(Context con){
		isPlay = true;
		mSoundList = new HashMap<Integer, Integer>();
		mCompletedCount = 0;
		mSePlayer =  new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);


		mRhythmData = createRhythmData();
		List<Integer> soundList = SoundRhythmData.getUniqueSoundId(mRhythmData);
		if(soundList.size() <= 0) {
			Toast.makeText(con, "一つも音が配置されていません", Toast.LENGTH_SHORT).show();
			return;
		}
		mSePlayer.setOnLoadCompleteListener(new onSoundLoadCompletedListener());
		for( int id : soundList){
			SoundDto sDto = SoundDto.getSoundData(con, id);
			int key = mSePlayer.load(sDto.getSoundFilePath(), 0);
			mSoundList.put(sDto.getSoundId(), key);
		}
	}

	public void stop(){
		if(mSePlayer != null)
			mSePlayer.release();
		if(mTimer != null)
			mTimer.cancel();
	    mTimer = null;
	    isPlay = false;
	    if(listener != null) listener.onStop(getComp_ms_id());
	}

	/**
	 * Json for Rhythm Create
	 * @return
	 */
	private List<SoundRhythmData> createRhythmData(){
		try{
			return JsonUtil.createRhythmJson(this.getCompositionJson());
		}catch(Exception e){

		}
		return null;
	}

	/**
	 * 全ての音がロードされた場合に処理を開始する
	 * @author katsuki-nakatani
	 *
	 */
	private class onSoundLoadCompletedListener implements OnLoadCompleteListener{
		@Override
		public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

			//タイマーの初期化処理
			mCompletedCount++;
			int millisec = 1000;
			int work = (int)getRhythm() - 1000;
			if(work < 0)
				work /= 2;
			millisec += work;
			//タイマーの初期化処理
			mCompletedCount++;
			//全てのSEが読み込まれたらタイマー開始
			if(mCompletedCount >= mSoundList.size()){
				mTimer = new Timer();
				mSelectedViewIndex = 0;
				 mTimer.schedule( new TimerTask(){
			        @Override
			        public void run() {
			            // mHandlerを通じてUI Threadへ処理をキューイング
			            mHandler.post( new Runnable() {
			                public void run() {
			                	if(mSelectedViewIndex >= mRhythmData.size()){
			                		stop();
			                    	return;
			                    }
			                    if(mRhythmData.get(mSelectedViewIndex).getSoundId()>= 0){
			                    	mSePlayer.play(mSoundList.get(mRhythmData.get(mSelectedViewIndex).getSoundId()),1,1,1,0,1);
			                    }
			                    //実行間隔分を加算処理
			                    mSelectedViewIndex++;
			                }
			            });
			        }
			    }, 100, millisec);
			}
		}
	}

    public void setAudioPlayStopListener(AudioPlayStopListener listener) {
        this.listener = listener;
    }

    public void removeDialogListener() {
        this.listener = null;
    }

}
