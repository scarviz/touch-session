package team.scarviz.touchsession.ui.Fragment;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Adapter.SoundRhythmGridAdapter;
import team.scarviz.touchsession.Data.SoundRythmData;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.Utility.StringUtility;
import team.scarviz.touchsession.ui.Activity.SoundEditActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class SoundEditFragment extends Fragment {

	List<SoundRythmData> mRhythmData = SoundRythmData.createSoundGridViewData();

	SoundDto mSoundData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		 setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		View v = inflater.inflate(R.layout.sound_edit_view, container,false);
		initView(v);
		return v;
	}

	public void setSound(SoundDto sDto){
		mSoundData = sDto;
		setSoundView(getView());
	}

	private void setSoundView(View v){
		((Button)v.findViewById(R.id.SoundEditViewbtnSoundItem)).setText("Sound" + mSoundData.getId());
		((Button)v.findViewById(R.id.SoundEditViewbtnSoundItem)).setBackgroundColor(mSoundData.getSoundColor());
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

	/**
	 *
	 * @param v
	 */
	private void initView(View v){

		//初期サウンドが選択されていない場合の処理
		if(StringUtility.isNullOrEmpty(((Button)v.findViewById(R.id.SoundEditViewbtnSoundItem)).getText())){
			setSoundView(v);
		}

		//音をタップした際にSlideDrawerを開く
		v.findViewById(R.id.SoundEditViewbtnSoundItem).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				((SoundEditActivity)getActivity()).openSlider();
			}
		});

		GridView gView = (GridView)v.findViewById(R.id.SoundEditViewGridView);
		SoundRhythmGridAdapter adapter = new SoundRhythmGridAdapter(getActivity(), mRhythmData);
		gView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
	    super.onResume();
	}
}
