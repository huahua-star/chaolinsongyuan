package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.Race;
import com.entity.Reservation;
import com.service.RaceService;
import com.service.ReservationService;
import com.utils.Base64Img;
import com.utils.JaxbUtil;
import com.utils.Publicsecurity.clientele;
import com.utils.Returned3.R;
import com.utils.UuidUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "公安旅店录入信息接口")
@RestController
@RequestMapping("/zzj/Thepublic")
public class Thepublic {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RaceService raceService;


    /**
     *
     * @param code 民族代码
     * @return
     */
    @ApiOperation(value = "民族代码  code 转换")
    @RequestMapping(value = "/GetRace", method = RequestMethod.GET)
    public String GetRace(String code) {
        log.info("GetRace()方法");
        if (StringUtils.isEmpty(code)){
            return "转换失败，缺少参数";
        }
        Race race=raceService.getOne(new QueryWrapper<Race>().eq("code",code));
        return race.getRaceid();
    }

    /**
     * @param imgurl       照片
     * @param cardType     证件类型
     * @param idEntityCard 证件号码(身份证号码)
     * @return
     */
    @ApiOperation(value = "公安旅店录入信息接口", httpMethod = "GET")
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public R information(String imgurl, String cardType, String idEntityCard,String accnt) throws Exception {

        String path = "D:\\img\\";//本地xml文件的路径
        String pathname;
        String fullpath;
        File newFile = null;
        String uuid = UuidUtils.getUUID();
        pathname = uuid + ".tmp";
        fullpath = path + pathname;
        System.out.println("fullpath:"+fullpath);
        File filename = new File(fullpath);
        try {
            if (!filename.exists()) {
                filename.createNewFile();
            }
        } catch (IOException e) {
            return R.error("创建文件失败");
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        clientele user = new clientele();
        Reservation reservation = GetArrivingReservationController.GetOneReservation(accnt);
        //Reservation reservation=reservationService.getOne(new QueryWrapper<Reservation>().eq("accnt",accnt));
        System.out.println("reservation:"+reservation);
        if (reservation == null ) {
            return R.error("信息为空无法录入");
        }
        if (cardType.equals("ID")) {
            user.setCARD_TYPE("11");//身份证
        }
        if (cardType.equals("PSP")) {
            user.setCARD_TYPE("93");//中华人民共和国护照
        }
        if (cardType.equals("HKP")) {
            user.setCARD_TYPE("20");//中华人民共和国往来港澳通行证
        }
        if (cardType.equals("TWP")) {
            user.setCARD_TYPE("台湾居民往来大陆许可证");
        }
        if (cardType.equals("FRP")) {
            user.setCARD_TYPE("外宾居留证");
        }
        if (cardType.equals("DP")) {
            user.setCARD_TYPE("外交护照");
        }
        if (cardType.equals("FTP")) {
            user.setCARD_TYPE("外宾旅游证");
        }
        if (cardType.equals("SP")) {
            user.setCARD_TYPE("公务护照");
        }
        if (cardType.equals("MO")) {
            user.setCARD_TYPE("90");//中国人民解放军军官证
        }
        if (cardType.equals("MS")) {
            user.setCARD_TYPE("92");//中国人民解放军士兵证
        }
        if (cardType.equals("OC")) {
            user.setCARD_TYPE("91");//中国人民武装警察部队警官证
        }
        if (cardType.equals("PTP")) {
            user.setCARD_TYPE("证照办理证明");
        }

        String checkintime = null;
        String checkouttime = null;
        String birthdaytime = null;

        checkintime = reservation.getArrival();
        checkouttime = reservation.getDeparture();
        birthdaytime = reservation.getBirth();


        if (null==reservation.getRace()||
                "".equals(reservation.getRace())||
                        reservation.getRace().equals("?")||
                        reservation.getRace().equals("？")
        ){
            reservation.setRace("HA");
        }
        String race=GetRace(reservation.getRace());
        if(race==null || "".equals(race)){
            race="01";//默认汉族
        }
        user.setENTER_TIME(checkintime);//入住时间
        user.setEXIT_TIME(checkouttime);//离店时间
        user.setBIRTHDAY(birthdaytime);//出生日期
        user.setFLAG("CHINESE");
        user.setCREATE_TIME(formatter2.format(new Date()));
        user.setID(UuidUtils.getUUID());
        user.setOPERATETYPE("0");//对该旅客的操作类型 只有入住
        user.setROOM_NO(reservation.getRoomno());//房间号
        user.setNAME(reservation.getName());//入住人姓名
        user.setSEX(reservation.getSex());//性别
        user.setNATION(race); //民族
        user.setCARD_NO(idEntityCard);//入住人身份证号码
        user.setREASON("22");//来京理由 默认22
        user.setOCCUPATION("22");//职业 默认22
        if(null==reservation.getStreet() || "".equals(reservation.getStreet()))
        {
            reservation.setStreet("无");
        }
        System.out.println("地址："+reservation.getStreet());
        user.setADDRDETAIL(reservation.getStreet());//地址
        //user.setADDRESS(reservation.getStreet());
        user.setCREDITCARD_NO(" ");//旅客信用卡号 可以为空
        user.setCREDITCARD_TYPE(" ");//旅客信用卡类型 可以为空
        user.setPHOTO(Base64Img.GetImageStr(imgurl));

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fullpath)),"utf-8"));
        String dataXml = JaxbUtil.convertToXml(user);
        System.out.println("dataXml!"+dataXml);
        out.write(dataXml); // \r\n即为换行
        out.flush();
        out.close(); // 最后记得关闭文件
        File parentDir = new File("\\\\10.10.4.31\\exchange\\data");  //部署时需要修改IP地址
        //File parentDir = new File("D:\\data");  //部署时需要修改IP地址
        File targetpath = new File(parentDir, uuid + ".tmp");
        SAXReader reader = new SAXReader();
        Document read = reader.read(fullpath);
        //创建输出流
        FileOutputStream outStream = new FileOutputStream(targetpath);
        //设置输出格式
        OutputFormat format = OutputFormat.createCompactFormat();
        format.setEncoding("utf-8");
        XMLWriter writers = new XMLWriter(outStream, format);
        writers.write(read);
        outStream.close();//释放流
        return R.ok();
    }
}
