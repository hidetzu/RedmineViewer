package com.redmine.util;

import java.util.Calendar;

public class StringHelper {

	public static String convertToDateString(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		String result = Integer.toString(year) + "-"
				+ Integer.toString(month) + "-"
				+ Integer.toString(day) + " "
				+ Integer.toString(hour) + ":"
				+ Integer.toString(minute) + ":"
				+ Integer.toString(second);
		return result;
	}
}
