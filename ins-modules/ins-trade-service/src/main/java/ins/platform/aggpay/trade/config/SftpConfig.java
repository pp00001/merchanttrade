package ins.platform.aggpay.trade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by ADD on 2018/10/17.
 */
@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "isvConfig.sftp")
public class SftpConfig {
    /**
     * sftp地址
     */
    private String host;

    /**
     * sftp端口
     */
    private Integer port;

    /**
     * sftp账号
     */
    private String username;

    /**
     * sftp密码
     */
    private String password;

    /**
     * 对账文件路径
     */
    private String billPath;

    /**
     * 生成excel文件路径
     */
    private String excelPath;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath;
    }

    public String getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

}
