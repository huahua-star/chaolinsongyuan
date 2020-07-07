package com.Thread;

import TTCEPackage.K7X0Util;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.utils.FaceUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
@Data
@Slf4j
public class CompareThread extends Thread {
    private String imgFileName;
    private int nVISCameraID;
    private int nNIRCameraID;
    public static int key=0;


    public CompareThread(String imgFileName,int nVISCameraID,int nNIRCameraID){
        this.imgFileName=imgFileName;

        this.nVISCameraID=nVISCameraID;

        this.nNIRCameraID=nNIRCameraID;
    }

    public void run() {
        log.info("进入发卡方法,6秒后将自动发卡");

    }
}
