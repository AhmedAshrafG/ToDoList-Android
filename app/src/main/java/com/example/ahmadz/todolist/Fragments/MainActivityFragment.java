package com.example.ahmadz.todolist.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.ahmadz.todolist.Activities.TodoEditActivity;
import com.example.ahmadz.todolist.Adapters.TodoListAdapter;
import com.example.ahmadz.todolist.Callbacks.TodoItemListener;
import com.example.ahmadz.todolist.Database.ContentProvider;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TodoItemListener{

	private final String TAG = this.getClass().getSimpleName();
	@Bind(R.id.todo_lv) ListView todoLv;
	@Bind(R.id.progressBar) ProgressBar progressBar;
	private Context mContext;
	private TodoListAdapter adapter;
	private ContentProvider provider;

	public MainActivityFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, root);

		mContext = getActivity();
		provider = ContentProvider.getInstance(mContext);
		adapter = new TodoListAdapter(mContext, new ArrayList<>(), this);
		todoLv.setAdapter(adapter);

		return root;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadItems();
	}

	private void loadItems() {
		progressBar.setVisibility(View.VISIBLE);

		provider.getTodoListObservable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.computation()).
				subscribe(new Observer<List<TodoItemModel>>() {
					@Override
					public void onCompleted() {
						progressBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onError(Throwable e) {
						progressBar.setVisibility(View.INVISIBLE);
						Log.i(TAG, "onError: " + e.getMessage());
					}

					@Override
					public void onNext(List<TodoItemModel> todoItemModels) {
						adapter.setData(todoItemModels);
					}
				});
	}

	@OnClick(R.id.fab)
	public void fabOnClick(){
		showMaterialDialog(-1, "");
	}

	private void showMaterialDialog(long ID, String oldName) {
		// shows a material dialog that accepts input and receives it in a listener.
		new MaterialDialog.Builder(mContext)
				.title("ToDo")
				.titleGravity(GravityEnum.CENTER)
				.titleColor(getResources().getColor(R.color.dialogColor))
				.inputType(InputType.TYPE_CLASS_TEXT)
				.input("Enter new ToDo name..", oldName, (dialog, input) -> {
					final String todoTitle = input.toString();

					if (todoTitle.equals("")) {
						new MaterialDialog.Builder(mContext)
								.title("Error")
								.content("Can't be empty!")
								.positiveText("OK")
								.show();

					}else {
						if (ID == -1)
							addTodoItem(todoTitle);
						else
							editTodoItem(ID, todoTitle);
					}
				}).show();
	}

	private void editTodoItem(long ID, String todoTitle) {
		adapter.editItemTitle(ID, todoTitle);
		provider.editTodoItemTitle(ID, todoTitle);
	}

	private void addTodoItem(String todoTitle) {
		long ID = provider.addTodoItem(todoTitle);
		adapter.addItem(new TodoItemModel(ID, todoTitle));
	}

	@Override
	public void itemLongPressed(int position) {
		new BottomSheet.Builder(getActivity())
				.title("Options:")
				.sheet(R.menu.todo_item_menu).darkTheme()
				.listener((dialog, which) -> {
					switch (which) {
						case R.id.menu_item_edit:
							TodoItemModel todoItem = adapter.getTodoItem(position);
							showMaterialDialog(todoItem.getID(), todoItem.getTitle());
							break;
						case R.id.menu_item_delete:
							deleteTodoItem(position);
							break;
					}
				}).show();
	}

	private void deleteTodoItem(int position) {
		adapter.removeItem(position);
		provider.deleteTodoItem(adapter.getTodoItem(position).getID());
	}

	@Override
	public void itemSinglePressed(int position) {
		Intent intent = new Intent(mContext, TodoEditActivity.class);
		intent.putExtra(getString(R.string.extra_todo_model), adapter.getTodoItem(position));
		startActivity(intent);
	}

	@Override
	public void itemRemovePressed(int position) {
		new MaterialDialog.Builder(mContext)
				.title("Delete ToDo")
				.content("Do you want to delete this ToDo Item?")
				.positiveText("Confirm")
				.negativeText("Cancel")
				.onPositive((dialog, which) -> 	deleteTodoItem(position))
				.show();
	}
}
