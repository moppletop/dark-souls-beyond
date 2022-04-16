package com.moppletop.dsb.domain.dto.external;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class ModifyClassFeaturesRequest {

    Map<String, List<String>> valueMap;

}
