package cz.miko.tabor.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationDetail extends Application {

	private Camp camp;
	private Gang gang;
	private User user;
	private List<Payment> payments;


	public BigDecimal getIndividualPriceOrCampPrice(){
		if (getPrice() != null) {
			return getPrice();
		}
		return getCamp().getPrice();
	}

	public BigDecimal getPaid() {
		BigDecimal result = BigDecimal.ZERO;
		if (payments!=null) {
			for(Payment payment : payments) {
				result = result.add(payment.getAmount());
			}
		}
		return result;
	}

	public boolean isApplicationPaid() {
		return getIndividualPriceOrCampPrice().compareTo(getPaid()) < 1;
	}

	public String getGangName() {
		if (gang == null) {
			return null;
		}
		return gang.getName();
	}

	public String getFirstName() {
		return getUser().getFirstName();
	}

	public String getLastName() {
		return getUser().getLastName();
	}

	public Date getBirthday() {
		return getUser().getBirthday();
	}

	public String getAddress() {
		return getUser().getAddress();
	}

	public void setCity(String city) {
		getUser().setCity(city);
	}

	public String getCity() {
		return getUser().getCity();
	}

	public void setPostCode(String postCode) {
		getUser().setPostCode(postCode);
	}

	public String getPostCode() {
		return getUser().getPostCode();
	}

	public void setSex(Sex sex) {
		getUser().setSex(sex);
	}

	public void setPhone(String phone) {
		getUser().setPhone(phone);
	}

	public void setBirthday(Date birthday) {
		getUser().setBirthday(birthday);
	}

	public Sex getSex() {
		return getUser().getSex();
	}

	public String getEmail() {
		return getUser().getEmail();
	}

	public void setEmail(String email) {
		getUser().setEmail(email);
	}

	public String getPhone() {
		return getUser().getPhone();
	}

	public void setAddress(String address) {
		getUser().setAddress(address);
	}

	public void setFirstName(String firstName) {
		getUser().setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		getUser().setLastName(lastName);
	}

	public long getAge() {
		return getUser().getAge();
	}

	public String getDisplayName() {
		return getUser().getDisplayName();
	}

	public long getAge(LocalDate now) {
		return getUser().getAge(now);
	}
}
