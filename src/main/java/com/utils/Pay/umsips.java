package com.utils.Pay;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;

public interface umsips extends Library {
    umsips instanceDll = (umsips) Native.loadLibrary("C:\\umsips\\umsapi.dll", umsips.class);
    int UMS_Init(int appType); // 初始化

    int UMS_SetReq(String cpInReq); // 设置入参

    int UMS_EnterCard();

    int UMS_CheckCard(byte[] state_out);

    int UMS_ReadCard(byte[] cpData);

    int UMS_EjectCard(); // 弹卡

    int UMS_CardSwallow(); // 吞卡

    int UMS_CardClose(); // 关闭读卡器

    int UMS_StartPin(); // 开启密码键盘

    int UMS_GetOnePass(byte[] key_out); // 获得键值

    int UMS_GetPin(); // 获取pin密文

    int UMS_ClearScreen(); // 清屏

    int UMS_TransCard(byte[] strReq, byte[] strResp); // 交易函数

    int Tlv_Init(Memory pTag, int iInBufLen); // 初始化TLV

    int Tlv_AddTag(byte[] cpInTagNameAsc, byte[] cpInData, int InDataLen); // 增加一个tag

    int Tlv_GetAscData(Memory cpInDataTLV, Memory cpOutDataAsc);

    int Tlv_GetTag(byte[] cpInTlvData, byte[] cpInTagAsc, byte[] cpOutData);

    int UMS_ShowData(int Low, byte[] Data);

}