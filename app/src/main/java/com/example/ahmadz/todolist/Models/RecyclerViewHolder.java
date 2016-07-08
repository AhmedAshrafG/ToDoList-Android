package com.example.ahmadz.todolist.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmadz.todolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmadz on 7/6/16.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{
	@Bind(R.id.title_tv) public TextView titleField;
	@Bind(R.id.date_tv) public TextView dateField;
	@Bind(R.id.priority_tv) public TextView priorityField;
	@Bind(R.id.text_container) public View itemContainer;
	@Bind(R.id.btn_remove) public ImageButton removeBtn;

	public RecyclerViewHolder(View root) {
		super(root);
		ButterKnife.bind(this, root);
	}

	public void setTitle(String title) {
		titleField.setText(title);
	}

	public void setDate(TodoItemModel todoDate){
		dateField.setText(todoDate.getDateAndTimeFormatted());
	}

	public void setPriority(Context mContext, int priority){
		switch (priority){
			case 0:
				priorityField.setText("Low");
				priorityField.setTextColor(mContext.getResources().getColor(R.color.low));
				break;
			case 1:
				priorityField.setText("Medium");
				priorityField.setTextColor(mContext.getResources().getColor(R.color.medium));
				break;
			case 2:
				priorityField.setText("High");
				priorityField.setTextColor(mContext.getResources().getColor(R.color.high));
				break;
		}
	}

	public void setAllTag(int position){
		titleField.setTag(position);
		dateField.setTag(position);
		priorityField.setTag(position);
		itemContainer.setTag(position);
		removeBtn.setTag(position);
	}
}
