package com.example.in_sky1.aop;


import com.example.in_sky1.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
public class OperationLogAspect {


    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}