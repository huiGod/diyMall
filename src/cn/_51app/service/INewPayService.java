package cn._51app.service;

import java.util.Map;

public interface INewPayService {

	/**
	 * tengh 2016年5月21日 下午3:22:36
	 * @param trade_no
	 * @return
	 * TODO 查询交易是否处理过
	 */
	int checkOrderIsPayed(String trade_no);

	/**
	 * tengh 2016年5月21日 下午3:37:17
	 * @param paramMap
	 * TODO 
	 * @return 插入处理记录 
	 */
	boolean insertPayRecord(Map<String, Object> paramMap);

	/**
	 * tengh 2016年5月23日 下午8:58:28
	 * @param paramMap
	 * @return
	 * TODO 改变订单状态
	 */
	boolean changeOrderStatus(Map<String, Object> paramMap);

	/**
	 * tengh 2016年5月23日 下午9:19:58
	 * @param paramMap
	 * @return
	 * TODO 记录交易记录
	 */
	boolean updatePayRecord(Map<String, Object> paramMap);

	/**
	 * tengh 2016年5月25日 上午11:28:09
	 * @param out_trade_no
	 * TODO 拆订单
	 */
	void separateOrder(String out_trade_no);

	boolean isH5Custom(String orderNo);

	boolean changeOrderStatus4H5Custom(Map<String, Object> paramMap);

	/**
	 * TODO 处理用户心
	 * @param orderNo
	 */
	void makeUserHeart(String orderNo);
	
	/**
	 * 根据订单号更改商品销售量 
	 * @param orderNo 订单号
	 * @author yuanqi 2017年2月13日 上午11:49:16
	 */
	void changeGoodsSell(String orderNo);
}
