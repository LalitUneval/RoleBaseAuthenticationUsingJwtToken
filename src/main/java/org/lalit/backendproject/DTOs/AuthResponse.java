package org.lalit.backendproject.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String token=null;
     public AuthResponse(String token){
         this.token=token;
     }
}
