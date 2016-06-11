package com.example.ahmadz.todolist.Database;

import android.content.Context;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import java.util.List;
import rx.Observable;

/**
 * Created by ahmadz on 6/5/16.
 */

public class ContentProvider {

	private MyDBHelper dbHelper;
	private static ContentProvider mInstance;
	private Observable<List<TodoItemModel>> todoListObservable;

	public static ContentProvider getInstance(Context context){
		if (mInstance == null)
			mInstance = new ContentProvider(context);

		return mInstance;
	}

	private ContentProvider(Context mContext){
		dbHelper = MyDBHelper.getInstance(mContext);
		initObservables();
	}

	private void initObservables() {
		todoListObservable = Observable.create(subscriber -> {
			try{
				subscriber.onNext(dbHelper.getAllTodoItems());
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
		return dbHelper.addTodoItem(todoTitle);
	}

	public void editTodoItemTitle(long id, String todoTitle) {
		dbHelper.editTodoItemTitle(id, todoTitle);
	}

	public void editTodoItem(long id, String todoTitle, String todoBody) {
		dbHelper.editTodoItem(id, todoTitle, todoBody);
	}

	public void deleteTodoItem(long id) {
		dbHelper.deleteTodoItem(id);
	}
}
