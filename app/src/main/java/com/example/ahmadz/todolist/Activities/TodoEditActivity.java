package com.example.ahmadz.todolist.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmadz.todolist.Database.ContentProvider;
import com.example.ahmadz.todolist.Models.DateHelper;
import com.example.ahmadz.todolist.Models.DialogFactory;
import com.example.ahmadz.todolist.Models.TimeHelper;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class TodoEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
	private String dateTag = "dateDialog";
	private String timeTag = "timeDialog";
	@Bind(R.id.toolbar) Toolbar toolbar;
	@Bind(R.id.et_title) EditText title_field;
	@Bind(R.id.et_body) EditText body_field;
	@Bind(R.id.date_tv) TextView dateTv;
	@Bind(R.id.time_tv) TextView timeTv;
	@Bind(R.id.priority_spinner) Spinner prioritySpinner;
	private TodoItemModel todoItem;
	private Context mContext;
	private DialogFactory mDialogFactory;
	private DateHelper mDateHelper;
	private TimeHelper mTimeHelper;
	private FragmentManager mFragManager;
	private ContentProvider mContentProvider;
	private long timeWhenGotHere;
	private int priorityWhenGotHere;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_edit);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

		mContext = this;
		mFragManager = getFragmentManager();
		mContentProvider = ContentProvider.getInstance(mContext);

		setExtras();
		setupDialogs();
	}

	private void setupDialogs() {
		mDialogFactory = DialogFactory.getInstance();
		mDateHelper = mDialogFactory.getDateHelper(this);
		mTimeHelper = mDialogFactory.getTimeHelper(this);
	}

	private void setExtras() {
		todoItem = (TodoItemModel) this.getIntent().getSerializableExtra(getString(R.string.extra_todo_model));
		title_field.setText(todoItem.getTitle());
		body_field.setText(todoItem.getBody());
		timeWhenGotHere = todoItem.getTodoDate().getTimeInMS();
		priorityWhenGotHere = todoItem.getPriority();
		prioritySpinner.setSelection(priorityWhenGotHere);

		updateTVs();
	}

	private void updateTVs(){
		dateTv.setText(todoItem.getTodoDate().getDateFormatted());
		timeTv.setText(todoItem.getTodoDate().getTimeFormatted());
	}

	private void saveStuffAndExit() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		mContentProvider.editTodoItem(todoItem.getID(), title, body);
		mContentProvider.editTodoItemTime(todoItem.getID(), todoItem.getTodoDate().getTimeInMS());
		mContentProvider.editTodoItemPriority(todoItem.getID(), todoItem.getPriority());

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

	@OnClick(R.id.date_tv)
	public void dateTvClicked(){
		showDatePickerDialog();
	}

	private void showDatePickerDialog() {
		mDateHelper.getDialog(todoItem.getTodoDate()).show(mFragManager, dateTag);
	}

	@OnClick(R.id.time_tv)
	public void timeTvClicked(){
		showTimePickerDialog();
	}

	private void showTimePickerDialog() {
		mTimeHelper.getDialog(todoItem.getTodoDate()).show(mFragManager, timeTag);
	}

	@Override
	public void onBackPressed() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		if (!todoItem.getBody().equals(body)
				|| !todoItem.getTitle().equals(title)
				|| !(todoItem.getTodoDate().getTimeInMS() == timeWhenGotHere)
				|| priorityWhenGotHere != todoItem.getPriority())
			showDialog();
		else
			this.finish();
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		todoItem.getTodoDate().setDate(year, monthOfYear, dayOfMonth);
		updateTVs();
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		todoItem.getTodoDate().setTime(hourOfDay, minute, second);
		updateTVs();
	}

	@OnItemSelected(R.id.priority_spinner)
	public void onPrioritySpinnerItemSelected(int position){
		todoItem.setPriority(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_todo_edit, menu);
		return true;
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
}
