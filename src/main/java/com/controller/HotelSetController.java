package com.controller;

import com.entity.HotelSetTable;
import com.service.HotelSetTableService;
import com.utils.Returned.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/HotelSet")
public class HotelSetController {

    @Autowired
    private HotelSetTableService hotelSetTableService;

    /**
     * 查询酒店设置
     */
    @ApiOperation(value = "查询酒店设置")
    @RequestMapping(value = "/getHotelSet", method = RequestMethod.GET)
    public CommonResult<?> getHotelSet() {
        log.info("getHotelSet()方法");
        List<HotelSetTable> hotelSetTables=hotelSetTableService.list();
        return CommonResult.success(hotelSetTables);
    }

    /**
     * 设置酒店设置
     */
    @ApiOperation(value = "设置酒店设置")
    @RequestMapping(value = "/setHotelSet", method = RequestMethod.POST)
    public CommonResult<?> setHotelSet(@RequestBody HotelSetTable hotelSetTable){
        log.info("setHotelSet()方法");
        hotelSetTableService.updateById(hotelSetTable);
        return CommonResult.success("成功");
    }

}
