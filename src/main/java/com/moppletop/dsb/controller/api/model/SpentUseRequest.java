package com.moppletop.dsb.controller.api.model;

import lombok.Value;

@Value
public class SpentUseRequest {

    String key;
    Integer uses;

}
