package team.scarviz.touchsession.ui.Fragment;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Adapter.ComposeListAdapter;
import team.scarviz.touchsession.Data.CompositionAdapterData;
import team.scarviz.touchsession.Listener.AudioPlayStopListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class SessionPlayFragment extends Fragment {

	List<CompositionAdapterData> mCompositionDatas = null;

	ProgressDialogFragment mProgressDialog;
	boolean isPlay = false;
	boolean isDialogActive = false;
	int stopCount = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.session_play_view, container,false);
		setHasOptionsMenu(true);
		if(mCompositionDatas == null){
			mCompositionDatas= CompositionAdapterData.getAllCompositionData(getActivity());
			for(CompositionAdapterData data : mCompositionDatas){
				data.setAudioPlayStopListener(new onAudioPlayStopListener());
			}
		}
		initView(v);

		return v;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu,
			MenuInflater inflater) {
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		return super.onOptionsItemSelected(item);
	}



    public static SessionPlayFragment newInstance() {
    	SessionPlayFragment frag = new SessionPlayFragment();
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

		ListView lView = (ListView)v.findViewById(R.id.SessionPlayViewComposeListView);
		ComposeListAdapter adapter = new ComposeListAdapter(getActivity(), mCompositionDatas);
		lView.setAdapter(adapter);
		v.findViewById(R.id.SessionPlayViewbtnPlayStop).setOnClickListener(new onPlayStopButtonClickListener());

	}

	private class onPlayStopButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!isPlay){
				for(CompositionAdapterData data : mCompositionDatas){
					if(data.isCheck()){
						data.play(getActivity());
						isPlay = true;
					}
				}

				if(!isPlay){
					//not checked
					Toast.makeText(getActivity(), "一つも選択されていません", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				for(CompositionAdapterData data : mCompositionDatas){
					data.stop();
				}
			}
		}
	}


	private class onAudioPlayStopListener implements AudioPlayStopListener{
		@Override
		public void onStop(int soundId) {
			boolean isNowPlaying = false;
			for(CompositionAdapterData adapter : mCompositionDatas){
				if(!adapter.isCheck()) continue;
				if(adapter.isPlay())
					isNowPlaying = true;
			}

			if(!isNowPlaying)
				isPlay = false;
		}
	}



	@Override
	public void onResume() {
	    super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		for(CompositionAdapterData data : mCompositionDatas){
			data.stop();
		}
	}
}
