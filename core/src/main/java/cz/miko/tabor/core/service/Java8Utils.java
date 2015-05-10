package cz.miko.tabor.core.service;


import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class Java8Utils {

	public static LocalDate toLocalDate(@Nullable Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}
