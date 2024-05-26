package com.example.WeeSeed;



import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class SFTPService {

    private final Session sftpSession;

    public SFTPService(Session sftpSession) {
        this.sftpSession = sftpSession;
    }

    public void uploadFile(byte[] bytes, String remoteFilePath) throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) sftpSession.openChannel("sftp");
        channelSftp.connect();
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            channelSftp.put(inputStream, remoteFilePath);
        } finally {
            channelSftp.disconnect();
        }
    }
}

