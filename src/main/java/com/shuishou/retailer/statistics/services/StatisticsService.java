package com.shuishou.retailer.statistics.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.goods.models.Goods;
import com.shuishou.retailer.goods.models.IGoodsDataAccessor;
import com.shuishou.retailer.indent.models.IIndentDataAccessor;
import com.shuishou.retailer.indent.models.Indent;
import com.shuishou.retailer.indent.models.IndentDetail;
import com.shuishou.retailer.statistics.views.StatItem;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;


@Service
public class StatisticsService implements IStatisticsService{
	private final static Logger logger = LoggerFactory.getLogger(StatisticsService.class);
	
	@Autowired
	private IGoodsDataAccessor goodsDA;
	
	@Autowired
	private IIndentDataAccessor indentDA;
	
	private DecimalFormat doubleFormat = new DecimalFormat("0.00");
	
	private HashMap<Integer, Goods> mapGoods;
	
	@Override
	@Transactional
	public ObjectResult statistics(int userId, Date startDate, Date endDate, int dimension, int sellGranularity,
			int sellByPeriod) {
		long l3 = 0;
		long l1 = System.currentTimeMillis();
		List<Indent> indents = indentDA.getIndentsByTime(startDate, endDate);
		if (indents == null || indents.isEmpty())
			return new ObjectResult("No order paid in this period", false);
		long l2 = System.currentTimeMillis();
		ObjectResult result = new ObjectResult(Result.OK, true);
		if (dimension == ConstantValue.STATISTICS_DIMENSTION_PAYWAY){
			ArrayList<StatItem> stats = statisticsPayway(indents);
			result.data = stats;
			l3 = System.currentTimeMillis();
			logger.debug("do statistics by payway use time  " + (l3-l2));
		} else if (dimension == ConstantValue.STATISTICS_DIMENSTION_SELL){
			if (sellGranularity != ConstantValue.STATISTICS_SELLGRANULARITY_BYGOODS
					&& sellGranularity != ConstantValue.STATISTICS_SELLGRANULARITY_BYCATEGORY2
					&& sellGranularity != ConstantValue.STATISTICS_SELLGRANULARITY_BYCATEGORY1){
				return new ObjectResult("Wrong param of Sell Granularity.", false);
			}
			ArrayList<StatItem> stats = statisticsSell(indents, sellGranularity);
			result.data = stats;
			l3 = System.currentTimeMillis();
			logger.debug("do statistics by payway use time  " + (l3-l2));
		} else if (dimension == ConstantValue.STATISTICS_DIMENSTION_PERIODSELL){
			if (sellByPeriod != ConstantValue.STATISTICS_PERIODSELL_PERDAY
					&& sellByPeriod != ConstantValue.STATISTICS_PERIODSELL_PERHOUR
					&& sellByPeriod != ConstantValue.STATISTICS_PERIODSELL_PERWEEK
					&& sellByPeriod != ConstantValue.STATISTICS_PERIODSELL_PERMONTH){
				return new ObjectResult("Wrong param of Sell By Period.", false);
			}
			ArrayList<StatItem> stats = statisticsSellByPeriod(indents, sellByPeriod, startDate, endDate);
			result.data = stats;
			l3 = System.currentTimeMillis();
			logger.debug("do statistics by payway use time  " + (l3-l2));
		}
		logger.debug("statistics, query use time = " + (l2 - l1) + ", stat use time = " + (l3 - l2) 
				+ ", dimension = " + dimension + ", sellGranularity = " + sellGranularity + ", sellByPeriod = " + sellByPeriod 
				+ ", indent size = " + indents.size() 
				+ ", start = " + ConstantValue.DFYMD.format(startDate) + ", end = " + ConstantValue.DFYMD.format(endDate));
		//format double value
		if (result.data != null){
			for(StatItem si : (ArrayList<StatItem>)result.data){
				si.paidPrice = Double.parseDouble(doubleFormat.format(si.paidPrice));
				si.totalPrice = Double.parseDouble(doubleFormat.format(si.totalPrice));
			}
		}
		return result;
	}
	
	@Transactional
	private void initGoodsMap(){
		List<Goods> goods = goodsDA.getAllGoods();
		mapGoods = new HashMap<>();
		for (Goods g : goods) {
			mapGoods.put(g.getId(), g);
		}
	}
	
	/**
	 * 首先根据sellByPeriod把时间段按粒度划分开, 然后遍历indent的列表, 找到对应的时间段的数据, 添加进去
	 * 客户端保证起始 结束 时间非空
	 * @param indents
	 * @param sellByPeriod
	 * @return
	 */
	@Transactional
	private ArrayList<StatItem> statisticsSellByPeriod(List<Indent> indents, int sellByPeriod, Date startDate, Date endDate){
		ArrayList<StatItem> stats = new ArrayList<>();
		//initial time period into map
		HashMap<String, StatItem> mapPeriod = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERDAY){
			do{
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime();
				StatItem si = new StatItem(ConstantValue.DFYMD.format(time1) + " - " + ConstantValue.DFWEEK.format(time1));
				mapPeriod.put(si.itemName, si);
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
			} while(c.getTime().getTime() <= endDate.getTime());
		} else if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERHOUR){
			c.set(Calendar.HOUR_OF_DAY, 0);
			do{
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime();
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				Date time2 = c.getTime();
				StatItem si = new StatItem(ConstantValue.DFYMDHMS.format(time1) + " - " + ConstantValue.DFHMS.format(time2) + " - " + ConstantValue.DFWEEK.format(time1));
				mapPeriod.put(si.itemName, si);
				c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + 1);
			} while (c.getTime().getTime() <= endDate.getTime());
		} else if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERWEEK){
			c.setFirstDayOfWeek(Calendar.SUNDAY);
			do{
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime(); //取当天的0时作为起始点, 后面需要用这个时间进行比较订单
				c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				Date time2 = c.getTime();//取当天的24时作为结束点, 后面需要用这个时间进行比较订单
				if (time2.getTime() > endDate.getTime())
					time2 = endDate;
				StatItem si = new StatItem(time1, time2);
				mapPeriod.put(si.itemName, si);
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);// set to next day
			} while (c.getTime().getTime() < endDate.getTime());
		} else if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERMONTH){
			do{
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime();//取当天的0时作为起始点, 后面需要用这个时间进行比较订单
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				Date time2 = c.getTime();//取当天的24时作为结束点, 后面需要用这个时间进行比较订单
				if (time2.getTime() > endDate.getTime())
					time2 = endDate;
				StatItem si = new StatItem(time1, time2);
				mapPeriod.put(si.itemName, si);
				c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);// set to next day
			} while (c.getTime().getTime() < endDate.getTime());
		}
		//start loop indents list
		for(Indent indent : indents){
			if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERDAY){
				c.setTime(indent.getCreateTime());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime();
				StatItem si = mapPeriod.get(ConstantValue.DFYMD.format(time1) + " - " + ConstantValue.DFWEEK.format(time1));
				si.soldAmount += 1;
				si.totalPrice += indent.getTotalPrice();
				si.paidPrice += indent.getPaidPrice();
			} else if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERHOUR){
				c.setTime(indent.getCreateTime());
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date time1 = c.getTime();
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				Date time2 = c.getTime();
				StatItem si = mapPeriod.get(ConstantValue.DFYMDHMS.format(time1) + " - " + ConstantValue.DFHMS.format(time2) + " - " + ConstantValue.DFWEEK.format(time1));
				si.soldAmount += 1;
				si.totalPrice += indent.getTotalPrice();
				si.paidPrice += indent.getPaidPrice();
			} else if (sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERWEEK
					|| sellByPeriod == ConstantValue.STATISTICS_PERIODSELL_PERMONTH){
				//比较StatItem的前后时间即可
				Iterator<StatItem> it = mapPeriod.values().iterator();
				while (it.hasNext()){
					StatItem si = it.next();
					if (indent.getCreateTime().getTime() > si.startTime.getTime()
							&& indent.getCreateTime().getTime() < si.endTime.getTime()){
						si.soldAmount ++;
						si.totalPrice += indent.getTotalPrice();
						si.paidPrice += indent.getPaidPrice();
						break;
					}
				}
			} 
		}
		Iterator<StatItem> its = mapPeriod.values().iterator();
		while(its.hasNext()){
			stats.add(its.next());
		}
		
		Collections.sort(stats, new Comparator<StatItem>(){

			@Override
			public int compare(StatItem o1, StatItem o2) {
				return o1.itemName.compareTo(o2.itemName);
			}});
		return stats;
	}
	
	/**
	 * 根据统计粒度, 讲每个indent的ditail下对应的dish/category进行分类统计, 如果对应的dish/category已经删除, 则记录为UNFOUND
	 * @param indents
	 * @param sellGranularity
	 * @return
	 */
	@Transactional
	private ArrayList<StatItem> statisticsSell(List<Indent> indents, int sellGranularity){
		if (mapGoods == null)
			initGoodsMap();
		ArrayList<StatItem> stats = new ArrayList<>();
		HashMap<String, StatItem> mapSell = new HashMap<>();
		//first define one UNFOUND for those dish/category1/category2 cannot be found
		String sUnfound = "UNFOUND";
		StatItem ssUnfound = new StatItem(sUnfound);
		mapSell.put(sUnfound, ssUnfound);
		for(Indent indent : indents){
			List<IndentDetail> details = indent.getItems();
			for(IndentDetail detail : details){
				Goods goods = mapGoods.get(detail.getGoodsId());
				if (sellGranularity == ConstantValue.STATISTICS_SELLGRANULARITY_BYGOODS){
					if (goods == null){
						ssUnfound.soldAmount += detail.getAmount();
						ssUnfound.totalPrice += detail.getSoldPrice();
					} else {
						StatItem ss = mapSell.get(goods.getName());
						if (ss == null){
							ss = new StatItem(goods.getName());
							mapSell.put(goods.getName(), ss);
						}
						accumulateIndentDetailInfo(ss, goods, detail);
					}
				} else if (sellGranularity == ConstantValue.STATISTICS_SELLGRANULARITY_BYCATEGORY2){
					if (goods == null || goods.getCategory2() == null){
						ssUnfound.soldAmount += detail.getAmount();
						ssUnfound.totalPrice += detail.getSoldPrice();
					} else {
						StatItem ss = mapSell.get(goods.getCategory2().getName());
						if (ss == null){
							ss = new StatItem(goods.getCategory2().getName());
							mapSell.put(goods.getCategory2().getName(), ss);
						}
						accumulateIndentDetailInfo(ss, goods, detail);
					}
				} else if (sellGranularity == ConstantValue.STATISTICS_SELLGRANULARITY_BYCATEGORY1){
					if (goods == null || goods.getCategory2() == null || goods.getCategory2().getCategory1() == null){
						ssUnfound.soldAmount += detail.getAmount();
						ssUnfound.totalPrice += detail.getSoldPrice();
					} else {
						StatItem ss = mapSell.get(goods.getCategory2().getCategory1().getName());
						if (ss == null){
							ss = new StatItem(goods.getCategory2().getCategory1().getName());
							mapSell.put(goods.getCategory2().getCategory1().getName(), ss);
						}
						accumulateIndentDetailInfo(ss, goods, detail);
					}
				}
			}
		}
		//remove unfound if it is 0
		if (ssUnfound.soldAmount == 0){
			mapSell.remove(sUnfound);
		}
		Iterator<StatItem> its = mapSell.values().iterator();
		while(its.hasNext()){
			stats.add(its.next());
		}
		return stats;
	}
	
	@Transactional
	private void accumulateIndentDetailInfo(StatItem ss, Goods goods, IndentDetail detail){
		ss.soldAmount += detail.getAmount();
		ss.totalPrice += detail.getSoldPrice();
	}
	
	@Transactional
	private ArrayList<StatItem> statisticsPayway(List<Indent> indents){
		ArrayList<StatItem> stats = new ArrayList<>();
		HashMap<String, StatItem> mapPayway = new HashMap<>();
		for(Indent indent : indents){
			if (indent.getPayWay() == null)
				continue;
			StatItem sp = mapPayway.get(indent.getPayWay());
			if (sp == null){
				sp = new StatItem(indent.getPayWay());
				mapPayway.put(indent.getPayWay(), sp);
			}
			sp.paidPrice += indent.getPaidPrice();
			sp.soldAmount++;
		}
		Iterator<StatItem> its = mapPayway.values().iterator();
		while(its.hasNext()){
			stats.add(its.next());
		}
		return stats;
	}
}
