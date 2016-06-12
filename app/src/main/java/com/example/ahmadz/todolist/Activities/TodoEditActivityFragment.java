package com.example.ahmadz.todolist.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmadz.todolist.Callbacks.BackPressedListener;
import com.example.ahmadz.todolist.Database.ContentProvider;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment holding the title and body of the selected to-do item for editing/viewing.
 */
public class TodoEditActivityFragment extends Fragment implements BackPressedListener{

	@Bind(R.id.et_title)
	EditText title_field;
	@Bind(R.id.et_body)
	EditText body_field;
	private TodoItemModel todoItem;
	private Context mContext;

	public TodoEditActivityFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_todo_edit, container, false);
		ButterKnife.bind(this, root);

		mContext = getActivity();
		setExtras();

		((TodoEditActivity) getActivity()).setBackPressListener(this);

		return root;
	}

	private void setExtras() {
		todoItem = (TodoItemModel) getActivity().getIntent().getSerializableExtra(getString(R.string.extra_todo_model));
		title_field.setText(todoItem.getTitle());
		body_field.setText(todoItem.getBody());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_todo_edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.menu_item_save) {
			saveStuffAndExit();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void saveStuffAndExit() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		ContentProvider.getInstance(mContext).editTodoItem(todoItem.getID(), title, body);
		getActivity().finish();
	}


	@Override
	public void backPressed() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		if (!todoItem.getBody().equals(body) || !todoItem.getTitle().equals(title))
			showDialog();
		else
			getActivity().finish();
	}

	private void showDialog() {
		new MaterialDialog.Builder(mContext)
				.title("Unsaved Data!")
				.content("Do you want to save before you quit?")
				.positiveText("Save!")
				.negativeText("Don't Save")
				.onPositive((dialog, which) -> saveStuffAndExit())
				.onNegative((dialog1, which1) -> getActivity().finish())
				.show();
	}
}
