package team.scarviz.touchsession.ui.Fragment;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Activity.SoundEditActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SoundEditFragment extends Fragment {

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

    public static SoundEditFragment newInstance() {
    	SoundEditFragment frag = new SoundEditFragment();
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

		v.findViewById(R.id.SoundEditViewbtnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				((SoundEditActivity)getActivity()).openSlider();
			}
		});
	}

	public void setText(String s){
		((TextView)getView().findViewById(R.id.SoundReadViewtxtMessage)).setText(s);
	}

	@Override
	public void onResume() {
	    super.onResume();
	}
}
