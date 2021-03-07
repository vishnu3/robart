package cc.robart.iot.demoproject.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Mapping;
import cc.robart.iot.demoproject.service.DemoprojectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cc.robart.iot.demoproject.persistent.Robot;
import cc.robart.iot.demoproject.repository.DemoprojectRepository;

/**
 * The only and single purpose controller of this demo project.
 *
 */
@RestController
public class DemoprojectController {
	
	private DemoprojectRepository mainRepository;
	private DemoprojectService service;
	
	// Never mind the MainRepository not being constructed explicitly.
	// Spring will automatically create this bean and inject it into the constructor here.
	public DemoprojectController(DemoprojectRepository mainRepository, DemoprojectService service) {
        this.service = service;
		this.mainRepository = mainRepository;
	}
	
	/**
	 * Returns a list of all robots currently stored in the database.
	 * @return List of all robots.
	 */
	@GetMapping(path="/robots/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Robot> getAllRobots() {
		return mainRepository.findAllRobots();
	}

	@GetMapping(path="/firmware/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Firmware> getAllFirmware() {
		return mainRepository.findAllFirmware();
	}

	@PutMapping(path="/firmware/upload")
	public HttpStatus addFirmware(@RequestBody Firmware firmware){
	   try{
	   	mainRepository.addFirmware(firmware);
	   return HttpStatus.OK;
	   }
	   catch (Exception e){
	   	return HttpStatus.INTERNAL_SERVER_ERROR;
	   }
	}

	@DeleteMapping("/firmware/remove/{name}")
	public HttpStatus deleteFirmware(@PathVariable String name){
		try{
			mainRepository.deleteFirmware(name);
			return HttpStatus.OK;
		}
		catch (Exception e){
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@PostMapping("/firmware/updateName")
	public HttpStatus updateFirmwareName(@RequestBody Firmware firmware){
		try{
			mainRepository.updateFirmware(firmware,true);
			return HttpStatus.OK;
		}catch (Exception e){
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@PostMapping("/firmware/updateData")
	public HttpStatus updateFirmwareData(@RequestBody Firmware firmware){
		try{
			mainRepository.updateFirmware(firmware,false);
			return HttpStatus.OK;
		}catch (Exception e){
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@PostMapping("/mapping")
	public ResponseEntity<String> createMapping(@RequestBody Mapping mapping){
		try{
			System.out.println(mapping.toString());
			return new ResponseEntity<>(
					service.processMappingData(mapping),
					HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					"Server Error",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user/update/firmware/{name}")
	public String downLoadFirmware(@PathVariable String name){
		try{
			return mainRepository.getLatestFirmware(name);
		}catch (Exception e){
			e.printStackTrace();
			return "Server Error";
		}

	}
	
	/**
	 * Just a demo call to see the possibilities of Spring controllers.
	 * Call it with e.g. http://localhost:8080/robots/democall/1?key1=hello&key2=world
	 * @param id A numeric ID
	 * @param key1 The string value of the request parameter "key1".
	 * @param key2 The string value of the request parameter "key2"
	 * @return A short text about the beauty of Spring development incorporating the given parameters.
	 */
	@GetMapping(path="/demo/{id}", produces=MediaType.TEXT_PLAIN_VALUE)
	public String demoGet(
			@PathVariable("id") Long id,
			@RequestParam("key1") String key1,
			@RequestParam("key2") String key2
			) {
		
		String text = "This is a sample call, just to show you how easy it is in Spring to "
					+ "incorporate path variables and request parameters of arbitrary types\n\n"
					+ "The given parameters are: \n"
					+ " * id: {0}\n"
					+ " * key1: {1}\n"
					+ " * key2: {2}\n";
						
		return MessageFormat.format(text, id, key1, key2);
	}
	
	/**
	 * Super simple demo call that shows how easy it is with Spring to post a Java object and also to return one.
	 * This call simple returns the robot object it has been given.
	 * Spring will automatically take care about JSON to Java (de-)serialization.
	 * 
	 * This is an example of how this controller can be called with curl from the command line:
	 * curl -X POST --header "Content-Type: application/json" -d '{"name" : "robot number 11", "hardware_version" : "a completly new model"}' http://localhost:8080/demo
	 * 
	 * 
	 * @param robot The robot object received via the POST call
	 * @return The same robot as received via the POST body.
	 */
	@PostMapping(path="/demo", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Robot demoPost(@RequestBody Robot robot) {
		return robot;
	}
	

}
