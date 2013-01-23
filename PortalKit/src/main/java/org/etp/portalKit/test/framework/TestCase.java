package org.etp.portalKit.test.framework;

public class TestCase {

    private String name;
    private String area;
    private String nrs;
    private String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setNrs(String nrs) {
        this.nrs = nrs;
    }

    public String getNrs() {
        return nrs;
    }
}
