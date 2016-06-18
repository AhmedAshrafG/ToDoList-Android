package com.example.ahmadz.todolist.Models;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

/**
 * Created by ahmadz on 6/18/16.
 */
public class DateHelper {
	private DatePickerDialog.OnDateSetListener listener;
	private DatePickerDialog mDateDialog;

	public DateHelper(DatePickerDialog.OnDateSetListener listener) {
		this.listener = listener;
	}

	public void setListener(DatePickerDialog.OnDateSetListener listener) {
		this.listener = listener;
		mDateDialog.setOnDateSetListener(listener);
	}

	public DatePickerDialog getDialog(TodoDate todoDate){
		return getDialog(
				todoDate.getYear(),
				todoDate.getMonth(),
				todoDate.getDay()
		);

	}

	public DatePickerDialog getDialog(int year, int month, int day){
		if (mDateDialog == null)
			mDateDialog = DatePickerDialog.newInstance(
					listener,
					year,
					month,
					day
			);
		return mDateDialog;
	}
}
