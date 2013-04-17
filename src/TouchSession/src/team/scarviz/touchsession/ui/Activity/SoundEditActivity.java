package team.scarviz.touchsession.ui.Activity;


import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Adapter.SoundListAdapter;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.ui.Fragment.SoundEditFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.navdrawer.SimpleSideDrawer;

public class SoundEditActivity extends FragmentActivity {

	SimpleSideDrawer mNav;

	List<SoundDto> mSoundDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);

		mNav  =  new  SimpleSideDrawer(  this  );
		mNav.setLeftBehindContentView(  R.layout.sound_list_view  );

		FragmentManager fm = getSupportFragmentManager();

		if(mSoundDatas == null)
			mSoundDatas = SoundDto.getAllData(getApplicationContext());

		if(mSoundDatas.size() <= 0){
			Toast.makeText(getApplicationContext(), "音データが一つもありません", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}


		if(fm.findFragmentById(R.id.rootLayout) != null){
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			SoundEditFragment fragment = SoundEditFragment.newInstance(mSoundDatas.get(0));
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}



		setSoundListAdapter();
	}

	public void openSlider(){
		if(mNav.isClosed())
			mNav.openLeftSide();
		else
			mNav.closeLeftSide();
	}

	/**
	 * サウンドリストアダプターの設定
	 */
	public void setSoundListAdapter(){
		ListView lView = (ListView)mNav.findViewById(R.id.SoundListViewList);
		SoundListAdapter adapter = new SoundListAdapter(getApplicationContext(),mSoundDatas );
		lView.setAdapter(adapter);
		lView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				SoundListAdapter adap = (SoundListAdapter)adapterView.getAdapter();
				SoundDto sDto = adap.getItem(pos);
				FragmentManager fm = getSupportFragmentManager();
				((SoundEditFragment)fm.findFragmentById(R.id.rootLayout)).setSound(sDto);
				mNav.closeLeftSide();
			}
		});

	}
}
