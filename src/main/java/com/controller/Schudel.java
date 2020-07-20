package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.RoomTypeSet;
import com.utils.Returned.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Calendar;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Schudel")
public class Schudel {

    private String wenTongImgurl="C:\\images\\test.jpg";
    private String wenTongNewImgurl="D:\\wentongimage\\";

    private String faceImgUrl="C:\\Users\\Administrator\\Desktop\\FaceCompare\\img\\face.jpg";
    private String faceNewImgUrl="D:\\faceimage\\";
    /**
     * 文通图片留存
     */
    @ApiOperation(value = "wenTongImgSave")
    @RequestMapping(value = "/wenTongImgSave", method = RequestMethod.GET)
    public void wenTongImgSave(String accnt) throws Exception {
        log.info("wenTongImgSave()方法");
        copy(wenTongImgurl, wenTongNewImgurl+accnt+".jpg");
    }

    /**
     * 人脸图片留存
     */
    @ApiOperation(value = "faceImgSave")
    @RequestMapping(value = "/faceImgSave", method = RequestMethod.GET)
    public void faceImgSave(String accnt) throws Exception {
        log.info("faceImgSave()方法");
        copy(faceImgUrl, faceNewImgUrl+accnt+".jpg");
    }




    public void copy(String oldPath,String newPath) throws Exception {
        InputStream in=new FileInputStream(oldPath);
        byte[] b=new byte[in.available()];
        in.read(b);
        OutputStream out=new FileOutputStream(newPath);
        out.write(b);
        in.close();
        out.close();
    }
}
