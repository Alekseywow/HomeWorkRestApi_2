package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserFullResponseModel {

    DataResponseModel data;
    SupportResponseModel support;

}
