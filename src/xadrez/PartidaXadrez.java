package xadrez;

import boardgame.Posicao;
import boardgame.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	
	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		iniciarConfig();
	}
	
	public PecaXadrez[][] getPecas(){
		System.out.println("aaaaa" + tabuleiro.getLinhas());
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i=0; i<tabuleiro.getLinhas(); i++) {
			for (int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	private void iniciarConfig() {
		tabuleiro.colocarPeca(new Torre(tabuleiro, Cor.BRACA), new Posicao(3, 3));
		tabuleiro.colocarPeca(new Rei(tabuleiro, Cor.PRETA), new Posicao(5, 5));
	}
}
