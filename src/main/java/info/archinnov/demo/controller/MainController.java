package info.archinnov.demo.controller;

import java.util.List;
import info.archinnov.demo.model.RateLimit;
import info.archinnov.demo.model.ValueWithTimestamp;
import info.archinnov.demo.service.DbService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( produces = "application/json")
public class MainController {

	@Inject
	private DbService dbService;

    @RequestMapping(value = "/timestamp", method = RequestMethod.GET)
    @ResponseBody
    public String getCurrentTime() {
        return dbService.getCurrentTime();
    }

	@RequestMapping(value = "/countdown/{ttl}", method = RequestMethod.PUT)
	@ResponseBody
	public void insertForCountDown(@PathVariable("ttl") int ttl) {
         dbService.insertForCountDown(ttl);
	}


    @RequestMapping(value = "/countdown", method = RequestMethod.GET)
    @ResponseBody
    public int getCountDown() {
        return dbService.getCountDown();
    }



    @RequestMapping(value = "/ratelimit/threshold/{threshold}", method = RequestMethod.PUT)
    @ResponseBody
    public List<RateLimit> setThresholdForRateLimit(@PathVariable("threshold") int threshold) {
        return dbService.setThresholdForRateLimit(threshold);
    }

    @RequestMapping(value = "/ratelimit/{value}/{ttl}", method = RequestMethod.PUT)
    @ResponseBody
    public List<RateLimit> insertForRateLimit(@PathVariable("value") String value,@PathVariable("ttl") int ttl) {
        return dbService.insertForRateLimit(value,ttl);
    }

    @RequestMapping(value = "/timestamp/{value}/{shift}", method = RequestMethod.PUT)
    @ResponseBody
    public ValueWithTimestamp insertWithCurrentTimePlus(@PathVariable("value") String value,@PathVariable("shift") int shiftInSecs) {
        return dbService.insertWithCurrentTimePlus(value,shiftInSecs);
    }


    @RequestMapping(value = "/writebarrier/{value}", method = RequestMethod.PUT)
    @ResponseBody
    public String insertForWriteBarrier(@PathVariable("value") String value) {
        return dbService.insertForWriteBarrier(value);
    }

    @RequestMapping(value = "/writebarrier/{shift}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteWithWriteBarrier(@PathVariable("shift") int shiftInSecs)  {
        dbService.deleteWithWriteBarrier(shiftInSecs);
    }


    @RequestMapping(value = "/db", method = RequestMethod.DELETE)
    @ResponseBody
    public String resetDb()  {
        dbService.resetDb();
        return "Tables successfully truncated";
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(IllegalStateException ex,HttpServletResponse response)
    {
        return ex.getMessage();
    }

}
