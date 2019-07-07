package com.babyeye.cloud.service;

import com.babyeye.cloud.service.impl.AuthServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wenquan.
 * @date 2019/4/28.
 */
@FeignClient(name = "auth", fallback = AuthServiceImpl.class)
public interface AuthService {

    /**
     * 鉴权验证token
     * @param url 请求路径
     * @param token jwt token
     * @return result
     */
    @GetMapping("/auth/permission")
    String authentication(@RequestParam("token") String token, @RequestParam("url") String url);
}
