package team.scarviz.touchsession.ui.Activity;


import net.kazzz.felica.FeliCaException;
import net.kazzz.felica.FeliCaTag;
import net.kazzz.felica.command.ReadResponse;
import net.kazzz.felica.lib.FeliCaLib;
import net.kazzz.felica.lib.FeliCaLib.ServiceCode;
import net.kazzz.felica.suica.Suica;
import team.scarviz.touchsession.R;
import team.scarviz.touchsession.ui.Fragment.SoundReadFragment;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class SoundReadActivity extends FragmentActivity {

	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private Tag mNfcTag;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);
		Intent intent = getIntent();
        String action = intent.getAction();

		FragmentManager fm = getSupportFragmentManager();
		SoundReadFragment fragment;
		if(fm.findFragmentById(R.id.rootLayout) != null){
			fragment = (SoundReadFragment)fm.findFragmentById(R.id.rootLayout);
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			fragment = SoundReadFragment.newInstance();
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}

    	mNfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    	if(mNfcTag != null){
    		try{
    		long lastblance = readHistoryData();
    		fragment.setmZandaka((int)lastblance);
    		}catch(Exception e){

    		}
    	}


/*    	 if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
    		        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
    		        || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
    		            // IDmを表示させる
    	//	            	((SoundReadFragment)fm.findFragmentById(R.id.rootLayout)).setText(idm);

    	//	            }
    		        }
    		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
    		mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
    		new Intent(getApplicationContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
*/
	}

/**
 * FeliCa 使用履歴を読み込みます
 *
 * @return
 */
protected long readHistoryData() throws Exception {

    try {
        FeliCaTag f = new FeliCaTag(this.mNfcTag);

        //polling は IDm、PMmを取得するのに必要
        f.polling(FeliCaLib.SYSTEMCODE_PASMO);

        //read
        ServiceCode sc = new ServiceCode(FeliCaLib.SERVICE_SUICA_HISTORY);
        byte addr = 0;
        ReadResponse result = f.readWithoutEncryption(sc, addr);

        StringBuilder sb = new StringBuilder();
        while ( result != null && result.getStatusFlag1() == 0  ) {
      //      sb.append("履歴 No.  " + (addr + 1) + "\n");
       //     sb.append("---------\n");
       //     sb.append("\n");
            Suica.History s = new Suica.History(result.getBlockData(), this);
      //      sb.append(s.getBalance());
      //      sb.append("---------------------------------------\n");
      //      sb.append("\n");
            return s.getBalance();


//            addr++;
            //Log.d(TAG, "addr = " + addr);
//            result = f.readWithoutEncryption(sc, addr);
        }

        String str = sb.toString();
        Log.d("TAG", str);
        return -1;
    } catch (FeliCaException e) {
        e.printStackTrace();
        Log.e("TAG", "readHistoryData", e);
        throw e;
    }
}


					/**
				* フォアグラウンドディスパッチシステムで、アプリ起動時には優先的にNFCのインテントを取得するように設定する
				*/
				private void setNfcIntentFilter(Activity activity, NfcAdapter nfcAdapter, PendingIntent seder) {
				// NDEF type指定
				IntentFilter typeNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
				try {
				typeNdef.addDataType("*/*");
				} catch (MalformedMimeTypeException e) {
				e.printStackTrace();
				}
				// NDEF スキーマ(http)指定
				IntentFilter httpNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
				httpNdef.addDataScheme("http");
				IntentFilter[] filters = new IntentFilter[] {
				typeNdef, httpNdef
				};
				// TECH指定
				String[][] techLists = new String[][] {
				new String[] { IsoDep.class.getName() },
				new String[] { NfcA.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { NfcF.class.getName() },
				new String[] { NfcV.class.getName() },
				new String[] { Ndef.class.getName() },
				new String[] { NdefFormatable.class.getName() },
				new String[] { MifareClassic.class.getName() },
				new String[] { MifareUltralight.class.getName() }
				};
				nfcAdapter.enableForegroundDispatch(activity, seder, filters, techLists);
				}

}
