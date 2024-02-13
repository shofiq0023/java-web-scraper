package com.shofiqul.scrapper.serviceimpl;

import com.shofiqul.scrapper.model.PlayStoreIconsEntity;
import com.shofiqul.scrapper.service.FileService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public boolean createWebpFile(PlayStoreIconsEntity entity) {
        String fileUrl = entity.getImageUrl();
        String title = entity.getTitle().replaceAll("[ ™—:]", "_");
        String destination = "output/" + title +".webp";

        try {
            URL url = new URL(fileUrl);
            BufferedImage bi = ImageIO.read(url);
            ImageIO.write(bi, "webp", new File(destination));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
