package com.example.ahmadz.todolist.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ahmadz.todolist.Models.DateHelper;
import com.example.ahmadz.todolist.Models.DialogFactory;
import com.example.ahmadz.todolist.Models.TimeHelper;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.HashMap;
import java.util.Map;

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

	private String TAG = this.getClass().getSimpleName();
	private TodoItemModel todoItem;
	private Context mContext;
	private DialogFactory mDialogFactory;
	private DateHelper mDateHelper;
	private TimeHelper mTimeHelper;
	private FragmentManager mFragManager;
	private long timeWhenGotHere;
	private int priorityWhenGotHere;
	private DatabaseReference mTodoListNode;
	private String todoUID;

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

		setupFireBaseDB();
		setExtras();
		setupDialogs();
		setupTestListeners();
	}

	private void setupTestListeners() {
		mTodoListNode.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Log.i(TAG, "onChildAdded: " + dataSnapshot.toString() + " " + s);
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Log.i(TAG, "onChildChanged: " + dataSnapshot.toString() + " " + s);
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Log.i(TAG, "onChildRemoved: " + dataSnapshot.toString());
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Log.i(TAG, "onChildMoved: " + dataSnapshot.toString() + " " + s);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.i(TAG, "onCancelled: " + databaseError.getMessage());
			}
		});
	}

	private void setupFireBaseDB() {
		String userUID = FirebaseAuth.getInstance()
				.getCurrentUser()
				.getUid();

		String USERS = getString(R.string.users_node);
		mTodoListNode = FirebaseDatabase.getInstance()
				.getReference(USERS)
				.child(userUID)
				.child(getString(R.string.todo_list_node));
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
		timeWhenGotHere = todoItem.getTimeInMS();
		priorityWhenGotHere = todoItem.getPriority();
		prioritySpinner.setSelection(priorityWhenGotHere);
		todoUID = this.getIntent().getStringExtra(getString(R.string.UID));

		updateTVs();
	}

	private void updateTVs(){
		dateTv.setText(todoItem.getDateFormatted());
		timeTv.setText(todoItem.getTimeFormatted());
	}

	private void saveStuffAndExit() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();
		int priority = prioritySpinner.getSelectedItemPosition();
		long timeInMS = todoItem.getTimeInMS();

		Map <String, Object> map = new HashMap<>();
		map.put(getString(R.string.todo_title), title);
		map.put(getString(R.string.todo_body), body);
		map.put(getString(R.string.todo_priority), priority);
		map.put(getString(R.string.todo_timeInMS), timeInMS);

		mTodoListNode.child(todoUID).setPriority(priority);
		mTodoListNode.child(todoUID).updateChildren(map);

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
		mDateHelper.getDialog(todoItem).show(mFragManager, dateTag);
	}

	@OnClick(R.id.time_tv)
	public void timeTvClicked(){
		showTimePickerDialog();
	}

	private void showTimePickerDialog() {
		mTimeHelper.getDialog(todoItem).show(mFragManager, timeTag);
	}

	@Override
	public void onBackPressed() {
		if (valuesChanged())
			showDialog();
		else
			this.finish();
	}

	private boolean valuesChanged() {
		String title = title_field.getText().toString();
		String body = body_field.getText().toString();

		return
				!todoItem.getBody().equals(body)
				|| !todoItem.getTitle().equals(title)
				|| todoItem.getTimeInMS() != timeWhenGotHere
				|| priorityWhenGotHere != todoItem.getPriority();
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		todoItem.setDate(year, monthOfYear, dayOfMonth);
		updateTVs();
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
		todoItem.setTime(hourOfDay, minute, second);
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
