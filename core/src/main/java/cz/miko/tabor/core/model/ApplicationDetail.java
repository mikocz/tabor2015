package cz.miko.tabor.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	private User user;
	private List<Payment> payments;


}
