package team.scarviz.touchsession.ui.Activity;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Fragment.SoundEditFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.navdrawer.SimpleSideDrawer;

public class SoundEditActivity extends FragmentActivity {

	SimpleSideDrawer mNav;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);

		mNav  =  new  SimpleSideDrawer(  this  );
		mNav.setLeftBehindContentView(  R.layout.sound_list_view  );

		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentById(R.id.rootLayout) != null){
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			SoundEditFragment fragment = SoundEditFragment.newInstance();
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}

	}

	public void openSlider(){
		if(mNav.isClosed())
			mNav.openLeftSide();
		else
			mNav.closeLeftSide();
	}
}
