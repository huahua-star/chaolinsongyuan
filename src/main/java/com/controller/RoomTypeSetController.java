package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.MiniRoomType;
import com.entity.RoomTypeSet;
import com.service.MiniRoomNoService;
import com.service.MiniRoomTypeService;
import com.service.RoomTypeSetService;
import com.utils.Returned.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/RoomTypeSet")
public class RoomTypeSetController {
    @Autowired
    private RoomTypeSetService roomTypeSetService;


    /**
     * 房型房价设置
     */
    @ApiOperation(value = "房型房价设置")
    @RequestMapping(value = "/getRoomTypeSet", method = RequestMethod.GET)
    public CommonResult<?> getRoomTypeSet(String priceCode) {
        log.info("getRoomTypeSet()方法");
        if (StringUtils.isEmpty(priceCode)){
            List<RoomTypeSet> roomTypeSets=roomTypeSetService.list();
            if (roomTypeSets.size()<=0){
                return CommonResult.failed("该code不存在");
            }
            return CommonResult.success(roomTypeSets);
        }else{
            RoomTypeSet roomTypeSet=roomTypeSetService.getOne(new QueryWrapper<RoomTypeSet>().eq("price_code",priceCode));
            return CommonResult.success(roomTypeSet);
        }
    }

    /**
     * 房型房价设置修改
     */
    @ApiOperation(value = "房型房价设置修改")
    @RequestMapping(value = "/setRoomTypeSet", method = RequestMethod.GET)
    public CommonResult<?> setRoomTypeSet(String id,String multipleDeposit,String roomPrice,String roomType,String priceCode) {
        log.info("setMiniSet()方法");
        if (StringUtils.isEmpty(id)){
            return CommonResult.failed("缺少参数");
        }
        RoomTypeSet roomTypeSet=roomTypeSetService.getById(id);
        if (!StringUtils.isEmpty(roomPrice)){
            roomTypeSet.setRoomPrice(roomPrice);
        }
        if (!StringUtils.isEmpty(multipleDeposit)){
            roomTypeSet.setMultipleDeposit(multipleDeposit);
        }
        if (!StringUtils.isEmpty(roomType)){
            roomTypeSet.setRoomType(roomType);
        }
        if (!StringUtils.isEmpty(priceCode)){
            roomTypeSet.setPriceCode(priceCode);
        }
        roomTypeSetService.updateById(roomTypeSet);
        return CommonResult.success("修改成功");
    }
    /**
     * addRoomTypeSet     添加
     */
    @ApiOperation(value = "addRoomTypeSet 添加")
    @PostMapping(value = "/addRoomTypeSet",consumes = "application/json")
    public CommonResult<?> addRoomTypeSet( @RequestBody RoomTypeSet roomTypeSet) {
        log.info("addRoomTypeSet()方法");
        roomTypeSetService.save(roomTypeSet);
        return CommonResult.success("新增成功");
    }

}
