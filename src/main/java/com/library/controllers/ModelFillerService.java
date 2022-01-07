package com.library.controllers;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Map;

@Component
public class ModelFillerService {
    public void fullFillModel(Model model, Map<String, Object> modelMap) {
        for (Map.Entry<String,Object> element : modelMap.entrySet()) {
            model.addAttribute(element.getKey(),element.getValue());
        }
    }
}
