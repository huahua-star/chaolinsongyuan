package com.entity;

import lombok.Data;

@Data
public class Bill {
    private String Date;
    private String Description;
    private String GuestName;
    private String RoomNo;
    private String RoomType;
    private String Debit;
    private String Time;
    private String Code;
    private String VAT;
    private String Paid;
}
