package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.app.AppLoginDTO;
import com.cskaoyan.wordmemorize.request.PhoneNumberLoginCommand;

public interface LoginService {


    // APP登录
    AppLoginDTO phoneNumberLogin(PhoneNumberLoginCommand command, String ipAddr);

}
