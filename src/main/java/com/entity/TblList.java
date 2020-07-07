package com.entity;

import lombok.Data;

import java.util.List;

@Data
public class TblList {
    private String amount;
    private List<TblTxnp> tblTxnpList;
}
