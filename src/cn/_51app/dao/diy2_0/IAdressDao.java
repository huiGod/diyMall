package cn._51app.dao.diy2_0;

import java.util.List;
import java.util.Map;

public interface IAdressDao {

	List<Map<String, Object>> queryAddress(String deviceNo, String app);

	/**
	 * TODO 修改地址
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @param name
	 * @param mobile
	 * @param province
	 * @param area
	 * @param isDefault
	 * @return
	 */
	int updateAdress(String deviceNo, String app, String addressId, String name, String mobile, String province,
			String area, String isDefault);

	/**
	 * TODO 删除地址
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 */
	boolean deleteAdress(String deviceNo, String app, String addressId);

	/**
	 * TODO 设置默认地址
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 */
	boolean isDefault(String deviceNo, String app, String addressId);

	/**
	 * TODO 获取地址（id版）
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> queryAddressForId(String userId);

	/**
	 * TODO 插入或修改地址（id版）
	 * @param userId
	 * @param addressId
	 * @param name
	 * @param mobile
	 * @param province
	 * @param area
	 * @param isDefault
	 * @return
	 */
	int updateAdressForId(String userId, String addressId, String name, String mobile, String province, String area,
			String isDefault);

	/**
	 * TODO 删除地址（id版）
	 * @param userId
	 * @param addressId
	 * @return
	 */
	boolean deleteAdressForId(String userId, String addressId);

	/**
	 * TODO 修改用户地址为默认地址（id版）
	 * @param userId
	 * @param addressId
	 * @return
	 */
	boolean isDefaultForId(String userId, String addressId);

	/**
	 * tengh 2016年12月5日 下午5:29:35
	 * @param id
	 * @return
	 * TODO 判断是不是偏远地区
	 */
	boolean isRemote(String id);

}
