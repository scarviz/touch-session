package team.scarviz.touchsession.ui.Fragment;


import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Listener.DialogListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class AlertDialogFragment extends DialogFragment {

	 private DialogListener listener = null;
	 private View mActionView = null;

    public static AlertDialogFragment newInstance(String title,String message) {
    	AlertDialogFragment frag = new AlertDialogFragment();
    	Bundle args = new Bundle();
		args.putString("message", message);
		args.putString("title", title);
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
	        	  final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
	              alertDialogBuilder.setTitle(getArguments().getString("title"));
	              alertDialogBuilder.setMessage(getArguments().getString("message"));
	              alertDialogBuilder.setIcon(R.drawable.ic_launcher);

	              alertDialogBuilder.setPositiveButton(android.R.string.ok,
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int whichButton) {
	                                listener.onPositiveClick(mActionView);
	                            }
	                        }
	                );
	                alertDialogBuilder.setNegativeButton(android.R.string.cancel,
	                        new DialogInterface.OnClickListener() {
	                            @Override
	                            public void onClick(DialogInterface dialog, int whichButton) {
	                                listener.onNegativeClick();
	                            }
	                        }
	                );

	            return alertDialogBuilder.create();
	        }

	        public void setDialogListener(DialogListener listener) {
	            this.listener = listener;
	        }

	        public void removeDialogListener() {
	            this.listener = null;
	        }

	        public void setActionView(View v){
	        	mActionView = v;
	        }
}
