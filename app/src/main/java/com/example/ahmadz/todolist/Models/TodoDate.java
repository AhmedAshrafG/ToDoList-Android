package com.example.ahmadz.todolist.Models;

import android.database.Cursor;

import com.example.ahmadz.todolist.Database.MyDBHelper;

import java.io.Serializable;

/**
 * Created by ahmadz on 6/18/16.
 */
public class TodoDate implements Serializable, Cloneable{
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;

	public TodoDate(Cursor cursor){
		if (cursor == null)
			setDefaults();
		else
			setValues(cursor);
	}

	private void setDefaults() {
		year = month = day = hour = minute = -1;
	}

	private void setValues(Cursor cursor) {
		this.year = cursor.getInt(cursor.getColumnIndex(MyDBHelper.COLUMN_ITEM_YEAR));
		this.month = cursor.getInt(cursor.getColumnIndex(MyDBHelper.COLUMN_ITEM_MONTH));
		this.day = cursor.getInt(cursor.getColumnIndex(MyDBHelper.COLUMN_ITEM_DAY));
		this.hour = cursor.getInt(cursor.getColumnIndex(MyDBHelper.COLUMN_ITEM_HOUR));
		this.minute = cursor.getInt(cursor.getColumnIndex(MyDBHelper.COLUMN_ITEM_MINUTE));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TodoDate))
			return false;

		TodoDate that = (TodoDate)o;
		return this.getYear() == that.getYear()
				&& this.getMonth() == that.getMonth()
				&& this.getDay() == that.getDay()
				&& this.getHour() == that.getHour()
				&& this.getMinute() == that.getMinute();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		setYear(year);
		setMonth(monthOfYear);
		setDay(dayOfMonth);
	}

	public void setTime(int hourOfDay, int minute) {
		setHour(hourOfDay);
		setMinute(minute);
	}
}
