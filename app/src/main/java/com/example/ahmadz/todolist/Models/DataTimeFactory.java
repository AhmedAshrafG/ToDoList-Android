package com.example.ahmadz.todolist.Models;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/**
 * Created by ahmadz on 6/18/16.
 */
public class DataTimeFactory {
	private static DataTimeFactory mInstance;

	public synchronized static DataTimeFactory getInstance() {
		if (mInstance == null)
			mInstance = new DataTimeFactory();
		return mInstance;
	}

	public DateHelper getDateHelper(DatePickerDialog.OnDateSetListener listener){
		return new DateHelper(listener);
	}

	public TimeHelper getTimeHelper(TimePickerDialog.OnTimeSetListener listener){
		return new TimeHelper(listener);
	}
}
