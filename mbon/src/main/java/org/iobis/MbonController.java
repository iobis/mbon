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

    @RequestMapping(value = "/requirement", method = RequestMethod.GET)
    public Object requirements() {
        return mbonService.requirements();
    }

    @RequestMapping(value = "/parent", method = RequestMethod.GET)
    public Object parents() {
        return mbonService.parents();
    }

    @RequestMapping(value = "/tool", method = RequestMethod.GET)
    public Object tools() {
        return mbonService.tools();
    }

    @RequestMapping(value = "/dataproduct", method = RequestMethod.GET)
    public Object dataproducts() {
        return mbonService.dataproducts();
    }

    @RequestMapping(value = "/datasystem", method = RequestMethod.GET)
    public Object datasystems() {
        return mbonService.datasystems();
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

    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.DELETE)
    public void deleteRequirement(@PathVariable(value = "id") Integer id) {
        mbonService.deleteRequirement(id);
    }

    @RequestMapping(value = "/dataproduct/{id}", method = RequestMethod.DELETE)
    public void deleteDataproduct(@PathVariable(value = "id") Integer id) {
        mbonService.deleteDataproduct(id);
    }

    @RequestMapping(value = "/datasystem/{id}", method = RequestMethod.DELETE)
    public void deleteDatasystem(@PathVariable(value = "id") Integer id) {
        mbonService.deleteDatasystem(id);
    }

    @RequestMapping(value = "/tool/{id}", method = RequestMethod.DELETE)
    public void deleteTool(@PathVariable(value = "id") Integer id) {
        mbonService.deleteTool(id);
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

    @RequestMapping(value = "/requirement", method = RequestMethod.POST)
    public void saveRequirement(@RequestBody String json) {
        JSONObject requirement = new JSONObject(json);
        logger.info(requirement);
        mbonService.saveRequirement(requirement);
    }

    @RequestMapping(value = "/tool", method = RequestMethod.POST)
    public void saveTool(@RequestBody String json) {
        JSONObject tool = new JSONObject(json);
        logger.info(tool);
        mbonService.saveTool(tool);
    }

    @RequestMapping(value = "/dataproduct", method = RequestMethod.POST)
    public void saveDataproduct(@RequestBody String json) {
        JSONObject dataproduct = new JSONObject(json);
        logger.info(dataproduct);
        mbonService.saveDataproduct(dataproduct);
    }

    @RequestMapping(value = "/datasystem", method = RequestMethod.POST)
    public void saveDatasystem(@RequestBody String json) {
        JSONObject datasystem = new JSONObject(json);
        logger.info(datasystem);
        mbonService.saveDatasystem(datasystem);
    }

    @RequestMapping(value = "/eov", method = RequestMethod.POST)
    public void saveEov(@RequestBody String json) {
        JSONObject eov = new JSONObject(json);
        logger.info(eov);
        mbonService.saveEov(eov);
    }

}