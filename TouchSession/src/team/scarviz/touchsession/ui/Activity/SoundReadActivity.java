package team.scarviz.touchsession.ui.Activity;


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

public class SoundReadActivity extends FragmentActivity {

	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_activity);
		Intent intent = getIntent();
        String action = intent.getAction();

		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentById(R.id.rootLayout) != null){
		}
		else{
			FragmentTransaction ft = fm.beginTransaction();
			SoundReadFragment fragment = SoundReadFragment.newInstance();
			ft.add(R.id.rootLayout, fragment);
			ft.commit();
		}

    	Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    	if(tag != null){
    	NfcF techF = NfcF.get(tag);
    	byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    	byte[] pmm = techF.getManufacturer();
    	byte[] systemCode = techF.getSystemCode();

    	tvRewrite(idm,pmm,systemCode);
    	}


    	 if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
    		        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
    		        || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
    		            // IDmを表示させる
    		            String idm = getIdm(getIntent());
    		            if (idm != null) {
    		            	((SoundReadFragment)fm.findFragmentById(R.id.rootLayout)).setText(idm);
    		            }
    		        }
    		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
    		mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
    		new Intent(getApplicationContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

	}

	private String getIdm(Intent intent) {
			String idm = null;
			StringBuffer idmByte = new StringBuffer();
			byte[] rawIdm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
			if (rawIdm != null) {
			for (int i = 0; i < rawIdm.length; i++) {
			idmByte.append(Integer.toHexString(rawIdm[i] & 0xff));
			}
			idm = idmByte.toString();
			}
				return idm;
	}

	public void tvRewrite(byte[] idm, byte[] pmm, byte[] systemCode) {

        StringBuffer sb = new StringBuffer();
        sb.append("IDm: ");
        for(int i = 0; i < idm.length; i++) {
        	sb.append(String.format("%02X",idm[i]));
        }
        sb.append("\n");
        sb.append("PMm: ");
        for(int i = 0; i < pmm.length; i++) {
        	sb.append(String.format("%02X",pmm[i]));
        }
        sb.append("\n");
        sb.append("System Code: ");
        for(int i = 0; i < systemCode.length; i++) {
        	sb.append(String.format("%02X",systemCode[i]));
        }
        sb.append("\n");

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.rootLayout) != null){
        	((SoundReadFragment)fm.findFragmentById(R.id.rootLayout)).setText(sb.toString());
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
