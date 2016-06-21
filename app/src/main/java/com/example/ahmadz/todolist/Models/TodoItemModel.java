package com.example.ahmadz.todolist.Models;

import java.io.Serializable;

/**
 * Created by ahmadz on 6/5/16.
 */
public class TodoItemModel implements Serializable{
	private long ID;
	private String title;
	private String body;
	private TodoDate todoDate;
	private int priority;

	public TodoItemModel(long ID, String title, TodoDate todoDate){
		this(ID, title, "", 2, todoDate);
	}

	public TodoItemModel(long ID, String title, String body, int priority, TodoDate todoDate){
		this.title = title;
		this.body = body;
		this.ID = ID;
		this.priority = priority;
		this.todoDate = todoDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public long getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TodoItemModel))
			return false;

		TodoItemModel that = (TodoItemModel)o;

		return this.getID() == that.getID();
	}

	public TodoDate getTodoDate() {
		return todoDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
