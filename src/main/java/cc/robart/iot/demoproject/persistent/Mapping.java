package cc.robart.iot.demoproject.persistent;

import java.util.List;

public class Mapping {
    private List<String> robots; //we can me it to List<Robot> to be more specific
    private String firmware;

    @Override
    public String toString() {
        return "Mapping{" +
                "robots=" + robots +
                ", firmware='" + firmware + '\'' +
                '}';
    }

    public Mapping(List<String> robots, String firmware) {
        this.robots = robots;
        this.firmware = firmware;
    }

    public List<String> getRobots() {
        return robots;
    }

    public void setRobots(List<String> robots) {
        this.robots = robots;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }
}
