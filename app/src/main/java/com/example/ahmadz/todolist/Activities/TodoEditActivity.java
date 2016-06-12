package com.example.ahmadz.todolist.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ahmadz.todolist.Callbacks.BackPressedListener;
import com.example.ahmadz.todolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoEditActivity extends AppCompatActivity {
	@Bind(R.id.toolbar)
	Toolbar toolbar;
	private BackPressedListener backPressedListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_edit);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


	public void setBackPressListener(BackPressedListener backPressedListener) {
		this.backPressedListener = backPressedListener;
	}

	@Override
	public void onBackPressed() {
		if (backPressedListener != null)
			backPressedListener.backPressed();
	}
}
