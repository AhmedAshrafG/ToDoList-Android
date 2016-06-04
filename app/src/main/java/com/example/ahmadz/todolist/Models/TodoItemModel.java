package com.example.ahmadz.todolist.Models;

/**
 * Created by ahmadz on 6/5/16.
 */
public class TodoItemModel {
	private String title;
	private String body;

	public TodoItemModel(String title){
		this(title, "");
	}
	public TodoItemModel(String title, String body){
		this.title = title;
		this.body = body;
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
}
