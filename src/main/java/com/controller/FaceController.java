package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.utils.FileCodeUtil;
import com.utils.Http.HttpUtil;
import com.utils.Returned2.Result;
import com.utils.Returned2.SetResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "人脸图片传输")
@RestController
@RequestMapping("/face")
public class FaceController {

    private static String url="http://10.10.130.250:59000/hesp/v1.0.0/api/checkIn/face";
    //private static String url="http://static.dalitek.tech:59000/hesp/v1.0.0/api/checkIn/face";


    /**
     * 人脸图片传输
     */
    @ApiOperation(value = "人脸图片传输", notes = "faceSend")
    @GetMapping(value = "/faceSend")
    public Result<Object> faceSend(String roomNum,String idCard,String facePath) throws Exception {
        Result<Object> result = new Result<>();
        String face=PictureController.getImgStr(facePath);
        //FileCodeUtil.encodeBase64File(facePath);
        Map<String,String> map=new HashMap<>();
        map.put("roomNum",roomNum);
        map.put("idCard",idCard);
        map.put("face",URLEncoder.encode(face, "utf8"));//
        Map<String,String> paramMap=map;
        String param= HttpUtil.getMapToString(paramMap);
        String postUrl=url;
        String returnResult= HttpUtil.sendPostFace(url,param);
        System.out.println("returnResult:"+returnResult);
        JSONObject jsonObj = JSONObject.parseObject(returnResult);
        String code=jsonObj.getString("code");
        String message=jsonObj.getString("message");
        if ("0".equals(code)){
            return SetResultUtil.setSuccessResult(result,"成功",jsonObj);
        }else{
            return SetResultUtil.setErrorMsgResult(result,message);
        }
    }

}
