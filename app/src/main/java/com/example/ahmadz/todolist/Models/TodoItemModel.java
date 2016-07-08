package com.example.ahmadz.todolist.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ahmadz on 6/5/16.
 */
public class TodoItemModel implements Serializable{
	private String title;
	private String body;
	private int priority;
	private long timeInMS;
	@Exclude
	private final Calendar mCal;
	public static int DEFAULT_PRIORITY = 1;

	public TodoItemModel(){
		this("");
	}

	public TodoItemModel(String title){
		this(title, "", TodoItemModel.DEFAULT_PRIORITY, -1);
	}

	public TodoItemModel(String title, String body, int priority, long timeInMS){
		this.title = title;
		this.body = body;
		this.priority = priority;

		mCal = Calendar.getInstance();

		if (timeInMS != -1)
			mCal.setTimeInMillis(timeInMS);

		this.timeInMS = mCal.getTimeInMillis();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TodoItemModel))
			return false;

		TodoItemModel that = (TodoItemModel) o;
		return this.getTimeInMS() == that.getTimeInMS();
	}

	public long getTimeInMS() {
		return timeInMS;
	}

	public void setDate(int year, int monthOfYear, int dayOfMonth) {
		mCal.set(Calendar.YEAR, year);
		mCal.set(Calendar.MONTH, monthOfYear);
		mCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		timeInMS = mCal.getTimeInMillis();
	}

	public void setTime(int hourOfDay, int minute, int second) {
		mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCal.set(Calendar.MINUTE, minute);
		mCal.set(Calendar.SECOND, second);
		timeInMS = mCal.getTimeInMillis();
	}
	@Exclude
	public String getTimeFormatted(){
		return String.format("%02d:%02d",mCal.get(Calendar.HOUR),mCal.get(Calendar.MINUTE));
	}
	@Exclude
	public String getDateFormatted(){
		return String.format("%02d-%02d-%04d",mCal.get(Calendar.DAY_OF_MONTH) ,mCal.get(Calendar.MONTH)+1,mCal.get(Calendar.YEAR));
	}

	@Exclude
	public int getYear() {
		return mCal.get(Calendar.YEAR);
	}
	@Exclude
	public int getMonth() {
		return mCal.get(Calendar.MONTH);
	}
	@Exclude
	public int getDay() {
		return mCal.get(Calendar.DAY_OF_MONTH);
	}
	@Exclude
	public int getHour() {
		return mCal.get(Calendar.HOUR);
	}
	@Exclude
	public int getMinute() {
		return mCal.get(Calendar.MINUTE);
	}
	@Exclude
	public String getDateAndTimeFormatted() {
		mCal.setTimeInMillis(timeInMS); // when back from firebase there's only the timeInMS.
		return getDateFormatted() + "\n" + getTimeFormatted();
	}
}
