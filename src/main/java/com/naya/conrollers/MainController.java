package com.naya.conrollers;

import com.naya.coords.LatLng;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Naya on 21.08.2016.
 */
@Controller
@RequestMapping(value = "/")
public class MainController {

    @RequestMapping(method = RequestMethod.POST)
    public List<LatLng> lala(@RequestParam double altitude,@RequestParam double distance,
                             @RequestParam double spacing, @RequestParam double angle, @RequestParam double overshoot1,
                             @RequestParam double overshoot2, @RequestParam
                             LatLng startLatLng, @RequestParam double minLaneSeparation, @RequestParam double leadin ){
        return null;
    }
}
