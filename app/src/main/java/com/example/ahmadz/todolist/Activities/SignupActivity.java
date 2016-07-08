package com.example.ahmadz.todolist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmadz.todolist.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

	@Bind (R.id.email) EditText inputEmail;
	@Bind (R.id.password) EditText inputPassword;
	@Bind (R.id.progressBar) ProgressBar progressBar;

	private FirebaseAuth firebaseAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		ButterKnife.bind(this);

		firebaseAuth = FirebaseAuth.getInstance();
	}

	@OnClick(R.id.sign_in_button)
	public void signInClicked(){
		startActivity(new Intent(SignupActivity.this, LoginActivity.class));
		finish();
	}

	@OnClick(R.id.sign_up_button)
	public void signUpClicked(){
		String email = inputEmail.getText().toString().trim();
		String password = inputPassword.getText().toString().trim();

		if (TextUtils.isEmpty(email)) {
			Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
			return;
		}

		if (password.length() < 6) {
			Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
			return;
		}

		progressBar.setVisibility(View.VISIBLE);

		createUser(email, password);
	}

	private void createUser(String email, String password) {
		//create user
		firebaseAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(SignupActivity.this, task -> {

					progressBar.setVisibility(View.GONE);

					if (!task.isSuccessful()) {
						Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
								Toast.LENGTH_SHORT).show();
					} else {
						startActivity(new Intent(SignupActivity.this, MainActivity.class));
						finish();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressBar.setVisibility(View.GONE);
	}
}
