package com.example.ahmadz.todolist.Models;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by ahmadz on 6/18/16.
 */
public class TimeHelper {
	private TimePickerDialog.OnTimeSetListener listener;

	public TimeHelper(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
	}

	public void setListener(TimePickerDialog.OnTimeSetListener listener) {
		this.listener = listener;
	}

	public TimePickerDialog getDialog(){
		Calendar now = Calendar.getInstance();
		return getDialog(
				now.get(Calendar.HOUR),
				now.get(Calendar.MINUTE)
		);
	}

	public TimePickerDialog getDialog(int hour, int minute){
		return TimePickerDialog.newInstance(
				listener,
				hour,
				minute,
				true
		);
	}
}
