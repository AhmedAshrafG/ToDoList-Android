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

	public static ContentProvider getInstance(Context context){
		if (mInstance == null)
			mInstance = new ContentProvider(context);

		return mInstance;
	}

	private ContentProvider(Context mContext){
		dbHelper = MyDBHelper.getInstance(mContext);
	}

	public Observable<List<TodoItemModel>> getTodoListObservable(){
		return Observable.create(subscriber -> {
			try{
				subscriber.onNext(dbHelper.getAllTodoItems());
				subscriber.onCompleted();

			}catch (Exception e){
				subscriber.onError(e);
			}
		});
	}

	public void addTodoItem(String todoTitle) {
		dbHelper.addTodoItem(todoTitle);
	}

	public void editTodoItem(long id, String todoTitle) {
		dbHelper.editTodoItemTitle(id, todoTitle);
	}

	public void deleteTodoItem(long id) {
		dbHelper.deleteTodoItem(id);
	}
}
