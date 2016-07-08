package com.example.ahmadz.todolist.Database;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ahmadz on 7/8/16.
 */
public class FireBaseHelper {

	private static FirebaseDatabase mDatabase;

	public static FirebaseDatabase getDatabase() {
		if (mDatabase == null) {
			mDatabase = FirebaseDatabase.getInstance();
			mDatabase.setPersistenceEnabled(true);
		}

		return mDatabase;
	}

}
