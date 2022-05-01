package com.moppletop.dsb.controller.api.model;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class ModifyClassFeaturesRequest {

    Map<String, List<String>> valueMap;

}
