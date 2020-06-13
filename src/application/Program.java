package application;

import xadrez.PartidaXadrez;

public class Program {

	public static void main(String[] args) {
		
		PartidaXadrez p = new PartidaXadrez();
		UI.printTabuleiro(p.getPecas());
	}

}
