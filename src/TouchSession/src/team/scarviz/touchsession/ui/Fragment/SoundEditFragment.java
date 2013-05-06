package team.scarviz.touchsession.ui.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Adapter.SoundRhythmGridAdapter;
import team.scarviz.touchsession.Data.SoundRhythmData;
import team.scarviz.touchsession.Dto.CompositionDto;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.Listener.DialogListener;
import team.scarviz.touchsession.Utility.HttpUtility;
import team.scarviz.touchsession.Utility.JsonUtil;
import team.scarviz.touchsession.Utility.StringUtility;
import team.scarviz.touchsession.ui.Activity.SoundEditActivity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SoundEditFragment extends Fragment {

	List<SoundRhythmData> mRhythmData = SoundRhythmData.createSoundGridViewData();

	SoundDto mSoundData;
	Timer mTimer = new Timer();
	Handler mHandler = new Handler();
	int mSelectedViewIndex = 0;
	ProgressDialogFragment mProgressDialog;
	boolean isPlay = false;
	boolean isDialogActive = false;
	private static int MAX_STREAMS = 10;
	SoundPool mSePlayer = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
	HashMap<Integer, Integer> mSoundList;
	int mCompletedCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sound_edit_view, container,false);
		setHasOptionsMenu(true);
		initView(v);
		return v;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu,
			MenuInflater inflater) {
		//
		menu.add("update")
        .setIcon(R.drawable.ic_menu_upload)
        .setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {

		if(StringUtility.compare("update",item.getTitle())){
			List<Integer> soundList = SoundRhythmData.getUniqueSoundId(mRhythmData);
			if(soundList.size() <= 0) {
				Toast.makeText(getActivity(), "一つも音が配置されていません", Toast.LENGTH_SHORT).show();
			}
			else if(StringUtility.isNullOrEmpty(((EditText)getView().findViewById(R.id.SoundEditViewbtnComposeTitle)).getText().toString())){
				Toast.makeText(getActivity(), "リズム名が未入力です。", Toast.LENGTH_SHORT).show();
			}else{
				AlertDialogFragment frag = AlertDialogFragment.newInstance("","リズムデータをアップロードします。よろしいですか？" );
				frag.setDialogListener(new DialogListener() {

					@Override
					public void onPositiveClick(View v) {
						CompositionDto compData = new CompositionDto();
						compData.setTitle(((EditText)getView().findViewById(R.id.SoundEditViewbtnComposeTitle)).getText().toString());
						compData.setRhythm(((SeekBar)getView().findViewById(R.id.SoundEditViewTimeSeekBar)).getProgress());
						try{
							compData.setCompositionJson(JsonUtil.createJsonRhythm(mRhythmData));
						} catch (JSONException e) {
							Toast.makeText(getActivity(), "JSONデータの生成に失敗", Toast.LENGTH_SHORT).show();
							return;
						}

						new OnSave(getActivity(), compData).execute();

					}

					@Override
					public void onNegativeClick() {
						// TODO 自動生成されたメソッド・スタブ

					}
				});
				frag.setActionView(getView());
				frag.show(getFragmentManager(), "alertDialog");
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void setSound(SoundDto sDto){
		mSoundData = sDto;
		setSoundView(getView());
		GridView gView = (GridView)getView().findViewById(R.id.SoundEditViewGridView);
		SoundRhythmGridAdapter adapter = (SoundRhythmGridAdapter)gView.getAdapter();
		adapter.setSoundDto(mSoundData);

	}

	private void setSoundView(View v){
		((TextView)v.findViewById(R.id.SoundEditViewTextName)).setText("Sound" + mSoundData.getId());
		((TextView)v.findViewById(R.id.SoundEditViewTextName)).setTextColor(mSoundData.getSoundColor());
	}

    public static SoundEditFragment newInstance(SoundDto sDto) {
    	SoundEditFragment frag = new SoundEditFragment();
    	frag.mSoundData = sDto;
    	Bundle args = new Bundle();
		frag.setArguments(args);
        return frag;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void setPlayEnabled(boolean bool){
		getView().findViewById(R.id.SoundEditViewbtnSoundItem).setEnabled(bool);
		getView().findViewById(R.id.SoundEditViewGridView).setEnabled(bool);
		getView().findViewById(R.id.SoundEditViewTimeSeekBar).setEnabled(bool);
	}

	/**
	 *
	 * @param v
	 */
	private void initView(View v){

		//サウンド初期セット
		if(StringUtility.isNullOrEmpty(((TextView)v.findViewById(R.id.SoundEditViewTextName)).getText())){
			setSoundView(v);
		}

		//SlideDrawer引出処理
		v.findViewById(R.id.SoundEditViewbtnSoundItem).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				((SoundEditActivity)getActivity()).openSlider();
			}
		});

		GridView gView = (GridView)v.findViewById(R.id.SoundEditViewGridView);
		SoundRhythmGridAdapter adapter = new SoundRhythmGridAdapter(getActivity(), mRhythmData,mSoundData);
		gView.setAdapter(adapter);
		v.findViewById(R.id.SoundEditViewbtnPlayStop).setOnClickListener(new onPlayStopButtonClickListener());

	}

	private class onPlayStopButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!isPlay)
				play();
			else
				stop();
		}
	}



	private void play(){
		setPlayEnabled(false);
		mSoundList = new HashMap<Integer, Integer>();
		mCompletedCount = 0;
		mSePlayer =  new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		List<Integer> soundList = SoundRhythmData.getUniqueSoundId(mRhythmData);
		if(soundList.size() <= 0) {
			Toast.makeText(getActivity(), "一つも音が配置されていません", Toast.LENGTH_SHORT).show();
			setPlayEnabled(true);
			return;
		}
		mSePlayer.setOnLoadCompleteListener(new onSoundLoadCompletedListener());
		for( int id : soundList){
			SoundDto sDto = SoundDto.getSoundData(getActivity(), id);
			int key = mSePlayer.load(sDto.getSoundFilePath(), 0);
			mSoundList.put(sDto.getSoundId(), key);
		}
		isPlay = true;
	}

	private void stop(){
		mSePlayer.release();
		if(mTimer != null)
			mTimer.cancel();
	    mTimer = null;
	    setPlayEnabled(true);
		isPlay = false;
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
			int work = ((SeekBar)getView().findViewById(R.id.SoundEditViewTimeSeekBar)).getProgress() - 1000;
			if(work < 0)
				work /= 2;
			millisec += work;

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
			                    	mTimer.cancel();
			                    	mTimer = null;
			                    	setPlayEnabled(true);
			                    	mSePlayer.release();
			                    	Toast.makeText(getActivity(), "再生を終了しました", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onResume() {
	    super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		stop();
	}


	/**
	 * save
	 */
	private class OnSave extends AsyncTask<Void,Void,CompositionDto>{
		Context mContext;
		CompositionDto mCompData;

		public OnSave(Context con,CompositionDto compData) {
			mContext = con;
			mCompData = compData;
		}

		@Override
		protected CompositionDto doInBackground(Void... params) {
			return HttpUtility.uploadCompositionData(mContext, mCompData);
		}

		/**
		 *
		 */
		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialogFragment.newInstance("リズムデータをアップロードしています");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show(getFragmentManager(), "progress");
			isDialogActive = true;
		}

		// upload proc
		@Override
		protected void onPostExecute(CompositionDto ret)
		{
			if(mProgressDialog != null){
				try{
					mProgressDialog.dismiss();
					}catch(Exception e){
					}
			}

			if(isAdded()){
				if(ret != null){
					Toast.makeText(getActivity(), "リズムデータをアップロードしました" ,Toast.LENGTH_SHORT).show();
					//DB登録
					ret.insert(getActivity());
					getActivity().finish();
				}
				else{
					Toast.makeText(getActivity(), "リズムデータのアップロードに失敗しました", Toast.LENGTH_SHORT).show();
					getActivity().finish();
				}
			}
			isDialogActive = false;
		}
	}
}
