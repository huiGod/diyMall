package cn._51app.dao.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn._51app.dao.BaseDao;
import cn._51app.dao.IGoodsInfoDao;

@Repository
public class GoodsInfoDao extends BaseDao implements IGoodsInfoDao {

	private DecimalFormat df = new DecimalFormat("######0.00");

	// private final String dgurl =PropertiesUtil.getValue("diy.goods.url");
	// private final String imgPath
	// =PropertiesUtil.getValue("diy.goods.img.path");

	@Transactional(readOnly = true)
	public String getGoodsInfoList(Map<String, Object> paramMap)
			throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String select = "SELECT dgi.id,dgi.storeId,dgi.name,dgi.icoUrl,dgi.feightNote"
				+ ",(SELECT paramPriceImg FROM diy_goods_param WHERE gId=dgi.id) AS paramPriceImg";
		String from = " FROM diy_goods_info dgi";
		String where = " WHERE dgi.isBoutique=2";
		String orderBy = " ORDER BY sort ASC";
		// String limit =" LIMIT :startIndex,:pageSize";
		String sql = select + from + where + orderBy;// +limit
		List<Map<String, Object>> lm = null;
		if (super.isCacheNull(cacheKey).equals("a")) {
			lm = super.npjt.queryForList(sql, paramMap);
			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
					String paramPriceImgStr = m.get("paramPriceImg").toString();
					String[] paramPriceArr = paramPriceImgStr.split("&");
					m.put("nowPrice",
							paramPriceArr[1].split("#")[0].split(",")[1]);
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				return super.saveAndGet(lm, cacheKey, cacheTime);
			} else {
				return "";
			}
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			lm = super.npjt.queryForList(sql, paramMap);
			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
					String paramPriceImgStr = m.get("paramPriceImg").toString();
					String[] paramPriceArr = paramPriceImgStr.split("&");
					m.put("nowPrice",
							paramPriceArr[1].split("#")[0].split(",")[1]);
				}
				return super.toJson(lm);
			} else {
				return "";
			}
		}
	}

	/**
	 * @author zhanglz 首页
	 */
	public String getHomeList(Map<String, Object> paramMap) throws Exception {
		// 获取缓存key
		String cacheKey = paramMap.get("cacheKey").toString();
		// 获取缓存时间
		int cacheTime = new Integer(paramMap.get("cacheTime").toString());
		// 照片书不查
		String sql = "SELECT g.id AS id,g.`name` AS `name`,g.`icoUrl` AS icoUrl,g.`transportfee` AS transportfee ,t.`now_price` AS nowPrice ,g.`sell`,d.`id` AS goodId,d.`name` AS goodName,g.isBoutique "
				+ "FROM `diy_goods_info` g,`diy_info_texture` t,`diy_good` d "
				+ "WHERE state=1 AND g.`id` = t.`info_id` AND t.`isdefault` = 1 AND g.isBoutique = 2 AND d.`id`=g.`good_id` AND g.goodsType IN('0','1','2') AND g.extra=1 "
				+ "ORDER BY g.sort";
		// 获取缓存成功
		if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
			// 查询不到缓存
		} else if (isCacheNull(cacheKey).equals("a")
				|| isCacheNull(cacheKey).equals("c")) {
			// 数据库查询数据
			List<Map<String, Object>> list = super.npjt.queryForList(sql,
					paramMap);
			if (!list.isEmpty()) {
				// 获取前缀
				String dgurl = paramMap.get("dgurl").toString();
				System.out.println("dgurl=" + dgurl);
				// 设置商品图，加上前缀
				for (Map<String, Object> map : list) {
					String icoUrl = null == map.get("icoUrl") ? "" : map.get(
							"icoUrl").toString();
					if (StringUtils.isNotEmpty(icoUrl)) {
						map.put("icoUrl", dgurl + icoUrl);
					}
					// 格式化价格
					map.put("nowPrice", df.format(map.get("nowPrice")));
					// 设置运费显示
					String transportfee = null == map.get("transportfee") ? ""
							: map.get("transportfee").toString();
					if (transportfee.equals("0.0")) {
						map.put("transportfee", "免运费");
					} else {
						map.put("transportfee", "运费  ￥" + transportfee);
					}
				}
				if (isCacheNull(cacheKey).equals("a"))
					return super.saveAndGet(list, cacheKey, cacheTime);
				else
					return super.toJson(list);
			}
		}
		// 如果数据是空的，无异常，无缓存，返回空字符串
		return "";
	}

	/**
	 * >>Faster
	 * 
	 * 以商品id查询标题数据
	 */
	public String getGoodsTitleById(Map<String, Object> paramMap)
			throws Exception {
		// 获取缓存key
		String cacheKey = paramMap.get("cacheKey").toString();

		// 内连接sql，texture_ids对应选择属性（多个），以info_id关联材质属性表
		String select = "SELECT dgi.id,dgi.title,dgi.transportfee,tex.org_price,tex.now_price,tex.texture_ids,dgi.isBoutique,dgi.recommend ";
		String from = " FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id =tex.info_id";
		String where = " WHERE dgi.id=:id AND tex.isdefault=1 AND dgi.state=1";
		String sql = select + from + where;
		// 设置返回或放入缓存数据
		Map<String, Object> m = null;
		// 缓存查不到
		if (isCacheNull(cacheKey).equals("a")) {
			try {
				// 查询数据库数据
				m = super.npjt.queryForMap(sql, paramMap);
				// 获取现价和原价
				m.put("nowPrice",
						m.get("now_price") != null ? df.format(m
								.get("now_price")) : "0.00");
				m.put("originalPrice",
						m.get("org_price") != null ? df.format(m
								.get("org_price")) : "0.00");
				// 设置返回属性，list形式
				List<String> tl = new ArrayList<String>();
				// 获取选择的材质属性
				String textureIds = m.get("texture_ids") != null ? m.get(
						"texture_ids").toString() : "未选择";
				// 以下划线分割数据
				String arrIds[] = textureIds.split("_");
				// 根据arrIds查询选择属性逗号隔开
				for (int i = 0; i < arrIds.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", arrIds[i]);
					String param = getSelectTexture(map);
					if (i == arrIds.length - 1) {
						tl.add(param);
					} else {
						tl.add(param + ",");
					}
				}
				String dgurl = paramMap.get("dgurl").toString();
				String recommend = m.get("recommend") == null ? "" : m.get(
						"recommend").toString();
				// 属性放入对象
				m.put("paramList", tl);
				m.put("recommend", getGoods(recommend, dgurl));

				// 设置运费显示
				String transportfee = null == m.get("transportfee") ? "" : m
						.get("transportfee").toString();
				if (transportfee.equals("0.0")) {
					m.put("transportfee", "免运费");
				} else {
					m.put("transportfee", "运费  ￥" + transportfee);
				}

			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			return super.saveAndGet(m, cacheKey, cacheTime);
		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
				m.put("nowPrice",
						m.get("now_price") != null ? m.get("now_price") : "0.0");
				m.put("originalPrice",
						m.get("org_price") != null ? m.get("org_price") : "0.0");

				List<String> tl = new ArrayList<String>();

				String textureIds = m.get("texture_ids") != null ? m.get(
						"texture_ids").toString() : "未选择";
				String arrIds[] = textureIds.split("_");
				for (int i = 0; i < arrIds.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", arrIds[i]);
					String param = getSelectTexture(map);
					if (i == arrIds.length - 1) {
						tl.add(param);
					} else {
						tl.add(param + ",");
					}
				}

				m.put("paramList", tl);

				// 设置运费显示
				String transportfee = null == m.get("transportfee") ? "" : m
						.get("transportfee").toString();
				if (transportfee.equals("0.0")) {
					m.put("transportfee", "免运费");
				} else {
					m.put("transportfee", "运费  ￥" + transportfee);
				}

			} catch (EmptyResultDataAccessException e) {
			}
			if (m != null) {
				// 转换json
				return super.toJson(m);
			} else {
				return "";
			}
		}

	}

	// 不知有何用的desert
	@Override
	public String getGoodsEditChart(Map<String, Object> paramMap)
			throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();

		String select = "SELECT previewImgUrl,previewImg";
		String from = " FROM diy_goods_info";
		String where = " id=:id";
		String sql = select + from + where;
		Map<String, Object> m = null;
		if (isCacheNull(cacheKey).equals("a")) {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
				String prArr[] = (m.get("previewImg").toString()).split(",");
				m.put("h", prArr[0]);
				m.put("w", prArr[1]);
				m.put("mr", prArr[2]);
				m.put("mb", prArr[3]);
				m.put("ml", prArr[4]);
				m.put("mt", prArr[5]);
				m.remove("previewImg");
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			if (m != null) {
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				return super.saveAndGet(m, cacheKey, cacheTime);
			}
		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
				String prArr[] = (m.get("previewImg").toString()).split(",");
				m.put("h", prArr[0]);
				m.put("w", prArr[1]);
				m.put("mr", prArr[2]);
				m.put("mb", prArr[3]);
				m.put("ml", prArr[4]);
				m.put("mt", prArr[5]);
				m.remove("previewImg");
			} catch (EmptyResultDataAccessException e) {
				return "";
			}
			if (m != null) {
				return super.toJson(m);
			}
		}
		return "";
	}

	/**
	 * >>Faster
	 * 
	 * 商品详情and介绍
	 * 
	 * @param paramMap
	 *            Map参数
	 */
	@Override
	public String getGoodsDetails(Map<String, Object> paramMap)
			throws Exception {
		// 获取缓存名称
		String cacheKey = paramMap.get("cacheKey").toString();
		// sql语句
		String select = "SELECT id,introduce,parameter,packAfterSale,priceNote";
		String from = " FROM diy_goods_info";
		String where = " WHERE id=:id";
		String sql = select + from + where;
		// 设置查询sql返回容器
		Map<String, Object> m = null;
		// 使用isCacheNull查看缓存空为a,异常为c,正常为b
		if (super.isCacheNull(cacheKey).equals("a")) {

			try {
				// 查询数据库获取数据
				m = super.npjt.queryForMap(sql, paramMap);

				// 无数据空查询异常
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			// 获取到数据执行
			if (m != null) {
				// 设置返回的Map类型容器
				Map<String, Object> responseMap = new HashMap<String, Object>();
				// 获取多张定制介绍图片，逗号分割
				String[] introduceArr = (m.get("introduce").toString())
						.split(",");
				// 以&分开，表示每一行的数据，获取商品参数
				String[] parameterArr1 = (m.get("parameter").toString())
						.split("&");
				// 设置lmTmp2的List来存放每一行数据
				List<Map<String, Object>> lmTmp2 = new ArrayList<Map<String, Object>>();
				// 循环放数据到lmTmp2
				for (int i = 0; i < parameterArr1.length; i++) {
					// 再次拆分数据，以#分开
					String[] parameterArr1_1Arr = (parameterArr1[i]).split("#");
					// 如果长度不是2个长度，就不读取这条数据
					if (parameterArr1_1Arr.length != 2)
						break;
					// 设置title作为#分开的数据1
					String title = parameterArr1_1Arr[0];
					// 设置txt作为#分开的数据2
					String txt = parameterArr1_1Arr[1];
					// 封装到Map
					Map<String, Object> mTmp = new HashMap<String, Object>();
					// 放到map
					mTmp.put("title", title);
					mTmp.put("txt", txt);
					// 添加到List
					lmTmp2.add(mTmp);
				}
				// 获取包装售后
				String[] packAfterSaleArr1 = (m.get("packAfterSale").toString())
						.split("&");
				List<Map<String, Object>> lmTmp3 = new ArrayList<Map<String, Object>>();
				// 设置lmTmp3的List来存放每一行数据
				for (int i = 0; i < packAfterSaleArr1.length; i++) {
					// 再次拆分数据，以#分开
					String[] packAfterSaleArr1_1Arr = (packAfterSaleArr1[i])
							.split("#");
					// 如果长度不是2个长度，就不读取这条数据
					if (packAfterSaleArr1_1Arr.length != 2)
						break;
					// 设置title作为#分开的数据1
					String title = packAfterSaleArr1_1Arr[0];
					// 设置txt作为#分开的数据2
					String txt = packAfterSaleArr1_1Arr[1];
					// 封装到Map
					Map<String, Object> mTmp = new HashMap<String, Object>();
					// 放到map
					mTmp.put("title", title);
					mTmp.put("txt", txt);
					// 添加到List
					lmTmp3.add(mTmp);
				}
				// 设置返回id
				responseMap.put("id", paramMap.get("id").toString());
				// 以数组形式创建list
				List<String> lstr = Arrays.asList(introduceArr);
				String dgurl = paramMap.get("dgurl").toString();
				// 设置介绍图片返回容器
				List<String> lsTmp = new ArrayList<String>();
				// 为每个图片url加上前缀
				for (int i = 0; i < lstr.size(); i++) {
					lsTmp.add(dgurl + lstr.get(i));
				}
				// 价格说明or申明
				String[] priceNoteArr = m.get("priceNote").toString()
						.split("#");
				// 设置关于我们说明
				Map<String, Object> mTmp = new HashMap<String, Object>();
				// 返回title
				mTmp.put("title", priceNoteArr[0]);
				// 返回txt
				if (priceNoteArr.length > 1) {
					mTmp.put("txt", priceNoteArr[1]);
				}
				// 封装各个map类型数据设置为返回数据
				responseMap.put("priceNote", mTmp);
				responseMap.put("introduceList", lsTmp);
				responseMap.put("parameterList", lmTmp2);
				responseMap.put("packAfterSaleList", lmTmp3);
				// 获取缓存名称
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				// 总数据放入缓存
				return super.saveAndGet(responseMap, cacheKey, cacheTime);

			}
			// 缓存有数据，读取缓存
		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
			// 缓存异常，同第一次读取缓存一样操作
		} else {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			if (m != null) {
				Map<String, Object> responseMap = new HashMap<String, Object>();

				String[] introduceArr = (m.get("introduce").toString())
						.split(",");

				String[] parameterArr1 = (m.get("parameter").toString())
						.split("&");
				List<Map<String, Object>> lmTmp2 = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < parameterArr1.length; i++) {
					String[] parameterArr1_1Arr = (parameterArr1[i]).split("#");
					if (parameterArr1_1Arr.length != 2)
						break;
					String title = parameterArr1_1Arr[0];
					String txt = parameterArr1_1Arr[1];
					Map<String, Object> mTmp = new HashMap<String, Object>();
					mTmp.put("title", title);
					mTmp.put("txt", txt);
					lmTmp2.add(mTmp);
				}
				String[] packAfterSaleArr1 = (m.get("packAfterSale").toString())
						.split("&");
				List<Map<String, Object>> lmTmp3 = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < packAfterSaleArr1.length; i++) {
					String[] packAfterSaleArr1_1Arr = (packAfterSaleArr1[i])
							.split("#");
					if (packAfterSaleArr1_1Arr.length != 2)
						break;
					String title = packAfterSaleArr1_1Arr[0];
					String txt = packAfterSaleArr1_1Arr[1];
					Map<String, Object> mTmp = new HashMap<String, Object>();
					mTmp.put("title", title);
					mTmp.put("txt", txt);
					lmTmp3.add(mTmp);
				}
				responseMap.put("id", paramMap.get("id").toString());
				List<String> lstr = Arrays.asList(introduceArr);
				String dgurl = paramMap.get("dgurl").toString();

				List<String> lsTmp = new ArrayList<String>();
				for (int i = 0; i < lstr.size(); i++) {
					lsTmp.add(dgurl + lstr.get(i));
				}
				String[] priceNoteArr = m.get("priceNote").toString()
						.split("#");
				Map<String, Object> mTmp = new HashMap<String, Object>();
				mTmp.put("title", priceNoteArr[0]);
				mTmp.put("txt", priceNoteArr[1]);

				responseMap.put("priceNote", mTmp);
				responseMap.put("introduceList", lsTmp);
				responseMap.put("parameterList", lmTmp2);
				responseMap.put("packAfterSaleList", lmTmp3);

				super.toJson(responseMap);
			}

		}
		// 缓存无数据，又查不出数据，也没有异常，那就返回空字符啦
		return "";

	}

	@Override
	public String getGoodsBuyParam(Map<String, Object> paramMap)
			throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();

		String select = "SELECT gi.id,gi.gtId,gi.storeId,gi.stock,tgp.paramPriceImg,tgp.param,gi.goodsType";
		String from = " FROM diy_goods_info gi INNER JOIN diy_goods_param tgp ON gi.id=tgp.gId";
		String where = " WHERE gi.id=:id";
		String sql = select + from + where;
		Map<String, Object> m = null;
		Map<String, Object> mTmp = null;
		String[] textArr = null;

		if (super.isCacheNull(cacheKey).equals("a")) {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			if (m != null) {

				Map<String, Object> responseMap = new HashMap<String, Object>();
				String title = null;
				String txt = null;

				// 只能有一个 &
				String paramPriceImg = m.get("paramPriceImg").toString();
				// 除掉重复
				Set<Map<String, Object>> tmpSet = null;
				List<Map<String, Object>> plm = new ArrayList<Map<String, Object>>();

				String[] paramPIArr = paramPriceImg.split("&");
				title = paramPIArr[0];
				txt = paramPIArr[1];
				textArr = txt.split("#");
				String httpRoot = "http://120.26.112.213:8083/file/";

				for (int i = 0; i < textArr.length; i++) {
					String[] textpArr = textArr[i].split(",");
					Map<String, Object> rm = new HashMap<String, Object>();
					rm.put("name", textpArr[0]);
					rm.put("nowPrice", textpArr[1]);
					rm.put("originalPrice", textpArr[2]);
					rm.put("img", httpRoot + textpArr[3]);
					if (textpArr.length == 5) {
						rm.put("mUrl", httpRoot + textpArr[4]);
					} else {
						rm.put("mUrl", "");
					}
					plm.add(rm);
				}
				mTmp = new HashMap<String, Object>();
				mTmp.put("title", title);
				mTmp.put("list", plm);
				tmpSet = new HashSet<Map<String, Object>>();
				tmpSet.add(mTmp);
				List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>(
						tmpSet);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> tmpNIList = (List<Map<String, Object>>) tmpList
						.get(0).get("list");
				// 默认图和价格
				responseMap.put("nowPrice", tmpNIList.get(0).get("nowPrice"));
				responseMap.put("img", tmpNIList.get(0).get("img"));

				responseMap.put("listA", tmpList);

				// 可能有多个 &
				Object objP = m.get("param");
				tmpSet = new HashSet<Map<String, Object>>();
				if (objP != null) {
					String objPStr = objP.toString();
					if (!(objPStr.trim()).equals("")) {
						String[] paramArr = objPStr.split("&");
						for (int i = 0; i < paramArr.length; i++) {
							String[] paramArr_Arr = (paramArr[i]).split("#");
							if (paramArr_Arr.length != 2)
								break;
							title = paramArr_Arr[0];
							txt = paramArr_Arr[1];
							textArr = txt.split(",");

							mTmp = new HashMap<String, Object>();

							mTmp.put("title", title);
							mTmp.put("list", Arrays.asList(textArr));
							// plm.clear();
							tmpSet.add(mTmp);
						}
					}
				}
				responseMap.put("listB", tmpSet);
				responseMap.put("id", paramMap.get("id").toString());
				responseMap.put("gtId", m.get("gtId").toString());
				responseMap.put("stock", m.get("stock").toString());
				responseMap.put("storeId", m.get("storeId").toString());
				responseMap.put("goodsType", m.get("goodsType").toString());
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				return super.saveAndGet(responseMap, cacheKey, cacheTime);

			}

		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			try {
				m = super.npjt.queryForMap(sql, paramMap);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			if (m != null) {

				Map<String, Object> responseMap = new HashMap<String, Object>();
				String title = null;
				String txt = null;

				// 只能有一个 &
				String paramPriceImg = m.get("paramPriceImg").toString();
				// 除掉重复
				Set<Map<String, Object>> tmpSet = null;
				List<Map<String, Object>> plm = new ArrayList<Map<String, Object>>();

				String[] paramPIArr = paramPriceImg.split("&");
				title = paramPIArr[0];
				txt = paramPIArr[1];
				textArr = txt.split("#");
				String httpRoot = "http://120.26.112.213:8083/file/";

				for (int i = 0; i < textArr.length; i++) {
					String[] textpArr = textArr[i].split(",");
					Map<String, Object> rm = new HashMap<String, Object>();
					rm.put("name", textpArr[0]);
					rm.put("nowPrice", textpArr[1]);
					rm.put("originalPrice", textpArr[2]);
					rm.put("img", httpRoot + textpArr[3]);
					if (textpArr.length == 5) {
						rm.put("mUrl", httpRoot + textpArr[4]);
					} else {
						rm.put("mUrl", "");
					}
					plm.add(rm);
				}
				mTmp = new HashMap<String, Object>();
				mTmp.put("title", title);
				mTmp.put("list", plm);
				tmpSet = new HashSet<Map<String, Object>>();
				tmpSet.add(mTmp);
				List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>(
						tmpSet);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> tmpNIList = (List<Map<String, Object>>) tmpList
						.get(0).get("list");
				// 默认图和价格
				responseMap.put("nowPrice", tmpNIList.get(0).get("nowPrice"));
				responseMap.put("img", tmpNIList.get(0).get("img"));

				responseMap.put("listA", tmpList);

				// 可能有多个 &
				Object objP = m.get("param");
				tmpSet = new HashSet<Map<String, Object>>();
				if (objP != null) {
					String objPStr = objP.toString();
					if (!(objPStr.trim()).equals("")) {
						String[] paramArr = objPStr.split("&");
						for (int i = 0; i < paramArr.length; i++) {
							String[] paramArr_Arr = (paramArr[i]).split("#");
							if (paramArr_Arr.length != 2)
								break;
							title = paramArr_Arr[0];
							txt = paramArr_Arr[1];
							textArr = txt.split(",");

							mTmp = new HashMap<String, Object>();

							mTmp.put("title", title);
							mTmp.put("list", Arrays.asList(textArr));
							// plm.clear();
							tmpSet.add(mTmp);
						}
					}
				}
				responseMap.put("listB", tmpSet);
				responseMap.put("id", paramMap.get("id").toString());
				responseMap.put("gtId", m.get("gtId").toString());
				responseMap.put("stock", m.get("stock").toString());
				responseMap.put("storeId", m.get("storeId").toString());
				responseMap.put("goodsType", m.get("goodsType").toString());
				return super.toJson(responseMap);
			}
		}
		return "";
	}

	@Override
	public Map<String, Object> getGoodsTypeIdAndParam(
			Map<String, Object> paramMap) throws Exception {

		String select = "SELECT dgp.id, dgp.paramPriceImg,dgp.param,dgi.goodsType";
		String from = " FROM diy_goods_param dgp INNER JOIN diy_goods_info dgi ON dgp.gId =dgi.id";
		// String where
		// =" WHERE id=(SELECT gtId FROM diy_goods_info WHERE id=:id)";
		String where = " WHERE dgp.gId=:id";
		String sql = select + from + where;
		Map<String, Object> m = null;
		try {
			m = super.npjt.queryForMap(sql, paramMap);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		int storeId = 0;
		try {
			sql = "SELECT storeId FROM diy_goods_info WHERE id=:id";
			storeId = super.npjt.queryForObject(sql, paramMap, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		if (m != null) {
			// sql ="SELECT storeId FROM diy_goods_info WHERE id=:id";
			// int storeId =super.npjt.queryForObject(sql, paramMap,
			// Integer.class);

			Map<String, Object> responseMap = new HashMap<String, Object>();
			String[] goodsBuyParamArr = (m.get("paramPriceImg").toString())
					.split("&");
			String nowPrice = goodsBuyParamArr[1].split("#")[0].split(",")[1];
			String buyGoodsParam = "";
			if (goodsBuyParamArr.length != 2)
				return null;

			String param1 = goodsBuyParamArr[1];
			String[] param1_arr = param1.split(",");
			buyGoodsParam = param1_arr[0] + ",";
			/*
			 * for(int i=0;i<goodsBuyParamArr.length;i++){ String[]
			 * goodsBuyParamArr_1Arr =(goodsBuyParamArr[i]).split(",");
			 * if(goodsBuyParamArr_1Arr.length!=2)break;
			 * 
			 * String txt =goodsBuyParamArr_1Arr[0]; String[] textArr
			 * =txt.split(","); buyGoodsParam+=textArr[0]+",";
			 * 
			 * }
			 */
			Object objp = m.get("param");
			if (objp != null) {
				String paramStr = objp.toString();
				if (!paramStr.equals("")) {
					String[] paramArr = paramStr.split("&");
					for (int i = 0; i < paramArr.length; i++) {
						String[] param_Arr1 = paramArr[i].split("#")[1]
								.split(",");
						buyGoodsParam += param_Arr1[0] + ",";
					}

				}
			}

			responseMap.put("gtId", m.get("id").toString());
			// 最后一个字符串为, 就删除
			int sl = buyGoodsParam.length();
			String tm = buyGoodsParam.substring(sl - 1, sl);
			if (tm.equals(",")) {
				buyGoodsParam = buyGoodsParam.substring(0, sl - 1);
			}
			responseMap.put("nowPrice", nowPrice);
			responseMap.put("param", buyGoodsParam);
			responseMap.put("storeId", storeId);
			return responseMap;

		}
		return null;

	}

	@Override
	public int insertShopCart(Map<String, Object> paramMap) throws Exception {
		String insert = "INSERT INTO diy_shopcart (goods_id,num,param,img_url,render_url,user,creat_time)";
		String value = " VALUES(:gid,:num,:param,:imgUrl,:previewUrl,:uId,now())";
		String sql = insert + value;
		return super.npjt.update(sql, paramMap);
	}

	@Override
	public int insertOrderGoods(Map<String, Object> paramMap) throws Exception {
		String insert = "INSERT INTO crt_download (music_id,sys,app_id,version,ctime)";
		String value = " VALUES(:musicId,:sys,:appId,:version,now())";
		String sql = insert + value;
		return super.npjt.update(sql, paramMap);
	}

	/**
	 * @author zhanglz
	 * @return 首页(旧)
	 */
	@Override
	public String home(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "select id,gtid,storeId,name,nowPrice,icoUrl,feightNote from diy_goods_info where isBoutique=2 order by sort limit :page,:pagesize";

		if (super.isCacheNull(cacheKey).equals("a")) {
			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);

			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				String json = super.saveAndGet(lm, cacheKey, cacheTime);
				if (json != null) {
					return json;
				}
				return super.toJson(lm);

			}

		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);
			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
				}
				return super.toJson(lm);
			}
		}
		return "";

	}

	/**
	 * @author zhanglz
	 * @return 精品汇
	 */
	@Override
	public String nice(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "SELECT dgi.id,dgi.name,dgi.icoUrl,tex.now_price,ROUND(dgi.transportfee,2) as transportfee,sell"
				+ " FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id=tex.info_id"
				+ " WHERE dgi.isBoutique=1 AND tex.isdefault=1 and dgi.extra=1 ORDER BY dgi.sort LIMIT :page,:pagesize";
		if (super.isCacheNull(cacheKey).equals("a")) {

			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);
			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
					// 格式化价格
					m.put("now_price", df.format(m.get("now_price")));

					String transportfee = (null == m.get("transportfee") ? ""
							: m.get("transportfee").toString());
					if (transportfee.equals("0.0")) {
						m.put("transportfee", "免运费");
					} else {
						m.put("transportfee", "运费  ￥" + transportfee);
					}
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				String json = super.saveAndGet(lm, cacheKey, cacheTime);
				if (json != null) {
					return json;
				}
				return super.toJson(lm);

			}

		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);
			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("icoUrl");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
					String transportfee = (null == m.get("transportfee") ? ""
							: m.get("transportfee").toString());
					if (transportfee.equals("0.0")) {
						m.put("transportfee", "免运费");
					} else {
						m.put("transportfee", "运费  ￥" + transportfee);
					}
				}
				return super.toJson(lm);
			}
		}
		return "";

	}

	@Override
	public String getGoodsChartParamById(Map<String, Object> paramMap)
			throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();

		String select = "SELECT dgi.id" + ",dgi.topEdgeInset"
				+ ",dgi.liftEdgeInset" + ",dgi.mindEdgeInset"
				+ ",dgi.widthScale" + ",dgi.hightScale" + ",dgi.saveSize"
				+ ",dgi.topEdge" + ",dgi.liftEdge" + ",dgi.downEdge"
				+ ",dgi.rightEdge" + ",dgi.bgURLStr" + ",dgi.fgURLStr"
				+ ",dgi.goodsType" + ",dgi.isBoutique" + ",dgi.good_id"
				// 获取的对应类型
				+ ",dmt.tmp"
				// 默认图
				+ ",dgi.imageUrl" + ",dgi.previewImgUrl";
		String from = " FROM diy_goods_info dgi";
		String left = " LEFT JOIN diy_material_title dmt ON dgi.good_id=dmt.t_id";
		String where = " WHERE dgi.id=:id";
		String sql = select + from + left + where;
		Map<String, Object> m = null;

		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {

			Map<String, Object> rm = new HashMap<String, Object>();
			try {
				m = this.npjt.queryForMap(sql, paramMap);
				String goodsType = m.get("goodsType").toString();
				String isBoutique = m.get("isBoutique").toString();
				List<Map<String, Object>> lmTmp = new ArrayList<Map<String, Object>>();
				Map<String, Object> mTmp = null;
				String http = "http://120.26.112.213:8083/file/";
				String http2 = "http://file.diy.51app.cn/";

				Object imgUrlObj = m.get("imageURL");
				String imageURL[] = {};
				if (imgUrlObj != null) {
					imageURL = imgUrlObj.toString().split(",");
				}
				// 表示有正反两面
				if (goodsType.equals("0")) {

					mTmp = new HashMap<String, Object>();
					// 2普通商品
					if (isBoutique.equals("2")) {
						String[] topEdgeArr = m.get("topEdge").toString()
								.split(",");
						String[] liftEdgeArr = m.get("liftEdge").toString()
								.split(",");
						String[] downEdgeArr = m.get("downEdge").toString()
								.split(",");
						String[] rightEdgeArr = m.get("rightEdge").toString()
								.split(",");
						String[] bgURLStrArr = m.get("bgURLStr").toString()
								.split(",");
						String[] fgURLStrArr = m.get("fgURLStr").toString()
								.split(",");

						mTmp.put("topEdgeInset", topEdgeArr[0]);
						mTmp.put("liftEdgeInset", liftEdgeArr[0]);
						mTmp.put("downEdgeInset", downEdgeArr[0]);
						mTmp.put("rightEdgeInset", rightEdgeArr[0]);
						mTmp.put("bgURLStrInset", http + bgURLStrArr[0]);
						mTmp.put("fgURLStrInset", http + fgURLStrArr[0]);
						mTmp.put("imageURL", http2 + imageURL[0]);
						mTmp.put("previewImgUrl",
								http2 + m.get("previewImgUrl"));
						lmTmp.add(mTmp);
						mTmp = new HashMap<String, Object>();
						if (topEdgeArr.length > 1) {
							mTmp.put("topEdgeInset", topEdgeArr[1]);
						}
						if (liftEdgeArr.length > 1) {
							mTmp.put("liftEdgeInset", liftEdgeArr[1]);
						}
						if (downEdgeArr.length > 1) {
							mTmp.put("downEdgeInset", downEdgeArr[1]);
						}
						if (rightEdgeArr.length > 1) {
							mTmp.put("rightEdgeInset", rightEdgeArr[1]);
						}
						if (bgURLStrArr.length > 1) {
							mTmp.put("bgURLStrInset", http + bgURLStrArr[1]);
						}
						if (fgURLStrArr.length > 1) {
							mTmp.put("fgURLStrInset", http + fgURLStrArr[1]);
						}
						if (imageURL.length > 1) {
							mTmp.put("imageURL", http2 + imageURL[1]);
						}
						mTmp.put("previewImgUrl",
								http2 + m.get("previewImgUrl"));
						lmTmp.add(mTmp);
					} else {

						String fgURLStr = m.get("fgURLStr").toString();
						mTmp.put("topEdgeInset", "");
						mTmp.put("liftEdgeInset", "");
						mTmp.put("downEdgeInset", "");
						mTmp.put("rightEdgeInset", "");
						mTmp.put("bgURLStrInset", "");
						mTmp.put("fgURLStrInset", http + fgURLStr);
						mTmp.put("imageURL", "");
						mTmp.put("previewImgUrl",
								http2 + m.get("previewImgUrl"));
					}
				} else {

					String fgURLStr = (String) m.get("fgURLStr");
					mTmp = new HashMap<String, Object>();
					if (isBoutique.equals("2")) {
						String topEdge = (String) m.get("topEdge");
						String liftEdge = (String) m.get("liftEdge");
						String downEdge = (String) m.get("downEdge");
						String rightEdge = (String) m.get("rightEdge");
						String bgURLStr = (String) m.get("bgURLStr");

						mTmp.put("topEdgeInset", topEdge == null ? "" : topEdge);
						mTmp.put("liftEdgeInset", liftEdge == null ? ""
								: liftEdge);
						mTmp.put("downEdgeInset", downEdge == null ? ""
								: downEdge);
						mTmp.put("rightEdgeInset", rightEdge == null ? ""
								: rightEdge);
						mTmp.put("bgURLStrInset", bgURLStr == null ? "" : http
								+ bgURLStr);
						mTmp.put("fgURLStrInset", fgURLStr == null ? "" : http
								+ fgURLStr);
						mTmp.put("imageURL", http2 + imageURL[0]);
						mTmp.put("previewImgUrl",
								http2 + m.get("previewImgUrl"));
						lmTmp.add(mTmp);
					} else {
						mTmp.put("topEdgeInset", "");
						mTmp.put("liftEdgeInset", "");
						mTmp.put("downEdgeInset", "");
						mTmp.put("rightEdgeInset", "");
						mTmp.put("bgURLStrInset", "");
						mTmp.put("fgURLStrInset", http + fgURLStr);
						mTmp.put("imageURL", "");
						mTmp.put("previewImgUrl",
								http2 + m.get("previewImgUrl"));
						lmTmp.add(mTmp);
					}

				}

				if (isBoutique.equals("2")) {
					rm.put("topEdgeInset", m.get("topEdgeInset").toString());
					rm.put("liftEdgeInset", m.get("liftEdgeInset").toString());
					rm.put("mindEdgeInset", m.get("mindEdgeInset").toString());
					rm.put("widthScale", m.get("widthScale").toString());
					rm.put("hightScale", m.get("hightScale").toString());// saveSize
					rm.put("saveSize", m.get("saveSize").toString());
				}
				String id = paramMap.get("id").toString();
				rm.put("goodsURLStr", "goods/title/" + id + ".do");
				rm.put("detailURLStr", "goods/details/" + isBoutique + "/" + id
						+ ".do");
				// rm.put("appraiseURLStr", "goods/comment/"+id+".do");
				rm.put("appraiseURLStr", "evaluation/toevaluation.do?id=" + id);
				rm.put("showURLStr", "goods/show/" + id + ".do");
				String tmp = m.get("tmp") != null ? m.get("tmp").toString()
						: "";

				rm.put("arr", lmTmp);
				rm.put("tmp", tmp);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			int cacheTime = Integer.parseInt(paramMap.get("cacheTime")
					.toString());
			if (super.isCacheNull(cacheKey).equals("a")) {
				return saveAndGet(rm, cacheKey, cacheTime);
			} else {
				return super.toJson(rm);
			}
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";

	}

	@Override
	public String getJPImgUrl(String name) throws Exception {

		String sql = "SELECT url FROM diy_boutique_imgurl WHERE name='" + name
				+ "'";
		return super.jt.queryForObject(sql, String.class);
	}

	@Override
	public List<Map<String, Object>> getCfImgUrlList(
			Map<String, Object> paramMap) throws Exception {
		// String cacheKey =paramMap.get("cacheKey").toString();
		// if(super.isCacheNull(cacheKey)){
		String htg = paramMap.get("htg").toString();
		paramMap.remove("htg");
		/*
		 * String sql=
		 * "SELECT dgp.bgURL,dgp.fgURL,dgp.edgeInset,dgp.imageTag,dgp.goods_type AS t"
		 * + " FROM diy_goods_info dgi INNER JOIN diy_goods_preview dgp" +
		 * " ON dgi.id=dgp.goods_id" +
		 * " WHERE dgp.goods_type=(SELECT isBoutique FROM diy_goods_info WHERE id=:id)"
		 * + " AND dgi.id=:id";
		 */
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dgp.bgURL,dgp.fgURL,dgp.edgeInset,dgp.imageTag,dgp.goods_type AS t");
		sql.append(" FROM diy_goods_info dgi INNER JOIN diy_goods_preview dgp");
		sql.append(" ON dgi.id=dgp.goods_id");
		sql.append(" WHERE dgp.goods_type=(SELECT isBoutique FROM diy_goods_info WHERE id=:id)");
		sql.append(" AND dgp.goods_id=:id");
		Object objp = paramMap.get("param");
		if (objp != null) {
			sql.append(" AND dgp.param=:param");
		} else {
			sql.append(" AND dgp.default=1");
		}

		List<Map<String, Object>> lm = super.npjt.queryForList(sql.toString(),
				paramMap);
		if (!lm.isEmpty()) {
			for (Map<String, Object> m : lm) {
				String t = m.get("t").toString();
				if (t.equals("1")) {
					String imgUrl = m.get("fgURL").toString();
					m.put("fgURL", htg + imgUrl);
				} else if (t.equals("2")) {
					String imgUrl = m.get("bgURL").toString();
					m.put("bgURL", htg + imgUrl);
					imgUrl = m.get("fgURL").toString();
					m.put("fgURL", htg + imgUrl);
				}
			}
			// int cacheTime
			// =Integer.parseInt(paramMap.get("cacheTime").toString());
			// saveCache(lm,cacheKey,cacheTime);
			// }

		}
		return lm;

	}

	@Override
	public void getGoodsShowImgList(Map<String, Object> paramMap)
			throws Exception {
		String http = "http://120.26.112.213:8083/file/";
		String cacheKey = paramMap.get("cacheKey").toString();
		if (super.isCacheNull(cacheKey).equals("a")) {
			String sql = "SELECT showImg FROM diy_goods_type WHERE id=:id";
			Map<String, Object> m = null;
			try {
				m = super.npjt.queryForMap(sql, paramMap);
			} catch (EmptyResultDataAccessException e) {
				return;
			}
			if (m != null) {
				List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
				Map<String, Object> responseMap = new HashMap<String, Object>();
				String[] goodsBuyParamArr = (m.get("showImg").toString())
						.split("&");
				for (int i = 0; i < goodsBuyParamArr.length; i++) {
					String[] goodsBuyParamArr_1Arr = (goodsBuyParamArr[i])
							.split("#");
					if (goodsBuyParamArr_1Arr.length != 2)
						break;
					String title = goodsBuyParamArr_1Arr[0];
					String txt = goodsBuyParamArr_1Arr[1];
					String[] textArr = txt.split(",");
					for (int j = 0; j < textArr.length; j++) {
						textArr[j] = http + textArr[j];
					}
					Map<String, Object> mTmp = new HashMap<String, Object>();
					mTmp.put("title", title);
					mTmp.put("list", Arrays.asList(textArr));
					tmpList.add(mTmp);
				}
				responseMap.put("list", tmpList);
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				super.saveAndGet(responseMap, cacheKey, cacheTime);
			}

		}
	}

	@Override
	public String getADImgList(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "SELECT img,url FROM diy_goods_ad WHERE state=1 AND version=:version1_0 ORDER BY sort ASC";
		if (super.isCacheNull(cacheKey).equals("a")) {

			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);

			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("img");
					Object obj2 = m.get("deImg");
					if (obj != null) {
						m.put("img", dgurl + obj);
					}
					if (obj2 != null) {
						m.put("deImg", dgurl + obj2);
					}
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				String json = super.saveAndGet(lm, cacheKey, cacheTime);
				if (json != null) {
					return json;
				}
				return super.toJson(lm);

			}
		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		} else {
			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);

			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("img");
					Object obj2 = m.get("deImg");
					if (obj != null) {
						m.put("img", dgurl + obj);
					}
					if (obj2 != null) {
						m.put("deImg", dgurl + obj2);
					}
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				return super.toJson(lm);

			}
		}
		return "";
	}

	@Override
	public String newADImgList(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "SELECT img,url,type,goodId FROM diy_goods_ad WHERE state=1 AND version=:version2_0 ORDER BY sort ASC";
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {

			List<Map<String, Object>> lm = super.npjt.queryForList(sql,
					paramMap);

			if (!lm.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : lm) {
					Object obj = m.get("img");
					Object obj2 = m.get("deImg");
					if (obj != null) {
						m.put("img", dgurl + obj);
					}
					if (obj2 != null) {
						m.put("deImg", dgurl + obj2);
					}
					if (m.get("goodId") == null) {
						m.put("goodId", "");
					}
				}
				int cacheTime = new Integer(paramMap.get("cacheTime")
						.toString());
				String json = super.saveAndGet(lm, cacheKey, cacheTime);
				if (json != null) {
					return json;
				}
				return super.toJson(lm);
			}
		} else if (isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	/**
	 * 返回材质属性
	 */
	@Override
	public String getSelectTexture(Map<String, Object> paramMap)
			throws Exception {
		String sql = "SELECT name FROM diy_goods_texture WHERE id=:id";
		return super.npjt.queryForObject(sql, paramMap, String.class);
	}

	/**
	 * @author zhanglz 选择材质页面
	 */
	@Override
	public String getGoodsShow(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl = paramMap.get("dgurl").toString();
			int id = new Integer(paramMap.get("id").toString());
			String infosql = "SELECT texture_ids,org_price,now_price,pre_url,box_url,save_size FROM `diy_info_texture` WHERE info_id = ? AND status=1 ";
			String infoend = " ORDER BY sort";
			// 查询商品的所有可能的属性
			List<Map<String, Object>> info = super.jt.queryForList(infosql
					+ infoend, new Object[] { id });
			String first = "";
			String second = "";
			int index = 0;
			for (Map<String, Object> map : info) {
				String pre_url = null == map.get("pre_url") ? "" : map.get(
						"pre_url").toString();
				String box_url = null == map.get("box_url") ? "" : map.get(
						"box_url").toString();
				String saveSize = null == map.get("save_size") ? "" : map.get(
						"save_size").toString();
				// 价格格式化
				map.put("org_price", df.format(map.get("org_price")));
				map.put("now_price", df.format(map.get("now_price")));
				map.put("save_size", saveSize);
				if (StringUtils.isNotEmpty(pre_url))
					map.put("pre_url", dgurl + pre_url);

				if (StringUtils.isNotEmpty(box_url))
					map.put("box_url", dgurl + box_url);
				// 获取材质
				String texture_ids = null == map.get("texture_ids") ? "" : map
						.get("texture_ids").toString();
				String[] f = texture_ids.split("_");
				index = f.length;
				// 根据属性长度决定层级
				if (index == 1) {
					first += f[0] + ",";
				} else if (index == 2) {
					first += f[0] + ",";
					second += f[1] + ",";
				}
			}
			String gsql = "SELECT gt.id,gt.name AS gname,g.`name` AS title FROM `diy_goods_texture` gt INNER JOIN `diy_good` g ON gt.`texture_type`=g.`id` ";
			String end = " ORDER BY gt.`sort` ";
			Map<String, Object> tempMap = null;
			// 查出一级属性列表
			if (index == 1) {
				String firstsql = gsql + " AND gt.id in ("
						+ first.substring(0, first.length() - 1) + ")";
				List<Map<String, Object>> fristList = jt.queryForList(firstsql
						+ end);
				// 查出属性类型（款式\型号...）
				String title = fristList.get(0).get("title").toString();
				tempMap = new HashMap<String, Object>();
				tempMap.put("title", title);
				tempMap.put("list", fristList);
				result.put("listA", tempMap);
			} else if (index == 2) {
				String firstsql = gsql + " AND gt.id in ("
						+ first.substring(0, first.length() - 1) + ")";
				List<Map<String, Object>> fristList = jt.queryForList(firstsql
						+ end);
				String title = fristList.get(0).get("title").toString();
				tempMap = new HashMap<String, Object>();
				tempMap.put("title", title);
				tempMap.put("list", fristList);
				result.put("listA", tempMap);

				String secondsql = gsql + " AND gt.id in ("
						+ second.substring(0, second.length() - 1) + ")";
				List<Map<String, Object>> secondList = jt
						.queryForList(secondsql + end);
				String titleB = secondList.get(0).get("title").toString();
				tempMap = new HashMap<String, Object>();
				tempMap.put("title", titleB);
				tempMap.put("list", secondList);
				result.put("listB", tempMap);

			}
			result.put("texture", info);

			infosql += " and isdefault=1";
			Map<String, Object> defaul = super.jt.queryForList(
					infosql + infoend, new Object[] { id }).get(0);// 默认显示
			String storesql = "select user_id,goodsType from diy_goods_info where id="
					+ id;
			Map<String, Object> store = super.jt.queryForMap(storesql);
			result.put("now_price", df.format(defaul.get("now_price")));
			result.put("org_price", df.format(defaul.get("org_price")));
			result.put("pre_url", dgurl + defaul.get("pre_url"));
			result.put("save_size", null == defaul.get("save_size") ? ""
					: defaul.get("save_size").toString());
			result.put("id", id);
			result.put("storeId", null == store.get("user_id") ? "" : store
					.get("user_id").toString());
			result.put("goodsType", null == store.get("goodsType") ? "" : store
					.get("goodsType").toString());
			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else {
				return super.toJson(result);
			}
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	/**
	 * @author zhanglz 预览图
	 */
	@Override
	public String getPreviewImg(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl = paramMap.get("dgurl").toString();
			String sql = "SELECT t.bg_url,t.ft_url,t.edge_inset,g.goodsType "
					+ "FROM `diy_info_texture` t,`diy_goods_info` g "
					+ "WHERE t.info_id=g.`id` AND g.id=? ";
			String[] paramArr = paramMap.get("param").toString().split(",");
			int id = new Integer(paramArr[0]);

			Map<String, Object> len1 = new HashMap<String, Object>();
			if (paramArr.length == 1) { // 默认材质预览图
				sql += " AND t.isdefault=1";
				len1 = jt.queryForList(sql, new Object[] { id }).get(0);

			} else if (paramArr.length > 1) { // 选择材质后的预览图
				String texture_ids = "";
				if (paramArr.length == 2)
					texture_ids = paramArr[1];
				else if (paramArr.length == 3)
					texture_ids = paramArr[1] + "_" + paramArr[2];
				sql += " and t.texture_ids='" + texture_ids + "'";
				len1 = jt.queryForList(sql, new Object[] { id }).get(0);
			}
			String bg_url = null == len1.get("bg_url") ? "" : len1
					.get("bg_url").toString();
			String ft_url = null == len1.get("ft_url") ? "" : len1
					.get("ft_url").toString();
			String edge_inset = null == len1.get("edge_inset") ? "" : len1.get(
					"edge_inset").toString();
			String goodsType = null == len1.get("goodsType") ? "" : len1.get(
					"goodsType").toString();
			String[] edge_inset_arr = edge_inset.split("#");
			Map<String, Object> tempMap = null;
			if (StringUtils.isNotEmpty(ft_url)) {
				String[] ft_url_arr = ft_url.split("#");
				for (int i = 0; i < ft_url_arr.length; i++) {
					tempMap = new HashMap<String, Object>();
					tempMap.put("fgURL", dgurl + ft_url_arr[i]);
					if (edge_inset_arr.length >= i)
						tempMap.put("edgeInset", edge_inset_arr[i]);
					tempMap.put("goodsType", goodsType);
					tempMap.put("imageTag", i);
					result.add(tempMap);
				}
			}
			if (StringUtils.isNotEmpty(bg_url)) {
				String[] bg_url_arr = bg_url.split("#");
				for (int i = 0; i < bg_url_arr.length; i++) {
					tempMap = new HashMap<String, Object>();
					tempMap.put("bgURL", dgurl + bg_url_arr[i]);
					if (edge_inset_arr.length >= i)
						tempMap.put("edgeInset", edge_inset_arr[i]);
					tempMap.put("goodsType", goodsType);
					tempMap.put("imageTag", i);
					result.add(tempMap);
				}
			}
			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";

	}

	@Override
	public String getPreviewImg2(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		del(cacheKey);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String dgurl = paramMap.get("dgurl").toString();

			String[] paramArr = paramMap.get("param").toString().split(",");
		
			String sql = "SELECT t.bg_url,t.ft_url,t.edge_inset,n.goodsType "
					+ "FROM `diy_info_texture2` t,`new_goods_info` n "
					+ "WHERE t.make_id=n.`id` AND t.isdefault=1";
			
			if(paramArr.length>0){
				sql+= " AND n.id=?";
			}

			/*String texture_ids = "";
			for (int i = 1; i < paramArr.length; i++) {
				texture_ids += paramArr[i] + "_";
			}
			if(texture_ids.length()>0){
				texture_ids = texture_ids.substring(0, texture_ids.length() - 1);
				sql += " AND t.texture_ids='" + texture_ids + "'";
			}*/

			// 吧相同材质的商品放在一起
			sql += " ORDER BY t.texture_ids desc";
			
			List<Map<String, Object>> maps;
			
			if (paramArr.length>0) {
				int id= new Integer(paramArr[0]);
				maps = jt.queryForList(sql, new Object[]{id});
			}else{
				maps = jt.queryForList(sql);
			}
			

			if (maps != null && maps.size() > 0) {
				for (Map<String, Object> map : maps) {
					String bg_url = null == map.get("bg_url") ? "" : map.get(
							"bg_url").toString();
					String ft_url = null == map.get("ft_url") ? "" : map.get(
							"ft_url").toString();
					String edge_inset = null == map.get("edge_inset") ? ""
							: map.get("edge_inset").toString();
					String goodsType = null == map.get("goodsType") ? "" : map
							.get("goodsType").toString();
					String[] edge_inset_arr = edge_inset.split("#");
					Map<String, Object> tempMap = null;
					if (StringUtils.isNotEmpty(ft_url)) {
						String[] ft_url_arr = ft_url.split("#");
						for (int i = 0; i < ft_url_arr.length; i++) {
							tempMap = new HashMap<String, Object>();
							tempMap.put("fgURL", dgurl + ft_url_arr[i]);
							if (edge_inset_arr.length >= (i+1))
								tempMap.put("edgeInset", edge_inset_arr[i]);
							tempMap.put("goodsType", goodsType);
							tempMap.put("imageTag", i);
							result.add(tempMap);
						}
					}
					if (StringUtils.isNotEmpty(bg_url)) {
						String[] bg_url_arr = bg_url.split("#");
						for (int i = 0; i < bg_url_arr.length; i++) {
							tempMap = new HashMap<String, Object>();
							tempMap.put("bgURL", dgurl + bg_url_arr[i]);
							if (edge_inset_arr.length >= (i+1))
								tempMap.put("edgeInset", edge_inset_arr[i]);
							tempMap.put("goodsType", goodsType);
							tempMap.put("imageTag", i);
							result.add(tempMap);
						}
					}
				}
			}

			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";

	}

	@Override
	public String getADJumpImg(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String dgurl = paramMap.get("dgurl").toString();
		String sql = "SELECT img_pic FROM diy_goods_ad WHERE id=:id";
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String imgList = super.npjt.queryForObject(sql, paramMap,
					String.class);
			String result[] = imgList.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < result.length; i++) {
				result[i] = dgurl + result[i];
				list.add(result[i]);
			}
			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(list, cacheKey, cacheTime);
			else
				return super.toJson(list);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	@Override
	public String youHome(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "SELECT dgi.id,dgi.`good_id` AS goods_type,dgi.name,dgi.icoUrl,tex.now_price,ROUND(dgi.transportfee,2) AS transportfee,sell "
				+ "FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id=tex.info_id "
				+ "WHERE dgi.isBoutique=1 AND tex.isdefault=1 ORDER BY dgi.sort";
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			List<Map<String, Object>> list = jt.queryForList(sql);
			if (!list.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				for (Map<String, Object> m : list) {
					Object obj = m.get("icoUrl");
					Object obj2 = m.get("ad_img");
					if (obj != null) {
						m.put("icoUrl", dgurl + obj);
					}
					if (obj2 != null) {
						m.put("ad_img", dgurl + obj2);
					}
					// 格式化价格
					m.put("now_price", df.format(m.get("now_price")));

					String transportfee = (null == m.get("transportfee") ? ""
							: m.get("transportfee").toString());
					if (transportfee.equals("0.0")) {
						m.put("transportfee", "免运费");
					} else {
						m.put("transportfee", "运费  ￥" + transportfee);
					}
				}
			}

			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(list, cacheKey, cacheTime);
			else
				return super.toJson(list);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	@Override
	public String youGoods(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		String sql = "SELECT dgi.id,dgi.title,dgi.`previewImgUrl`,dgi.`sell`,dgi.transportfee,dgi.recommend,tex.org_price,tex.now_price,tex.texture_ids,dgi.isBoutique,dgi.h5url,tex.`pre_url` "
				+ "FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id =tex.info_id "
				+ "WHERE dgi.id=? AND tex.isdefault=1";
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			Map<String, Object> map = jt.queryForMap(sql,
					new Object[] { paramMap.get("id") });
			if (!map.isEmpty()) {
				String dgurl = paramMap.get("dgurl").toString();
				// 推荐商品，用逗号隔开
				String recommend = map.get("recommend") == null ? "" : map.get(
						"recommend").toString();
				Object obj = map.get("previewImgUrl");
				if (obj != null) {
					map.put("previewImgUrl", dgurl + obj);
				}
				Object pre_url = map.get("pre_url");
				if (pre_url != null) {
					map.put("pre_url", dgurl + pre_url);
				}
				Object h5url = map.get("h5url");
				if (h5url != null) {
					map.put("h5url", dgurl + h5url);
				}
				// 格式化价格
				map.put("now_price", df.format(map.get("now_price")));
				map.put("org_price", df.format(map.get("org_price")));
				String transportfee = (null == map.get("transportfee") ? ""
						: map.get("transportfee").toString());
				if (transportfee.equals("0.0")) {
					map.put("transportfee", "免运费");
				} else {
					map.put("transportfee", "运费  ￥" + transportfee);
				}
				// 查询材质
				List<String> lt = new ArrayList<>();
				String textureIds = map.get("texture_ids") != null ? map.get(
						"texture_ids").toString() : "未选择";
				String arrIds[] = textureIds.split("_");
				for (int i = 0; i < arrIds.length; i++) {
					Map<String, Object> tmap = new HashMap<String, Object>();
					tmap.put("id", arrIds[i]);
					String param = getSelectTexture(tmap);
					if (i == arrIds.length - 1) {
						lt.add(param);
					} else {
						lt.add(param + ",");
					}
				}
				map.put("paramList", lt);
				map.put("recommend", getGoods(recommend, dgurl));
			}
			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(map, cacheKey, cacheTime);
			else
				return super.toJson(map);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";

	}

	// 推荐商品详情查询以及属性
	private List<Map<String, Object>> getGoods(String recommend, String dgurl) {
		String arr[] = recommend.split(",");
		String sql = "SELECT dgi.id,dgi.title,dgi.`previewImgUrl`,dgi.transportfee,dgi.recommend,tex.org_price,tex.now_price,tex.texture_ids ,dgi.good_id,dgi.isBoutique "
				+ "FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id =tex.info_id  "
				+ "WHERE dgi.id=? AND tex.isdefault=1";
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		if (arr[0].equals("")) {
			return null;
		}
		for (int i = 0; i < arr.length; i++) {
			if (jt.queryForList(sql, arr[i]).isEmpty()) {
				continue;
			}
			Map<String, Object> map = super.jt.queryForList(sql, arr[i]).get(0);
			// 改变图片前缀
			Object obj = map.get("previewImgUrl");
			if (obj != null) {
				map.put("previewImgUrl", dgurl + obj);
			}
			// 改变价格
			map.put("now_price", df.format(map.get("now_price")));
			String transportfee = (null == map.get("transportfee") ? "" : map
					.get("transportfee").toString());
			if (transportfee.equals("0.0")) {
				map.put("transportfee", "免运费");
			} else {
				map.put("transportfee", "运费  ￥" + transportfee);
			}
			reList.add(map);
		}
		return reList;
	}

	@Override
	public String youHome2(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		// 读取不是导航条的数据
		String sql = "SELECT * FROM diy_home_nav WHERE type!=1 AND status=1 ORDER by sort";
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			List<Map<String, Object>> list = super.jt.queryForList(sql);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				String dgurl = paramMap.get("dgurl").toString();
				String type = map.get("type") == null ? "" : map.get("type")
						.toString();
				// 跳转id(关联商品)
				String pid = map.get("pid") == null ? "" : map.get("pid")
						.toString();
				// 精品与团体定制商品
				if (type.equals("2") || type.equals("4")) {
					String gsql = "SELECT dgi.id,dgi.`good_id` AS goods_type,dgi.name,dgi.icoUrl,tex.now_price,ROUND(dgi.transportfee,2) AS transportfee,sell "
							+ "FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id=tex.info_id "
							+ "WHERE tex.isdefault=1 AND dgi.id=? ORDER BY dgi.sort";
					List<Map<String, Object>> glist = jt
							.queryForList(gsql, pid);
					if (!glist.isEmpty()) {
						// 加上前缀，只跳转一个商品
						Map<String, Object> m = glist.get(0);
						m.put("type", Integer.parseInt(type));
						m.put("icoUrl", dgurl + m.get("icoUrl"));
						m.put("now_price", df.format(m.get("now_price")));
						result.add(m);
						m.put("cimg", dgurl + map.get("cimg"));
					}
					// cimg图标或广告图片
				} else {
					result.add(map);
					map.put("cimg", dgurl + map.get("cimg"));
				}
			}

			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);

		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	@Override
	public String homeV2(Map<String, Object> paramMap) throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			int cacheTime = new Integer(paramMap.get("cacheTime").toString());
			String sql = "SELECT * FROM diy_home_nav ORDER by sort";
			List<Map<String, Object>> list = super.jt.queryForList(sql);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				String dgurl = paramMap.get("dgurl").toString();
				String type = map.get("type") == null ? "" : map.get("type")
						.toString();
				String pid = map.get("pid") == null ? "" : map.get("pid")
						.toString();
				if ("8".equals(type)) {
					/** 商品 **/
					String gsql = "SELECT dgi.id as goodsId,dgi.`good_id` AS goods_type,dgi.name as goodsName,dgi.icoUrl,tex.now_price,ROUND(dgi.transportfee,2) AS transportfee,sell,dgi.isBoutique "
							+ "FROM diy_goods_info dgi INNER JOIN diy_info_texture tex ON dgi.id=tex.info_id "
							+ "WHERE tex.isdefault=1 AND dgi.id=? ORDER BY dgi.sort";
					List<Map<String, Object>> glist = jt
							.queryForList(gsql, pid);
					if (!glist.isEmpty()) {
						Map<String, Object> m = glist.get(0);
						m.put("name", map.get("name"));
						m.put("type", Integer.parseInt(type));
						m.put("icoUrl", dgurl + m.get("icoUrl"));
						m.put("cimg", dgurl + map.get("cimg"));
						m.put("color", map.get("color"));
						m.put("now_price", df.format(m.get("now_price")));
						String transportfee = (null == m.get("transportfee") ? ""
								: m.get("transportfee").toString());
						if (transportfee.equals("0.0")) {
							m.put("transportfee", "免运费");
						} else {
							m.put("transportfee", "运费  ￥" + transportfee);
						}
						result.add(m);
					}

				} else if ("1".equals(type)) {
					/** 导航条 **/
					result.add(map);
				} else if ("3".equals(type)) {
					/** banner **/
					Integer isgoods = (Integer) map.get("isgoods");
					if (isgoods == 1) { // 跳转商品
						String goodSql = "SELECT id,good_id,isBoutique FROM `diy_goods_info` WHERE id="
								+ map.get("pid");
						Map<String, Object> goodMap = jt.queryForMap(goodSql);
						map.put("goodsId", goodMap.get("id"));
						map.put("goods_type", goodMap.get("good_id"));
						map.put("isBoutique", goodMap.get("isBoutique"));
					}
					map.put("cimg", dgurl + map.get("cimg"));
					result.add(map);
				} else if ("7".equals(type)) {
					/** 专题 **/
					String nsql = "SELECT `id`,`name`,`img_url`,`text`,`pid`,`type` as sp_type FROM `diy_special` WHERE nav_id=? AND `status`=1";
					List<Map<String, Object>> glist = jt.queryForList(nsql,
							type);
					if (!glist.isEmpty()) {
						for (Map<String, Object> map2 : glist) {
							Integer spType = (Integer) map2.get("sp_type");
							if (spType == 1) { // 跳转商品
								String goodSql = "SELECT id,good_id,isBoutique FROM `diy_goods_info` WHERE id="
										+ map2.get("pid");
								Map<String, Object> goodMap = jt
										.queryForMap(goodSql);
								map2.put("goodsId", goodMap.get("id"));
								map2.put("goods_type", goodMap.get("good_id"));
								map2.put("isBoutique",
										goodMap.get("isBoutique"));
							}
							map2.put("img_url", dgurl + map2.get("img_url"));
							map2.put("type", 7);
							result.add(map2);
						}
					}
				}
			}
			if (super.isCacheNull(cacheKey).equals("a"))
				return super.saveAndGet(result, cacheKey, cacheTime);
			else
				return super.toJson(result);
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";
	}

	@Override
	public String getGoodsChartParamByIdV2(Map<String, Object> paramMap)
			throws Exception {
		String cacheKey = paramMap.get("cacheKey").toString();

		String select = "SELECT dgi.id" + ",dgi.topEdgeInset"
				+ ",dgi.liftEdgeInset" + ",dgi.mindEdgeInset"
				+ ",dgi.widthScale" + ",dgi.hightScale" + ",dgi.saveSize"
				+ ",dgi.topEdge" + ",dgi.liftEdge" + ",dgi.downEdge"
				+ ",dgi.rightEdge" + ",dgi.bgURLStr" + ",dgi.fgURLStr"
				+ ",dgi.goodsType" + ",dgi.isBoutique" + ",dgi.good_id"
				// 获取的对应类型
				+ ",dmt.tmp"
				// 默认图
				+ ",dgi.imageUrl";
		String from = " FROM diy_goods_info dgi";
		String left = " LEFT JOIN diy_material_title dmt ON dgi.good_id=dmt.t_id";
		String where = " WHERE dgi.id=:id";
		String sql = select + from + left + where;
		Map<String, Object> m = null;
		if (super.isCacheNull(cacheKey).equals("a")
				|| super.isCacheNull(cacheKey).equals("c")) {
			Map<String, Object> rm = new HashMap<String, Object>();
			try {
				m = this.npjt.queryForMap(sql, paramMap);
				String goodsType = m.get("goodsType").toString();
				String isBoutique = m.get("isBoutique").toString();
				List<Map<String, Object>> lmTmp = new ArrayList<Map<String, Object>>();
				Map<String, Object> mTmp = null;
				String http = "http://120.26.112.213:8083/file/";
				String http2 = "http://file.diy.51app.cn/";
				Object imgUrlObj = m.get("imageURL");
				String imageURL[] = {};
				if (imgUrlObj != null) {
					imageURL = imgUrlObj.toString().split(",");
				}
				// 表示有正反两面
				if (goodsType.equals("0")) {
					mTmp = new HashMap<String, Object>();
					if (isBoutique.equals("2")) {
						String[] topEdgeArr = m.get("topEdge").toString()
								.split(",");
						String[] liftEdgeArr = m.get("liftEdge").toString()
								.split(",");
						String[] downEdgeArr = m.get("downEdge").toString()
								.split(",");
						String[] rightEdgeArr = m.get("rightEdge").toString()
								.split(",");
						String[] bgURLStrArr = m.get("bgURLStr").toString()
								.split(",");
						String[] fgURLStrArr = m.get("fgURLStr").toString()
								.split(",");
						mTmp.put("topEdgeInset", topEdgeArr[0]);
						mTmp.put("liftEdgeInset", liftEdgeArr[0]);
						mTmp.put("downEdgeInset", downEdgeArr[0]);
						mTmp.put("rightEdgeInset", rightEdgeArr[0]);
						mTmp.put("bgURLStrInset", http + bgURLStrArr[0]);
						mTmp.put("fgURLStrInset", http + fgURLStrArr[0]);
						mTmp.put("imageURL", http2 + imageURL[0]);
						lmTmp.add(mTmp);
						mTmp = new HashMap<String, Object>();
						if (topEdgeArr.length > 1) {
							mTmp.put("topEdgeInset", topEdgeArr[1]);
						}
						if (liftEdgeArr.length > 1) {
							mTmp.put("liftEdgeInset", liftEdgeArr[1]);
						}
						if (downEdgeArr.length > 1) {
							mTmp.put("downEdgeInset", downEdgeArr[1]);
						}
						if (rightEdgeArr.length > 1) {
							mTmp.put("rightEdgeInset", rightEdgeArr[1]);
						}
						if (bgURLStrArr.length > 1) {
							mTmp.put("bgURLStrInset", http + bgURLStrArr[1]);
						}
						if (fgURLStrArr.length > 1) {
							mTmp.put("fgURLStrInset", http + fgURLStrArr[1]);
						}
						if (imageURL.length > 1) {
							mTmp.put("imageURL", http2 + imageURL[1]);
						}
						lmTmp.add(mTmp);
					} else {
						String fgURLStr = m.get("fgURLStr").toString();
						mTmp.put("topEdgeInset", "");
						mTmp.put("liftEdgeInset", "");
						mTmp.put("downEdgeInset", "");
						mTmp.put("rightEdgeInset", "");
						mTmp.put("bgURLStrInset", "");
						mTmp.put("fgURLStrInset", http + fgURLStr);
						mTmp.put("imageURL", "");
					}
				} else {
					String fgURLStr = m.get("fgURLStr").toString();
					mTmp = new HashMap<String, Object>();
					if (isBoutique.equals("2")) {
						String topEdge = m.get("topEdge").toString();
						String liftEdge = m.get("liftEdge").toString();
						String downEdge = m.get("downEdge").toString();
						String rightEdge = m.get("rightEdge").toString();
						String bgURLStr = m.get("bgURLStr").toString();
						mTmp.put("topEdgeInset", topEdge);
						mTmp.put("liftEdgeInset", liftEdge);
						mTmp.put("downEdgeInset", downEdge);
						mTmp.put("rightEdgeInset", rightEdge);
						mTmp.put("bgURLStrInset", http + bgURLStr);
						mTmp.put("fgURLStrInset", http + fgURLStr);
						mTmp.put("imageURL", http2 + imageURL[0]);
						lmTmp.add(mTmp);
					} else {
						mTmp.put("topEdgeInset", "");
						mTmp.put("liftEdgeInset", "");
						mTmp.put("downEdgeInset", "");
						mTmp.put("rightEdgeInset", "");
						mTmp.put("bgURLStrInset", "");
						mTmp.put("fgURLStrInset", http + fgURLStr);
						mTmp.put("imageURL", "");
						lmTmp.add(mTmp);
					}
				}
				if (isBoutique.equals("2")) {
					rm.put("topEdgeInset", m.get("topEdgeInset").toString());
					rm.put("liftEdgeInset", m.get("liftEdgeInset").toString());
					rm.put("mindEdgeInset", m.get("mindEdgeInset").toString());
					rm.put("widthScale", m.get("widthScale").toString());
					rm.put("hightScale", m.get("hightScale").toString());// saveSize
					rm.put("saveSize", m.get("saveSize").toString());
				}
				String id = paramMap.get("id").toString();
				rm.put("goodsURLStr", "goods/titleV2/" + id + ".do");
				rm.put("detailURLStr", "goods/details/" + isBoutique + "/" + id
						+ ".do");
				// rm.put("appraiseURLStr", "goods/comment/"+id+".do");
				rm.put("appraiseURLStr", "evaluation/toevaluation.do?id=" + id);
				rm.put("showURLStr", "goods/show/" + id + ".do");
				String tmp = m.get("tmp") != null ? m.get("tmp").toString()
						: "";
				rm.put("arr", lmTmp);
				rm.put("tmp", tmp);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
			int cacheTime = Integer.parseInt(paramMap.get("cacheTime")
					.toString());
			if (super.isCacheNull(cacheKey).equals("a")) {
				return saveAndGet(rm, cacheKey, cacheTime);
			} else {
				return super.toJson(rm);
			}
		} else if (super.isCacheNull(cacheKey).equals("b")) {
			return super.q(cacheKey);
		}
		return "";

	}
}
