package com.example.ahmadz.todolist.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.example.ahmadz.todolist.Adapters.TodoListAdapter;
import com.example.ahmadz.todolist.Callbacks.TodoItemListener;
import com.example.ahmadz.todolist.Database.FireBaseHelper;
import com.example.ahmadz.todolist.Models.RecyclerViewHolder;
import com.example.ahmadz.todolist.Models.TodoItemModel;
import com.example.ahmadz.todolist.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TodoItemListener {

	private final String TAG = this.getClass().getSimpleName();
	@Bind(R.id.toolbar) Toolbar toolbar;
	@Bind(R.id.todo_lv) RecyclerView todoRecyclerView;
	@Bind(R.id.progressBar) ProgressBar progressBar;
	@Bind(R.id.empty_message) TextView emptyMessage;

	private Context mContext;
	private FirebaseAuth mAuth;
	private FirebaseRecyclerAdapter<TodoItemModel, RecyclerViewHolder> adapter;
	private DatabaseReference mUserDB;
	private FirebaseAuth.AuthStateListener authListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

		mContext = this;

		setupAuthentication();

		if (mAuth.getCurrentUser() != null){
			progressBar.setVisibility(View.VISIBLE);
			setupFireBaseDB();
			setupFireBaseUI();
			checkForEmptiness();
		}
	}

	private void setupAuthentication() {
		mAuth = FirebaseAuth.getInstance();

		authListener = fireBaseAuth -> {
			Log.i(TAG, "setupAuthentication: done");
			FirebaseUser user = fireBaseAuth.getCurrentUser();
			if (user == null) {
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
			}
		};
	}

	private void setupFireBaseDB() {
		String mUserUID = mAuth.getCurrentUser().getUid();
		mUserDB = FireBaseHelper.getDatabase()
				.getReference(getString(R.string.users_node))
				.child(mUserUID);

		mUserDB.child(getString(R.string.todo_list_node))
				.addChildEventListener(new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
						Log.i(TAG, "onChildAdded: ");
						progressBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onChildChanged(DataSnapshot dataSnapshot, String s) {
						Log.i(TAG, "onChildChanged: ");
					}

					@Override
					public void onChildRemoved(DataSnapshot dataSnapshot) {
						Log.i(TAG, "onChildRemoved: ");
					}

					@Override
					public void onChildMoved(DataSnapshot dataSnapshot, String s) {
						Log.i(TAG, "onChildMoved: ");
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {
						Log.i(TAG, "onCancelled: " + databaseError.getMessage());
						progressBar.setVisibility(View.INVISIBLE);
					}
				});
	}

	private void setupFireBaseUI() {
		todoRecyclerView.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setReverseLayout(true);
		llm.setStackFromEnd(true);
		todoRecyclerView.setLayoutManager(llm);

		adapter = new TodoListAdapter(
				mContext,
				this,
				TodoItemModel.class,
				R.layout.todo_item_layout,
				RecyclerViewHolder.class,
				mUserDB.child(getString(R.string.todo_list_node)).orderByPriority()
		);
		todoRecyclerView.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(authListener);
	}

	@OnClick(R.id.fab)
	public void fabOnClick(){
		showMaterialDialog(-1, "");
	}

	private void showMaterialDialog(int position, String oldName) {
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
						//adding a new item.
						if (oldName.equals(""))
							addTodoItem(todoTitle);
							//editing an existing item.
						else
							editTodoItem(position, todoTitle);
					}
				}).show();
	}

	private void editTodoItem(int position, String todoTitle) {
		adapter.getRef(position).child(getString(R.string.todo_title)).setValue(todoTitle);
	}

	private void addTodoItem(String todoTitle) {
		TodoItemModel todoItem = new TodoItemModel(todoTitle);
		DatabaseReference newTodo = mUserDB.child(getString(R.string.todo_list_node)).push();
		newTodo.setValue(todoItem);
		newTodo.setPriority(TodoItemModel.DEFAULT_PRIORITY);
		checkForEmptiness();
	}

	@Override
	public void itemLongPressed(int position) {
		new BottomSheet.Builder(this)
				.title("Options:")
				.sheet(R.menu.todo_item_menu).darkTheme()
				.listener((dialog, which) -> {
					switch (which) {
						case R.id.menu_item_edit:
							TodoItemModel todoItem = adapter.getItem(position);
							showMaterialDialog(position, todoItem.getTitle());
							break;
						case R.id.menu_item_delete:
							showDeleteDialog(position);
							break;
					}
				}).show();
	}

	private void checkForEmptiness() {
		if (true/*adapter.getItemCount() > 0*/)
			emptyMessage.setVisibility(View.INVISIBLE);
		else
			emptyMessage.setVisibility(View.VISIBLE);
	}

	@Override
	public void itemSinglePressed(int position) {
		Intent intent = new Intent(mContext, TodoEditActivity.class);
		intent.putExtra(getString(R.string.extra_todo_model), adapter.getItem(position));
		intent.putExtra(getString(R.string.UID), adapter.getRef(position).getKey());
		startActivity(intent);
	}

	@Override
	public void itemRemovePressed(int position) {
		showDeleteDialog(position);
	}

	private void showDeleteDialog(int position) {
		new MaterialDialog.Builder(mContext)
				.title("Delete ToDo")
				.content("Do you want to delete this ToDo Item?")
				.positiveText("Confirm")
				.negativeText("Cancel")
				.onPositive((dialog, which) -> 	deleteTodoItem(position))
				.show();
	}

	private void deleteTodoItem(int position) {
		adapter.getRef(position).removeValue();
		checkForEmptiness();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_signout:
				signOut();
				break;
			case R.id.action_settings:
				Toast.makeText(this, "Not yet Implemented!", Toast.LENGTH_SHORT).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void signOut() {
		new MaterialDialog.Builder(this)
				.title("Log-Out")
				.content("Are you sure you wanna Logout?")
				.positiveText("Yes")
				.negativeText("Cancel")
				.onPositive((dialog, which) -> signMeOut())
				.show();
	}

	public void signMeOut(){
		mAuth.signOut();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (authListener != null)
			mAuth.removeAuthStateListener(authListener);
	}
}
