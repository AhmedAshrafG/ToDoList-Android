package com.example.ahmadz.todolist.Models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ahmadz on 6/18/16.
 */
public class TodoDate implements Serializable{
	private final Calendar mCal;

	public TodoDate(){
		this(-1);
	}

	public TodoDate(long timeInMS){
		mCal = Calendar.getInstance();

		if (timeInMS == -1)
			mCal.getTimeInMillis();
		else
			mCal.setTimeInMillis(timeInMS);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TodoDate))
			return false;

		TodoDate that = (TodoDate)o;
		return this.getTimeInMS() == that.getTimeInMS();
	}

	public long getTimeInMS() {
		return mCal.getTimeInMillis();
	}

	public void setTimeInMS(long timeInMS) {
		mCal.setTimeInMillis(timeInMS);
	}

	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		mCal.set(Calendar.YEAR, year);
		mCal.set(Calendar.MONTH, monthOfYear);
		mCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}

	public void setTime(int hourOfDay, int minute, int second) {
		mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCal.set(Calendar.MINUTE, minute);
		mCal.set(Calendar.SECOND, second);
	}

	public String getTimeFormatted(){
		return String.format("%s:%s",mCal.get(Calendar.HOUR),mCal.get(Calendar.MINUTE));
	}

	public String getDateFormatted(){
		return String.format("%s-%s-%s",mCal.get(Calendar.DAY_OF_MONTH) ,mCal.get(Calendar.MONTH)+1,mCal.get(Calendar.YEAR));
	}

	public int getYear() {
		return mCal.get(Calendar.YEAR);
	}
	public int getMonth() {
		return mCal.get(Calendar.MONTH);
	}
	public int getDay() {
		return mCal.get(Calendar.DAY_OF_MONTH);
	}
	public int getHour() {
		return mCal.get(Calendar.HOUR);
	}
	public int getMinute() {
		return mCal.get(Calendar.MINUTE);
	}
}
