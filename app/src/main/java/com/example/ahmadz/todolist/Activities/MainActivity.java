package com.example.ahmadz.todolist.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ahmadz.todolist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{
	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
