package cn._51app.dao;

import java.util.List;
import java.util.Map;

import cn._51app.entity.EvaluationInfo;

public interface IUUMallDao {

	Integer haveOpenid(String openid);
	
	Integer haveMobile(String mobile);

	int insertOpenid(Map<String, Object> paramMap);

	Map<String, Object> findUser4Openid(String openid);

	Map<String, Object> findUser4Mobile(String mobile);

	int binding(String mobile, int id);

	String queryPreUrl(String infoId, String textureIds);

	int getShopNum(String deviceNo, String app);
	
	String youNav(Map<String,Object>paramMap) throws Exception;
	
	String youSpecial(Map<String,Object>paramMap)throws Exception;
	
	String youStrategy(Map<String,Object>paramMap)throws Exception;

	int homeOrderNum(String deviceNo, String app);

	Map<String, Object> getOrder4No(String order_no);

	List<Map<String, Object>> queryShopList(String deviceNo, String app);

	Map<String, Object> getOrderComm(String order_no);

	List<Map<String, Object>> shopBuy(String shopNos, String deviceNo,
			String app);

	Integer saveComment(String goodsId, Integer evalType, String content,
			String deviceNo, String mobile, String texture) throws Exception;

	List<Map<String, Object>> queryAddress(String deviceNo, String app);

	String getOpenId(String deviceNo, String app);

	List<Map<String, Object>> getOrderList(String deviceNo, String app,
			int page, int number, Integer state);

	List<Map<String, Object>> getShopInfoByShopNos(String shopNo);

	Integer isBoutique(String id);

	Integer isShopRepeat(String infoId, String textureIds, String deviceNo,
			String app);

	Integer mergeShop(String infoId, String textureIds, String deviceNo,
			String app, int num);

	double createOrderByShops(String orderNo, String shopNo, String infoId,
			String textureIds, String textureName, String userId,
			String imgUrl, String fileType, String num, String name,
			String nowPrice, String payId, double orgPrice, double desPrice,
			String couponId, double totalFee, Map<String, Object> addressMap,
			String deviceNo, String app, String transportFee, String remark);

	double activeOrder(String orderNo, String couponId, String addressId,
			String num, String payId, double totalFee, double orgPrice,
			double desPrice, String deviceNo, String app,
			Map<String, Object> addressMap, String remark);

}
