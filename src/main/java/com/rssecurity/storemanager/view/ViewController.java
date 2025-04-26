package com.rssecurity.storemanager.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class ViewController {

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/{page}")
    public String getPage(@PathVariable String page, @RequestHeader(required = false, defaultValue = "false") String isUpdate, Model model) {
        System.out.println(isUpdate);
        if (isUpdate.equals("true")) {
            return "index";
        } else {
            return "fragments/" + page;
        }
    }
}
