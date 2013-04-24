package team.scarviz.touchsession.Adapter;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Data.SoundRhythmData;
import team.scarviz.touchsession.Dto.SoundDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public class SoundRhythmGridAdapter extends ArrayAdapter<SoundRhythmData> {
	protected LayoutInflater mInflater;
	private List<SoundRhythmData> mItems;
	private SoundDto mSoundDto;
	ViewHolder viewHolder;


	static class ViewHolder{
		public ImageButton ImageRhytmView;
	}

	/**
	 * �R���X�g���N�^
	 * @param context			�R���e�L�X�g
	 * @param rowLayoutResourceId	���\�[�XID
	 * @param items					�A�C�e��
	 */
	public SoundRhythmGridAdapter(Context context,List<SoundRhythmData> items,SoundDto sDto) {
		super(context, 0, items);
		this.mItems = items;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSoundDto = sDto;
	}

	public void setSoundDto(SoundDto sDto){
		mSoundDto = sDto;
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
			viewHolder.ImageRhytmView = (ImageButton)view.findViewById(R.id.SoundEditViewGridItemImageView);
			view.setTag(viewHolder);
		} else {
			view = convertView;
   		}
		if(mItems.get(position).getSoundId() >= 0)
			((ViewHolder)view.getTag()).ImageRhytmView.setBackgroundColor(mItems.get(position).getFilterColor());

		((ViewHolder)view.getTag()).ImageRhytmView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				   mItems.get(position).setSoundId(mSoundDto.getSoundId());
				   mItems.get(position).setFilterColor(mSoundDto.getSoundColor());
				   //adapter.notifyDataSetChanged();
				   notifyDataSetChanged();
			}
		});

		return view;
	}

}