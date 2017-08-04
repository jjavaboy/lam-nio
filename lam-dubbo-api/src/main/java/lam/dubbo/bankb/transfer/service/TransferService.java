package lam.dubbo.bankb.transfer.service;

import lam.dubbo.bankb.transfer.model.Transfer;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public interface TransferService {
	
	public boolean doTransfer(Transfer transfer);

}
