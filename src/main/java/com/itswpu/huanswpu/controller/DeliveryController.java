package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.entity.Delivery;
import com.itswpu.huanswpu.entity.User;
import com.itswpu.huanswpu.service.DeliveryService;
import com.itswpu.huanswpu.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/delivery")
@Slf4j
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();//获取手机号

        if(StringUtils.isNotEmpty(phone)){//手机号非空
            //工具类 生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云提供的短信服务API("签名","模板",手机号,动态验证码)完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //需要将生成的验证码保存到Session
            //session.setAttribute(phone,code);
            //将生成的验证码保存到Session
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success(code);//模拟验证码发送功能
//            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<Delivery> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);
        //从redis中获得缓存的验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<Delivery> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Delivery::getPhone,phone);

            Delivery delivery = deliveryService.getOne(queryWrapper);
            if(delivery == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                delivery = new Delivery();
                delivery.setPhone(phone);
                delivery.setStatus(1);
                deliveryService.save(delivery);
            }

            session.setAttribute("delivery",delivery.getId());
            //如果用户登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);
            return R.success(delivery);
        }
        return R.error("登录失败");
    }

    /**
     * 移动端骑手退出登录
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录商家的id
        request.getSession().removeAttribute("delivery");
        return R.success("用户退出成功");
    }

}
