package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.HotelSetTable;
import com.entity.RoomDescription;
import com.entity.RoomListNum;
import com.service.HotelSetTableService;
import com.service.RoomDescriptionService;
import com.utils.Returned.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/RoomDescription")
public class RoomDescriptionController {
    @Autowired
    private RoomDescriptionService roomDescriptionService;


    @ApiOperation(value = "查询房间描述")
    @RequestMapping(value = "/getRoomDescription", method = RequestMethod.GET)
    public CommonResult<?> getRoomDescription(String roomnum) {
        log.info("getRoomDescription()方法,roomnum:{}",roomnum);
        List<RoomDescription> roomDescriptionList=null;
        if (StringUtils.isEmpty(roomnum)){
            roomDescriptionList=roomDescriptionService.list();
        }else{
            roomDescriptionList=new ArrayList<>();
            String[] arr=roomnum.split(",");
            for (int i=0;i<arr.length;i++){
                RoomDescription roomDescription=roomDescriptionService.getOne(new QueryWrapper<RoomDescription>().eq("roomnum",arr[i]));
                if (null==roomDescription){
                    roomDescription=new RoomDescription();
                    roomDescription.setDescription("暂无描述");
                    roomDescription.setDescriptionEng("No description");
                    roomDescription.setRoomnum(arr[i]);
                }
                roomDescriptionList.add(roomDescription);
            }
        }
        return CommonResult.success(roomDescriptionList);
    }
    @ApiOperation(value = "修改房间描述")
    @RequestMapping(value = "/setRoomDescription", method = RequestMethod.POST)
    public CommonResult<?> setRoomDescription(@RequestBody RoomDescription roomDescription) {
        log.info("setRoomDescription()方法");
        boolean flag=roomDescriptionService.updateById(roomDescription);
        if (flag){
            return CommonResult.success("成功修改");
        }else{
            return CommonResult.failed("修改失败");
        }
    }

    @ApiOperation(value = "新增单个房间描述")
    @RequestMapping(value = "/addRoomDescription", method = RequestMethod.POST)
    public CommonResult<?> addRoomDescription(@RequestBody RoomDescription roomDescription) {
        log.info("addRoomDescription()方法");
        boolean flag=roomDescriptionService.save(roomDescription);
        if (flag){
            return CommonResult.success("成功新增");
        }else{
            return CommonResult.failed("新增失败");
        }
    }

}
