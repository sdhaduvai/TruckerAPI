package io.egen.controller;

import io.egen.entity.Vehicle;
import io.egen.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
@CrossOrigin(origins = "*")
@Api(description = "Vehicle related endpoints")
public class VehicleController {

    Logger log = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    VehicleService service;
//, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value = "Put all vehicles from API",
            notes = "Returns a list of all vehicles available in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public List<Vehicle> process(@RequestBody List<Vehicle> items) {
        log.info("Updating vehicle info");
        service.process(items);
        return items;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Find All Vehicles",
            notes = "Returns a list of all vehicles available in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public List<Vehicle> findAll() {
        log.info("Retreiving vehicle info");
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Find Vehicle by ID",
            notes = "Return a single vehicle or throws 404")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public Vehicle findOne(
            @ApiParam(value = "id of the vehicle", required = true) @PathVariable("id") String id) {
        return service.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Vehicle create(@RequestBody Vehicle veh) {
        return service.create(veh);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{vin}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Vehicle update(@PathVariable("vin") String vin, @RequestBody Vehicle veh) {
        return service.update(vin, veh);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{vin}")
    public void delete(@PathVariable("vin") String vin) {
        service.delete(vin);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{vin}/location")
    @ResponseBody
    public List<List> getLocations(@ApiParam(value = "ID of the vehicle", required = true)
                                 @PathVariable("vin") String vin) {
        return service.getLocations(vin);
    }
}