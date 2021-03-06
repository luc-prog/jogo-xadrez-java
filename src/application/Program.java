package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturada = new ArrayList<>();
		
		while (!partidaXadrez.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printPartida(partidaXadrez, capturada);
				System.out.println();
				System.out.print("Source: ");
				PosicaoXadrez source = UI.lerPosicaoXadrez(sc);
				
				boolean[][] possiveisMovimentos = partidaXadrez.possivelMovimentos(source);
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas(), possiveisMovimentos);
	
				System.out.println();
				System.out.print("Target: ");
				PosicaoXadrez target = UI.lerPosicaoXadrez(sc);
	
				PecaXadrez pecaCapturada = partidaXadrez.movimentoXadrez(source, target);
				if (pecaCapturada != null) {
					capturada.add(pecaCapturada);
				}
				
				if (partidaXadrez.getPromoted() != null) {
					System.out.print("Enter piece for promotion (B/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();
					while (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
						System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
						type = sc.nextLine().toUpperCase();
					}
				}
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
		UI.clearScreen();
		UI.printPartida(partidaXadrez, capturada);
	}
}
