package top.iotequ.ewallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.ewallet.pojo.EwBill;
import top.iotequ.ewallet.utility.EwDataRepo;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.service.BaseIotequService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
public class EwBillService extends BaseIotequService<EwBill> {
	@Autowired
	private EwDataRepo ewDataRepo;
	@Override
	public void beforeList(List<EwBill> list, HttpServletRequest request) {
		super.beforeList(list, request);
		for (EwBill bill : list) {
			bill.setIsValid(ewDataRepo.isValid(bill));
		}
	}

	@Override
	public void beforeUpdate(EwBill obj, HttpServletRequest request) throws IotequException {
		super.beforeUpdate(obj, request);
		if (!Objects.isNull(obj.getNo())) {
			obj.setIsValid(ewDataRepo.isValid(obj));
		}
	}
}
