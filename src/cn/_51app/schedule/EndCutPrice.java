package cn._51app.schedule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn._51app.controller.BaseController;
import cn._51app.dao.diy2_0.IZeroGoodDao;

/**
 * @author Administrator
 * 扫描结束砍价
 */
@Component
public class EndCutPrice extends BaseController{
	
	@Autowired
	private IZeroGoodDao iZeroGoodDao;
	
	@Scheduled(cron="0 0/1 * * * ?")
	public void checkTask(){
		List<Map<String, Object>> cutPriceList=this.iZeroGoodDao.queryEndList();
		String ids="(";
		if(cutPriceList!=null && cutPriceList.size()>0){
			for (Map<String, Object> map : cutPriceList) {
				Integer id=(Integer)map.get("id");
				ids+=id+",";
			}
			ids=ids.substring(0,ids.length()-1)+")";
			this.iZeroGoodDao.endCutPriceStatus(ids);
		}
	}
}
