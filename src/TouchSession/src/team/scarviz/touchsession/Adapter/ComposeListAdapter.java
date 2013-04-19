package team.scarviz.touchsession.Adapter;

import java.util.List;

import team.scarviz.touchsession.R;
import team.scarviz.touchsession.Data.CompositionAdapterData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ComposeListAdapter  extends ArrayAdapter<CompositionAdapterData>{

	protected LayoutInflater mInflater;
	private List<CompositionAdapterData> mItems;
	ViewHolder viewHolder;


	static class ViewHolder{
		public CompoundButton CheckBox;
	}

	/**
	 *
	 * @param context
	 * @param items
	 */
	public ComposeListAdapter(Context context,List<CompositionAdapterData> items) {
		super(context, 0, items);
		this.mItems = items;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	/**
	 *
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view ;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.compose_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.CheckBox = (CompoundButton)view.findViewById(R.id.ComposeListItemCheckBox);
			view.setTag(viewHolder);
		} else {
			view = convertView;
   		}
		((ViewHolder)view.getTag()).CheckBox.setChecked(mItems.get(position).isCheck());
		((ViewHolder)view.getTag()).CheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean flg) {
				mItems.get(position).setCheck(flg);
			}
		});

		((ViewHolder)view.getTag()).CheckBox.setText(mItems.get(position).getTitle());
		return view;
	}

}
