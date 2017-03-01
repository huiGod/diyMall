package cn._51app.dao;

import java.util.List;
import java.util.Map;

public interface INewAddressDao {
	/**
	 * tengh 2016年5月17日 下午5:39:27
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询地址
	 */
	List<Map<String, Object>> queryAddress(String deviceNo, String app);

	/**
	 * tengh 2016年5月17日 下午7:37:26
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @param name
	 * @param mobile
	 * @param province
	 * @param area
	 * @param isDefault
	 * TODO 修改地址
	 */
	int updateAdress(String deviceNo, String app, String addressId, String name, String mobile, String province,
			String area, String isDefault);

	/**
	 * tengh 2016年5月17日 下午7:58:58
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 * TODO 删除订单
	 */
	boolean deleteAdress(String deviceNo, String app, String addressId);
}
