package org.lam.hongbao.service.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.lam.hongbao.core.model.HongBao;
import org.lam.hongbao.core.model.HongBaoRecord;

import com.google.gson.Gson;

import lam.util.MathUtil;

/**
* <p>
* produce hong bao
* </p>
* @author linanmiao
* @date 2017年1月19日
* @version 1.0
*/
public class HongBaoFactory {
	
	/**
	 * 生成红包值原理：可分配值N1 = 总红包值 -（最小红包值 * 红包数），<br/>
	 * 步骤1：在N1的范围内，取一个随机数R1，第一个红包的钱 = 最小红包值 + 随机数R1，生成第一个红包后，则可分配值N1 = N1 - R1。<br/>
	 * 第二个红包执行步骤1的过程，以此类推，...，直到最后一个红包。<br/>
	 * 最后一个红包的钱则直接 = 最小红包值 + 可分配值N1<br/>
	 * 缺点：由于使用随机数，往往最先生成的红包是大的，越往后生成的红包可能是最小的，红包的钱应该符合正态分布的趋势才是我们需要的。<br/>
	 * @param hongBao
	 * @return
	 */
	public static List<HongBaoRecord> generateRandomMoneyHongBao(HongBao hongBao){
		checkHongBao(hongBao);
		int remain = hongBao.getMoney() - hongBao.getNum() * hongBao.getMinMoney();
		
		List<HongBaoRecord> recordList = new ArrayList<HongBaoRecord>(hongBao.getNum());
		
		Random random = new Random();
		for(int idx = 0; idx < hongBao.getNum(); idx++){
			HongBaoRecord record = new HongBaoRecord();
			record.setCreateTime(new Date());
			record.setHongbaoId(hongBao.getId());
			if(idx == hongBao.getNum() - 1){
				record.setMoney(hongBao.getMinMoney() + remain);
			}else{
				int diffMoney = random.nextInt(remain);
				remain -= diffMoney;
				record.setMoney(hongBao.getMinMoney() + diffMoney);
			}
			recordList.add(record);
		}
		return recordList;
	}
	
	private static void checkHongBao(HongBao hongBao){
		if(hongBao.getMinMoney() > hongBao.getMaxMoney()){
			throw new IllegalArgumentException(String.format("minMoney(%d) bigger than maxMoney(%d).", hongBao.getMinMoney(), hongBao.getMaxMoney()));
		}
		if(hongBao.getMinMoney() > hongBao.getMoney()){
			throw new IllegalArgumentException(String.format("money(%d) smaller than minMoney(%d).", hongBao.getMoney(), hongBao.getMinMoney()));
		}
		if(hongBao.getMoney() > hongBao.getMaxMoney()){
			throw new IllegalArgumentException(String.format("money(%d) bigger than maxMoney(%d).", hongBao.getMoney(), hongBao.getMaxMoney()));
		}
		int remain = hongBao.getMoney() - hongBao.getNum() * hongBao.getMinMoney();
		if(remain < 0){
			throw new IllegalArgumentException(String.format("minMoney(%d) is too large.", hongBao.getMinMoney()));
		}
	}
	
	/**
	 * 生成高斯分布（正态分布）的随机红包钱数
	 * @param hongBao
	 * @return
	 */
	public static List<HongBaoRecord> generateGaussianDistributedHongBao(HongBao hongBao){
		checkHongBao(hongBao);
		List<HongBaoRecord> list = new ArrayList<HongBaoRecord>();
		double average = ((double)hongBao.getMoney()) / ((double)hongBao.getNum());
		double deviation = 1; //假设方差为4
		double remain = hongBao.getMoney();
		for(int idx = 0; idx < hongBao.getNum(); idx++){
			HongBaoRecord record = new HongBaoRecord();
			record.setCreateTime(new Date());
			record.setHongbaoId(hongBao.getId());
			if(idx == hongBao.getNum() - 1){
				record.setMoney(remain);
			}else{
				double gaussian = MathUtil.positiveGaussian(average, deviation);
				gaussian = MathUtil.decimal(gaussian, "#.00");
				remain = MathUtil.sum(remain, 0 - gaussian);
				record.setMoney(gaussian);
			}
			list.add(record);
		}
		
		return list;
	}
	
	public static void main(String[] args){
		Gson gson = new Gson();
		HongBao hongBao = new HongBao();
		hongBao.setCreateTime(new Date());
		hongBao.setId(1L);
		hongBao.setMoney(15);
		hongBao.setNum(7);
	
		//List<HongBaoRecord> recordList = generateRandomMoneyHongBao(hongBao);
		List<HongBaoRecord> recordList = generateGaussianDistributedHongBao(hongBao);
		for(HongBaoRecord record : recordList){
			System.out.println(gson.toJson(record));
		}
		
		double d = 0;
		for(HongBaoRecord record : recordList){
			d = MathUtil.sum(d, record.getMoney());
		}
		System.out.println("d:" + d);
	}
	
}
