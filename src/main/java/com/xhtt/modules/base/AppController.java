package com.xhtt.modules.base;


import com.xhtt.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AppController {
    @GetMapping("favicon.ico")
    public R notToken() {
        return R.ok().put("msg", "ok");
    }
}
