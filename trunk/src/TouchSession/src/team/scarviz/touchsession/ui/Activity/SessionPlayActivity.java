package team.scarviz.touchsession.ui.Activity;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Fragment.SessionPlayFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class SessionPlayActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);

		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentById(R.id.rootLayout) != null){
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			SessionPlayFragment fragment = SessionPlayFragment.newInstance();
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}
	}
}
