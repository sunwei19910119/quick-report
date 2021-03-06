package com.xhtt.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhtt.common.exception.RRException;
import com.xhtt.modules.sys.entity.*;
import com.xhtt.modules.sys.service.SsoLoginService;
import com.xhtt.modules.sys.service.SysGovernmentInfoService;
import com.xhtt.modules.sys.service.SysInterVersionService;
import com.xhtt.modules.sys.service.SysUserService;
import com.xhtt.modules.zb.entity.UserZbPowerEntity;
import com.xhtt.modules.zb.service.UserZbPowerService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


/**
 * @author
 */
@Service
public class SsoLoginServiceImpl implements SsoLoginService {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysGovernmentInfoService sysGovernmentInfoService;
    @Autowired
    private SysInterVersionService sysInterVersionService;
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private UserZbPowerService userZbPowerService;

    @Override
    public SysUserEntity ssoLogin(String token) throws RRException {
        SysUserEntity sysUser = null;
        int level = 0;
        SysGovernmentInfoEntity sysGovernmentInfo = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String url1 = null;
        SysInterVersionEntity sysInterVersion = sysInterVersionService.getById(1);
        if (sysInterVersion != null) {
            url1 = sysInterVersion.getInterUrl1();
        } else {
            url1 = "http://192.168.209.183:7078";
        }
        int i = 0;
        HttpClient client = HttpClients.createDefault();
        try {
            LocalDateTime t17 = LocalDateTime.now();
            String param1 = URLEncoder.encode(token, "UTF-8");
            String urlstr = url1 + "/api/user/me?token=" + param1;
            HttpGet request = new HttpGet(urlstr);
            request.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String strResult = EntityUtils.toString(entity, "utf-8");
            ReturnInfoMode returnInfo = JSON.parseObject(strResult, ReturnInfoMode.class);

            java.time.Duration duration17 = java.time.Duration.between(t17, LocalDateTime.now());
            System.out.println("me?????????" + df.format(LocalDateTime.now()) + " me?????????" + duration17.toMillis() + " ms");
            System.out.println(urlstr);
            if (returnInfo.getCode().equals("200")) {
                if (returnInfo.getData() != null) {
                    if (returnInfo.getData().getUserInfo().get("userRole").toString().equals("daab7224-f279-443d-b3e0-ff6691359f67")) {//??????????????????
                        if (returnInfo.getData().getThirdPartInfo() != null) {
                            ThirdPartInfoMode govInfo = returnInfo.getData().getThirdPartInfo();
                            LocalDateTime t20 = LocalDateTime.now();
                            synchronized (this) {
                                sysGovernmentInfo = sysGovernmentInfoService.getbygovOtherID(Long.valueOf(govInfo.getDwid()));
                                //?????????????????????????????????????????????????????????????????????????????????
                                if (sysGovernmentInfo == null) {
                                    sysGovernmentInfo = new SysGovernmentInfoEntity();
                                    i = 2;
                                } else {
                                    i = 3;
                                }
                                sysGovernmentInfo.setGovOtherId(govInfo.getDwid());
                                sysGovernmentInfo.setProvinceCode("32");
                                sysGovernmentInfo.setMarketCode("3204");
                                if (StringUtils.isNotEmpty(govInfo.getSzqx())) {
                                    if (govInfo.getSzqx().length() == 4) {

                                        sysGovernmentInfo.setMarketCode(govInfo.getSzqx());
                                    } else {

                                        sysGovernmentInfo.setCountyCode(govInfo.getSzqx());
                                    }
                                }

                                //??????????????????
                                if (StringUtils.isNotEmpty(govInfo.getAreaId()) && govInfo.getAreaId().length() == 4) {
                                    //??????
                                    level = 1;
                                }else{
                                    //?????????
                                    level = 0;
                                }

                                if(StringUtils.isNotEmpty(govInfo.getSzxz())){
                                    sysGovernmentInfo.setTownCode(govInfo.getSzxz());
                                }
                                if(StringUtils.isNotEmpty(govInfo.getSzcw())){
                                    sysGovernmentInfo.setVillageCode(govInfo.getSzcw());
                                }
                                sysGovernmentInfo.setIdNo(govInfo.getSfzh());
                                if (govInfo.getSfzxzz() != null) {
                                    sysGovernmentInfo.setIsSpecialTreat(Integer.parseInt(govInfo.getSfzxzz()));
                                }
                                sysGovernmentInfo.setIdNo(govInfo.getSfzh());
                                sysGovernmentInfo.setGovName(govInfo.getZfdwmc());
                                if (i == 2) {
                                    sysGovernmentInfoService.save(sysGovernmentInfo);
                                } else if (i == 3) {
                                    sysGovernmentInfoService.updateById(sysGovernmentInfo);
                                }
                            }
                            java.time.Duration duration20 = java.time.Duration.between(t20, LocalDateTime.now());
                            System.out.println("??????????????????" + df.format(LocalDateTime.now()) + " ??????????????????" + duration20.toMillis() + " ms");
                        } else {
                            throw new RRException(String.format("?????????????????????"));
                        }

                        if (returnInfo.getData().getUserInfo().get("userName") != null && returnInfo.getData().getUserInfo().get("userName") != "") {
                            LocalDateTime t6 = LocalDateTime.now();
                            sysUser = this.check(returnInfo.getData().getUserInfo(), sysGovernmentInfo.getId(), 2, token,level,returnInfo.getData().getThirdPartInfo());
                            java.time.Duration duration6 = java.time.Duration.between(t6, LocalDateTime.now());
                            System.out.println("????????????????????????????????????" + sysUser.getNickName() + " ????????????????????????????????????????????????" + duration6.toMillis() + " ms");
                        }
                    }
                } else {
                    throw new RRException(String.format("?????????????????????????????????????????????"));
                }
            } else {
                throw new RRException(String.format("token?????????"));
            }

        } catch (IOException e) {
            throw new RRException(String.format("???????????????"));
        }

        return sysUser;

    }


    private SysUserEntity check(Map<String, Object> userInfo, Integer orgId, Integer type, String token,Integer level,ThirdPartInfoMode thirdPartInfoMode) {
        SysUserEntity sysUser2 = this.verification(userInfo);
        String userConnectId = null;
        if (userInfo.get("userConnectId") != null) {
            userConnectId = userInfo.get("userConnectId").toString();
        }

        SysUserEntity sysUser;
        synchronized (this) {
            sysUser = sysUserService.getUserName(sysUser2.getName(), orgId);
            String password = new Sha256Hash("Qwe@1234...").toHex();
            if (sysUser == null) {
                sysUser = new SysUserEntity();
                sysUser.setName(sysUser2.getName());
                sysUser.setWorkNo(sysUser2.getName());
                sysUser.setPassword(password);
                sysUser.setSalt("");
                sysUser.setMobile(sysUser2.getMobile());
                sysUser.setType(type);
                sysUser.setOrgId(orgId);
            }
            sysUser.setLevel(level);
            sysUser.setUserConnectId(userConnectId);
            sysUser.setBmdm(thirdPartInfoMode.getBmdm());
            sysUser.setCitySysToken(token);
            sysUser.setNickName(sysUser2.getNickName());
            sysUser.setLastLoginDate(LocalDateTime.now());

            sysUser.setRole(0);
            //??????????????????
            UserZbPowerEntity userZbPowerEntity = userZbPowerService.selectByUserConnectId(userConnectId);
            if(userZbPowerEntity == null){
                throw new RRException(String.format("????????????????????????"));
            }
            if(userZbPowerEntity.getZbldKey() == 1){
                sysUser.setRole(1);
            }

            sysUserService.saveOrUpdate(sysUser);
        }
        return sysUser;
    }

    private SysUserEntity verification(Map<String, Object> userInfo) {
        SysUserEntity sysUser = new SysUserEntity();
        if (userInfo.get("userName") != null) {
            sysUser.setName(userInfo.get("userName").toString());
        }
        if (userInfo.get("userMobile") != null) {
            sysUser.setMobile(userInfo.get("userMobile").toString());
        }
        if (userInfo.get("nickName") != null) {
            sysUser.setNickName(userInfo.get("nickName").toString());
        }
        return sysUser;
    }

}
