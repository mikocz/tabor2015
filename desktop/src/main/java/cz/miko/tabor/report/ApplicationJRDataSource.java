package cz.miko.tabor.report;

import cz.miko.tabor.core.model.ApplicationDetail;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.List;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
public class ApplicationJRDataSource implements JRDataSource {

	private final List<ApplicationDetail> applList;
	private int index = -1;


	public ApplicationJRDataSource(List<ApplicationDetail> applList) {
		this.applList = applList;
	}

	@Override
	public boolean next() throws JRException {
		return ++this.index < this.applList.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {

		String fieldName = jrField.getName();

		ApplicationDetail actualApplication = this.applList.get(this.index);

		try {
			return PropertyUtils.getProperty(actualApplication,fieldName);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
