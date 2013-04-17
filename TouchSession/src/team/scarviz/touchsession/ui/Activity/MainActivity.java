package team.scarviz.touchsession.ui.Activity;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Fragment.MainFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);

		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentById(R.id.rootLayout) != null){
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			MainFragment fragment = MainFragment.newInstance();
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}
	}

	public void transitSoundReadFragment(){
		Intent in = new Intent(MainActivity.this,SoundReadActivity.class);
		in.setAction(Intent.ACTION_VIEW);
		startActivity(in);
	}

	public void transitSoundEditFragment(){
		Intent in = new Intent(MainActivity.this,SoundEditActivity.class);
		in.setAction(Intent.ACTION_VIEW);
		startActivity(in);
	}
}
