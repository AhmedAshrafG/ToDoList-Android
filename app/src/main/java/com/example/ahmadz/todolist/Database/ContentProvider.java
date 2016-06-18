package com.example.ahmadz.todolist.Database;

import android.content.Context;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import java.util.List;
import rx.Observable;

/**
 * Created by ahmadz on 6/5/16.
 */

public class ContentProvider {

	private MyDBHelper mDBHelper;
	private static ContentProvider mInstance;
	private Observable<List<TodoItemModel>> todoListObservable;

	public synchronized static ContentProvider getInstance(Context context){
		if (mInstance == null)
			mInstance = new ContentProvider(context);

		return mInstance;
	}

	private ContentProvider(Context mContext){
		mDBHelper = MyDBHelper.getInstance(mContext);
		initObservables();
	}

	private void initObservables() {
		todoListObservable = Observable.create(subscriber -> {
			try{
				subscriber.onNext(mDBHelper.getAllTodoItems());
				subscriber.onCompleted();

			}catch (Exception e){
				subscriber.onError(e);
			}
		});
	}

	public Observable<List<TodoItemModel>> getTodoListObservable(){
		return todoListObservable;
	}

	public long addTodoItem(String todoTitle) {
		return mDBHelper.addTodoItem(todoTitle);
	}

	public void editTodoItemTitle(long id, String todoTitle) {
		mDBHelper.editTodoItemTitle(id, todoTitle);
	}

	public void editTodoItem(long id, String todoTitle, String todoBody) {
		mDBHelper.editTodoItem(id, todoTitle, todoBody);
	}

	public void deleteTodoItem(long id) {
		mDBHelper.deleteTodoItem(id);
	}

	public void editTodoItemDate(long id, int year, int monthOfYear, int dayOfMonth) {
		mDBHelper.editTodoDate(id, year, monthOfYear, dayOfMonth);
	}

	public void editTodoItemTime(long id, int hourOfDay, int minute) {
		mDBHelper.editTodoTime(id, hourOfDay, minute);
	}
}
