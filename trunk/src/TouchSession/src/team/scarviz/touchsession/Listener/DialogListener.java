package team.scarviz.touchsession.Listener;

import java.util.EventListener;

import android.view.View;


public interface DialogListener extends EventListener {


    public void onPositiveClick(View v);

    public void onNegativeClick();

}
