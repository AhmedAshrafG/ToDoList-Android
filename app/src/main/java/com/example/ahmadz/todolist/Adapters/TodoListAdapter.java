package com.example.ahmadz.todolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ahmadz.todolist.Callbacks.TodoItemListener;
import com.example.ahmadz.todolist.Models.TodoDate;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmadz on 3/7/16.
 */
public class TodoListAdapter extends BaseAdapter {

	private List<TodoItemModel> todoItems;
	private LayoutInflater inflater;
	private TodoItemListener todoItemListener;
	private Context mContext;

	public TodoListAdapter(Context mContext, List<TodoItemModel> todoItems, TodoItemListener todoItemListener){
		this.mContext = mContext;
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
		TodoItemModel todoItem = getTodoItem(position);
		viewHolder.setTitle(todoItem.getTitle());
		viewHolder.setDate(todoItem.getTodoDate());
		viewHolder.setPriority(todoItem.getPriority());
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
		//remove button click callback trigger.
		viewHolder.removeBtn.setOnClickListener(v -> {
			int tag = (int)v.getTag();
			if (todoItemListener != null)
				todoItemListener.itemRemovePressed(tag);
		});

		return convertView;
	}

	public void setData(List<TodoItemModel> todoItems) {
		this.todoItems = todoItems;
		notifyDataSetChanged();
	}

	public void removeItem(int position){
		todoItems.remove(position);
		notifyDataSetChanged();
	}

	public void editItemTitle(long ID, String title){
		TodoItemModel item = getItemByID(ID);
		if (item != null) {
			item.setTitle(title);
			notifyDataSetChanged();
		}
	}

	private TodoItemModel getItemByID(long ID) {
		for (TodoItemModel item: todoItems)
			if (item.getID() == ID)
				return item;

		return null;
	}

	public void addItem(TodoItemModel todoItem){
		todoItems.add(todoItem);
		notifyDataSetChanged();
	}

	public TodoItemModel getTodoItem(int position) {
		return todoItems.get(position);
	}

	class ViewHolder{
		@Bind(R.id.title_tv) TextView titleField;
		@Bind(R.id.date_tv) TextView dateField;
		@Bind(R.id.priority_tv) TextView priorityField;
		@Bind(R.id.text_container) View itemContainer;
		@Bind(R.id.btn_remove) ImageButton removeBtn;

		public ViewHolder(View view){
			ButterKnife.bind(this, view);
		}

		public void setTitle(String title) {
			titleField.setText(title);
		}

		public void setDate(TodoDate todoDate){
			dateField.setText(todoDate.getDateAndTimeFormatted());
		}

		public void setPriority(int priority){
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
}
