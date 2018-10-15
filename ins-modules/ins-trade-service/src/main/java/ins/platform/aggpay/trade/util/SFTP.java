/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


/**
 * @ClassName: SFTP
 * @Author: sfhq1026
 * @Description: TODO(sftp demo)
 * @Date: 2013-3-1 下午12:25:24
 */
public class SFTP {
	
	private static Logger logger = LoggerFactory.getLogger(SFTP.class);
	

    /**
     * @param args
     */
    public static void main(String[] args) {
        
    	/*SFTP ftp = new SFTP();
        String host="200.31.154.19";
        int port = 22;
        String username="root";
        String password="123456";
        String directory="/home/imix/";
        String uploadFile = "D:/ipconfig.txt";
        String downloadFile = "ipconfig.txt";
        String saveFile = "D:/ipconfig.txt";
        String deleteFile = "ipconfig.txt";
        
        ChannelSftp sftp = ftp.connect(host, port, username, password);
        ftp.upload(directory, uploadFile, sftp);
        ftp.download(directory, downloadFile, saveFile, sftp);
        ftp.delete(directory, deleteFile, sftp);
        try {
            sftp.cd(directory);
            sftp.mkdir("createFolder");
        } catch (SftpException e) {
            e.printStackTrace();
        }finally{
            try {
            //如果没有sesstion的disconnect，程序不会退出
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
            sftp.disconnect();
            sftp.exit();
        }*/
    }

    /**
     * @AddBy: sfhq1026
     * @Description: TODO(connect the host)
     * @param host
     * @param port
     * @param username
     * @param password
     * @return Map<String, Object>
     */
    public Map<String, Object> connect(String host, int port, String username, String password) {
        Map<String, Object> resutl = new HashMap<String, Object>();
    	ChannelSftp csftp = null;
        JSch jsch = new JSch();
        try {
            Session sshSession = jsch.getSession(username, host, port);
            logger.info("准备连接sftp, user="+username);
            
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            logger.info("sftp连接成功, user="+username+",主机："+host);
            
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            
            csftp = (ChannelSftp)channel;
            
            resutl.put("code", "0");
            resutl.put("sftp", csftp);
            resutl.put("info", "连接sftp成功");
        } catch (Exception e) {
            logger.error("连接sftp异常，user="+username+",主机："+host+"异常信息："+e.getClass() + "," + e.getMessage()+")",e);
            resutl.put("code", "1");
            resutl.put("info", "连接sftp异常，user="+username+",主机："+host+"异常信息："+"Exception:" + e.getClass() + "," + e.getMessage()+")");
        }
        return resutl;
    }
    
    /**
     * @AddBy: sfhq1026
     * @Description: TODO(upload file to host)
     * @param directory
     * @param uploadFile
     * @param sftp
     * @return boolean
     */
    public boolean upload(String directory, String uploadFile, ChannelSftp sftp){
		FileInputStream fis = null;
		try {
			File file = new File(uploadFile);
			fis = new FileInputStream(file);
			sftp.cd(directory);
			sftp.put(fis, file.getName());
			logger.info("upload file success, file:" + uploadFile);
		} catch (Exception e) {
			logger.error("upload file failed, file:" + uploadFile + "," + e.getMessage());
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("上传文件关闭流异常，" + e.getMessage());
				}
				fis = null;
			}
		}
		return true;
    }
    /**
     * @AddBy: sfhq1026
     * @Description: TODO(download file from host)
     * @param directory
     * @param downloadFile
     * @param saveFile
     * @param sftp
     * @return Map<String, Object>
     */
    public Map<String, Object> download(String directory, String downloadFile, String local, ChannelSftp sftp){
       
    	Map<String, Object> result = new HashMap<String, Object>();
    	FileOutputStream fos = null;
        try {
        	String saveFile = local + downloadFile;
        	File fileDir =new File(local);
        	//如果文件夹不存在则创建     
        	if(!fileDir.exists()  && !fileDir.isDirectory()){        
        	    fileDir.mkdir();     
        	}   
        	File file = new File(saveFile);
        	fos = new FileOutputStream(file);
            sftp.cd(directory);
            sftp.get(downloadFile, fos);
            logger.info("sftp download file success, file:"+downloadFile);
            result.put("code", "0");
            result.put("info", "文件下载成功");
            result.put("file", file);
        } catch (FileNotFoundException e) {
            
            logger.error("sftp下载文件异常文件未找到, file:"+downloadFile+"异常信息："+e.getClass() + "," + e.getMessage()+")",e);
            result.put("code", "2");
            result.put("info", "sftp下载文件异常文件未找到, file:"+downloadFile+"异常信息："+e.getClass() + "," + e.getMessage()+")");
        } catch (SftpException e) {
        	 logger.error("sftp下载文件异常文件未找到, file:"+downloadFile+"异常信息："+e.getClass() + "," + e.getMessage()+")",e);
             result.put("code", "3");
             result.put("info", "sftp下载文件异常, file:"+downloadFile+"异常信息："+e.getClass() + "," + e.getMessage()+")");
        } finally {
        	if(fos != null){
        		try {
					fos.close();
				} catch (IOException e) {
					logger.error("上传文件关闭流异常，" + e.getMessage());
				}
        		fos = null;
        	}
        }
        return result;
    }
    
    /**
     * @AddBy: sfhq1026
     * @Description: TODO(delete file of host)
     * @param directory
     * @param deleteFile
     * @param sftp
     * @return boolean
     */
    public boolean delete(String directory, String deleteFile, ChannelSftp sftp){
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            logger.info("delete file success, file:"+deleteFile);
        } catch (SftpException e) {
        	logger.error("delete file failed, file:"+deleteFile);
            return false;
        }
        return true;
    }
    
    /**
     * @AddBy: sfhq1026
     * @Description: TODO(get file list from directory of host)
     * @param directory
     * @param sftp
     * @return
     */
    /* CI检测问题，故注释该方法
     * @SuppressWarnings({ "rawtypes", "unchecked" })
	public List listFiles(String directory, ChannelSftp sftp){
        try {
             Vector vector = sftp.ls(directory);
             List list = new ArrayList();
             Collections.copy(list, vector);
             return list;
        } catch (SftpException e) {
        	logger.error("list directory failed, directory:"+directory);
        }
        return null;
    }*/
    
}