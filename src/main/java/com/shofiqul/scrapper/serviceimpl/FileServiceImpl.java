package com.shofiqul.scrapper.serviceimpl;

import com.shofiqul.scrapper.service.FileService;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public boolean fileTest() {
        String fileUrl = "https://play-lh.googleusercontent.com/zX7jmUbnCkH1LlhGFIffDv76OgJjIy3zZvzC6DPO-Cl-BPXfNVluTCDHTX6YSpvxKUrd=s256-rw";
        String destination = "image.webp";

        try {
            URL url = new URL(fileUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destination);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b);
            }

            is.close();
            os.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
