package cn._51app.service.diy2_0;

public interface IAdressService {

	/**
	 * TODO 获取用户地址
	 * @param deviceNo
	 * @param app
	 * @return
	 * @throws Exception
	 */
	String getAdress(String deviceNo, String app) throws Exception;

	/**
	 * TODO 获取用户地址（id版）
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String getAdressForId(String userId) throws Exception;

}
