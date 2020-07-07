package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctc.wstx.util.StringUtil;
import com.entity.Certificate;
import com.entity.MiniRoomNo;
import com.entity.MiniRoomType;
import com.service.MiniRoomNoService;
import com.service.MiniRoomTypeService;
import com.utils.Returned.CommonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Mini")
public class MiniRoomTypeController {
    @Autowired
    private MiniRoomTypeService miniRoomTypeService;

    @Autowired
    private MiniRoomNoService miniRoomNoService;


    /**
     * mini吧根据房型设置查询
     */
    @ApiOperation(value = "mini吧根据房型设置查询")
    @RequestMapping(value = "/getMiniSet", method = RequestMethod.GET)
    public CommonResult<?> getMiniSet(String code) {
        log.info("getMiniSet()方法");
        if (StringUtils.isEmpty(code)){
            List<MiniRoomType> miniRoomTypeList=miniRoomTypeService.list();
            if (miniRoomTypeList.size()<=0){
                return CommonResult.failed("该code不存在");
            }
            return CommonResult.success(miniRoomTypeList);
        }else{
            MiniRoomType miniRoomType=miniRoomTypeService.getOne(new QueryWrapper<MiniRoomType>().eq("code",code));
            return CommonResult.success(miniRoomType);
        }
    }

    /**
     * mini吧房型设置表修改 --  是否支持迷你吧  押金倍数
     */
    @ApiOperation(value = "mini吧房型设置表修改")
    @RequestMapping(value = "/setMiniSet", method = RequestMethod.GET)
    public CommonResult<?> setMiniSet(String code,String isTrueMini,String multipleDeposit,String description) {
        log.info("setMiniSet()方法");
        if (StringUtils.isEmpty(code)){
            return CommonResult.failed("缺少参数");
        }
        MiniRoomType miniRoomType=miniRoomTypeService.getOne(new QueryWrapper<MiniRoomType>().eq("code",code));
        if (!StringUtils.isEmpty(isTrueMini)){
            miniRoomType.setIsTrueMini(isTrueMini);
        }
        if (!StringUtils.isEmpty(multipleDeposit)){
            miniRoomType.setMultipleDeposit(multipleDeposit);
        }
        if (!StringUtils.isEmpty(description)){
            miniRoomType.setDescription(description);
        }
        miniRoomTypeService.updateById(miniRoomType);
        return CommonResult.success("修改成功");
    }

    /**
     * mini吧房型表     添加
     */
    @ApiOperation(value = "mini吧房型设置表 添加")
    @PostMapping(value = "/addMiniSet",consumes = "application/json")
    public CommonResult<?> addMiniSet( @RequestBody MiniRoomType miniRoomType) {
        log.info("addMiniSet()方法");
        miniRoomTypeService.save(miniRoomType);
        return CommonResult.success("新增成功");
    }

    /**
     * mini吧根据房间号设置
     */
    @ApiOperation(value = "mini吧根据房间号设置")
    @RequestMapping(value = "/getMiniRoomSet", method = RequestMethod.GET)
    public CommonResult<?> getMiniRoomSet(String roomno) {
        log.info("getMiniRoomSet()方法");
        if (StringUtils.isEmpty(roomno)){
            List<MiniRoomNo> miniRoomNoList=miniRoomNoService.list();
            if (miniRoomNoList.size()<=0){
                return CommonResult.failed("该房间号不存在");
            }
            return CommonResult.success(miniRoomNoList);
        }else{
            MiniRoomNo miniRoomNo=miniRoomNoService.getOne(new QueryWrapper<MiniRoomNo>().eq("roomno",roomno));
            return CommonResult.success(miniRoomNo);
        }
    }

    /**
     * mini吧根据房间号设置
     */
    @ApiOperation(value = "mini吧根据房间号设置")
    @RequestMapping(value = "/setMiniRoomSet", method = RequestMethod.GET)
    public CommonResult<?> setMiniRoomSet(String roomno,String isTrueMini) {
        log.info("setMiniRoomSet()方法");
        if (StringUtils.isEmpty(roomno)){
            return CommonResult.failed("缺少参数");
        }
        MiniRoomNo miniRoomNo=miniRoomNoService.getOne(new QueryWrapper<MiniRoomNo>().eq("roomno",roomno));
        if (!StringUtils.isEmpty(isTrueMini)){
            miniRoomNo.setIsTrueMini(isTrueMini);
        }
        miniRoomNoService.updateById(miniRoomNo);
        return CommonResult.success("修改成功");
    }

    /**
     * mini吧房型表   添加
     */
    @ApiOperation(value = "mini吧房间设置表 添加")
    @PostMapping(value = "/addMiniNoSet",consumes = "application/json")
    public CommonResult<?> addMiniNoSet( @RequestBody MiniRoomNo miniRoomNo) {
        log.info("addMiniNoSet()方法");
        miniRoomNoService.save(miniRoomNo);
        return CommonResult.success("新增成功");
    }




}
