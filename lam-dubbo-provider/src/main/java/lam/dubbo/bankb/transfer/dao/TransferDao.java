package lam.dubbo.bankb.transfer.dao;

import java.util.Map;

import lam.dubbo.bankb.transfer.model.Transfer;
import lam.dubbo.common.MyBatisDao;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
@MyBatisDao
public interface TransferDao {
	
	public Transfer getById(String messageId);
	
	public int insert(Transfer transfer);
	
	public int updateStatus(Map<String, Object> param);

}
