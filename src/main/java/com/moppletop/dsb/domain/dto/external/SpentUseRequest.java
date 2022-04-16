package com.moppletop.dsb.domain.dto.external;

import lombok.Value;

@Value
public class SpentUseRequest {

    String key;
    Integer uses;

}
