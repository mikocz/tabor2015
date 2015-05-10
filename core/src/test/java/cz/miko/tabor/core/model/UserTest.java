package cz.miko.tabor.core.model;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class UserTest {

	@Test
	public void shouldGetAge() throws Exception {
		User user = new User();
		LocalDate birthday = LocalDate.of(1995, Month.MAY, 2);
		user.setBirthday(Date.from(birthday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		assertEquals(19, user.getAge(LocalDate.of(2015,Month.MAY, 1)));
		assertEquals(20, user.getAge(LocalDate.of(2015,Month.MAY, 2)));
	}
}
