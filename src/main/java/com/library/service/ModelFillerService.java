package com.library.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Map;

@Service
public class ModelFillerService {
    public void fullFillModel(Model model, Map<String, Object> modelMap) {
        for (Map.Entry<String,Object> element : modelMap.entrySet()) {
            model.addAttribute(element.getKey(),element.getValue());
        }
    }
}
