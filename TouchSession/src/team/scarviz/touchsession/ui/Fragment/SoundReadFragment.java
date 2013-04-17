package team.scarviz.touchsession.ui.Fragment;

import team.scarviz.touchsession.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SoundReadFragment extends Fragment {


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
		View v = inflater.inflate(R.layout.sound_read_view, container,false);
		return v;
	}

    public static SoundReadFragment newInstance() {
    	SoundReadFragment frag = new SoundReadFragment();
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
	private void initDataView(View v){
	}

	public void setText(String s){
		((TextView)getView().findViewById(R.id.SoundReadViewtxtMessage)).setText(s);
	}

	@Override
	public void onResume() {
	    super.onResume();
	}

}
