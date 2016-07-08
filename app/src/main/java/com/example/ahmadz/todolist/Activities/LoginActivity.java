package com.example.ahmadz.todolist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmadz.todolist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

	@Bind(R.id.email) EditText inputEmail;
	@Bind(R.id.password) EditText inputPassword;
	@Bind(R.id.progressBar) ProgressBar progressBar;

	private final String TAG = this.getClass().getSimpleName();
	private FirebaseAuth mAuth;
	private OnCompleteListener<AuthResult> onCompletionListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		mAuth = FirebaseAuth.getInstance();

		if (mAuth.getCurrentUser() != null) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}

		setupAuthentication();
	}

	private void setupAuthentication() {
		onCompletionListener =  task -> {
			progressBar.setVisibility(View.GONE);
			Log.i(TAG, "setupAuthentication: " + task.isSuccessful());

			if (!task.isSuccessful()) {
				Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

			} else {
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
	}

	@OnClick(R.id.sign_in_button)
	public void signInClicked(){
		String email = inputEmail.getText().toString();
		String password = inputPassword.getText().toString();

		if (TextUtils.isEmpty(email)) {
			Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
			return;
		}

		progressBar.setVisibility(View.VISIBLE);

		signIn(email, password);
	}

	private void signIn(String email, String password) {
		//authenticate user
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, onCompletionListener);
	}

	@OnClick(R.id.sign_up_button)
	public void signUpClicked(){
		startActivity(new Intent(LoginActivity.this, SignupActivity.class));
		finish();
	}
}
