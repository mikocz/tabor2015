package cz.miko.tabor.core.service;

import cz.miko.tabor.core.dao.ApplicationMapper;
import cz.miko.tabor.core.dao.GangMapper;
import cz.miko.tabor.core.dao.PaymentMapper;
import cz.miko.tabor.core.event.CrudOperation;
import cz.miko.tabor.core.event.EntityUpdateEvent;
import cz.miko.tabor.core.model.Application;
import cz.miko.tabor.core.model.ApplicationDetail;
import cz.miko.tabor.core.model.Gang;
import cz.miko.tabor.core.model.Payment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Michal Kolesnac, FG Forrest a.s. (c) 2015
 * @version $Id: $
 */
@Service
public class ApplicationManager extends AbstractManager  {

	@Autowired
	private ApplicationMapper applicationMapper;

	@Autowired
	private GangMapper gangMapper;

	@Autowired
	private PaymentMapper paymentMapper;

	@Transactional
	public void storeApplication(@NotNull Application application) {
		Assert.notNull(application.getCampId());
		if (application.getId() == null) {
			applicationMapper.insertApplication(application);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this,Application.class, CrudOperation.INSERT, application));
		} else {
			applicationMapper.updateApplication(application);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this, Application.class, CrudOperation.UPDATE, application));
		}
	}

	public void storePayment(@NotNull Payment payment) {
		if(payment.getId()==null){
			paymentMapper.insertPayment(payment);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this,Payment.class,CrudOperation.INSERT, payment));
		} else {
			paymentMapper.updatePayment(payment);
			getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this, Payment.class, CrudOperation.UPDATE, payment));
		}
	}

	public List<ApplicationDetail> getApplicationDetailForActualCamp(Integer campId) {
		Map<String, Object> params =  new HashMap<>();
		params.put("campId", campId);
		return applicationMapper.getFullApplications(params);
	}

	public List<Gang> getGangListByCamp(int campId) {
		return gangMapper.getGangListByCamp(campId);
	}

	public Application getApplicationByCampAndPreCode(Integer campId, String preCode) {
		// TODO MKO
		return null;
	}

	public Application getApplicationByCampAndCode(Integer campId, String code) {
		// TODO MKO
		return null;
	}

	public void removeApplication(ApplicationDetail application) {
		paymentMapper.removePaymentByApplicationId(application.getId());
		applicationMapper.removeApplication(application.getId());
		getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this,Application.class,CrudOperation.DELETE,null));
	}

	public void removePayment(Payment payment) {
		paymentMapper.removePaymentById(payment.getId());
		getApplicationEventPublisher().publishEvent(new EntityUpdateEvent(this,Payment.class,CrudOperation.DELETE,null));
	}
}
