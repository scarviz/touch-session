package team.scarviz.touchsession.Adapter;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Dto.SoundDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SoundListAdapter extends ArrayAdapter<SoundDto> {
	protected LayoutInflater mInflater;
	private List<SoundDto> mItems;
	ViewHolder viewHolder;


	static class ViewHolder{
		public TextView TextSoundName;
		public ImageView ImageBarColor;
	}

	/**
	 * コンストラクタ
	 * @param context				コンテキスト
	 * @param rowLayoutResourceId	リソースID
	 * @param items					アイテム
	 */
	public SoundListAdapter(Context context,List<SoundDto> items) {
		super(context, 0, items);
		this.mItems = items;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	/**
	 * View表示時に呼ばれる
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view ;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.sound_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.TextSoundName = (TextView)view.findViewById(R.id.SoundListItemText);
			viewHolder.ImageBarColor = (ImageView)view.findViewById(R.id.SoundListItemColorBar);
			view.setTag(viewHolder);
		} else {
			view = convertView;
   		}
		((ViewHolder)view.getTag()).TextSoundName.setText("Sound" + mItems.get(position).getId());
		((ViewHolder)view.getTag()).ImageBarColor.setColorFilter(mItems.get(position).getSoundColor(),android.graphics.PorterDuff.Mode.SRC_ATOP);
		
		
		return view;
	}
}
