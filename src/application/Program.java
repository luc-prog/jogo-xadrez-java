package application;

import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez p = new PartidaXadrez();
		
		while (true) {
			
			UI.printTabuleiro(p.getPecas());
			System.out.println();
			System.out.print("Source: ");
			PosicaoXadrez source = UI.lerPosicaoXadrez(sc);

			System.out.println();
			System.out.print("Target: ");
			PosicaoXadrez target = UI.lerPosicaoXadrez(sc);

			PecaXadrez capturedPiece = p.movimentoXadrez(source, target);
		}
	}
}
