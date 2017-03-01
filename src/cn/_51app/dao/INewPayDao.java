package cn._51app.dao;

import java.util.Map;

public interface INewPayDao {

	/**
	 * tengh 2016年5月21日 下午3:24:48
	 * @param trade_no
	 * @return
	 * TODO 查询交易是否处理过
	 */
	int checkOrderIsPayed(String trade_no);

	/**
	 * tengh 2016年5月21日 下午3:38:30
	 * @param paramMap
	 * @return
	 * TODO
	 */ 
	boolean insertPayRecord(Map<String, Object> paramMap);

	/**
	 * tengh 2016年5月23日 下午8:59:59
	 * @param paramMap
	 * @return
	 * TODO 改变订单状态
	 */
	boolean changeOrderStatus(Map<String, Object> paramMap);

	/**
	 * tengh 2016年5月23日 下午9:20:49
	 * @param paramMap
	 * @return
	 * TODO 记录交易记录
	 */
	boolean updatePayRecord(Map<String, Object> paramMap);

	int isH5Custom(String orderNo);

	boolean changeOrderStatus4H5Custom(Map<String, Object> paramMap);
	
	void changeGoodsSell(String orderNo) throws Exception;

}
