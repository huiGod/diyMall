package cn._51app.service;

public interface INewAddressService {

	/**
	 * tengh 2016年5月17日 下午5:26:44
	 * @param deviceNo
	 * @param app
	 * @return
	 * TODO 查询地址信息
	 */
	String getAdress(String deviceNo, String app) throws Exception;

	/**
	 * tengh 2016年5月17日 下午6:29:14
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @param name
	 * @param mobile
	 * @param province
	 * @param area
	 * @param isDefault
	 * @return
	 * TODO 修改地址
	 */
	String updateAdress(String deviceNo, String app, String addressId, String name, String mobile, String province,
			String area, String isDefault) throws Exception;

	/**
	 * tengh 2016年5月17日 下午7:56:43
	 * @param deviceNo
	 * @param app
	 * @param addressId
	 * @return
	 * TODO  删除地址
	 */
	boolean deleteAdress(String deviceNo, String app, String addressId) throws Exception;


}
