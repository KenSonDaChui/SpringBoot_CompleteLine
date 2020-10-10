package com.jiucheng.controller;

import com.jiucheng.service.DataToEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * EsDataController
 *
 * @auther qiaoba
 * @date 2020/10/10 0010 9:27
 **/
@RestController
public class DataToEsController {

    @Autowired
    private DataToEsService dataToEsService;

    /**
     * 启动作业
     * @return
     */
    @RequestMapping("/startJob")
    public @ResponseBody
    String scheduleDB2ESJob(){
        return dataToEsService.startJob();
    }

    /**
     * 停止作业
     * @return
     */
    @RequestMapping("/stopJob")
    public @ResponseBody String stopDB2ESJob(){
        return dataToEsService.stopJob();
    }

}
