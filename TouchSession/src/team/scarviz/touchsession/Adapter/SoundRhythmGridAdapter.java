package team.scarviz.touchsession.Adapter;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Data.SoundRythmData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class SoundRhythmGridAdapter extends ArrayAdapter<SoundRythmData> {
	protected LayoutInflater mInflater;
	private List<SoundRythmData> mItems;
	ViewHolder viewHolder;


	static class ViewHolder{
		public ImageView ImageRhytmView;
	}

	/**
	 * �R���X�g���N�^
	 * @param context			�R���e�L�X�g
	 * @param rowLayoutResourceId	���\�[�XID
	 * @param items					�A�C�e��
	 */
	public SoundRhythmGridAdapter(Context context,List<SoundRythmData> items) {
		super(context, 0, items);
		this.mItems = items;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}


	/**
	 * �r���[�̕\���A�r���[�̎擾�����߂��邽�ьĂяo�����
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view ;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.sound_edit_view_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ImageRhytmView = (ImageView)view.findViewById(R.id.SoundEditViewGridItemImageView);
			view.setTag(viewHolder);
		} else {
			view = convertView;
   		}
		if(mItems.get(position).getSoundId() >= 0)
			((ViewHolder)view.getTag()).ImageRhytmView.setColorFilter(mItems.get(position).getFilterColor());

		return view;
	}
}
