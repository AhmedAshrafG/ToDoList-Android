package com.example.ahmadz.todolist.Adapters;

import android.content.Context;

import com.example.ahmadz.todolist.Callbacks.TodoItemListener;
import com.example.ahmadz.todolist.Models.RecyclerViewHolder;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by ahmadz on 3/7/16.
 */
public class TodoListAdapter extends FirebaseRecyclerAdapter<TodoItemModel, RecyclerViewHolder>{
	private TodoItemListener todoItemListener;
	private Context mContext;

	public TodoListAdapter(Context mContext, TodoItemListener todoItemListener, Class<TodoItemModel> todoItemModelClass, int todo_item_layout, Class<RecyclerViewHolder> recyclerViewHolderClass, DatabaseReference child) {
		super(todoItemModelClass, todo_item_layout, recyclerViewHolderClass, child);
		this.mContext = mContext;
		this.todoItemListener = todoItemListener;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void populateViewHolder(RecyclerViewHolder viewHolder, TodoItemModel model, int position) {
		//set tags.
		viewHolder.setAllTag(position);
		//set values.
		viewHolder.setTitle(model.getTitle());
		viewHolder.setDate(model);
		viewHolder.setPriority(mContext, model.getPriority());
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
	}
}
