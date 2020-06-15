package application;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez p = new PartidaXadrez();
		
		while (true) {
			try {
				UI.clearScreen();
				UI.printTabuleiro(p.getPecas());
				System.out.println();
				System.out.print("Source: ");
				PosicaoXadrez source = UI.lerPosicaoXadrez(sc);
				
				boolean[][] possiveisMovimentos = p.possivelMovimentos(source);
				UI.clearScreen();
				UI.printTabuleiro(p.getPecas(), possiveisMovimentos);
	
				System.out.println();
				System.out.print("Target: ");
				PosicaoXadrez target = UI.lerPosicaoXadrez(sc);
	
				PecaXadrez capturedPiece = p.movimentoXadrez(source, target);
			}
			catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}
