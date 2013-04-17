package team.scarviz.touchsession.ui.Fragment;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Activity.MainActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class MainFragment extends Fragment {


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
		View v = inflater.inflate(R.layout.main_view, container,false);
		initView(v);
		return v;
	}

    public static MainFragment newInstance() {
    	MainFragment frag = new MainFragment();
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
		v.findViewById(R.id.MainViewbtnNFC).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				((MainActivity)getActivity()).transitSoundReadFragment();
			}
		});

		v.findViewById(R.id.MainViewbtnEdit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				((MainActivity)getActivity()).transitSoundEditFragment();
			}
		});
	}

	@Override
	public void onResume() {
	    super.onResume();
	}
}
