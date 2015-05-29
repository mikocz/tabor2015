package cz.miko.tabor.core.model;

import cz.miko.tabor.core.service.TaborUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Entity {

	private String firstName;
	private String lastName;
	private Date birthday;
	private String address;
	private String city;
	private String postCode;
	private Sex sex;
	private String note;
	private String email;
	private String phone;

	public long getAge() {
		LocalDate now = LocalDate.now();
		return getAge(now);
	}

	protected long getAge(LocalDate now) {
		LocalDate birthday = TaborUtils.toLocalDate(getBirthday());
		return birthday.until(now, ChronoUnit.YEARS);
	}

	public String getDisplayName() {
		return firstName + " " + lastName;
	}
}
