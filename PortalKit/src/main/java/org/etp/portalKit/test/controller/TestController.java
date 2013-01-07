package org.etp.portalKit.test.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.etp.portalKit.common.util.CompressUtil;
import org.etp.portalKit.test.bean.TestBean;
import org.etp.portalKit.test.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The purpose of this class is provide a test controller. To show how
 * this framework works.
 */
@Controller
public class TestController {
    @Resource(name = "testservice")
    private TestService testservice;

    /**
     * @param bean
     * @param request
     * @return next page
     */
    @RequestMapping(value = "/test/redirect/ok", method = RequestMethod.POST)
    public String redirectOK(TestBean bean, HttpServletRequest request, Model model) {
        testservice.test(bean);
        model.addAttribute("msg", "you successful");
        return "common/success";
    }

    /**
     * @param bean
     * @param request
     * @return next page
     */
    @RequestMapping(value = "/test/redirect/error", method = RequestMethod.POST)
    public String redirectError(TestBean bean, HttpServletRequest request) {
        testservice.test(bean);
        throw new RuntimeException("you failed.");
    }

    /**
     * @param tb
     * @param response
     * @return map
     * @throws Exception
     */
    @RequestMapping(value = "/test/ajax/post/ok", method = RequestMethod.POST)
    public @ResponseBody
    TestBean okHandlePost(@RequestBody TestBean tb, HttpServletResponse response) {
        System.out.println("testBean = " + tb.getTestId() + " " + tb.getTestName());
        tb.setTestId(2);
        tb.setTestName("zuohao");
        testservice.test2(tb);
        return tb;
    }

    /**
     * @param tb
     * @param response
     * @return map
     */
    @RequestMapping(value = "/test/ajax/get/ok", method = RequestMethod.GET)
    public @ResponseBody
    TestBean okHandleGet(TestBean tb, HttpServletResponse response) {
        System.out.println("testBean = " + tb.getTestId() + " " + tb.getTestName());
        tb.setTestId(2);
        tb.setTestName("zuohao");
        testservice.test2(tb);
        return tb;
    }

    /**
     * @param tb
     * @param response
     * @return map
     * @throws Exception
     */
    @RequestMapping(value = "/test/ajax/post/error", method = RequestMethod.POST)
    public @ResponseBody
    TestBean errorHandlePost(TestBean tb, HttpServletResponse response) {
        System.out.println("testBean = " + tb.getTestId() + " " + tb.getTestName());
        tb.setTestId(2);
        tb.setTestName("zuohao");
        testservice.test2(tb);
        throw new RuntimeException("hello exception");
    }

    /**
     * @param tb
     * @param response
     * @return map
     * @throws Exception
     */
    @RequestMapping(value = "/test/ajax/get/error", method = RequestMethod.GET)
    public @ResponseBody
    TestBean errorHandleGet(TestBean tb, HttpServletResponse response) {
        System.out.println("testBean = " + tb.getTestId() + " " + tb.getTestName());
        tb.setTestId(2);
        tb.setTestName("zuohao");
        testservice.test2(tb);
        throw new RuntimeException("hello exception");
    }

    /**
     * @return map
     */
    @RequestMapping(value = "/test/ajax/post/ungzip", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> unGzip() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("result", "success");
        try {
            CompressUtil.unGzip("C:\\Users\\ehaozuo\\Downloads\\testing\\portal-sdk-cxp-R13B02-bin.tar.gz");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("result", "failure");
        }
        return result;
    }

    /**
     * @return map
     */
    @RequestMapping(value = "/test/ajax/post/untar", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> untar() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("result", "success");
        try {
            CompressUtil.unTar("C:\\Users\\ehaozuo\\Downloads\\testing\\portal-sdk-cxp-R13B02-bin.tar");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("result", "failure");
        }
        return result;
    }
}
