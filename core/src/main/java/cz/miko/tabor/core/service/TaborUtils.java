package cz.miko.tabor.core.service;


import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class TaborUtils {

	public static DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd. MM. yyyy");

	public static String localDateToString(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return myDateFormatter.format(localDate);
	}

	public static String dateToString(Date date) {
		if (date == null) {
			return null;
		}
		return myDateFormatter.format(toLocalDate(date));
	}

	public static LocalDate toLocalDate(@Nullable Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date toDate(@Nullable LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
