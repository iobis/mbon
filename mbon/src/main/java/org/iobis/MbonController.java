package org.iobis;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MbonController {

    @Autowired
    MbonService mbonService;

    private static final Logger logger = Logger.getLogger(MbonController.class);

    @RequestMapping("/eov")
    public Object eovs() {
        return mbonService.eovs();
    }

    @RequestMapping(value = "/network", method = RequestMethod.GET)
    public Object networks() {
        return mbonService.networks();
    }

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public Object questions() {
        return mbonService.questions();
    }

    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public Object parents() {
        return mbonService.parents();
    }

    @RequestMapping(value = "/tool", method = RequestMethod.GET)
    public Object tools() {
        return mbonService.tools();
    }

    @RequestMapping(value = "/geojson", method = RequestMethod.GET)
    public Object geojson() {
        return mbonService.geojson();
    }

    @RequestMapping(value = "/graphdata", method = RequestMethod.GET)
    public Object graph() {
        return mbonService.graph();
    }

    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public Object activities() {
        return mbonService.activities();
    }

    @RequestMapping(value = "/activity/{id}", method = RequestMethod.DELETE)
    public void deleteActivity(@PathVariable(value = "id") Integer id) {
        mbonService.deleteActivity(id);
    }

    @RequestMapping(value = "/network/{id}", method = RequestMethod.DELETE)
    public void deleteNetwork(@PathVariable(value = "id") Integer id) {
        mbonService.deleteNetwork(id);
    }

    @RequestMapping(value = "/eov/{id}", method = RequestMethod.DELETE)
    public void deleteEov(@PathVariable(value = "id") Integer id) {
        mbonService.deleteEov(id);
    }

    @RequestMapping(value = "/question/{id}", method = RequestMethod.DELETE)
    public void deleteQuestion(@PathVariable(value = "id") Integer id) {
        mbonService.deleteQuestion(id);
    }

    @RequestMapping(value = "/output/{id}", method = RequestMethod.GET)
    public Object output(@PathVariable(value = "id") Integer id) {
        return mbonService.output(id);
    }

    @RequestMapping(value = "/network", method = RequestMethod.POST)
    public void saveNetwork(@RequestBody String json) {
        JSONObject network = new JSONObject(json);
        logger.info(network);
        mbonService.saveNetwork(network);
    }

    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    public void saveActivity(@RequestBody String json) {
        JSONObject activity = new JSONObject(json);
        logger.info(activity);
        mbonService.saveActivity(activity);
    }

    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public void saveQuestion(@RequestBody String json) {
        JSONObject question = new JSONObject(json);
        logger.info(question);
        mbonService.saveQuestion(question);
    }

    @RequestMapping(value = "/eov", method = RequestMethod.POST)
    public void saveEov(@RequestBody String json) {
        JSONObject eov = new JSONObject(json);
        logger.info(eov);
        mbonService.saveEov(eov);
    }

}