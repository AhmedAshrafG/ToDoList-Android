package com.example.ahmadz.todolist.Models;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/**
 * Created by ahmadz on 6/18/16.
 */
public class TimeHelper {
	private TimePickerDialog.OnTimeSetListener listener;
	private TimePickerDialog mTimeDialog;

	public TimeHelper(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
	}

	public void setListener(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
		mTimeDialog.setOnTimeSetListener(listener);
	}

	public TimePickerDialog getDialog(TodoDate todoDate){
		return getDialog(
				todoDate.getHour(),
				todoDate.getMinute()
		);
	}

	public TimePickerDialog getDialog(int hour, int minute){
		mTimeDialog = TimePickerDialog.newInstance(
					listener,
					hour,
					minute,
					true
			);
		return mTimeDialog;
	}
}
