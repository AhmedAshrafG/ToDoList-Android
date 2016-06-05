package com.example.ahmadz.todolist.Models;

/**
 * Created by ahmadz on 6/5/16.
 */
public class TodoItemModel {
	private long ID;
	private String title;
	private String body;

	public TodoItemModel(long ID, String title){
		this(ID, title, "");
	}

	public TodoItemModel(long ID, String title, String body){
		this.title = title;
		this.body = body;
		this.ID = ID;
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

	public void setBody(String body) {
		this.body = body;
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
}
