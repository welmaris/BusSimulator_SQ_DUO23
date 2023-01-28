package Simulator.bussimulator;

import com.thoughtworks.xstream.XStream;

public class ETAHandler {
    public void sendETAs(Bus bus, int nu){
        Bericht bericht = bus.createBericht(nu);
        bericht.ETAs = bus.getETAs(nu);
        bericht.eindpunt = bus.getEindpuntName();
        sendBericht(bericht);
    }

    public void sendLastETA(Bus bus, int nu){
        Bericht bericht = bus.createBericht(nu);
        String eindpunt = bus.getEindpuntName();
        ETA eta = bus.getETA();
        bericht.ETAs.add(eta);
        bericht.eindpunt = eindpunt;
        sendBericht(bericht);
    }

    public void sendBericht(Bericht bericht){
        XStream xstream = new XStream();
        xstream.alias("Bericht", Bericht.class);
        xstream.alias("ETA", ETA.class);
        String xml = xstream.toXML(bericht);
        Producer producer = new Producer();
        producer.sendBericht(xml);
    }
}
