package com.example.ahmadz.todolist.Callbacks;

/**
 * Created by ahmadz on 6/5/16.
 */
public interface TodoItemListener {
	void itemLongPressed(int position);
	void itemSinglePressed(int position);
	void itemRemovePressed(int position);
}
