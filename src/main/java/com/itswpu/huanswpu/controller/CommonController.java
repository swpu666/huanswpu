package com.itswpu.huanswpu.controller;

import com.itswpu.huanswpu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")//通过配置文件application.yml注入
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//这个file要和前端的保持一致
        //前端上传的file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();//获得上传时的原始文件名
        //截取之前文件的类型后缀(.jpg)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止之前上传的原始文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        File dir = new File(basePath);//创建一个目录对象
        //判断当前目录是否存在
        if(!dir.exists()){//如果上面配置的目录basePath不存在，需要创建
            dir.mkdirs();
        }

        try {//将临时文件转存(transferTo())到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName); //返回文件名称
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");//设置回去的是“”类型的文件

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){//循环输出写会浏览器
                outputStream.write(bytes,0,len);
                outputStream.flush();//刷新
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
