package models.lombok;

import lombok.Data;

@Data
public class GenerateTokenResponseLombok {

    private String token;
    private String expires;
    private String status;
    private String result;
}
