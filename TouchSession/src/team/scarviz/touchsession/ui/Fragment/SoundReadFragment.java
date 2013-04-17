package team.scarviz.touchsession.ui.Fragment;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Dto.SoundDto;
import team.scarviz.touchsession.Utility.HttpUtility;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SoundReadFragment extends Fragment {

	private boolean isActive = false;
	ProgressDialogFragment mDialog;

	private int mZandaka = -1;


	public int getmZandaka() {
		return mZandaka;
	}

	public void setmZandaka(int mZandaka) {
		this.mZandaka = mZandaka;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		 setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		View v = inflater.inflate(R.layout.sound_read_view, container,false);
		initDataView(v);
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
		if(mZandaka >= 0)
			this.uploadSoundData(mZandaka);
	}
	@Override
	public void onResume() {
	    super.onResume();
	    if ( !isActive ) {
	        final DialogFragment df = ( DialogFragment )
	                getFragmentManager().findFragmentByTag( "progresss" );
	        if ( df != null ) {
	            df.dismiss();
	        }
	    }
	}

	/**
	 * サウンドデータのアップロード
	 * @param zandaka
	 */
	public void uploadSoundData(int zandaka){
  		new OnSave(getActivity(),zandaka).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}




	/**
	 * �f�[�^�X�V����
	 */
	private class OnSave extends AsyncTask<Void,Void,SoundDto>{
		Context mContext;
		int mZandaka;

		public OnSave(Context con,int zandaka) {
			mContext = con;
			mZandaka = zandaka;
		}

		@Override
		protected SoundDto doInBackground(Void... params) {
			return HttpUtility.uploadSoundData(mContext, mZandaka);
		}

		/**
		 *
		 */
		@Override
		protected void onPreExecute() {
			mDialog = ProgressDialogFragment.newInstance("曲データを生成しています");
			mDialog.setCancelable(false);
			mDialog.show(getFragmentManager(), "progress");
			isActive = true;
		}

			/**
		 * �I���㏈��
		 */
		@Override
		protected void onPostExecute(SoundDto ret)
		{
			if(mDialog != null){
				try{
					mDialog.dismiss();
					}catch(Exception e){
					}
			}

			if(isAdded()){
				if(ret != null){
					Toast.makeText(getActivity(), "曲データを生成しました" ,Toast.LENGTH_SHORT).show();
					ret.insert(getActivity());
					getActivity().finish();
				}
				else{
					Toast.makeText(getActivity(), "曲データの生成に失敗しました", Toast.LENGTH_SHORT).show();
					getActivity().finish();
				}
			}
			isActive = false;
		}
	}







}
