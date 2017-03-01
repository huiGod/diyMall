package cn._51app.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn._51app.dao.IUserDao;
import cn._51app.service.IUserService;
import cn._51app.util.PropertiesUtil;

@Service
public class UserService implements IUserService{
	
	@Autowired
	private IUserDao userDao;
	
	private ObjectMapper mapper=new ObjectMapper();
	
	private final String updownloadRootDir =PropertiesUtil.getValue("uploadUrl.sys");
	
	@Override
	public String getUser(String deviceNo,String app) throws Exception{
		Map<String, Object> map = userDao.getUser(deviceNo, app);
		String json = mapper.writeValueAsString(map);
		return json;
	}
	
	
	public String updateHead(MultipartFile imgFile,String deviceNo) throws IOException{
		
		if(imgFile!=null && !imgFile.isEmpty()){
			File file=new File(updownloadRootDir+"user");
			if(!file.exists()){
				file.mkdirs();
			}
			String resultPath="user/"+deviceNo+".jpg";
			File imgFilePath=new File(updownloadRootDir+resultPath);
			if(!imgFilePath.exists()){
				imgFilePath.createNewFile();
			}
			imgFile.transferTo(imgFilePath);
		}
		return "";
	}
}
