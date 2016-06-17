package com.example.ahmadz.todolist.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmadz.todolist.Database.ContentProvider;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoEditActivity extends AppCompatActivity {
	@Bind(R.id.toolbar)
	Toolbar toolbar;
	@Bind(R.id.et_title)
	EditText title_field;
	@Bind(R.id.et_body)
	EditText body_field;
	private TodoItemModel todoItem;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_edit);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mContext = this;
		setExtras();
	}

	private void setExtras() {
		todoItem = (TodoItemModel) this.getIntent().getSerializableExtra(getString(R.string.extra_todo_model));
		title_field.setText(todoItem.getTitle());
		body_field.setText(todoItem.getBody());
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
		this.finish();
	}

	private void showDialog() {
		new MaterialDialog.Builder(mContext)
				.title("Unsaved Data!")
				.content("Do you want to save before you quit?")
				.positiveText("Save!")
				.negativeText("Don't Save")
				.onPositive((dialog, which) -> saveStuffAndExit())
				.onNegative((dialog1, which1) -> this.finish())
				.show();
	}

	@Override
	public void onBackPressed() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		if (!todoItem.getBody().equals(body) || !todoItem.getTitle().equals(title))
			showDialog();
		else
			this.finish();
	}
}
