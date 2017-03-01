package cn._51app.service.diy2_0;

public interface IAdressService2 {

	/**
	 * TODO 获取
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String getAddress(String deviceNo, String app) throws Exception;

	/**
	 * TODO 删除地址
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	boolean deleteAddress(String deviceNo, String app, String addressId) throws Exception;

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
	 * @throws Exception
	 */
	String updateAddress(String deviceNo, String app, String addressId, String name, String mobile, String province,
			String area, String isDefault) throws Exception;

	/**
	 * TODO 设置默认地址
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	boolean isDefault(String deviceNo, String app, String addressId) throws Exception;

	/**
	 * TODO 获取用户（id版）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getAddressForId(String userId) throws Exception;

	/**
	 * TODO 更新地址（id版）
	 * @param userId
	 * @param addressId
	 * @param name
	 * @param mobile
	 * @param province
	 * @param area
	 * @param isDefault
	 * @return
	 * @throws Exception
	 */
	String updateAddressForId(String userId, String addressId, String name, String mobile, String province, String area,
			String isDefault) throws Exception;

	/**
	 * TODO 删除地址（id版）
	 * @param userId
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	boolean deleteAddressForId(String userId, String addressId) throws Exception;

	/**
	 * TODO 修改地址为默认地址
	 * @param userId
	 * @param addressId
	 * @return
	 * @throws Exception
	 */
	boolean isDefaultForId(String userId, String addressId) throws Exception;

}
