package team.scarviz.touchsession.ui.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {


    public static ProgressDialogFragment newInstance(String message) {
    	ProgressDialogFragment frag = new ProgressDialogFragment();
    	Bundle args = new Bundle();
		args.putString("message", message);
		frag.setArguments(args);
        return frag;
    }

    @Override
    public void onDestroyView() {
    	if(getDialog() != null && getRetainInstance())
    		getDialog().setDismissMessage(null);
    	super.onDestroyView();
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
	}

	        @Override
		        public Dialog onCreateDialog(Bundle savedInstanceState) {
		            ProgressDialog dialog = new ProgressDialog(getActivity());
		            dialog.setMessage(getArguments().getString("message"));
		            return dialog;
	        }
}
