package org.perf.httpVsDbCompare.controller;

import org.perf.httpVsDbCompare.client.PgClient;
import org.perf.httpVsDbCompare.service.DbService;
import org.perf.httpVsDbCompare.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class CompareController {
    
    private static final Logger LOGGER = Logger.getLogger(CompareController.class.getName());
    
    @Autowired
    private DbService dbService;
    
    @Autowired
    private HttpService httpService;

    @GetMapping("/")
    public String home(Model model) {
        return "compare";
    }
    
    @GetMapping("/start")
    public @ResponseBody String start(@RequestParam Integer numRecords, @RequestParam String requestType) {
        LOGGER.log(Level.INFO, "Start called, numRecords {0}", numRecords);
        String res = "";
        if (requestType.equals("db")) {
            res = dbService.estimate(numRecords);
        } else if (requestType.equals("http")) {
            res = httpService.estimate(numRecords);
        }
        LOGGER.log(Level.INFO, "Result for one by one {0} records, {1} request: {2} seconds",
                new Object[]{numRecords, requestType, res});
        return res;
    }
    
    @GetMapping("/startBatch")
    public @ResponseBody String startBatch(@RequestParam Integer numRecords, @RequestParam String requestType) {
        LOGGER.log(Level.INFO, "Start batch called, numRecords {0}", numRecords);
        String res = "";
        if (requestType.equals("db")) {
            res = dbService.estimateBatch(numRecords);
        } else if (requestType.equals("http")) {
            res = httpService.estimateBatch(numRecords);
        }
        LOGGER.log(Level.INFO, "Result for batch {0} records, {1} request: {2} seconds",
                new Object[]{numRecords, requestType, res});
        return res;
    }
    
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//        model.addAttribute("status", status);
//        model.addAttribute("exception", exception);
//        return "error";
//    }
}
