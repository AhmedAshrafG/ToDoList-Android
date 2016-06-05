package com.example.ahmadz.todolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmadz.todolist.Callbacks.TodoItemListener;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmadz on 3/7/16.
 */
public class TodoListAdapter extends BaseAdapter {

	private List<TodoItemModel> todoItems;
	private LayoutInflater inflater;
	private TodoItemListener todoItemListener;

	public TodoListAdapter(Context mContext, List<TodoItemModel> todoItems, TodoItemListener todoItemListener){
		inflater = LayoutInflater.from(mContext);
		this.todoItems = todoItems;
		this.todoItemListener = todoItemListener;
		inflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		return todoItems.size();
	}

	@Override
	public Object getItem(int position) {
		return todoItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null){
			//initialize convertView and save it in a viewHolder for later usage.
			convertView = inflater.inflate(R.layout.todo_item_layout, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		}else{
			//already there, get it!
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//set tags.
		viewHolder.setAllTag(position);
		//set values.
		viewHolder.setTitle((String)getItem(position));
		//long click callback trigger.
		viewHolder.itemContainer.setOnLongClickListener(v -> {
			int tag = (int)v.getTag();
			if (todoItemListener != null)
				todoItemListener.itemLongPressed(tag);
			return true;
		});
		//single click callback trigger.
		viewHolder.itemContainer.setOnClickListener(v -> {
			int tag = (int)v.getTag();
			if (todoItemListener != null)
				todoItemListener.itemSinglePressed(tag);
		});

		return convertView;
	}

	static class ViewHolder{
		@BindView(R.id.title_tv)
		TextView itemTitle;
		@BindView(R.id.item_container)
		View itemContainer;

		public ViewHolder(View view){
			ButterKnife.bind(this, view);
		}

		public void setTitle(String title) {
			itemTitle.setText(title);
		}

		public void setAllTag(int position){
			itemTitle.setTag(position);
			itemContainer.setTag(position);
		}
	}
}
