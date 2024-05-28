package com.example.WeeSeed;



import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SFTPConfig {

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private int sftpPort;

    @Value("${sftp.user}")
    private String sftpUser;

    @Value("${sftp.password}")
    private String sftpPassword;

    @Bean
    public Session sftpSession() throws Exception {
        Session session;
        JSch jsch = new JSch();
        session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        try {

            session.setPassword(sftpPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

        }catch (Exception e){

        }
        return session;
    }
}
