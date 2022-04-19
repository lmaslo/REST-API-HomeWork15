package models;

public class GenerateTokenResponse {
  /*  {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImFsZXgiLCJwYXNzd29yZCI6ImFzZHNhZCNmcmV3X0RGUzIiLCJpYXQiOjE2NTAzNzY3ODR9.c7sA6rkU1Xa9v-SK4OwakK12VAErAJ4QuGy4pFte7no",
            "expires": "2022-04-26T13:59:44.700Z",
            "status": "Success",
            "result": "User authorized successfully."
    }*/


    private String token;
    private String expires;
    private String status;
    private String result;

    public String getToken() {
        return token;
    }


    public String getExpires() {
        return expires;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}
