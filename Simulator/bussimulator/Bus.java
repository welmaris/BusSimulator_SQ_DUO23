package Simulator.bussimulator;

import Simulator.bussimulator.Halte.Positie;

import java.util.ArrayList;

public class Bus{

	private Bedrijven bedrijf;
	private Lijnen lijn;
	private int halteNummer;
	private int totVolgendeHalte;
	private int richting;
	private boolean bijHalte;
	private String busID;
	
	Bus(Lijnen lijn, Bedrijven bedrijf, int richting){
		this.lijn=lijn;
		this.bedrijf=bedrijf;
		this.richting=richting;
		this.halteNummer = -1;
		this.totVolgendeHalte = 0;
		this.bijHalte = false;
		this.busID = "Niet gestart";
	}
	
	public void setbusID(int starttijd){
		this.busID=starttijd+lijn.name()+richting;
	}
	
	public void naarVolgendeHalte(){
		Positie volgendeHalte = lijn.getHalte(halteNummer+richting).getPositie();
		totVolgendeHalte = lijn.getHalte(halteNummer).afstand(volgendeHalte);
	}
	
	public boolean halteBereikt(){
		halteNummer+=richting;
		bijHalte=true;
		if ((halteNummer>=lijn.getLengte()-1) || (halteNummer == 0)) {
			System.out.printf("Bus %s heeft eindpunt (halte %s, richting %d) bereikt.%n", 
					lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer)*richting);
			return true;
		}
		else {
			System.out.printf("Bus %s heeft halte %s, richting %d bereikt.%n", 
					lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer)*richting);		
			naarVolgendeHalte();
		}		
		return false;
	}
	
	public void start() {
		halteNummer = (richting==1) ? 0 : lijn.getLengte()-1;
		System.out.printf("Bus %s is vertrokken van halte %s in richting %d.%n", 
				lijn.name(), lijn.getHalte(halteNummer), lijn.getRichting(halteNummer)*richting);		
		naarVolgendeHalte();
	}
	
	public boolean move(){
		boolean eindpuntBereikt = false;
		bijHalte=false;
		if (halteNummer == -1) {
			start();
		}
		else {
			totVolgendeHalte--;
			if (totVolgendeHalte==0){
				eindpuntBereikt=halteBereikt();
			}
		}
		return eindpuntBereikt;
	}

	public Bericht createBericht(int nu){
		return new Bericht(lijn.name(), bedrijf.name(), busID, nu);
	}

	public ArrayList<ETA> getETAs(int nu) {
		ArrayList<ETA> ETAs = new ArrayList<>();

		if (bijHalte) {
			ETA eta = new ETA(lijn.getHalte(halteNummer).name(),lijn.getRichting(halteNummer)*richting,0);
//			bericht.ETAs.add(eta);
			ETAs.add(eta);
		}
		Halte.Positie eerstVolgende=lijn.getHalte(halteNummer+richting).getPositie();
		int tijdNaarHalte=totVolgendeHalte+nu;
		for (int i = halteNummer+richting ; !(i>=lijn.getLengte()) && !(i < 0); i=i+richting ){
			tijdNaarHalte+= lijn.getHalte(i).afstand(eerstVolgende);
			ETA eta = new ETA(lijn.getHalte(i).name(), lijn.getRichting(i)*richting,tijdNaarHalte);
//			System.out.println(bericht.lijnNaam + " naar halte" + eta.halteNaam + " t=" + tijdNaarHalte);
//			bericht.ETAs.add(eta);
			ETAs.add(eta);
			eerstVolgende=lijn.getHalte(i).getPositie();
		}
		return ETAs;
	}

	public ETA getETA(){
		return new ETA(getEindpuntName(), lijn.getRichting(halteNummer)*richting, 0);
	}

	public String getEindpuntName(){
		return lijn.getHalte(lijn.getLengte()-richting).name();
//		Blijkbaar is dit ook eindpunt
//		return lijn.getHalte(halteNummer).name();
	}


}
