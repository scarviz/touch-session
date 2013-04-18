package team.scarviz.touchsession.ui.Activity;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.ui.Fragment.MainFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

		SoundDto s = new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/1.wav");
 		s.setSoundId(10);
		s.insert(getApplicationContext());
		new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/2.wav");
 		s.setSoundId(11);
		s.insert(getApplicationContext());
		new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/3.wav");
 		s.setSoundId(12);
		s.insert(getApplicationContext());
		new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/4.wav");
 		s.setSoundId(13);
		s.insert(getApplicationContext());
		new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/5.wav");
 		s.setSoundId(14);
		s.insert(getApplicationContext());
		new SoundDto();
 		s.setSoundFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchsession/6.wav");
 		s.setSoundId(15);
		s.insert(getApplicationContext());

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

	public void transitSesssionEditFragment(){
		Intent in = new Intent(MainActivity.this,SessionEditActivity.class);
		in.setAction(Intent.ACTION_VIEW);
		startActivity(in);
	}
}
