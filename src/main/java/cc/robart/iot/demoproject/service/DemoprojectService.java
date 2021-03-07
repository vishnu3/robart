package cc.robart.iot.demoproject.service;

import cc.robart.iot.demoproject.persistent.Mapping;
import cc.robart.iot.demoproject.repository.DemoprojectRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class DemoprojectService {

    private DemoprojectRepository mainRepository;


    public DemoprojectService(DemoprojectRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public String processMappingData(Mapping mapping){

        List<String> inValidateRobot = mainRepository.validateRobot(mapping.getRobots());
        boolean firmwareValid = mainRepository.validateFirmware(mapping.getFirmware());

        if(inValidateRobot.isEmpty()) {
            if (firmwareValid) {
                return updateMapping(mapping);
            } else {
                return String.format("Invalid Firmware %s", mapping.getFirmware());
            }
        }else {
            return String.format("Invalid Robot %s",
                    mapping.getRobots().stream().reduce(null, (x,y)->x+","+y).toString());
        }
    }

    public String updateMapping(Mapping mapping){
        int count = mainRepository.updateOldMapping(mapping.getRobots());
        System.out.println(String.format("Number of records Marked Inactive %d ",count));

        count = mainRepository.addNewMapping(mapping.getFirmware(),mapping.getRobots());
        System.out.println(String.format("Number of New Mapping %d Added",count));

        return "New Mapping Added";

    }

}
