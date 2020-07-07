package com.controller;

import com.utils.Sample;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@SuppressWarnings("all")
@RestController
@RequestMapping("/audio")
public class Speech {
    /**
     * 根据文字生成语音
     * @param text 语音内容
     * @return
     */
    @ApiOperation(value="根据文字生成语音-getAutdio", notes="根据文字生成语音-getAutdio")
    @GetMapping(value = "/getAutdio")
    public void getAutdio(String text, HttpServletRequest request, HttpServletResponse response){
        try {
            File file = new File("C:/audio/"+text+".mp3");
            byte[] buff;
            if(!file.exists()){
                //本地不存在语音文件
                //百度语音合成
                buff = Sample.synthesis(text);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buff);
                fileOutputStream.close();

            }else{
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int x = 0 ;
                while ((x = fileInputStream.read() )!= -1){
                    byteArrayOutputStream.write(x);
                }
                buff = byteArrayOutputStream.toByteArray();
                fileInputStream.close();
                byteArrayOutputStream.close();

            }
            //设置发送到客户端的响应内容类型
            response.setContentType("audio/*");
            OutputStream out = response.getOutputStream();
            out.write(buff);
            //关闭响应输出流
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("getAutdio()方法出现异常");
        } catch (IOException e) {
            throw new RuntimeException("getAutdio()方法出现异常");
        } catch (Exception e){
            throw new RuntimeException("getAutdio()方法出现异常");
        }
    }

    public static void main(String[] args) {
        getAutdio("哈喽");
    }
    public static void getAutdio(String text){
        try {
            File file = new File("D:/"+text+".mp3");
            byte[] buff;
            if(!file.exists()){
                //本地不存在语音文件
                //百度语音合成
                buff = Sample.synthesis(text);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buff);
                fileOutputStream.close();

            }else{
                FileInputStream fileInputStream = new FileInputStream(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int x = 0 ;
                while ((x = fileInputStream.read() )!= -1){
                    byteArrayOutputStream.write(x);
                }
                buff = byteArrayOutputStream.toByteArray();
                fileInputStream.close();
                byteArrayOutputStream.close();

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("getCheckInPerson()方法出现异常");
        } catch (IOException e) {
            throw new RuntimeException("getCheckInPerson()方法出现异常");
        } catch (Exception e){
            throw new RuntimeException("getCheckInPerson()方法出现异常");
        }
    }
}