package com.entity;

import lombok.Data;
import org.apache.tools.ant.util.LeadPipeInputStream;

import java.util.List;
@Data
public class MiniProduct {
    private String accnt;
    private String pccode;
    private String pcid;
    private List<MiniPro> miniProList;
}
